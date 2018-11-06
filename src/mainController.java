import cube.CubeSolver;
import cube.RotationType;
import cube.RubiksCube;
import cube.SolutionMove;
import cubeRecognition.RecognitionControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;

public class mainController {
    //Rubik's Cube Scene
    @FXML
    private Pane scene3D;
    private RubiksCube rubiksCube;
    private CubeSolver cubeSolver;

    //Option Controls
    @FXML
    private Button solveCube, shuffleCube, scanCube, prevStep, nextStep, undo, redo;
    @FXML
    private Label moveInstruction;
    @FXML
    private Slider animationSpeed;

    //Environmental Variables
    private Stage stage;
    private Popup recognition;
    private RecognitionControl recognitionControl;

    public void initialize(){
        //Set up Rubik's cube
        rubiksCube = new RubiksCube(20);
        rubiksCube.getScene().heightProperty().bind(scene3D.heightProperty());
        rubiksCube.getScene().widthProperty().bind(scene3D.widthProperty());
        scene3D.getChildren().add(rubiksCube.getScene());

        //Set up solver
        cubeSolver = new CubeSolver(this.rubiksCube);

        recognition = new Popup();
        FXMLLoader loader = new FXMLLoader(main.class.getResource("FXML_layouts\\RecognitionScreen.fxml"));
        System.out.println();
        try { recognition.getContent().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        recognitionControl = loader.getController();
        recognitionControl.setPopup(recognition);

        inputHandler();
    }

    private void inputHandler(){
        scanCube.setOnAction(event ->{
            recognitionControl.setStage(this.stage);
            recognition.show(stage);
        });

        solveCube.setOnAction(event -> {
            cubeSolver.solveCube();
            nextStep.setDisable(false);
        });

        shuffleCube.setOnAction(event -> {
            cubeSolver.shuffleCube(30);
        });

        recognition.setOnHidden(event -> {
            if(recognitionControl.isCubeFound()){
                rubiksCube.setRubiksCube(recognitionControl.getRubiksCube());
            }
        });

        nextStep.setOnAction(actionEvent -> {
            SolutionMove solutionMove = cubeSolver.getSolutionSet().getNextMove();
            if (solutionMove != null){
                prevStep.setDisable(false);
                moveInstruction.setText("Do " + solutionMove.getMoveType() + " " + solutionMove.getRotationType() + " On the " + solutionMove.getLayerColor() + " layer.");
                rubiksCube.doMove(solutionMove.getMoveType(), solutionMove.getLayerNum(), solutionMove.getRotationType());
            }

            if(!cubeSolver.getSolutionSet().hasNext())
                nextStep.setDisable(true);

            if(cubeSolver.getSolutionSet().hasPrev())
                prevStep.setDisable(false);
        });

        prevStep.setOnAction(actionEvent -> {
            SolutionMove solutionMove = cubeSolver.getSolutionSet().getPreviousMove();
            if (solutionMove != null) {
                solutionMove.flipRotation();
                moveInstruction.setText("Do " + solutionMove.getMoveType() + " " + solutionMove.getRotationType() + " On the " + solutionMove.getLayerColor() + " layer.");
                rubiksCube.doMove(solutionMove.getMoveType(), solutionMove.getLayerNum(), solutionMove.getRotationType());
                solutionMove.flipRotation();
            }

            if(!cubeSolver.getSolutionSet().hasPrev())
                prevStep.setDisable(true);

            if(cubeSolver.getSolutionSet().hasNext())
                nextStep.setDisable(false);
        });

        animationSpeed.valueProperty().addListener((observableValue, number, t1) ->{
            rubiksCube.setCubeAnimationSpeed(number.floatValue());
            System.out.println(number.floatValue());
        });
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
