package cube;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Objects;

public class RubiksCube {
    private Sticker[][] sticker = new Sticker[6][9];
    private EmptyCube emptyCube;
    private Group rubiksCube = new Group();
    private Group rotateLayer;
    private Layer currentLayer = new Layer();
    private double mousePosX;
    private double mousePosY;
    private boolean isMoving = false;
    private double cubeRotation = 0;
    public CubeSolver cubeSolver = new CubeSolver(this);

    //Multi Move Actions
    private boolean moveSequence = false;
    private LinkedList<MoveType> moves = null;
    private LinkedList<RotationType> movesDirection = null;
    private int sequenceIndex = 0;
    LinkedList<Integer> layerNumber = null;

    //Scene Variables
    static Label label = new Label("Number of moves to solve: 0");
    SubScene scene;
    PerspectiveCamera camera;
    Group root;
    Rotate rotateX = new Rotate(20, Rotate.X_AXIS);
    Rotate rotateY = new Rotate(35, Rotate.Y_AXIS);
    double rotatationSpeed = 0.6; //0.6
    boolean onCube = false;

    //Animation
    double cubeAnimationSpeed = 0.1;
    
    public RubiksCube(double pieceSize) {
        emptyCube = new EmptyCube(pieceSize);
        rubiksCube.getChildren().add(emptyCube.getCube());
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                sticker[i][j] = new Sticker(Color.WHITE, pieceSize - 2.1);
                rubiksCube.getChildren().add(sticker[i][j].getSticker());
            }
        }
        placeStickers();

        setScene();
    }

    public void setRubiksCube(Color[][] cube) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                sticker[i][j].setColor(cube[i][j]);
            }
        }
        updateCube();
    }

    private void setScene(){
        label.setTranslateY( -100);
        label.setTranslateX(-80);

        //Root
        root = new Group();
        root.getChildren().addAll(this.getRubiksCube());
        root.getTransforms().addAll(rotateX, rotateY);

        //Light
        AmbientLight ambientLight = new AmbientLight();
        root.getChildren().addAll(ambientLight);

        //Camera
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(Double.MAX_VALUE);
        camera.setTranslateZ(-200);

        //Scene
        scene = new SubScene(root, 485.0, 374.0, true, SceneAntialiasing.BALANCED);
        scene.setCamera(camera);

        scene.setFill(Color.rgb(80,80,80));

        actionHandler();
    }

    public void actionHandler() {

        this.getRubiksCube().setOnMousePressed(event -> onCube = true);

        this.getRubiksCube().setOnMouseReleased(event -> onCube = false);

        scene.setOnMousePressed(event -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            if (!onCube) {
                double deltaX = mousePosX - event.getSceneX();
                double deltaY = mousePosY - event.getSceneY();

                double newAngleX = rotateX.getAngle() - (deltaY * rotatationSpeed);
                double newAngleY = rotateY.getAngle() + (deltaX * rotatationSpeed);

                newAngleX = ClipValues(-45, 45, newAngleX);

                newAngleY = Math.abs(newAngleY % 360);

                if (newAngleY > 360) {
                    newAngleY = 0.1;
                }

                if (newAngleY < 1) {
                    newAngleY = 359.9;
                }

                rotateX.setAngle(newAngleX);
                rotateY.setAngle(newAngleY);

                mousePosX = event.getSceneX();
                mousePosY = event.getSceneY();
                this.setCubeRotation(rotateY.getAngle());
            }
        });

        scene.setOnScroll(event -> camera.setTranslateZ(camera.getTranslateZ() + event.getDeltaY()));

        //<editor-fold desc="Sticker Detection">
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                int finalJ = j;
                int finalI = i;

                sticker[i][j].getSticker().setOnMousePressed(event -> {
                    mousePosX = event.getSceneX();
                    mousePosY = event.getSceneY();
                });

                sticker[i][j].getSticker().setOnMouseReleased(event -> {
                    if (!isMoving) {
                        double deltaX = mousePosX - event.getSceneX();
                        double deltaY = mousePosY - event.getSceneY();

                        if (Math.abs(deltaY) > Math.abs(deltaX)) {
                            RotationType rotation;

                            //<editor-fold desc="Stickers on Red Layer">
                            if (finalI == 0) {
                                //todo: Left side of layer
                                if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                    //Choose rotation
                                    if (deltaY < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.L, LayerColor.Red, rotation);
                                }
                                //todo: middle side of layer
                                if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                    if (deltaY < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;

                                    animateRotate(MoveType.M, LayerColor.Red, rotation);
                                }
                                //todo: left side of layer
                                if (finalJ == 2 || finalJ == 5 || finalJ == 8) {
                                    if (deltaY < 0) {
                                        rotation = RotationType.CounterClockWise;
                                    } else rotation = RotationType.ClockWise;

                                    animateRotate(MoveType.R, LayerColor.Red, rotation);
                                }
                            }
                            //</editor-fold>

                            //<editor-fold desc="Stickers on Green Layer">
                            if (finalI == 1) {
                                //todo: Left side of layer
                                if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                    //Choose rotation
                                    if (deltaY < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.L, LayerColor.Green, rotation);
                                }
                                //todo: middle side of layer
                                if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                    if (deltaY < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;

                                    animateRotate(MoveType.M, LayerColor.Green, rotation);
                                }
                                //todo: left side of layer
                                if (finalJ == 2 || finalJ == 5 || finalJ == 8) {
                                    if (deltaY < 0) {
                                        rotation = RotationType.CounterClockWise;
                                    } else rotation = RotationType.ClockWise;

                                    animateRotate(MoveType.R, LayerColor.Green, rotation);
                                }
                            }
                            //</editor-fold>

                            //<editor-fold desc="Stickers on Orange Layer">
                            if (finalI == 2) {
                                //todo: Left side of layer
                                if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                    //Choose rotation
                                    if (deltaY < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.L, LayerColor.Orange, rotation);
                                }
                                //todo: middle side of layer
                                if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                    if (deltaY < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;

                                    animateRotate(MoveType.M, LayerColor.Orange, rotation);
                                }
                                //todo: left side of layer
                                if (finalJ == 2 || finalJ == 5 || finalJ == 8) {
                                    if (deltaY < 0) {
                                        rotation = RotationType.CounterClockWise;
                                    } else rotation = RotationType.ClockWise;

                                    animateRotate(MoveType.R, LayerColor.Orange, rotation);
                                }
                            }
                            //</editor-fold>

                            //<editor-fold desc="Sticker on Blue Layer">
                            if (finalI == 3) {
                                //todo: Left side of layer
                                if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                    //Choose rotation
                                    if (deltaY < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.L, LayerColor.Blue, rotation);
                                }
                                //todo: middle side of layer
                                if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                    if (deltaY < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;

                                    animateRotate(MoveType.M, LayerColor.Blue, rotation);
                                }
                                //todo: left side of layer
                                if (finalJ == 2 || finalJ == 5 || finalJ == 8) {
                                    if (deltaY < 0) {
                                        rotation = RotationType.CounterClockWise;
                                    } else rotation = RotationType.ClockWise;

                                    animateRotate(MoveType.R, LayerColor.Blue, rotation);
                                }
                            }
                            //</editor-fold>

                            //<editor-fold desc="Sticker on Yellow Layer">
                            if (finalI == 4) {
                                if (cubeRotation < 360 && cubeRotation > 315 || cubeRotation < 45 && cubeRotation > 0) {
                                    //<editor-fold desc="Facing Red">
                                    //todo: Left side of layer
                                    if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                        //Choose rotation
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.L, LayerColor.Yellow, rotation);
                                    }
                                    //todo: middle side of layer
                                    if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;

                                        animateRotate(MoveType.M, LayerColor.Yellow, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 2 || finalJ == 5 || finalJ == 8) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;

                                        animateRotate(MoveType.R, LayerColor.Yellow, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 135 && cubeRotation > 45) {
                                    //<editor-fold desc="Facing Green">
                                    if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                        //Choose rotation
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.D, LayerColor.Yellow, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.E, LayerColor.Yellow, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.U, LayerColor.Yellow, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 225 && cubeRotation > 135) {
                                    //<editor-fold desc="Facing Orange">
                                    if (finalJ == 8 || finalJ == 5 || finalJ == 2) {
                                        //Choose rotation
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.R, LayerColor.Yellow, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.M, LayerColor.Yellow, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.L, LayerColor.Yellow, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 315 && cubeRotation > 225) {
                                    //<editor-fold desc="Facing Blue">
                                    if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                        //Choose rotation
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.U, LayerColor.Yellow, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.E, LayerColor.Yellow, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.D, LayerColor.Yellow, rotation);
                                    }
                                    //</editor-fold>
                                }

                            }
                            //</editor-fold>

                            //<editor-fold desc="Sticker on White Layer">
                            if (finalI == 5) {
                                if (cubeRotation < 360 && cubeRotation > 315 || cubeRotation < 45 && cubeRotation > 0) {
                                    //<editor-fold desc="Facing Red">

                                    if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.L, LayerColor.White, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.M, LayerColor.White, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 2 || finalJ == 5 || finalJ == 8) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.R, LayerColor.White, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 135 && cubeRotation > 45) {
                                    //<editor-fold desc="Facing Green">
                                    if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                        //Choose rotation
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.U, LayerColor.White, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.E, LayerColor.White, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.D, LayerColor.White, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 225 && cubeRotation > 135) {
                                    //<editor-fold desc="Facing Orange">
                                    if (finalJ == 8 || finalJ == 5 || finalJ == 2) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.R, LayerColor.White, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.M, LayerColor.White, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.L, LayerColor.White, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 315 && cubeRotation > 225) {
                                    //<editor-fold desc="Facing Blue">
                                    if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                        //Choose rotation
                                        if (deltaY < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.U, LayerColor.White, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.E, LayerColor.White, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                        if (deltaY < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.D, LayerColor.White, rotation);
                                    }
                                    //</editor-fold>
                                }
                            }
                            //</editor-fold>
                        } else {

                            RotationType rotation;

                            //<editor-fold desc="Stickers on Red Layer">
                            if (finalI == 0) {
                                //todo: Top side of layer
                                if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                    //Choose rotation
                                    if (deltaX < 0) {
                                        rotation = RotationType.CounterClockWise;
                                    } else rotation = RotationType.ClockWise;
                                    animateRotate(MoveType.U, LayerColor.Red, rotation);
                                }
                                //todo: middle side of layer
                                if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                    if (deltaX < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.E, LayerColor.Red, rotation);
                                }
                                //todo: bottom side of layer
                                if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                    if (deltaX < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.D, LayerColor.Red, rotation);
                                }
                            }
                            //</editor-fold>

                            //<editor-fold desc="Stickers on Green Layer">
                            if (finalI == 1) {
                                //todo: Top side of layer
                                if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                    //Choose rotation
                                    if (deltaX < 0) {
                                        rotation = RotationType.CounterClockWise;
                                    } else rotation = RotationType.ClockWise;
                                    animateRotate(MoveType.U, LayerColor.Green, rotation);
                                }
                                //todo: middle side of layer
                                if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                    if (deltaX < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.E, LayerColor.Green, rotation);
                                }
                                //todo: bottom side of layer
                                if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                    if (deltaX < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.D, LayerColor.Green, rotation);
                                }
                            }
                            //</editor-fold>

                            //<editor-fold desc="Stickers on Orange Layer">
                            if (finalI == 2) {
                                //todo: Top side of layer
                                if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                    //Choose rotation
                                    if (deltaX < 0) {
                                        rotation = RotationType.CounterClockWise;
                                    } else rotation = RotationType.ClockWise;
                                    animateRotate(MoveType.U, LayerColor.Orange, rotation);
                                }
                                //todo: middle side of layer
                                if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                    if (deltaX < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.E, LayerColor.Orange, rotation);
                                }
                                //todo: bottom side of layer
                                if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                    if (deltaX < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.D, LayerColor.Orange, rotation);
                                }
                            }
                            //</editor-fold>

                            //<editor-fold desc="Sticker on Blue Layer">
                            if (finalI == 3) {
                                //todo: Top side of layer
                                if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                    //Choose rotation
                                    if (deltaX < 0) {
                                        rotation = RotationType.CounterClockWise;
                                    } else rotation = RotationType.ClockWise;
                                    animateRotate(MoveType.U, LayerColor.Blue, rotation);
                                }
                                //todo: middle side of layer
                                if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                    if (deltaX < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.E, LayerColor.Blue, rotation);
                                }
                                //todo: bottom side of layer
                                if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                    if (deltaX < 0) {
                                        rotation = RotationType.ClockWise;
                                    } else rotation = RotationType.CounterClockWise;
                                    animateRotate(MoveType.D, LayerColor.Blue, rotation);
                                }
                            }
                            //</editor-fold>

                            //<editor-fold desc="Sticker on Yellow Layer">
                            if (finalI == 4) {
                                if (cubeRotation < 360 && cubeRotation > 315 || cubeRotation < 45 && cubeRotation > 0) {
                                    //<editor-fold desc="Facing Red">
                                    if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                        //Choose rotation
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.U, LayerColor.Yellow, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.E, LayerColor.Yellow, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.D, LayerColor.Yellow, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 135 && cubeRotation > 45) {
                                    //<editor-fold desc="Facing Green">
                                    if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                        //Choose rotation
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.L, LayerColor.Yellow, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.M, LayerColor.Yellow, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 2 || finalJ == 5 || finalJ == 8) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.R, LayerColor.Yellow, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 225 && cubeRotation > 135) {
                                    //<editor-fold desc="Facing Orange">
                                    if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                        //Choose rotation
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.D, LayerColor.Yellow, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.E, LayerColor.Yellow, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.U, LayerColor.Yellow, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 315 && cubeRotation > 225) {
                                    //<editor-fold desc="Facing Blue">
                                    if (finalJ == 2 || finalJ == 5 || finalJ == 8) {
                                        //Choose rotation
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.R, LayerColor.Yellow, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.M, LayerColor.Yellow, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.L, LayerColor.Yellow, rotation);
                                    }
                                    //</editor-fold>
                                }

                            }
                            //</editor-fold>

                            //<editor-fold desc="Sticker on White Layer">
                            if (finalI == 5) {
                                if (cubeRotation < 360 && cubeRotation > 315 || cubeRotation < 45 && cubeRotation > 0) {
                                    //<editor-fold desc="Facing Red">

                                    if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                        //Choose rotation
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.U, LayerColor.White, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.E, LayerColor.White, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 6 || finalJ == 7 || finalJ == 8) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.D, LayerColor.White, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 135 && cubeRotation > 45) {
                                    //<editor-fold desc="Facing Green">
                                    if (finalJ == 2 || finalJ == 5 || finalJ == 8) {
                                        //Choose rotation
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.R, LayerColor.White, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.M, LayerColor.White, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.L, LayerColor.White, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 225 && cubeRotation > 135) {
                                    //<editor-fold desc="Facing Orange">
                                    if (finalJ == 8 || finalJ == 7 || finalJ == 6) {
                                        //Choose rotation
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.D, LayerColor.White, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 3 || finalJ == 4 || finalJ == 5) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.E, LayerColor.White, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 0 || finalJ == 1 || finalJ == 2) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.U, LayerColor.White, rotation);
                                    }
                                    //</editor-fold>
                                }
                                if (cubeRotation < 315 && cubeRotation > 225) {
                                    //<editor-fold desc="Facing Blue">
                                    if (finalJ == 0 || finalJ == 3 || finalJ == 6) {
                                        //Choose rotation
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.L, LayerColor.White, rotation);
                                    }

                                    //todo: middle side of layer
                                    if (finalJ == 1 || finalJ == 4 || finalJ == 7) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.CounterClockWise;
                                        } else rotation = RotationType.ClockWise;
                                        animateRotate(MoveType.M, LayerColor.White, rotation);
                                    }
                                    //todo: left side of layer
                                    if (finalJ == 2 || finalJ == 5 || finalJ == 8) {
                                        if (deltaX < 0) {
                                            rotation = RotationType.ClockWise;
                                        } else rotation = RotationType.CounterClockWise;
                                        animateRotate(MoveType.R, LayerColor.White, rotation);
                                    }
                                    //</editor-fold>
                                }
                            }
                            //</editor-fold>
                        }
                    }
                });
            }
        }
        //</editor-fold>

    }

    public void rotateLayer(MoveType moveType, LayerColor layerColor, RotationType rotationType) {
        //todo: set the layer
        currentLayer.setLayer(this.sticker);
        currentLayer.chooseMove(layerColor,rotationType,moveType);
        sticker = currentLayer.getSticker();
        updateCube();
    }

    private void updateCube(){
        rubiksCube.getChildren().clear();
        rubiksCube.getChildren().add(emptyCube.getCube());
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                rubiksCube.getChildren().add(sticker[i][j].getSticker());
            }
        }
    }

    private void placeStickers() {
        placeRedStickers();
        placeGreenStickers();
        placeOrangeStickers();
        placeBlueStickers();
        placeYellowStickers();
        placeWhiteStickers();
    }

    private void placeRedStickers() {
        //todo: set color
        for (int i = 0; i < 9; i++) {
            sticker[0][i].setColor(Color.RED);
        }

        //todo: set red layer stickers
        sticker[0][0].getSticker().setTranslateX(emptyCube.getPiece()[0][6].getTranslateX());
        sticker[0][0].getSticker().setTranslateY(emptyCube.getPiece()[0][6].getTranslateY());
        sticker[0][0].getSticker().setTranslateZ(emptyCube.getPiece()[0][6].getTranslateZ() - 10.5);

        sticker[0][1].getSticker().setTranslateX(emptyCube.getPiece()[0][7].getTranslateX());
        sticker[0][1].getSticker().setTranslateY(emptyCube.getPiece()[0][7].getTranslateY());
        sticker[0][1].getSticker().setTranslateZ(emptyCube.getPiece()[0][7].getTranslateZ() - 10.5);

        sticker[0][2].getSticker().setTranslateX(emptyCube.getPiece()[0][8].getTranslateX());
        sticker[0][2].getSticker().setTranslateY(emptyCube.getPiece()[0][8].getTranslateY());
        sticker[0][2].getSticker().setTranslateZ(emptyCube.getPiece()[0][8].getTranslateZ() - 10.5);

        sticker[0][3].getSticker().setTranslateX(emptyCube.getPiece()[1][6].getTranslateX());
        sticker[0][3].getSticker().setTranslateY(emptyCube.getPiece()[1][6].getTranslateY());
        sticker[0][3].getSticker().setTranslateZ(emptyCube.getPiece()[1][6].getTranslateZ() - 10.5);

        sticker[0][4].getSticker().setTranslateX(emptyCube.getPiece()[1][7].getTranslateX());
        sticker[0][4].getSticker().setTranslateY(emptyCube.getPiece()[1][7].getTranslateY());
        sticker[0][4].getSticker().setTranslateZ(emptyCube.getPiece()[1][7].getTranslateZ() - 10.5);

        sticker[0][5].getSticker().setTranslateX(emptyCube.getPiece()[1][8].getTranslateX());
        sticker[0][5].getSticker().setTranslateY(emptyCube.getPiece()[1][8].getTranslateY());
        sticker[0][5].getSticker().setTranslateZ(emptyCube.getPiece()[1][8].getTranslateZ() - 10.5);

        sticker[0][6].getSticker().setTranslateX(emptyCube.getPiece()[2][6].getTranslateX());
        sticker[0][6].getSticker().setTranslateY(emptyCube.getPiece()[2][6].getTranslateY());
        sticker[0][6].getSticker().setTranslateZ(emptyCube.getPiece()[2][6].getTranslateZ() - 10.5);

        sticker[0][7].getSticker().setTranslateX(emptyCube.getPiece()[2][7].getTranslateX());
        sticker[0][7].getSticker().setTranslateY(emptyCube.getPiece()[2][7].getTranslateY());
        sticker[0][7].getSticker().setTranslateZ(emptyCube.getPiece()[2][7].getTranslateZ() - 10.5);

        sticker[0][8].getSticker().setTranslateX(emptyCube.getPiece()[2][8].getTranslateX());
        sticker[0][8].getSticker().setTranslateY(emptyCube.getPiece()[2][8].getTranslateY());
        sticker[0][8].getSticker().setTranslateZ(emptyCube.getPiece()[2][8].getTranslateZ() - 10.5);
    }

    private void placeGreenStickers() {
        //todo: rotate stickers and set color
        for (int i = 0; i < 9; i++) {
            sticker[1][i].getSticker().getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
            sticker[1][i].setColor(Color.GREEN);
        }
        //todo: set green layer stickers
        sticker[1][0].getSticker().setTranslateX(emptyCube.getPiece()[0][8].getTranslateX() + 10.5);
        sticker[1][0].getSticker().setTranslateY(emptyCube.getPiece()[0][8].getTranslateY());
        sticker[1][0].getSticker().setTranslateZ(emptyCube.getPiece()[0][8].getTranslateZ());

        sticker[1][1].getSticker().setTranslateX(emptyCube.getPiece()[0][5].getTranslateX() + 10.5);
        sticker[1][1].getSticker().setTranslateY(emptyCube.getPiece()[0][5].getTranslateY());
        sticker[1][1].getSticker().setTranslateZ(emptyCube.getPiece()[0][5].getTranslateZ());

        sticker[1][2].getSticker().setTranslateX(emptyCube.getPiece()[0][2].getTranslateX() + 10.5);
        sticker[1][2].getSticker().setTranslateY(emptyCube.getPiece()[0][2].getTranslateY());
        sticker[1][2].getSticker().setTranslateZ(emptyCube.getPiece()[0][2].getTranslateZ());

        sticker[1][3].getSticker().setTranslateX(emptyCube.getPiece()[1][8].getTranslateX() + 10.5);
        sticker[1][3].getSticker().setTranslateY(emptyCube.getPiece()[1][8].getTranslateY());
        sticker[1][3].getSticker().setTranslateZ(emptyCube.getPiece()[1][8].getTranslateZ());

        sticker[1][4].getSticker().setTranslateX(emptyCube.getPiece()[1][5].getTranslateX() + 10.5);
        sticker[1][4].getSticker().setTranslateY(emptyCube.getPiece()[1][5].getTranslateY());
        sticker[1][4].getSticker().setTranslateZ(emptyCube.getPiece()[1][5].getTranslateZ());

        sticker[1][5].getSticker().setTranslateX(emptyCube.getPiece()[1][2].getTranslateX() + 10.5);
        sticker[1][5].getSticker().setTranslateY(emptyCube.getPiece()[1][2].getTranslateY());
        sticker[1][5].getSticker().setTranslateZ(emptyCube.getPiece()[1][2].getTranslateZ());

        sticker[1][6].getSticker().setTranslateX(emptyCube.getPiece()[2][8].getTranslateX() + 10.5);
        sticker[1][6].getSticker().setTranslateY(emptyCube.getPiece()[2][8].getTranslateY());
        sticker[1][6].getSticker().setTranslateZ(emptyCube.getPiece()[2][8].getTranslateZ());

        sticker[1][7].getSticker().setTranslateX(emptyCube.getPiece()[2][5].getTranslateX() + 10.5);
        sticker[1][7].getSticker().setTranslateY(emptyCube.getPiece()[2][5].getTranslateY());
        sticker[1][7].getSticker().setTranslateZ(emptyCube.getPiece()[2][5].getTranslateZ());

        sticker[1][8].getSticker().setTranslateX(emptyCube.getPiece()[2][2].getTranslateX() + 10.5);
        sticker[1][8].getSticker().setTranslateY(emptyCube.getPiece()[2][2].getTranslateY());
        sticker[1][8].getSticker().setTranslateZ(emptyCube.getPiece()[2][2].getTranslateZ());
    }

    private void placeOrangeStickers() {
        //todo: set color
        for (int i = 0; i < 9; i++) {
            sticker[2][i].setColor(Color.ORANGE);
        }

        //todo: set orange layer stickers
        sticker[2][0].getSticker().setTranslateX(emptyCube.getPiece()[0][2].getTranslateX());
        sticker[2][0].getSticker().setTranslateY(emptyCube.getPiece()[0][2].getTranslateY());
        sticker[2][0].getSticker().setTranslateZ(emptyCube.getPiece()[0][2].getTranslateZ() + 10.5);

        sticker[2][1].getSticker().setTranslateX(emptyCube.getPiece()[0][1].getTranslateX());
        sticker[2][1].getSticker().setTranslateY(emptyCube.getPiece()[0][1].getTranslateY());
        sticker[2][1].getSticker().setTranslateZ(emptyCube.getPiece()[0][1].getTranslateZ() + 10.5);

        sticker[2][2].getSticker().setTranslateX(emptyCube.getPiece()[0][0].getTranslateX());
        sticker[2][2].getSticker().setTranslateY(emptyCube.getPiece()[0][0].getTranslateY());
        sticker[2][2].getSticker().setTranslateZ(emptyCube.getPiece()[0][0].getTranslateZ() + 10.5);

        sticker[2][3].getSticker().setTranslateX(emptyCube.getPiece()[1][2].getTranslateX());
        sticker[2][3].getSticker().setTranslateY(emptyCube.getPiece()[1][2].getTranslateY());
        sticker[2][3].getSticker().setTranslateZ(emptyCube.getPiece()[1][2].getTranslateZ() + 10.5);

        sticker[2][4].getSticker().setTranslateX(emptyCube.getPiece()[1][1].getTranslateX());
        sticker[2][4].getSticker().setTranslateY(emptyCube.getPiece()[1][1].getTranslateY());
        sticker[2][4].getSticker().setTranslateZ(emptyCube.getPiece()[1][1].getTranslateZ() + 10.5);

        sticker[2][5].getSticker().setTranslateX(emptyCube.getPiece()[1][0].getTranslateX());
        sticker[2][5].getSticker().setTranslateY(emptyCube.getPiece()[1][0].getTranslateY());
        sticker[2][5].getSticker().setTranslateZ(emptyCube.getPiece()[1][0].getTranslateZ() + 10.5);

        sticker[2][6].getSticker().setTranslateX(emptyCube.getPiece()[2][2].getTranslateX());
        sticker[2][6].getSticker().setTranslateY(emptyCube.getPiece()[2][2].getTranslateY());
        sticker[2][6].getSticker().setTranslateZ(emptyCube.getPiece()[2][2].getTranslateZ() + 10.5);

        sticker[2][7].getSticker().setTranslateX(emptyCube.getPiece()[2][1].getTranslateX());
        sticker[2][7].getSticker().setTranslateY(emptyCube.getPiece()[2][1].getTranslateY());
        sticker[2][7].getSticker().setTranslateZ(emptyCube.getPiece()[2][1].getTranslateZ() + 10.5);

        sticker[2][8].getSticker().setTranslateX(emptyCube.getPiece()[2][0].getTranslateX());
        sticker[2][8].getSticker().setTranslateY(emptyCube.getPiece()[2][0].getTranslateY());
        sticker[2][8].getSticker().setTranslateZ(emptyCube.getPiece()[2][0].getTranslateZ() + 10.5);
    }

    private void placeBlueStickers() {
        //todo: rotate stickers and set color
        for (int i = 0; i < 9; i++) {
            sticker[3][i].getSticker().getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
            sticker[3][i].setColor(Color.BLUE);
        }
        //todo: set blue layer stickers
        sticker[3][0].getSticker().setTranslateX(emptyCube.getPiece()[0][0].getTranslateX() - 10.5);
        sticker[3][0].getSticker().setTranslateY(emptyCube.getPiece()[0][0].getTranslateY());
        sticker[3][0].getSticker().setTranslateZ(emptyCube.getPiece()[0][0].getTranslateZ());

        sticker[3][1].getSticker().setTranslateX(emptyCube.getPiece()[0][3].getTranslateX() - 10.5);
        sticker[3][1].getSticker().setTranslateY(emptyCube.getPiece()[0][3].getTranslateY());
        sticker[3][1].getSticker().setTranslateZ(emptyCube.getPiece()[0][3].getTranslateZ());

        sticker[3][2].getSticker().setTranslateX(emptyCube.getPiece()[0][6].getTranslateX() - 10.5);
        sticker[3][2].getSticker().setTranslateY(emptyCube.getPiece()[0][6].getTranslateY());
        sticker[3][2].getSticker().setTranslateZ(emptyCube.getPiece()[0][6].getTranslateZ());

        sticker[3][3].getSticker().setTranslateX(emptyCube.getPiece()[1][0].getTranslateX() - 10.5);
        sticker[3][3].getSticker().setTranslateY(emptyCube.getPiece()[1][0].getTranslateY());
        sticker[3][3].getSticker().setTranslateZ(emptyCube.getPiece()[1][0].getTranslateZ());

        sticker[3][4].getSticker().setTranslateX(emptyCube.getPiece()[1][3].getTranslateX() - 10.5);
        sticker[3][4].getSticker().setTranslateY(emptyCube.getPiece()[1][3].getTranslateY());
        sticker[3][4].getSticker().setTranslateZ(emptyCube.getPiece()[1][3].getTranslateZ());

        sticker[3][5].getSticker().setTranslateX(emptyCube.getPiece()[1][6].getTranslateX() - 10.5);
        sticker[3][5].getSticker().setTranslateY(emptyCube.getPiece()[1][6].getTranslateY());
        sticker[3][5].getSticker().setTranslateZ(emptyCube.getPiece()[1][6].getTranslateZ());

        sticker[3][6].getSticker().setTranslateX(emptyCube.getPiece()[2][0].getTranslateX() - 10.5);
        sticker[3][6].getSticker().setTranslateY(emptyCube.getPiece()[2][0].getTranslateY());
        sticker[3][6].getSticker().setTranslateZ(emptyCube.getPiece()[2][0].getTranslateZ());

        sticker[3][7].getSticker().setTranslateX(emptyCube.getPiece()[2][3].getTranslateX() - 10.5);
        sticker[3][7].getSticker().setTranslateY(emptyCube.getPiece()[2][3].getTranslateY());
        sticker[3][7].getSticker().setTranslateZ(emptyCube.getPiece()[2][3].getTranslateZ());

        sticker[3][8].getSticker().setTranslateX(emptyCube.getPiece()[2][6].getTranslateX() - 10.5);
        sticker[3][8].getSticker().setTranslateY(emptyCube.getPiece()[2][6].getTranslateY());
        sticker[3][8].getSticker().setTranslateZ(emptyCube.getPiece()[2][6].getTranslateZ());
    }

    private void placeYellowStickers() {
        //todo: rotate stickers and set color
        for (int i = 0; i < 9; i++) {
            sticker[4][i].getSticker().getTransforms().add(new Rotate(90, Rotate.X_AXIS));
            sticker[4][i].setColor(Color.YELLOW);
        }
        //todo: set yellow layer stickers
        sticker[4][0].getSticker().setTranslateX(emptyCube.getPiece()[0][0].getTranslateX());
        sticker[4][0].getSticker().setTranslateY(emptyCube.getPiece()[0][0].getTranslateY() - 10.5);
        sticker[4][0].getSticker().setTranslateZ(emptyCube.getPiece()[0][0].getTranslateZ());

        sticker[4][1].getSticker().setTranslateX(emptyCube.getPiece()[0][1].getTranslateX());
        sticker[4][1].getSticker().setTranslateY(emptyCube.getPiece()[0][1].getTranslateY() - 10.5);
        sticker[4][1].getSticker().setTranslateZ(emptyCube.getPiece()[0][1].getTranslateZ());

        sticker[4][2].getSticker().setTranslateX(emptyCube.getPiece()[0][2].getTranslateX());
        sticker[4][2].getSticker().setTranslateY(emptyCube.getPiece()[0][2].getTranslateY() - 10.5);
        sticker[4][2].getSticker().setTranslateZ(emptyCube.getPiece()[0][2].getTranslateZ());

        sticker[4][3].getSticker().setTranslateX(emptyCube.getPiece()[0][3].getTranslateX());
        sticker[4][3].getSticker().setTranslateY(emptyCube.getPiece()[0][3].getTranslateY() - 10.5);
        sticker[4][3].getSticker().setTranslateZ(emptyCube.getPiece()[0][3].getTranslateZ());

        sticker[4][4].getSticker().setTranslateX(emptyCube.getPiece()[0][4].getTranslateX());
        sticker[4][4].getSticker().setTranslateY(emptyCube.getPiece()[0][4].getTranslateY() - 10.5);
        sticker[4][4].getSticker().setTranslateZ(emptyCube.getPiece()[0][4].getTranslateZ());

        sticker[4][5].getSticker().setTranslateX(emptyCube.getPiece()[0][5].getTranslateX());
        sticker[4][5].getSticker().setTranslateY(emptyCube.getPiece()[0][5].getTranslateY() - 10.5);
        sticker[4][5].getSticker().setTranslateZ(emptyCube.getPiece()[0][5].getTranslateZ());

        sticker[4][6].getSticker().setTranslateX(emptyCube.getPiece()[0][6].getTranslateX());
        sticker[4][6].getSticker().setTranslateY(emptyCube.getPiece()[0][6].getTranslateY() - 10.5);
        sticker[4][6].getSticker().setTranslateZ(emptyCube.getPiece()[0][6].getTranslateZ());

        sticker[4][7].getSticker().setTranslateX(emptyCube.getPiece()[0][7].getTranslateX());
        sticker[4][7].getSticker().setTranslateY(emptyCube.getPiece()[0][7].getTranslateY() - 10.5);
        sticker[4][7].getSticker().setTranslateZ(emptyCube.getPiece()[0][7].getTranslateZ());

        sticker[4][8].getSticker().setTranslateX(emptyCube.getPiece()[0][8].getTranslateX());
        sticker[4][8].getSticker().setTranslateY(emptyCube.getPiece()[0][8].getTranslateY() - 10.5);
        sticker[4][8].getSticker().setTranslateZ(emptyCube.getPiece()[0][8].getTranslateZ());
    }

    private void placeWhiteStickers() {
        //todo: rotate stickers and set color
        for (int i = 0; i < 9; i++) {
            sticker[5][i].getSticker().getTransforms().add(new Rotate(90, Rotate.X_AXIS));
            sticker[5][i].setColor(Color.WHITE);
        }
        //todo: set yellow layer stickers
        sticker[5][0].getSticker().setTranslateX(emptyCube.getPiece()[2][6].getTranslateX());
        sticker[5][0].getSticker().setTranslateY(emptyCube.getPiece()[2][6].getTranslateY() + 10.5);
        sticker[5][0].getSticker().setTranslateZ(emptyCube.getPiece()[2][6].getTranslateZ());

        sticker[5][1].getSticker().setTranslateX(emptyCube.getPiece()[2][7].getTranslateX());
        sticker[5][1].getSticker().setTranslateY(emptyCube.getPiece()[2][7].getTranslateY() + 10.5);
        sticker[5][1].getSticker().setTranslateZ(emptyCube.getPiece()[2][7].getTranslateZ());

        sticker[5][2].getSticker().setTranslateX(emptyCube.getPiece()[2][8].getTranslateX());
        sticker[5][2].getSticker().setTranslateY(emptyCube.getPiece()[2][8].getTranslateY() + 10.5);
        sticker[5][2].getSticker().setTranslateZ(emptyCube.getPiece()[2][8].getTranslateZ());

        sticker[5][3].getSticker().setTranslateX(emptyCube.getPiece()[2][3].getTranslateX());
        sticker[5][3].getSticker().setTranslateY(emptyCube.getPiece()[2][3].getTranslateY() + 10.5);
        sticker[5][3].getSticker().setTranslateZ(emptyCube.getPiece()[2][3].getTranslateZ());

        sticker[5][4].getSticker().setTranslateX(emptyCube.getPiece()[2][4].getTranslateX());
        sticker[5][4].getSticker().setTranslateY(emptyCube.getPiece()[2][4].getTranslateY() + 10.5);
        sticker[5][4].getSticker().setTranslateZ(emptyCube.getPiece()[2][4].getTranslateZ());

        sticker[5][5].getSticker().setTranslateX(emptyCube.getPiece()[2][5].getTranslateX());
        sticker[5][5].getSticker().setTranslateY(emptyCube.getPiece()[2][5].getTranslateY() + 10.5);
        sticker[5][5].getSticker().setTranslateZ(emptyCube.getPiece()[2][5].getTranslateZ());

        sticker[5][6].getSticker().setTranslateX(emptyCube.getPiece()[2][0].getTranslateX());
        sticker[5][6].getSticker().setTranslateY(emptyCube.getPiece()[2][0].getTranslateY() + 10.5);
        sticker[5][6].getSticker().setTranslateZ(emptyCube.getPiece()[2][0].getTranslateZ());

        sticker[5][7].getSticker().setTranslateX(emptyCube.getPiece()[2][1].getTranslateX());
        sticker[5][7].getSticker().setTranslateY(emptyCube.getPiece()[2][1].getTranslateY() + 10.5);
        sticker[5][7].getSticker().setTranslateZ(emptyCube.getPiece()[2][1].getTranslateZ());

        sticker[5][8].getSticker().setTranslateX(emptyCube.getPiece()[2][2].getTranslateX());
        sticker[5][8].getSticker().setTranslateY(emptyCube.getPiece()[2][2].getTranslateY() + 10.5);
        sticker[5][8].getSticker().setTranslateZ(emptyCube.getPiece()[2][2].getTranslateZ());
    }

    private Group getLayer(MoveType moveType, LayerColor layerColor) {

        switch (moveType) {
            //<editor-fold desc="Equatorial Move">
            case E:

                if (layerColor == LayerColor.Red || layerColor == LayerColor.Green ||
                        layerColor == LayerColor.Orange || layerColor == LayerColor.Blue) {
                    return new Group(emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][1], emptyCube.getPiece()[1][2],
                            emptyCube.getPiece()[1][3], emptyCube.getPiece()[1][4], emptyCube.getPiece()[1][5],
                            emptyCube.getPiece()[1][6], emptyCube.getPiece()[1][7], emptyCube.getPiece()[1][8],
                            sticker[0][3].getSticker(), sticker[0][4].getSticker(), sticker[0][5].getSticker(),
                            sticker[1][3].getSticker(), sticker[1][4].getSticker(), sticker[1][5].getSticker(),
                            sticker[2][3].getSticker(), sticker[2][4].getSticker(), sticker[2][5].getSticker(),
                            sticker[3][3].getSticker(), sticker[3][4].getSticker(), sticker[3][5].getSticker());
                } else {
                    return new Group(emptyCube.getPiece()[0][3], emptyCube.getPiece()[0][4], emptyCube.getPiece()[0][5],
                            emptyCube.getPiece()[1][3], emptyCube.getPiece()[1][4], emptyCube.getPiece()[1][5],
                            emptyCube.getPiece()[2][3], emptyCube.getPiece()[2][4], emptyCube.getPiece()[2][5],
                            sticker[1][1].getSticker(), sticker[1][4].getSticker(), sticker[1][7].getSticker(),
                            sticker[3][1].getSticker(), sticker[3][4].getSticker(), sticker[3][7].getSticker(),
                            sticker[4][3].getSticker(), sticker[4][4].getSticker(), sticker[4][5].getSticker(),
                            sticker[5][3].getSticker(), sticker[5][4].getSticker(), sticker[5][5].getSticker());
                }
                //</editor-fold>
                // <editor-fold desc="Middle Move">
            case M:

                if (layerColor == LayerColor.Red || layerColor == LayerColor.Yellow ||
                        layerColor == LayerColor.Orange || layerColor == LayerColor.White) {
                    return new Group(emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][4], emptyCube.getPiece()[0][7],
                            emptyCube.getPiece()[1][1], emptyCube.getPiece()[1][4], emptyCube.getPiece()[1][7],
                            emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][4], emptyCube.getPiece()[2][7],
                            sticker[0][1].getSticker(), sticker[0][4].getSticker(), sticker[0][7].getSticker(),
                            sticker[2][1].getSticker(), sticker[2][4].getSticker(), sticker[2][7].getSticker(),
                            sticker[4][1].getSticker(), sticker[4][4].getSticker(), sticker[4][7].getSticker(),
                            sticker[5][1].getSticker(), sticker[5][4].getSticker(), sticker[5][7].getSticker());
                } else {
                    return new Group(emptyCube.getPiece()[0][3], emptyCube.getPiece()[0][4], emptyCube.getPiece()[0][5],
                            emptyCube.getPiece()[1][3], emptyCube.getPiece()[1][4], emptyCube.getPiece()[1][5],
                            emptyCube.getPiece()[2][3], emptyCube.getPiece()[2][4], emptyCube.getPiece()[2][5],
                            sticker[1][1].getSticker(), sticker[1][4].getSticker(), sticker[1][7].getSticker(),
                            sticker[3][1].getSticker(), sticker[3][4].getSticker(), sticker[3][7].getSticker(),
                            sticker[4][3].getSticker(), sticker[4][4].getSticker(), sticker[4][5].getSticker(),
                            sticker[5][3].getSticker(), sticker[5][4].getSticker(), sticker[5][5].getSticker());
                }
                //</editor-fold>
                //<editor-fold desc="Standing Move">
            case S:
                if (layerColor == LayerColor.Red || layerColor == LayerColor.Orange) {
                    return new Group(emptyCube.getPiece()[0][3], emptyCube.getPiece()[0][4], emptyCube.getPiece()[0][5],
                            emptyCube.getPiece()[1][3], emptyCube.getPiece()[1][4], emptyCube.getPiece()[1][5],
                            emptyCube.getPiece()[2][3], emptyCube.getPiece()[2][4], emptyCube.getPiece()[2][5],
                            sticker[1][1].getSticker(), sticker[1][4].getSticker(), sticker[1][7].getSticker(),
                            sticker[3][1].getSticker(), sticker[3][4].getSticker(), sticker[3][7].getSticker(),
                            sticker[4][3].getSticker(), sticker[4][4].getSticker(), sticker[4][5].getSticker(),
                            sticker[5][3].getSticker(), sticker[5][4].getSticker(), sticker[5][5].getSticker());
                }
                if (layerColor == LayerColor.Green || layerColor == LayerColor.Blue) {
                    return new Group(emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][4], emptyCube.getPiece()[0][7],
                            emptyCube.getPiece()[1][1], emptyCube.getPiece()[1][4], emptyCube.getPiece()[1][7],
                            emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][4], emptyCube.getPiece()[2][7],
                            sticker[0][1].getSticker(), sticker[0][4].getSticker(), sticker[0][7].getSticker(),
                            sticker[2][1].getSticker(), sticker[2][4].getSticker(), sticker[2][7].getSticker(),
                            sticker[4][1].getSticker(), sticker[4][4].getSticker(), sticker[4][7].getSticker(),
                            sticker[5][1].getSticker(), sticker[5][4].getSticker(), sticker[5][7].getSticker());
                }
                if (layerColor == LayerColor.Yellow || layerColor == LayerColor.White) {
                    return new Group(emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][1], emptyCube.getPiece()[1][2],
                            emptyCube.getPiece()[1][3], emptyCube.getPiece()[1][4], emptyCube.getPiece()[1][5],
                            emptyCube.getPiece()[1][6], emptyCube.getPiece()[1][7], emptyCube.getPiece()[1][8],
                            sticker[0][3].getSticker(), sticker[0][4].getSticker(), sticker[0][5].getSticker(),
                            sticker[1][3].getSticker(), sticker[1][4].getSticker(), sticker[1][5].getSticker(),
                            sticker[2][3].getSticker(), sticker[2][4].getSticker(), sticker[2][5].getSticker(),
                            sticker[3][3].getSticker(), sticker[3][4].getSticker(), sticker[3][5].getSticker());
                }
                //</editor-fold>
                //<editor-fold desc="Right Move">
            case R:
                //<editor-fold desc="Return Green">
                if (layerColor == LayerColor.Red || layerColor == LayerColor.Yellow || layerColor == LayerColor.White) {
                    return new Group(emptyCube.getPiece()[0][2], emptyCube.getPiece()[0][5], emptyCube.getPiece()[0][8],
                            emptyCube.getPiece()[1][2], emptyCube.getPiece()[1][5], emptyCube.getPiece()[1][8],
                            emptyCube.getPiece()[2][2], emptyCube.getPiece()[2][5], emptyCube.getPiece()[2][8],
                            sticker[1][0].getSticker(), sticker[1][1].getSticker(), sticker[1][2].getSticker(),
                            sticker[1][3].getSticker(), sticker[1][4].getSticker(), sticker[1][5].getSticker(),
                            sticker[1][6].getSticker(), sticker[1][7].getSticker(), sticker[1][8].getSticker(),
                            sticker[0][2].getSticker(), sticker[0][5].getSticker(), sticker[0][8].getSticker(),
                            sticker[2][0].getSticker(), sticker[2][3].getSticker(), sticker[2][6].getSticker(),
                            sticker[4][2].getSticker(), sticker[4][5].getSticker(), sticker[4][8].getSticker(),
                            sticker[5][2].getSticker(), sticker[5][5].getSticker(), sticker[5][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Orange">
                if (layerColor == LayerColor.Green) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][2],
                            emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][1], emptyCube.getPiece()[1][2],
                            emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][2],
                            sticker[2][0].getSticker(), sticker[2][1].getSticker(), sticker[2][2].getSticker(),
                            sticker[2][3].getSticker(), sticker[2][4].getSticker(), sticker[2][5].getSticker(),
                            sticker[2][6].getSticker(), sticker[2][7].getSticker(), sticker[2][8].getSticker(),
                            sticker[1][2].getSticker(), sticker[1][5].getSticker(), sticker[1][8].getSticker(),
                            sticker[3][0].getSticker(), sticker[3][3].getSticker(), sticker[3][6].getSticker(),
                            sticker[4][0].getSticker(), sticker[4][1].getSticker(), sticker[4][2].getSticker(),
                            sticker[5][6].getSticker(), sticker[5][7].getSticker(), sticker[5][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Blue">
                if (layerColor == LayerColor.Orange) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][3], emptyCube.getPiece()[0][6],
                            emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][3], emptyCube.getPiece()[1][6],
                            emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][3], emptyCube.getPiece()[2][6],
                            sticker[3][0].getSticker(), sticker[3][1].getSticker(), sticker[3][2].getSticker(),
                            sticker[3][3].getSticker(), sticker[3][4].getSticker(), sticker[3][5].getSticker(),
                            sticker[3][6].getSticker(), sticker[3][7].getSticker(), sticker[3][8].getSticker(),
                            sticker[0][0].getSticker(), sticker[0][3].getSticker(), sticker[0][6].getSticker(),
                            sticker[4][0].getSticker(), sticker[4][3].getSticker(), sticker[4][6].getSticker(),
                            sticker[2][2].getSticker(), sticker[2][5].getSticker(), sticker[2][8].getSticker(),
                            sticker[5][0].getSticker(), sticker[5][3].getSticker(), sticker[5][6].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Red">
                if (layerColor == LayerColor.Blue) {
                    return new Group(emptyCube.getPiece()[0][6], emptyCube.getPiece()[0][7], emptyCube.getPiece()[0][8],
                            emptyCube.getPiece()[1][6], emptyCube.getPiece()[1][7], emptyCube.getPiece()[1][8],
                            emptyCube.getPiece()[2][6], emptyCube.getPiece()[2][7], emptyCube.getPiece()[2][8],
                            sticker[0][0].getSticker(), sticker[0][1].getSticker(), sticker[0][2].getSticker(),
                            sticker[0][3].getSticker(), sticker[0][4].getSticker(), sticker[0][5].getSticker(),
                            sticker[0][6].getSticker(), sticker[0][7].getSticker(), sticker[0][8].getSticker(),
                            sticker[4][6].getSticker(), sticker[4][7].getSticker(), sticker[4][8].getSticker(),
                            sticker[1][0].getSticker(), sticker[1][3].getSticker(), sticker[1][6].getSticker(),
                            sticker[5][0].getSticker(), sticker[5][1].getSticker(), sticker[5][2].getSticker(),
                            sticker[3][2].getSticker(), sticker[3][5].getSticker(), sticker[3][8].getSticker());
                }
                //</editor-fold>
                //</editor-fold>
                //<editor-fold desc="Left Move">
            case L:
                //<editor-fold desc="Return Green">
                if (layerColor == LayerColor.Orange) {
                    return new Group(emptyCube.getPiece()[0][2], emptyCube.getPiece()[0][5], emptyCube.getPiece()[0][8],
                            emptyCube.getPiece()[1][2], emptyCube.getPiece()[1][5], emptyCube.getPiece()[1][8],
                            emptyCube.getPiece()[2][2], emptyCube.getPiece()[2][5], emptyCube.getPiece()[2][8],
                            sticker[1][0].getSticker(), sticker[1][1].getSticker(), sticker[1][2].getSticker(),
                            sticker[1][3].getSticker(), sticker[1][4].getSticker(), sticker[1][5].getSticker(),
                            sticker[1][6].getSticker(), sticker[1][7].getSticker(), sticker[1][8].getSticker(),
                            sticker[0][2].getSticker(), sticker[0][5].getSticker(), sticker[0][8].getSticker(),
                            sticker[2][0].getSticker(), sticker[2][3].getSticker(), sticker[2][6].getSticker(),
                            sticker[4][2].getSticker(), sticker[4][5].getSticker(), sticker[4][8].getSticker(),
                            sticker[5][2].getSticker(), sticker[5][5].getSticker(), sticker[5][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Orange">
                if (layerColor == LayerColor.Blue) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][2],
                            emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][1], emptyCube.getPiece()[1][2],
                            emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][2],
                            sticker[2][0].getSticker(), sticker[2][1].getSticker(), sticker[2][2].getSticker(),
                            sticker[2][3].getSticker(), sticker[2][4].getSticker(), sticker[2][5].getSticker(),
                            sticker[2][6].getSticker(), sticker[2][7].getSticker(), sticker[2][8].getSticker(),
                            sticker[1][2].getSticker(), sticker[1][5].getSticker(), sticker[1][8].getSticker(),
                            sticker[3][0].getSticker(), sticker[3][3].getSticker(), sticker[3][6].getSticker(),
                            sticker[4][0].getSticker(), sticker[4][1].getSticker(), sticker[4][2].getSticker(),
                            sticker[5][6].getSticker(), sticker[5][7].getSticker(), sticker[5][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Blue">
                if (layerColor == LayerColor.Red || layerColor == LayerColor.Yellow || layerColor == LayerColor.White) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][3], emptyCube.getPiece()[0][6],
                            emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][3], emptyCube.getPiece()[1][6],
                            emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][3], emptyCube.getPiece()[2][6],
                            sticker[3][0].getSticker(), sticker[3][1].getSticker(), sticker[3][2].getSticker(),
                            sticker[3][3].getSticker(), sticker[3][4].getSticker(), sticker[3][5].getSticker(),
                            sticker[3][6].getSticker(), sticker[3][7].getSticker(), sticker[3][8].getSticker(),
                            sticker[0][0].getSticker(), sticker[0][3].getSticker(), sticker[0][6].getSticker(),
                            sticker[4][0].getSticker(), sticker[4][3].getSticker(), sticker[4][6].getSticker(),
                            sticker[2][2].getSticker(), sticker[2][5].getSticker(), sticker[2][8].getSticker(),
                            sticker[5][0].getSticker(), sticker[5][3].getSticker(), sticker[5][6].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Red">
                if (layerColor == LayerColor.Green) {
                    return new Group(emptyCube.getPiece()[0][6], emptyCube.getPiece()[0][7], emptyCube.getPiece()[0][8],
                            emptyCube.getPiece()[1][6], emptyCube.getPiece()[1][7], emptyCube.getPiece()[1][8],
                            emptyCube.getPiece()[2][6], emptyCube.getPiece()[2][7], emptyCube.getPiece()[2][8],
                            sticker[0][0].getSticker(), sticker[0][1].getSticker(), sticker[0][2].getSticker(),
                            sticker[0][3].getSticker(), sticker[0][4].getSticker(), sticker[0][5].getSticker(),
                            sticker[0][6].getSticker(), sticker[0][7].getSticker(), sticker[0][8].getSticker(),
                            sticker[4][6].getSticker(), sticker[4][7].getSticker(), sticker[4][8].getSticker(),
                            sticker[1][0].getSticker(), sticker[1][3].getSticker(), sticker[1][6].getSticker(),
                            sticker[5][0].getSticker(), sticker[5][1].getSticker(), sticker[5][2].getSticker(),
                            sticker[3][2].getSticker(), sticker[3][5].getSticker(), sticker[3][8].getSticker());
                }
                //</editor-fold>
                //</editor-fold>
                //<editor-fold desc="Down Move">
            case D:

                //<editor-fold desc="Return White">
                if (layerColor == LayerColor.Red || layerColor == LayerColor.Green ||
                        layerColor == LayerColor.Orange || layerColor == LayerColor.Blue) {
                    return new Group(emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][2],
                            emptyCube.getPiece()[2][3], emptyCube.getPiece()[2][4], emptyCube.getPiece()[2][5],
                            emptyCube.getPiece()[2][6], emptyCube.getPiece()[2][7], emptyCube.getPiece()[2][8],
                            sticker[5][0].getSticker(), sticker[5][1].getSticker(), sticker[5][2].getSticker(),
                            sticker[5][3].getSticker(), sticker[5][4].getSticker(), sticker[5][5].getSticker(),
                            sticker[5][6].getSticker(), sticker[5][7].getSticker(), sticker[5][8].getSticker(),
                            sticker[0][6].getSticker(), sticker[0][7].getSticker(), sticker[0][8].getSticker(),
                            sticker[1][6].getSticker(), sticker[1][7].getSticker(), sticker[1][8].getSticker(),
                            sticker[2][6].getSticker(), sticker[2][7].getSticker(), sticker[2][8].getSticker(),
                            sticker[3][6].getSticker(), sticker[3][7].getSticker(), sticker[3][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Orange">
                if (layerColor == LayerColor.White) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][2],
                            emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][1], emptyCube.getPiece()[1][2],
                            emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][2],
                            sticker[2][0].getSticker(), sticker[2][1].getSticker(), sticker[2][2].getSticker(),
                            sticker[2][3].getSticker(), sticker[2][4].getSticker(), sticker[2][5].getSticker(),
                            sticker[2][6].getSticker(), sticker[2][7].getSticker(), sticker[2][8].getSticker(),
                            sticker[1][2].getSticker(), sticker[1][5].getSticker(), sticker[1][8].getSticker(),
                            sticker[3][0].getSticker(), sticker[3][3].getSticker(), sticker[3][6].getSticker(),
                            sticker[4][0].getSticker(), sticker[4][1].getSticker(), sticker[4][2].getSticker(),
                            sticker[5][6].getSticker(), sticker[5][7].getSticker(), sticker[5][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Red">
                if (layerColor == LayerColor.Yellow) {
                    return new Group(emptyCube.getPiece()[0][6], emptyCube.getPiece()[0][7], emptyCube.getPiece()[0][8],
                            emptyCube.getPiece()[1][6], emptyCube.getPiece()[1][7], emptyCube.getPiece()[1][8],
                            emptyCube.getPiece()[2][6], emptyCube.getPiece()[2][7], emptyCube.getPiece()[2][8],
                            sticker[0][0].getSticker(), sticker[0][1].getSticker(), sticker[0][2].getSticker(),
                            sticker[0][3].getSticker(), sticker[0][4].getSticker(), sticker[0][5].getSticker(),
                            sticker[0][6].getSticker(), sticker[0][7].getSticker(), sticker[0][8].getSticker(),
                            sticker[4][6].getSticker(), sticker[4][7].getSticker(), sticker[4][8].getSticker(),
                            sticker[1][0].getSticker(), sticker[1][3].getSticker(), sticker[1][6].getSticker(),
                            sticker[5][0].getSticker(), sticker[5][1].getSticker(), sticker[5][2].getSticker(),
                            sticker[3][2].getSticker(), sticker[3][5].getSticker(), sticker[3][8].getSticker());
                }
                //</editor-fold>
                //</editor-fold>
                //<editor-fold desc="Up Move">
            case U:
                //<editor-fold desc="Return Yellow">
                if (layerColor == LayerColor.Red || layerColor == LayerColor.Green ||
                        layerColor == LayerColor.Orange || layerColor == LayerColor.Blue) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][2],
                            emptyCube.getPiece()[0][3], emptyCube.getPiece()[0][4], emptyCube.getPiece()[0][5],
                            emptyCube.getPiece()[0][6], emptyCube.getPiece()[0][7], emptyCube.getPiece()[0][8],
                            sticker[4][0].getSticker(), sticker[4][1].getSticker(), sticker[4][2].getSticker(),
                            sticker[4][3].getSticker(), sticker[4][4].getSticker(), sticker[4][5].getSticker(),
                            sticker[4][6].getSticker(), sticker[4][7].getSticker(), sticker[4][8].getSticker(),
                            sticker[0][0].getSticker(), sticker[0][1].getSticker(), sticker[0][2].getSticker(),
                            sticker[1][0].getSticker(), sticker[1][1].getSticker(), sticker[1][2].getSticker(),
                            sticker[2][0].getSticker(), sticker[2][1].getSticker(), sticker[2][2].getSticker(),
                            sticker[3][0].getSticker(), sticker[3][1].getSticker(), sticker[3][2].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Orange">
                if (layerColor == LayerColor.Yellow) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][2],
                            emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][1], emptyCube.getPiece()[1][2],
                            emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][2],
                            sticker[2][0].getSticker(), sticker[2][1].getSticker(), sticker[2][2].getSticker(),
                            sticker[2][3].getSticker(), sticker[2][4].getSticker(), sticker[2][5].getSticker(),
                            sticker[2][6].getSticker(), sticker[2][7].getSticker(), sticker[2][8].getSticker(),
                            sticker[1][2].getSticker(), sticker[1][5].getSticker(), sticker[1][8].getSticker(),
                            sticker[3][0].getSticker(), sticker[3][3].getSticker(), sticker[3][6].getSticker(),
                            sticker[4][0].getSticker(), sticker[4][1].getSticker(), sticker[4][2].getSticker(),
                            sticker[5][6].getSticker(), sticker[5][7].getSticker(), sticker[5][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Red">
                if (layerColor == LayerColor.White) {
                    return new Group(emptyCube.getPiece()[0][6], emptyCube.getPiece()[0][7], emptyCube.getPiece()[0][8],
                            emptyCube.getPiece()[1][6], emptyCube.getPiece()[1][7], emptyCube.getPiece()[1][8],
                            emptyCube.getPiece()[2][6], emptyCube.getPiece()[2][7], emptyCube.getPiece()[2][8],
                            sticker[0][0].getSticker(), sticker[0][1].getSticker(), sticker[0][2].getSticker(),
                            sticker[0][3].getSticker(), sticker[0][4].getSticker(), sticker[0][5].getSticker(),
                            sticker[0][6].getSticker(), sticker[0][7].getSticker(), sticker[0][8].getSticker(),
                            sticker[4][6].getSticker(), sticker[4][7].getSticker(), sticker[4][8].getSticker(),
                            sticker[1][0].getSticker(), sticker[1][3].getSticker(), sticker[1][6].getSticker(),
                            sticker[5][0].getSticker(), sticker[5][1].getSticker(), sticker[5][2].getSticker(),
                            sticker[3][2].getSticker(), sticker[3][5].getSticker(), sticker[3][8].getSticker());
                }
                //</editor-fold>
                //</editor-fold>
                //<editor-fold desc="Front Move">
            case F:

                //<editor-fold desc="Return Red">
                if (layerColor == LayerColor.Red) {
                    return new Group(emptyCube.getPiece()[0][6], emptyCube.getPiece()[0][7], emptyCube.getPiece()[0][8],
                            emptyCube.getPiece()[1][6], emptyCube.getPiece()[1][7], emptyCube.getPiece()[1][8],
                            emptyCube.getPiece()[2][6], emptyCube.getPiece()[2][7], emptyCube.getPiece()[2][8],
                            sticker[0][0].getSticker(), sticker[0][1].getSticker(), sticker[0][2].getSticker(),
                            sticker[0][3].getSticker(), sticker[0][4].getSticker(), sticker[0][5].getSticker(),
                            sticker[0][6].getSticker(), sticker[0][7].getSticker(), sticker[0][8].getSticker(),
                            sticker[4][6].getSticker(), sticker[4][7].getSticker(), sticker[4][8].getSticker(),
                            sticker[1][0].getSticker(), sticker[1][3].getSticker(), sticker[1][6].getSticker(),
                            sticker[5][0].getSticker(), sticker[5][1].getSticker(), sticker[5][2].getSticker(),
                            sticker[3][2].getSticker(), sticker[3][5].getSticker(), sticker[3][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Green">
                if (layerColor == LayerColor.Green) {
                    return new Group(emptyCube.getPiece()[0][2], emptyCube.getPiece()[0][5], emptyCube.getPiece()[0][8],
                            emptyCube.getPiece()[1][2], emptyCube.getPiece()[1][5], emptyCube.getPiece()[1][8],
                            emptyCube.getPiece()[2][2], emptyCube.getPiece()[2][5], emptyCube.getPiece()[2][8],
                            sticker[1][0].getSticker(), sticker[1][1].getSticker(), sticker[1][2].getSticker(),
                            sticker[1][3].getSticker(), sticker[1][4].getSticker(), sticker[1][5].getSticker(),
                            sticker[1][6].getSticker(), sticker[1][7].getSticker(), sticker[1][8].getSticker(),
                            sticker[0][2].getSticker(), sticker[0][5].getSticker(), sticker[0][8].getSticker(),
                            sticker[2][0].getSticker(), sticker[2][3].getSticker(), sticker[2][6].getSticker(),
                            sticker[4][2].getSticker(), sticker[4][5].getSticker(), sticker[4][8].getSticker(),
                            sticker[5][2].getSticker(), sticker[5][5].getSticker(), sticker[5][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Orange">
                if (layerColor == LayerColor.Orange) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][2],
                            emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][1], emptyCube.getPiece()[1][2],
                            emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][2],
                            sticker[2][0].getSticker(), sticker[2][1].getSticker(), sticker[2][2].getSticker(),
                            sticker[2][3].getSticker(), sticker[2][4].getSticker(), sticker[2][5].getSticker(),
                            sticker[2][6].getSticker(), sticker[2][7].getSticker(), sticker[2][8].getSticker(),
                            sticker[1][2].getSticker(), sticker[1][5].getSticker(), sticker[1][8].getSticker(),
                            sticker[3][0].getSticker(), sticker[3][3].getSticker(), sticker[3][6].getSticker(),
                            sticker[4][0].getSticker(), sticker[4][1].getSticker(), sticker[4][2].getSticker(),
                            sticker[5][6].getSticker(), sticker[5][7].getSticker(), sticker[5][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Blue">
                if (layerColor == LayerColor.Blue) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][3], emptyCube.getPiece()[0][6],
                            emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][3], emptyCube.getPiece()[1][6],
                            emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][3], emptyCube.getPiece()[2][6],
                            sticker[3][0].getSticker(), sticker[3][1].getSticker(), sticker[3][2].getSticker(),
                            sticker[3][3].getSticker(), sticker[3][4].getSticker(), sticker[3][5].getSticker(),
                            sticker[3][6].getSticker(), sticker[3][7].getSticker(), sticker[3][8].getSticker(),
                            sticker[0][0].getSticker(), sticker[0][3].getSticker(), sticker[0][6].getSticker(),
                            sticker[4][0].getSticker(), sticker[4][3].getSticker(), sticker[4][6].getSticker(),
                            sticker[2][2].getSticker(), sticker[2][5].getSticker(), sticker[2][8].getSticker(),
                            sticker[5][0].getSticker(), sticker[5][3].getSticker(), sticker[5][6].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Yellow">
                if (layerColor == LayerColor.Yellow) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][2],
                            emptyCube.getPiece()[0][3], emptyCube.getPiece()[0][4], emptyCube.getPiece()[0][5],
                            emptyCube.getPiece()[0][6], emptyCube.getPiece()[0][7], emptyCube.getPiece()[0][8],
                            sticker[4][0].getSticker(), sticker[4][1].getSticker(), sticker[4][2].getSticker(),
                            sticker[4][3].getSticker(), sticker[4][4].getSticker(), sticker[4][5].getSticker(),
                            sticker[4][6].getSticker(), sticker[4][7].getSticker(), sticker[4][8].getSticker(),
                            sticker[0][0].getSticker(), sticker[0][1].getSticker(), sticker[0][2].getSticker(),
                            sticker[1][0].getSticker(), sticker[1][1].getSticker(), sticker[1][2].getSticker(),
                            sticker[2][0].getSticker(), sticker[2][1].getSticker(), sticker[2][2].getSticker(),
                            sticker[3][0].getSticker(), sticker[3][1].getSticker(), sticker[3][2].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return White">
                if (layerColor == LayerColor.White) {
                    return new Group(emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][2],
                            emptyCube.getPiece()[2][3], emptyCube.getPiece()[2][4], emptyCube.getPiece()[2][5],
                            emptyCube.getPiece()[2][6], emptyCube.getPiece()[2][7], emptyCube.getPiece()[2][8],
                            sticker[5][0].getSticker(), sticker[5][1].getSticker(), sticker[5][2].getSticker(),
                            sticker[5][3].getSticker(), sticker[5][4].getSticker(), sticker[5][5].getSticker(),
                            sticker[5][6].getSticker(), sticker[5][7].getSticker(), sticker[5][8].getSticker(),
                            sticker[0][6].getSticker(), sticker[0][7].getSticker(), sticker[0][8].getSticker(),
                            sticker[1][6].getSticker(), sticker[1][7].getSticker(), sticker[1][8].getSticker(),
                            sticker[2][6].getSticker(), sticker[2][7].getSticker(), sticker[2][8].getSticker(),
                            sticker[3][6].getSticker(), sticker[3][7].getSticker(), sticker[3][8].getSticker());
                }
                //</editor-fold>
                //</editor-fold>
                //<editor-fold desc="Back Move">
            case B:

                //<editor-fold desc="Return Red">
                if (layerColor == LayerColor.Orange) {
                    return new Group(emptyCube.getPiece()[0][6], emptyCube.getPiece()[0][7], emptyCube.getPiece()[0][8],
                            emptyCube.getPiece()[1][6], emptyCube.getPiece()[1][7], emptyCube.getPiece()[1][8],
                            emptyCube.getPiece()[2][6], emptyCube.getPiece()[2][7], emptyCube.getPiece()[2][8],
                            sticker[0][0].getSticker(), sticker[0][1].getSticker(), sticker[0][2].getSticker(),
                            sticker[0][3].getSticker(), sticker[0][4].getSticker(), sticker[0][5].getSticker(),
                            sticker[0][6].getSticker(), sticker[0][7].getSticker(), sticker[0][8].getSticker(),
                            sticker[4][6].getSticker(), sticker[4][7].getSticker(), sticker[4][8].getSticker(),
                            sticker[1][0].getSticker(), sticker[1][3].getSticker(), sticker[1][6].getSticker(),
                            sticker[5][0].getSticker(), sticker[5][1].getSticker(), sticker[5][2].getSticker(),
                            sticker[3][2].getSticker(), sticker[3][5].getSticker(), sticker[3][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Green">
                if (layerColor == LayerColor.Blue) {
                    return new Group(emptyCube.getPiece()[0][2], emptyCube.getPiece()[0][5], emptyCube.getPiece()[0][8],
                            emptyCube.getPiece()[1][2], emptyCube.getPiece()[1][5], emptyCube.getPiece()[1][8],
                            emptyCube.getPiece()[2][2], emptyCube.getPiece()[2][5], emptyCube.getPiece()[2][8],
                            sticker[1][0].getSticker(), sticker[1][1].getSticker(), sticker[1][2].getSticker(),
                            sticker[1][3].getSticker(), sticker[1][4].getSticker(), sticker[1][5].getSticker(),
                            sticker[1][6].getSticker(), sticker[1][7].getSticker(), sticker[1][8].getSticker(),
                            sticker[0][2].getSticker(), sticker[0][5].getSticker(), sticker[0][8].getSticker(),
                            sticker[2][0].getSticker(), sticker[2][3].getSticker(), sticker[2][6].getSticker(),
                            sticker[4][2].getSticker(), sticker[4][5].getSticker(), sticker[4][8].getSticker(),
                            sticker[5][2].getSticker(), sticker[5][5].getSticker(), sticker[5][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Orange">
                if (layerColor == LayerColor.Red) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][2],
                            emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][1], emptyCube.getPiece()[1][2],
                            emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][2],
                            sticker[2][0].getSticker(), sticker[2][1].getSticker(), sticker[2][2].getSticker(),
                            sticker[2][3].getSticker(), sticker[2][4].getSticker(), sticker[2][5].getSticker(),
                            sticker[2][6].getSticker(), sticker[2][7].getSticker(), sticker[2][8].getSticker(),
                            sticker[1][2].getSticker(), sticker[1][5].getSticker(), sticker[1][8].getSticker(),
                            sticker[3][0].getSticker(), sticker[3][3].getSticker(), sticker[3][6].getSticker(),
                            sticker[4][0].getSticker(), sticker[4][1].getSticker(), sticker[4][2].getSticker(),
                            sticker[5][6].getSticker(), sticker[5][7].getSticker(), sticker[5][8].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Blue">
                if (layerColor == LayerColor.Green) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][3], emptyCube.getPiece()[0][6],
                            emptyCube.getPiece()[1][0], emptyCube.getPiece()[1][3], emptyCube.getPiece()[1][6],
                            emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][3], emptyCube.getPiece()[2][6],
                            sticker[3][0].getSticker(), sticker[3][1].getSticker(), sticker[3][2].getSticker(),
                            sticker[3][3].getSticker(), sticker[3][4].getSticker(), sticker[3][5].getSticker(),
                            sticker[3][6].getSticker(), sticker[3][7].getSticker(), sticker[3][8].getSticker(),
                            sticker[0][0].getSticker(), sticker[0][3].getSticker(), sticker[0][6].getSticker(),
                            sticker[4][0].getSticker(), sticker[4][3].getSticker(), sticker[4][6].getSticker(),
                            sticker[2][2].getSticker(), sticker[2][5].getSticker(), sticker[2][8].getSticker(),
                            sticker[5][0].getSticker(), sticker[5][3].getSticker(), sticker[5][6].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return Yellow">
                if (layerColor == LayerColor.White) {
                    return new Group(emptyCube.getPiece()[0][0], emptyCube.getPiece()[0][1], emptyCube.getPiece()[0][2],
                            emptyCube.getPiece()[0][3], emptyCube.getPiece()[0][4], emptyCube.getPiece()[0][5],
                            emptyCube.getPiece()[0][6], emptyCube.getPiece()[0][7], emptyCube.getPiece()[0][8],
                            sticker[4][0].getSticker(), sticker[4][1].getSticker(), sticker[4][2].getSticker(),
                            sticker[4][3].getSticker(), sticker[4][4].getSticker(), sticker[4][5].getSticker(),
                            sticker[4][6].getSticker(), sticker[4][7].getSticker(), sticker[4][8].getSticker(),
                            sticker[0][0].getSticker(), sticker[0][1].getSticker(), sticker[0][2].getSticker(),
                            sticker[1][0].getSticker(), sticker[1][1].getSticker(), sticker[1][2].getSticker(),
                            sticker[2][0].getSticker(), sticker[2][1].getSticker(), sticker[2][2].getSticker(),
                            sticker[3][0].getSticker(), sticker[3][1].getSticker(), sticker[3][2].getSticker());
                }
                //</editor-fold>

                //<editor-fold desc="Return White">
                if (layerColor == LayerColor.Yellow) {
                    return new Group(emptyCube.getPiece()[2][0], emptyCube.getPiece()[2][1], emptyCube.getPiece()[2][2],
                            emptyCube.getPiece()[2][3], emptyCube.getPiece()[2][4], emptyCube.getPiece()[2][5],
                            emptyCube.getPiece()[2][6], emptyCube.getPiece()[2][7], emptyCube.getPiece()[2][8],
                            sticker[5][0].getSticker(), sticker[5][1].getSticker(), sticker[5][2].getSticker(),
                            sticker[5][3].getSticker(), sticker[5][4].getSticker(), sticker[5][5].getSticker(),
                            sticker[5][6].getSticker(), sticker[5][7].getSticker(), sticker[5][8].getSticker(),
                            sticker[0][6].getSticker(), sticker[0][7].getSticker(), sticker[0][8].getSticker(),
                            sticker[1][6].getSticker(), sticker[1][7].getSticker(), sticker[1][8].getSticker(),
                            sticker[2][6].getSticker(), sticker[2][7].getSticker(), sticker[2][8].getSticker(),
                            sticker[3][6].getSticker(), sticker[3][7].getSticker(), sticker[3][8].getSticker());
                }
                //</editor-fold>
                //</editor-fold>
        }
        return null;
    }

    private void animateRotate(MoveType moveType, LayerColor layerColor, RotationType rotationType) {
        isMoving = true;
        int rotationVal;
        Timeline tl = null;

        rotateLayer = getLayer(moveType, layerColor);

        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);

        rotateLayer.getTransforms().addAll(rotateX, rotateY, rotateZ);

        //<editor-fold desc="Initialize Animation">
        switch (moveType) {

            //<editor-fold desc="Right Move">
            case R:
                if (layerColor == LayerColor.Red || layerColor == LayerColor.Orange ||
                        layerColor == LayerColor.Yellow || layerColor == LayerColor.White) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;

                    if (layerColor == LayerColor.Orange) {
                        rotationVal *= -1;
                    }
                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateX.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateX.angleProperty(), rotationVal))
                    );
                } else {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    if (layerColor == LayerColor.Blue) {
                        rotationVal *= -1;
                    }
                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                }
                break;
            //</editor-fold>

            //<editor-fold desc="Left Move">
            case L:


                if (layerColor == LayerColor.Red || layerColor == LayerColor.Orange ||
                        layerColor == LayerColor.Yellow || layerColor == LayerColor.White) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;

                    if (layerColor == LayerColor.Orange) {
                        rotationVal *= -1;
                    }

                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateX.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateX.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Blue || layerColor == LayerColor.Green) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;

                    if (layerColor == LayerColor.Blue) {
                        rotationVal *= -1;
                    }

                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                }
                break;
            //</editor-fold>

            //<editor-fold desc="Up Move">
            case U:

                if (layerColor == LayerColor.Yellow) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.White) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                } else {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateY.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateY.angleProperty(), rotationVal))
                    );
                }
                break;
            //</editor-fold>

            //<editor-fold desc="Down Move">
            case D:
                if (layerColor == LayerColor.Yellow) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.White) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                } else {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateY.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateY.angleProperty(), rotationVal))
                    );
                }
                break;
            //</editor-fold>

            //<editor-fold desc="Front Move">
            case F:
                if (layerColor == LayerColor.Green) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateX.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateX.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Blue) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateX.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateX.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Red) {

                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Orange) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Yellow) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateY.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateY.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.White) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateY.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateY.angleProperty(), rotationVal))
                    );
                }
                break;
            //</editor-fold>

            //<editor-fold desc="Back Move">
            case B:
                if (layerColor == LayerColor.Green) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateX.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateX.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Blue) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateX.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateX.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Red) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Orange) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Yellow) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateY.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateY.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.White) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateY.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateY.angleProperty(), rotationVal))
                    );
                }
                break;
            //</editor-fold>

            //<editor-fold desc="Middle Move">
            case M:
                if (layerColor == LayerColor.Red || layerColor == LayerColor.Yellow ||
                        layerColor == LayerColor.Orange || layerColor == LayerColor.White) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;

                    if (layerColor == LayerColor.Orange) {
                        rotationVal *= -1;
                    }



                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateX.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateX.angleProperty(), rotationVal))
                    );
                } else {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    if (layerColor == LayerColor.Blue) {
                        rotationVal *= -1;

                        if(rotationType == RotationType.ClockWise){
                            rotationType = RotationType.CounterClockWise;
                        } else rotationType = RotationType.ClockWise;
                    }

                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                }
                break;
            //</editor-fold>

            //<editor-fold desc="Equatorial Move">
            case E:
                if (layerColor == LayerColor.Red || layerColor == LayerColor.Blue ||
                        layerColor == LayerColor.Orange || layerColor == LayerColor.Green) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;

                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateY.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateY.angleProperty(), rotationVal))
                    );
                } else {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    if (layerColor == LayerColor.White) {
                        rotationVal *= -1;
                    }

                    tl = new Timeline(

                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                }
                break;
            //</editor-fold>

            //<editor-fold desc="Standing Move">
            case S:
                if (layerColor == LayerColor.Green) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateX.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateX.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Blue) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateX.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateX.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Red) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Orange) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateZ.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateZ.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.Yellow) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = 90;
                    } else rotationVal = -90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateY.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateY.angleProperty(), rotationVal))
                    );
                } else if (layerColor == LayerColor.White) {
                    if (rotationType == RotationType.ClockWise) {
                        rotationVal = -90;
                    } else rotationVal = 90;
                    tl = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(rotateY.angleProperty(), 0)),
                            new KeyFrame(Duration.seconds(cubeAnimationSpeed), new KeyValue(rotateY.angleProperty(), rotationVal))
                    );
                }
                break;
            //</editor-fold>
        }
        //</editor-fold>

        rubiksCube.getChildren().add(rotateLayer);
        Objects.requireNonNull(tl).play();

        RotationType finalRotationType = rotationType;
        tl.setOnFinished(event1 -> {
            isMoving = false;
            rotateLayer(moveType, layerColor, finalRotationType);
            rotateLayer.getTransforms().clear();
            rubiksCube.getChildren().remove(emptyCube.getCube());
            rubiksCube.getChildren().add(emptyCube.getCube());
            if (moveSequence) {
                sequenceIndex++;
                nextMoveInSequence();
            }
        });
    }

    public Group getRubiksCube() {
        return rubiksCube;
    }

    public void setCubeRotation(double cubeRotation) {
        this.cubeRotation = cubeRotation;
    }

    public void doMove(MoveType moveType, int layerNumber, RotationType rotationType) {
        if (!isMoving) {
            LayerColor layerColor;

            //<editor-fold desc="Layer Number Switch">
            switch (layerNumber) {
                case 0:
                    layerColor = LayerColor.Red;
                    break;
                case 1:
                    layerColor = LayerColor.Green;
                    break;
                case 2:
                    layerColor = LayerColor.Orange;
                    break;
                case 3:
                    layerColor = LayerColor.Blue;
                    break;
                case 4:
                    layerColor = LayerColor.Yellow;
                    break;
                case 5:
                    layerColor = LayerColor.White;
                    break;
                default:
                    layerColor = LayerColor.Red;
                    break;
            }
            //</editor-fold>

            animateRotate(moveType, layerColor, rotationType);
        }
    }

    public void doMoves(LinkedList<MoveType> moves, LinkedList<RotationType> movesDirection, LinkedList<Integer> layerNumber) {
        this.moves = moves;
        this.movesDirection = movesDirection;
        this.layerNumber = layerNumber;
        moveSequence = true;
        sequenceIndex = 0;
        nextMoveInSequence();
    }

    private void nextMoveInSequence() {
        if (sequenceIndex < moves.size()) {
            doMove(moves.get(sequenceIndex), layerNumber.get(sequenceIndex), movesDirection.get(sequenceIndex));
        } else moveSequence = false;
    }

    public Sticker[][] getSticker() {
        return this.sticker;
    }

    private double ClipValues(double min, double max, double value) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        } else return value;
    }

    public SubScene getScene() {
        return scene;
    }
}
