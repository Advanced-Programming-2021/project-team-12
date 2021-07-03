package view;

import Exceptions.MyException;
import Utility.CommandMatcher;
import controllers.ImportOrExportControl;
import controllers.LoadFile;
import controllers.SaveFile;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Card;

import java.io.File;
import java.util.regex.Matcher;

public class ImportOrExport extends Application {
    public static Stage stage;
    public Button msg;
    public TextField cardName;
    public Button importData;

    @Override
    public void start(Stage stage) throws Exception {
        ImportOrExport.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ImportOrExport.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void importMonsterFile(ActionEvent actionEvent) {
        importData.setVisible(false);
        Card card;
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json File", "*.json"));
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                card = new ImportOrExportControl().doImport(file, "m");
                msg.setText("Card Import successfully");
                msg.setStyle("-fx-text-fill: #16045c ");
                importData.setText("Name: " + card.getCardName() + "\n-----------\nAttack: " + card.getAttack()
                                    + "\n-----------\nDefence: " + card.getDefence() + "\n-----------\nDescription: " + card.getDescription());
                importData.setVisible(true);
            } catch (Exception e) {
                msg.setText("Invalid Monster Card");
                msg.setStyle("-fx-text-fill: #88001b ");
            }
            msg.setVisible(true);
        }
    }

    public void exportFile(ActionEvent actionEvent) {
        importData.setVisible(false);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            try {
                new ImportOrExportControl().doExport(selectedDirectory, cardName.getText().trim());
                msg.setText("Card export successfully");
                msg.setStyle("-fx-text-fill: #16045c ");
            } catch (Exception e) {
                msg.setText("Card With This Name Does Not Exits");
                msg.setStyle("-fx-text-fill: #88001b ");
            }
            msg.setVisible(true);
        }
    }

    public void back(MouseEvent mouseEvent) {
        importData.setVisible(false);
        try {
            new MainMenu().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importSpell(ActionEvent actionEvent) {
        Card card;
        importData.setVisible(false);
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json File", "*.json"));
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                card = new ImportOrExportControl().doImport(file, "s");
                msg.setText("Card Import successfully");
                msg.setStyle("-fx-text-fill: #16045c ");
                importData.setText("Name: " + card.getCardName() + "\n-----------\nAttack: " + card.getAttack()
                        + "\n-----------\nDefence: " + card.getDefence() + "\n-----------\nDescription: " + card.getDescription());
                importData.setVisible(true);
            } catch (Exception e) {
                msg.setText("Invalid Spell Card");
                msg.setStyle("-fx-text-fill: #88001b ");
            }
            msg.setVisible(true);
        }
    }

    public void importTrap(ActionEvent actionEvent) {
        Card card;
        importData.setVisible(false);
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json File", "*.json"));
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                card = new ImportOrExportControl().doImport(file, "t");
                msg.setText("Card Import successfully");
                msg.setStyle("-fx-text-fill: #16045c ");
                importData.setText("Name: " + card.getCardName() + "\n-----------\nAttack: " + card.getAttack()
                        + "\n-----------\nDefence: " + card.getDefence() + "\n-----------\nDescription: " + card.getDescription());
                importData.setVisible(true);
            } catch (Exception e) {
                msg.setText("Invalid Monster Card");
                msg.setStyle("-fx-text-fill: #88001b ");
            }
            msg.setVisible(true);
        }
    }
}
