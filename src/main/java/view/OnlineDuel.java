package view;

import Exceptions.MyException;
import controllers.DuelControl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import models.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class OnlineDuel extends Application {
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Pane pane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/OnlineDuel.fxml")));
        Scene scene = new Scene(pane);
        createOnlineDuelObjects(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void createOnlineDuelObjects(Pane pane) {
        ArrayList<User> sortedUsers = User.getUsers();
        if(sortedUsers.size() <= 0){
            Text error = new Text();
            error.setFont(Font.font("Yu-Gi-Oh! Matrix Small Caps 1", FontWeight.BOLD, 30));
            error.setLayoutX(60);
            error.setLayoutY(175);
            error.setFill(Color.valueOf("gold"));
            error.setText("You are the only one online at the moment! Sorry...");
            pane.getChildren().add(error);
        } else {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setPrefHeight(400.0);
            scrollPane.setPrefWidth(600.0);
            Pane innerPane = new Pane();
            //sortedUsers.remove(MainMenu.user);
            setInnerPane(innerPane, sortedUsers);
            createGoBackButton(pane);
            createOnlineDuelText(sortedUsers, innerPane);
            createOnlineDuelImage(sortedUsers, innerPane);
            scrollPane.setContent(innerPane);
            pane.getChildren().add(scrollPane);
        }
    }

    private void createGoBackButton(Pane pane) {

    }

    private void setInnerPane(Pane innerPane, ArrayList<User> sortedUsers) {
        int maxHeight = sortedUsers.size() / 3;
        if(maxHeight * 176 > 400){
            innerPane.setPrefHeight(176 * maxHeight);
            innerPane.setPrefWidth(600.0);
            Image image = new Image(getClass().getResource("/PNG/NEW/background3.png").toExternalForm());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(600.0);
            imageView.setFitHeight(176 * maxHeight);
            imageView.setX(0);
            imageView.setY(0);
            innerPane.getChildren().add(imageView);
        } else {
            innerPane.setPrefHeight(400.0);
            Image image = new Image(getClass().getResource("/PNG/NEW/background3.jpg").toExternalForm());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(600.0);
            imageView.setFitHeight(400.0);
            imageView.setX(0);
            imageView.setY(0);
            innerPane.getChildren().add(imageView);
        }
    }

    private void createOnlineDuelText(ArrayList<User> sortedUsers, Pane innerPane) {
        for (int i = 0; i < sortedUsers.size(); i++) {
            int placementOfX =  i % 3;
            int placementOfY = i / 3;
            Text nickNameText = new Text();
            nickNameText.setFont(Font.font("Yu-Gi-Oh! Matrix Small Caps 1", FontWeight.BOLD, 25));
            nickNameText.setFill(Color.valueOf("white"));
            nickNameText.setText(sortedUsers.get(i).getNickName());
            setTextXAndY(sortedUsers.get(i).getNickName(), nickNameText, placementOfX, placementOfY);
            innerPane.getChildren().add(nickNameText);
        }
    }

    private void setTextXAndY(String nickName, Text nickNameText, int placementOfX, int placementOfY) {
        if(nickName.length() > 10){
            nickNameText.setLayoutX(54 + 182 * placementOfX);
            nickNameText.setLayoutY(176 + 176 * placementOfY);
        } else{
            nickNameText.setLayoutX(90 + 182 * placementOfX);
            nickNameText.setLayoutY(176 + 176 * placementOfY);
        }
    }

    private void createOnlineDuelImage(ArrayList<User> sortedUsers, Pane innerPane) {
        for (int i = 0; i < sortedUsers.size(); i++) {
            int placementOfX =  i % 3;
            int placementOfY = i / 3;
            File pictureFile =  new File(MainMenu.user.getAvatarAddress());
            String string = pictureFile.toURI().toString();
            Image image = new Image(string);
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setImage(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setLayoutX(54 + 182 * placementOfX);
            imageView.setLayoutY(48 + 176 * placementOfY);
            int finalI = i;
            imageView.setOnMouseClicked(e ->{
                try {
                    new DuelControl(sortedUsers.get(finalI).getName(), Duel.rounds);
                } catch (MyException myException) {
                    myException.printStackTrace();
                }
            });
            innerPane.getChildren().add(imageView);
        }
    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        new Duel().start(stage);
    }
}
