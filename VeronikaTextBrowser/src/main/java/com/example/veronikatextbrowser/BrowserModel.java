package com.example.veronikatextbrowser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class BrowserModel {
    public String getContent(String url, Integer port) {
        String lineIn;
        // https://pixabay.com/de/images/search/safari/
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        if (url.startsWith("https://")) {
            url = url.replace("https://", "http://");
        }

        String address = URLParser.parseAddress(url);
        System.out.println(address);
        String path = URLParser.parsePath(url);
        System.out.println(path);
        StringBuilder builder = new StringBuilder();

        //Network errors are always possible
        //This solves raw approach, mor specific way how to connect to the server
        try (Socket s = new Socket(address, port);
             OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
             BufferedReader inReader = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));) { //https://stackoverflow.com/questions/24393112/socket-inputstream-and-utf-8
            // www.google.com/images
            // Send our request, using the HTTP 1.0 protocol
            // Note: HTTP specifies \r\n line endings, though most programs don't care
            out.write("GET " + path + " HTTP/1.0\r\n");
            out.write("User-Agent: Browser0\r\n");
            out.write("Host: " + address + ":" + port + "\r\n");
            out.write("Accept: text/html, */*\r\n\r\n"); // Blank line at the end!!!
            out.flush();

            //Start reading
            while ((lineIn = inReader.readLine()) != null) {
                builder.append(lineIn + "\n");
            }
        }

        // If an error occurred, show the error message in txtInhalt
        catch (Exception err) {
            builder.append(" ERROR: " + err.toString());
        }
        System.out.println("fin");
        // divider zeigt die position von den zwei spaces. Mit substring entfernen wir den Inhalt bevor den zwei spaces, damit die antwort des Serves nicht angezeigt wird
        int divider = builder.indexOf("\n\n");
       // System.out.println(builder.substring(0, divider));
        return builder.substring(divider + 2);
    }


    private void goTrough(Node node, StringBuilder builder, int tabs) {
        for (int i = 0; i < tabs; i++) System.out.print("\t");
        String text = "*";
        if (node instanceof TextNode) {
            TextNode textNode = (TextNode) node;
            text = textNode.text();
            if (!text.isBlank()) {
                builder.append(text);//.append("<br>");
            }
            return;
        }
        String tag = node.nodeName();
        System.out.println(tag + " " + node.childNodeSize() + " " + text);

        // do something before looking at children
        if (tag.equals("a")) {
            builder.append("<a href='").append(node.attr("href")).append("'>");
        } else if (tag.equals("p") || tag.equals("ol") || tag.equals("ul")) {
            if (!builder.substring(builder.length() - 4).equals("<br>"))
                builder.append("<br>");
        } else if (tag.equals("li")) {
            builder.append("&#8226; ");
        } else if (tag.equals("b")) {
            builder.append("<b>");
        }
        // look at children
        for (Node child : node.childNodes()) {
            goTrough(child, builder, tabs + 1);
        }
        // do something after looking at children
        List<String> breakingTags = List.of("h1", "h2", "h3", "h4", "h5", "h6", "p", "div", "li");
        if (tag.equals("a")) {
            builder.append("</a> ");
        } else if (breakingTags.contains(tag)) {
            builder.append("<br>");
        } else if (tag.equals("b")) {
            builder.append("</b>");
        }

    }


    public String filter(String html) {
        // other code here

        // Parse the HTML of the webpage into a Document object
        Document doc = Jsoup.parse(html);

        // Remove all the style elements from the Document
        doc.select("style").remove();

        // Get the body element of the Document
        Element body = doc.body();

        // Use a StringBuilder to build the formatted text
        StringBuilder builder = new StringBuilder();
        goTrough(body, builder, 0);

        System.out.println(builder.toString());
        // Return the formatted text
        return builder.toString();
    }

}
