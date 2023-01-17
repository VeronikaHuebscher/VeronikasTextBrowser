module com.example.veronikatextbrowser {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires java.desktop;
    requires javafx.web;


    opens com.example.veronikatextbrowser to javafx.fxml;
    exports com.example.veronikatextbrowser;
}