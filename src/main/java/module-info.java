module org.example.piggame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens org.example.piggame to javafx.fxml;
    exports org.example.piggame;
}