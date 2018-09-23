package cubeRecognition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;
import java.io.IOException;
import java.util.HashMap;


public class RecognitionControl {
    @FXML
    private ImageView raw, filtered, found;
    @FXML
    private Rectangle redDisplay,orangeDisplay,blueDisplay,greenDisplay,whiteDisplay,yellowDisplay;
    @FXML
    private Button startButton,closeButton,goodCapture,retakeCapture;
    @FXML
    private Slider threshold;
    @FXML
    private Label thresholdLabel,instructions;
    @FXML
    private HBox hbox;

    //Stage Controls
    private Popup[] colorChooser = new Popup[6];
    private CustomPopup[] popupController = new CustomPopup[6];
    private Stage stage;
    private boolean canOpenPopup = true;
    private ImageProc imageProc;
    private Double clickX,clickY;
    private Popup popup;
    private int instruction = 0;

    //Camera Controls
    private VideoCapture capture;
    private boolean cameraActive;
    private int cameraID =0; //-1 gives a device choosing dialog
    private Mat rawMat, filteredMat,foundMat;
    DaemeonThread daemeonThread;
    boolean cubeFound = false;

    //Cube Build
    byte[] cubeColors;
    Color[][] rubiksCube = new Color[6][9];
    private HashMap<Byte,Color> intToColor;


    private String[] stages = {
      "Put any face of the cube to the camera.",
      "Rotate the cube 90° towards your left",
            "Rotate the cube 90° towards your left",
            "Rotate the cube 90° towards your left",
            "Rotate the cube 90° towards your left, then rotate the cube 90° to show the top face",
            "Rotate the cube 180° to show the bottom face"
    };

    //OpenCV Loading
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public void initialize(){
        for(int i = 0; i<6;i++){
            for(int j = 0;j<9;j++)
                rubiksCube[i][j] = Color.WHITE;
        }


        intToColor = new HashMap<>();

        intToColor.put((byte)1,Color.RED);
        intToColor.put((byte)2,Color.GREEN);
        intToColor.put((byte)3,Color.BLUE);
        intToColor.put((byte)4,Color.ORANGE);
        intToColor.put((byte)5,Color.YELLOW);
        intToColor.put((byte)6,Color.WHITE);


        initializePopups();
        imageProc = new ImageProc();
        threshold.valueProperty().addListener((observable, oldValue, newValue) -> {
            thresholdLabel.setText("Threshold: " + newValue.intValue());
            imageProc.setThreshold(newValue.intValue());
        });

        capture = new VideoCapture();
        cameraActive = false;
        startButton.setOnAction(event -> startCamera());

        hbox.setOnMousePressed(event -> {
            clickX = event.getSceneX();
            clickY = event.getSceneY();
        });

        hbox.setOnMouseDragged(event -> {
            popup.setX(event.getScreenX()-clickX);
            popup.setY(event.getScreenY()-clickY);

        });

        closeButton.setOnAction(event -> popup.hide());

        goodCapture.setOnAction(event -> {
            setCubeLayer();
            instruction++;


            retakeCapture.setDisable(true);
            goodCapture.setDisable(true);
            if(instruction==6){
                stopCamera();
                popup.hide();
                instruction=0;
            }
            cubeFound = false;
            instructions.setText(stages[instruction]);

        });

        retakeCapture.setOnAction(event -> {
            retakeCapture.setDisable(true);
            goodCapture.setDisable(true);

            cubeFound = false;
        });
        instructions.setText(stages[0]);
    }

    private void startCamera() {
        if (!cameraActive) {
            //Open Capture
            capture.open(cameraID);

            //Check if feed is live
            if (capture.isOpened()) {
                //Set Button Text and camera boolean
                startButton.setText("Stop Camera");
                cameraActive = true;
                //cameraT.start();
                daemeonThread = new DaemeonThread();
                daemeonThread.runnable = true;
                Thread t = new Thread(daemeonThread);
                t.setDaemon(true);
                t.start();
            }
        } else {
           stopCamera();
        }
    }

    private void stopCamera(){
        daemeonThread.runnable = false;
        startButton.setText("Start Camera");
        cameraActive = false;
        capture.release();
        resetImages();
    }

    private void resetImages(){
        rawMat.setTo(new Scalar(45,45,45));
        filteredMat.setTo(new Scalar(45,45,45));
        foundMat.setTo(new Scalar(45,45,45));
        raw.setImage(imageProc.matToImage(rawMat));
        filtered.setImage(imageProc.matToImage(filteredMat));
        found.setImage(imageProc.matToImage(foundMat));
    }

    private void doCamera() {
        Mat frame = new Mat();
        capture.read(frame);
        rawMat = frame;
        filteredMat = imageProc.filterImage(rawMat);

        imageProc.drawBounds(rawMat);

        if(foundMat == null) {
            foundMat = rawMat.clone();
            foundMat.setTo(new Scalar(45, 45, 45));
        }

        if(!cubeFound)
            cubeColors = imageProc.findCountours(filteredMat,rawMat,foundMat);

        if(cubeColors != null){
           cubeFound = true;
           retakeCapture.setDisable(false);
           goodCapture.setDisable(false);
        }

        raw.setImage(imageProc.matToImage(rawMat));
        filtered.setImage(imageProc.matToImage(filteredMat));
        found.setImage(imageProc.matToImage(foundMat));
    }

    private void initializePopups(){
        for (int i = 0; i < 6; i++) {
            colorChooser[i] = new Popup();
            Class c = null;
            try {
                c = Class.forName("main");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            FXMLLoader loader = new FXMLLoader(c.getResource(".\\FXML_layouts\\CustomPopup.fxml"));

            try {
                colorChooser[i].getContent().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }

            popupController[i] = loader.getController();
            popupController[i].setPopup(colorChooser[i]);

            colorChooser[i].setOnShown(event -> canOpenPopup=false);
            int finalI = i;
            colorChooser[i].setOnHidden(event -> {
                canOpenPopup=true;

                int r = (int) (popupController[finalI].getColor().getRed() * 255);
                int g = (int) (popupController[finalI].getColor().getGreen() * 255);
                int b = (int) (popupController[finalI].getColor().getBlue() * 255);

                switch (finalI){
                    case 0:
                        redDisplay.setFill(popupController[finalI].getColor());
                        imageProc.setRubiksColor(1,r,g,b);
                        break;
                    case 1:
                        orangeDisplay.setFill(popupController[finalI].getColor());
                        imageProc.setRubiksColor(4,r,g,b);
                        break;
                    case 2:
                        blueDisplay.setFill(popupController[finalI].getColor());
                        imageProc.setRubiksColor(3,r,g,b);
                        break;
                    case 3:
                        greenDisplay.setFill(popupController[finalI].getColor());
                        imageProc.setRubiksColor(2,r,g,b);
                        break;
                    case 4:
                        yellowDisplay.setFill(popupController[finalI].getColor());
                        imageProc.setRubiksColor(5,r,g,b);
                        break;
                    case 5:
                        whiteDisplay.setFill(popupController[finalI].getColor());
                        imageProc.setRubiksColor(6,r,g,b);
                        break;
                }
            });
        }

        redDisplay.setOnMouseClicked(event -> {
            if (canOpenPopup)
                colorChooser[0].show(stage);
        });
        orangeDisplay.setOnMouseClicked(event -> {
            if (canOpenPopup)
                colorChooser[1].show(stage);
        });
        blueDisplay.setOnMouseClicked(event -> {
            if (canOpenPopup)
                colorChooser[2].show(stage);
        });
        greenDisplay.setOnMouseClicked(event -> {
            if (canOpenPopup)
                colorChooser[3].show(stage);
        });
        yellowDisplay.setOnMouseClicked(event -> {
            if (canOpenPopup)
                colorChooser[4].show(stage);
        });
        whiteDisplay.setOnMouseClicked(event -> {
            if (canOpenPopup)
                colorChooser[5].show(stage);
        });

        //Set colors
        popupController[0].setColor(Color.rgb(220,0,0));
        popupController[1].setColor(Color.rgb(255,85,0));
        popupController[2].setColor(Color.rgb(0,0,255));
        popupController[3].setColor(Color.rgb(0,195,0));
        popupController[4].setColor(Color.rgb(210,210,0));
        popupController[5].setColor(Color.rgb(255,255,255));

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPopup(Popup popup){this.popup = popup;}

    private void setCubeLayer(){
        for(int i = 0; i<9;i++){
            Color c = intToColor.get(cubeColors[i]);
            rubiksCube[instruction][i] = c;
        }
    }

    class DaemeonThread implements Runnable{
        volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this){
                while (runnable){

                    if(!runnable){
                        try {
                            this.wait();
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    } else doCamera();
                }
            }
        }
    }
}

