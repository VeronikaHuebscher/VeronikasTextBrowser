package com.example.veronikatextbrowser;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileNotFoundException;


public class BrowserView extends JEditorPane {

    private BrowserModel model;
    private Stage stage;
    //protected Label lblPort = new Label("Port");
    //protected TextField txtPort = new TextField("8080");


    //protected TextArea txtWebPage = new TextArea();
    OnSearchListener onSearchListener;

    interface OnSearchListener {
        void onSearchClicked(String url, HTMLEditor editor);
    }

    protected BrowserView(Stage stage, BrowserModel model) throws FileNotFoundException {

        this.stage = stage;
        this.model = model;

        BorderPane root = new BorderPane();
        VBox topBar = createTopBar();
        root.setCenter(topBar);


        //txtWebPage.setWrapText(true);

        Scene scene = new Scene(root, 800, 450);

        //scene.getStylesheets().add(getClass().getResource("Browser.css").toExternalForm());
        stage.setTitle("Browser");
        stage.setScene(scene);;
        stage.setFullScreen(false);

    }

    private VBox createTopBar() {

        TabPane tabPane = new TabPane();

        VBox layout = new VBox(10); // VBox with spacing of 10. Button sits above TabPane

        Button addTab = new Button("Create Tab");

        addTab.setOnAction(event -> {
            Tab tab = new Tab("New");
            tab.setContent(createTab(tab));
            tabPane.getTabs().add(tab); // Adding new tab at the end, so behind all the other tabs
            tabPane.getSelectionModel().selectLast(); // Selecting the last tab, which is the newly created one
        });
        addTab.fire();
        layout.getChildren().addAll(addTab, tabPane); // Adding button and TabPane to VBox

        return layout;
    }

    private HBox createControls(HTMLEditor htmlEditor, Tab newTab){
        HBox controlls = new HBox();
        controlls.setAlignment(Pos.CENTER);
        controlls.setId("TopBox");
        Region spacer1 = new Region();
        Label lblIP = new Label("URL ");
        TextField txtIP = new TextField("");
        txtIP.setPromptText("write your url in here");
        Button btnGo = new Button("Search");


        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        //root.setTop(controlls);

        controlls.getChildren().addAll(spacer1, lblIP, txtIP, btnGo, spacer2);
        txtIP.setId("IP ");
        //Style
        //btnGo.setStyle("-fx-background-color: #89CFF0");
        controlls.setStyle("-fx-background-color: #89CFF0");
        txtIP.setPrefWidth(300);

        btnGo.setOnAction(e -> {
            newTab.setText(URLParser.parseAddress(txtIP.getText()));
            onSearchListener.onSearchClicked(txtIP.getText(), htmlEditor);
        });

        txtIP.setOnAction(e -> {
            newTab.setText(URLParser.parseAddress(txtIP.getText()));
            onSearchListener.onSearchClicked(txtIP.getText(), htmlEditor);
        });


        return controlls;
    }
    private BorderPane createTab(Tab newTab){
        BorderPane tab = new BorderPane();
        HTMLEditor txtWebPage = new HTMLEditor();
        Node web = txtWebPage.lookup(".web-view");
        tab.setTop(createControls(txtWebPage, newTab));
        hideHTMLEditorToolbars(txtWebPage);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        tab.setCenter(scrollPane);
        scrollPane.setContent(txtWebPage);

        return tab;
    }
    public static void hideHTMLEditorToolbars(final HTMLEditor editor) //https://stackoverflow.com/questions/10075841/how-to-hide-the-controls-of-htmleditor
    {
        editor.setVisible(false);
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
                for(Node node : nodes)
                {
                    node.setVisible(false);
                    node.setManaged(false);
                }
                editor.setVisible(true);
            }
        });
    }

    public void start() {
        stage.show();
    }
}
