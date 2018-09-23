import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class mainController {
    //Rubik's Cube Scene
    @FXML
    private Pane scene3D;

    //Option Controls
    @FXML
    private Button solveCube, shuffleCube, scanCube, prevStep, nextStep, undo, redo;

    //Environmental Variables
    private Stage stage;


    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
