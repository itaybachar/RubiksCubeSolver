import cubeRecognition.RecognitionControl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;

public class mainController {
    //Rubik's Cube Scene
    @FXML
    private Pane scene3D;

    //Option Controls
    @FXML
    private Button solveCube, shuffleCube, scanCube, prevStep, nextStep, undo, redo;

    //Environmental Variables
    private Stage stage;
    private Popup recognition;
    private RecognitionControl recognitionControl;

    public void initialize(){
        recognition = new Popup();
        FXMLLoader loader = new FXMLLoader(main.class.getResource("FXML_layouts\\RecognitionScreen.fxml"));
        System.out.println();
        try { recognition.getContent().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        recognitionControl = loader.getController();
        recognitionControl.setPopup(recognition);
        scanCube.setOnAction(event ->{
            recognitionControl.setStage(this.stage);
            recognition.show(stage);
        });
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
