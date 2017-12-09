package CDBurn;

import CDBurn.Services.Tools;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class Controller {
    @FXML
    private ListView<String> listView;
    @FXML
    private Button fileChooserButton;
    @FXML
    private Button write;
    @FXML
    private Label  progress;

    private StringProperty progressValue = new SimpleStringProperty();
    private CDBurn cdBurn = new CDBurn(progressValue, this);
    private ObservableList<String> fileNames = FXCollections.observableArrayList();
    private FileChooser fileChooser = new FileChooser();

    @FXML
    private void initialize() {
        listView.setItems(fileNames);
        fileChooser.setTitle("Choose file");
        progress.textProperty().bind(progressValue);
    }

    public void chooseFile() {
        File file = fileChooser.showOpenDialog(new Stage());
        fileNames.add(file.getName());
        cdBurn.addFile(file);
    }

    public void writeFiles() {
        fileChooserButton.setDisable(true);
        write.setDisable(true);
        runAsynchronouslyWriteProcess();
    }

    public void switchStateButtons() {
        Platform.runLater(() -> {
            fileChooserButton.setDisable(false);
            write.setDisable(false);
            fileNames.clear();
        });
    }

    private void runAsynchronouslyWriteProcess() {
        new Thread(() -> cdBurn.burnFiles()).start();
    }
}
