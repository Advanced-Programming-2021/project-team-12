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
    public static int rounds = 1;
    @Override
    public void start(Stage stage) throws Exception {
        Duel.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Duel.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void singleDuelAI(ActionEvent actionEvent) throws Exception {
        rounds = 1;
        new DuelControl(1);
    }

    public void matchDuelAI(ActionEvent actionEvent) throws Exception {
        rounds = 3;
        new DuelControl(3);
    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        new MainMenu().start(stage);
    }

    public void PlayOnlineMatch(ActionEvent actionEvent) throws Exception {
        rounds = 3;
        new OnlineDuel().start(stage);
    }

    public void playOnlineSingle(ActionEvent actionEvent) throws Exception {
        rounds = 1;
        new OnlineDuel().start(stage);
    }
}
