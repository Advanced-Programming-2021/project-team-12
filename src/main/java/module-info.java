module Graphic {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.json;
    requires json.simple;
    requires java.desktop;
    requires gson;

    opens view to javafx.fxml;
    exports view;
}