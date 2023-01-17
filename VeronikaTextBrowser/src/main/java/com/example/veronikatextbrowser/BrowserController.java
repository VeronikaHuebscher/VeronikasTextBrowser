package com.example.veronikatextbrowser;

import javafx.scene.web.HTMLEditor;

public class BrowserController{


    protected BrowserController(BrowserModel model, BrowserView view) {

        // register ourselves to listen for button clicks
        //view.btnGo.setOnAction(e -> a(model, view));
        //view.txtIP.setOnAction(e -> a(model, view));
        view.onSearchListener = new BrowserView.OnSearchListener() {
            @Override
            public void onSearchClicked(String url, HTMLEditor editor) {
                a(model, url, editor);
            }
        };
    }

    private void a(BrowserModel model, String url, HTMLEditor editor) {
        //String ipAddress = url;//view.txtIP.getText();
        Integer port = 80;
        //Integer port = Integer.parseInt(view.txtPort.getText());
        String webPage = model.getContent(url, port);
        if(webPage.isEmpty()){
            webPage = "There is no content"; //no http site?
        }else {
            webPage = model.filter(webPage);
        }
        editor.setHtmlText(webPage);
    }
}
