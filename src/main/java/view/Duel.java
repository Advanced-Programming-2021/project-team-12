package view;

import Exceptions.MyException;
import controllers.DuelControl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Duel extends Application {
    public static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        Duel.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Duel.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void singleDuelAI(ActionEvent actionEvent) throws MyException {
        new DuelControl(1);
    }

    public void matchDuelAI(ActionEvent actionEvent) throws MyException {
        new DuelControl(3);
    }

    public void PlayOnline(ActionEvent actionEvent) throws Exception {
        new OnlineDuel().start(stage);
    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        new MainMenu().start(stage);
    }
}
