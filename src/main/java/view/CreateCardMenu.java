package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CreateCardMenu extends Application {
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        CreateCardMenu.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CreateCardMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }


    public void createMonsterCard(MouseEvent mouseEvent) throws Exception {
       new CreateMonsterCard().start(stage);
    }

    public void createSpellCard(MouseEvent mouseEvent) throws Exception {
        new CreateSpellCard().start(stage);
    }

    public void createTrapCard(MouseEvent mouseEvent) throws Exception {
        new CreateTrapCard().start(stage);
    }
}
