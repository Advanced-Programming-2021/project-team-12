package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

public class GameView extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Pane pane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GameView.fxml")));
        createBackground("normal", pane);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void createBackground(String type, Pane pane) {
        Image image = new Image(getClass().getResource("/PNG/Field/FieldPNG/fie_" + type + ".png").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setImage(image);
        imageView.setFitWidth(600.0);
        imageView.setFitHeight(600.0);
        imageView.setPreserveRatio(false);
        imageView.setX(200);
        imageView.setY(0);
        pane.getChildren().add(imageView);
    }
}
