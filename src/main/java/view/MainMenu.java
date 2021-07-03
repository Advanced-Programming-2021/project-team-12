package view;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;

public class MainMenu extends Application {
    public static User user;
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        MainMenu.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void setUser(User _user) {
        user = _user;
    }

    public void playGame(ActionEvent actionEvent) {
//        new Duel().start(stage);
    }

    public void goToShop(ActionEvent actionEvent) throws Exception {
        new Shop().start(stage);
    }

    public void goToImportExport(ActionEvent actionEvent) {
//        new ImportOrExport().start(stage);
    }

    public void goToDeckMenu(ActionEvent actionEvent) {
//        new DeckMenu().start(stage);
    }

    public void goToChangeProfile(ActionEvent actionEvent) throws Exception {
        new Profile().start(stage);
    }

    public void logout(ActionEvent actionEvent) throws Exception {
        new RegistrationMenu().start(stage);
    }

    public void goToScoreboard(ActionEvent mouseEvent) throws Exception {
        new Scoreboard().start(stage);
    }
}
