package cube;

import javafx.scene.paint.Color;
import java.util.LinkedList;
import java.util.Random;

public class CubeSolver {
    RubiksCube rubiksCube;

    //Shuffle
    LinkedList<MoveType> shuffledMoves = new LinkedList<>();
    LinkedList<RotationType> shuffledMovesDirection = new LinkedList<>();

    //Solve
    private Layer currentLayer = new Layer();
    private Sticker[][] tempRubiksCube = new Sticker[6][9];
    LinkedList<MoveType> solutionMoves = new LinkedList<>();
    LinkedList<RotationType> solutionMovesRotation = new LinkedList<>();
    LinkedList<Integer> solutionLayerNumber = new LinkedList<>();

    public CubeSolver(RubiksCube rubiksCube) {
        this.rubiksCube = rubiksCube;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                tempRubiksCube[i][j] = new Sticker(Color.WHITE, 2);
            }
        }
    }

    public void shuffleCube(int moveNumber) {
        shuffledMoves.clear();
        shuffledMovesDirection.clear();

        for (int i = 0; i < moveNumber; i++) {
            Random random = new Random();
            int randomMove = random.nextInt(MoveType.values().length);
            shuffledMoves.add(MoveType.values()[randomMove]);

            int direction = random.nextInt(RotationType.values().length);
            shuffledMovesDirection.add(RotationType.values()[direction]);
        }

        LinkedList<Integer> layerNum = new LinkedList<>();
        for (int i = 0; i < moveNumber; i++) {
            layerNum.add(0);
        }
        rubiksCube.doMoves(shuffledMoves, shuffledMovesDirection, layerNum);
    }

    public void solveCube() {

        //<editor-fold desc="Set Color of temp cube">
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                tempRubiksCube[i][j].setColor(rubiksCube.getSticker()[i][j].getColor());
            }
        }
        //</editor-fold>

        solutionMoves.clear();
        solutionMovesRotation.clear();
        solutionLayerNumber.clear();

        do {
            FirstCross();
            InsertCorners();
            InsertSecondLayer();
            SecondCross();
            OLL();
            Headlights();
            PLL();
            lineUpCube();
        } while(!isCubeSolved());
        rubiksCube.label.setText("Number of moves to solve: " + solutionMoves.size());
        //send move set to main cube
        rubiksCube.doMoves(solutionMoves, solutionMovesRotation, solutionLayerNumber);
    }

    private void FirstCross() {
        Color bottomColor = tempRubiksCube[5][4].getColor();
        boolean complete = false;

        while (!complete) {
            complete = true;

            //Scan sides for completion
            for (int i = 0; i < 4; i++) {

                //Set index of bottom edge piece
                int bottomEdgeIndex = getBottomEdgeIndex(i);
                Color currentSideColor = tempRubiksCube[i][4].getColor();

                if (tempRubiksCube[i][7].getColor() == tempRubiksCube[i][4].getColor() && tempRubiksCube[5][bottomEdgeIndex].getColor() == bottomColor) {
                } else {
                    complete = false;
                    boolean moveDone = false;

                    //<editor-fold default-state="collapsed" desc="Check for piece in top">
                    for (int j = 0; j < 4; j++) {
                        //Set index of top edge piece
                        int topEdgeIndex = getTopEdgeIndex(j);

                        if (tempRubiksCube[4][topEdgeIndex].getColor() == bottomColor && tempRubiksCube[j][1].getColor() == currentSideColor ||
                                tempRubiksCube[4][topEdgeIndex].getColor() == currentSideColor && tempRubiksCube[j][1].getColor() == bottomColor) {
                            Color topColor = tempRubiksCube[4][topEdgeIndex].getColor();

                            int distance = getDistance(j,i);

                            if (topColor == bottomColor) {
                                //Move piece to destination
                                topLayerAlignment(j,distance);

                                //Put piece into place
                                solutionMovesAdd(MoveType.F, RotationType.ClockWise, i, 2);
                                moveDone = true;
                            } else {
                                //Move piece to destination
                                switch (distance) {
                                    case 0:
                                        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, i, 1);
                                        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                                        break;
                                    case 1:
                                        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                                        break;
                                    case 2:
                                        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, i, 1);
                                        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                                        break;
                                    case 3:
                                        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, i, 2);
                                        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                                        break;
                                }

                                //Put piece into place
                                solutionMovesAdd(MoveType.F, RotationType.ClockWise, i, 1);
                                solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                                moveDone = true;

                            }

                            break;
                        }

                    }
                    //</editor-fold>
                    if (moveDone)
                        break;
                    //<editor-fold default-state="collapsed" desc="Check for piece in middle">
                    for (int j = 0; j < 4; j++) {
                        if (tempRubiksCube[j][5].getColor() == bottomColor && tempRubiksCube[(j + 1) % 4][3].getColor() == currentSideColor ||
                                tempRubiksCube[j][5].getColor() == currentSideColor && tempRubiksCube[(j + 1) % 4][3].getColor() == bottomColor) {
                            solutionMovesAdd(MoveType.R, RotationType.ClockWise, j, 1);
                            solutionMovesAdd(MoveType.U, RotationType.ClockWise, j, 1);
                            solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, j, 1);
                            moveDone = true;
                            break;
                        }

                    }
                    //</editor-fold>
                    if (moveDone)
                        break;
                    //<editor-fold default-state="collapsed" desc="Check for piece in bottom">
                    for (int j = 0; j < 4; j++) {
                        if (i != j) {
                            if (tempRubiksCube[j][7].getColor() == bottomColor && tempRubiksCube[5][getBottomEdgeIndex(j)].getColor() == currentSideColor ||
                                    tempRubiksCube[j][7].getColor() == currentSideColor && tempRubiksCube[5][getBottomEdgeIndex(j)].getColor() == bottomColor) {
                                solutionMovesAdd(MoveType.F, RotationType.ClockWise, j, 2);
                                solutionMovesAdd(MoveType.U, RotationType.ClockWise, j, 1);
                                solutionMovesAdd(MoveType.F, RotationType.ClockWise, j, 2);
                                break;
                            }
                        } else {
                            if (tempRubiksCube[j][7].getColor() == bottomColor && tempRubiksCube[5][getBottomEdgeIndex(j)].getColor() == currentSideColor) {
                                solutionMovesAdd(MoveType.F, RotationType.ClockWise, j, 2);
                                solutionMovesAdd(MoveType.U, RotationType.ClockWise, j, 1);
                                solutionMovesAdd(MoveType.F, RotationType.ClockWise, j, 2);
                                break;
                            }
                        }
                    }
                    //</editor-fold>
                }
            }
        }
    }

    private void InsertCorners(){
        Color bottomColor = tempRubiksCube[5][4].getColor();
        boolean complete = false;

        while(!complete) {
            complete = true;

            //Scan sides for completion
            for (int i = 0; i < 4; i++) {

                //Set index of Bottom corner
                int bottomCornerIndex = getBottomCornerIndex(i);

                //Get surrounding side colors
                Color currentSideColor = tempRubiksCube[i][4].getColor();
                Color nextSideColor = tempRubiksCube[(i + 1) % 4][4].getColor();

                if (tempRubiksCube[i][8].getColor() == currentSideColor && tempRubiksCube[(i + 1) % 4][6].getColor() == nextSideColor &&
                        tempRubiksCube[5][bottomCornerIndex].getColor() == bottomColor) {
                } else {
                    complete = false;
                    boolean moveDone = false;
                    //<editor-fold default-state="collapsed" desc="Check for piece in top">
                    for(int j = 0; j<4;j++) {
                        int topCornerIndex = getTopCornerIndex(j);

                        if(tempRubiksCube[j][2].getColor() == bottomColor && tempRubiksCube[(j+1)%4][0].getColor() == nextSideColor && tempRubiksCube[4][topCornerIndex].getColor() == currentSideColor ||
                           tempRubiksCube[j][2].getColor() == nextSideColor && tempRubiksCube[(j+1)%4][0].getColor() == currentSideColor && tempRubiksCube[4][topCornerIndex].getColor() == bottomColor ||
                           tempRubiksCube[j][2].getColor() == currentSideColor && tempRubiksCube[(j+1)%4][0].getColor() == bottomColor && tempRubiksCube[4][topCornerIndex].getColor() == nextSideColor){

                            //Get top corner color
                            Color topColor = tempRubiksCube[4][topCornerIndex].getColor();

                            //Align to place
                            topLayerAlignment(j,getDistance(j,i));

                            if(topColor == currentSideColor){
                                //Insert into place
                                solutionMovesAdd(MoveType.F,RotationType.CounterClockWise,i,1);
                                solutionMovesAdd(MoveType.U,RotationType.CounterClockWise,i,1);
                                solutionMovesAdd(MoveType.F,RotationType.ClockWise,i,1);
                                moveDone = true;
                            } else if(topColor == bottomColor){
                                //Insert into place
                                solutionMovesAdd(MoveType.R,RotationType.ClockWise,i,1);
                                solutionMovesAdd(MoveType.U,RotationType.ClockWise,i,2);
                                solutionMovesAdd(MoveType.R,RotationType.CounterClockWise,i,1);
                                solutionMovesAdd(MoveType.U,RotationType.CounterClockWise,i,1);
                                solutionMovesAdd(MoveType.R,RotationType.ClockWise,i,1);
                                solutionMovesAdd(MoveType.U,RotationType.ClockWise,i,1);
                                solutionMovesAdd(MoveType.R,RotationType.CounterClockWise,i,1);
                                moveDone = true;
                            } else {
                                //Insert into place
                                solutionMovesAdd(MoveType.R,RotationType.ClockWise,i,1);
                                solutionMovesAdd(MoveType.U,RotationType.ClockWise,i,1);
                                solutionMovesAdd(MoveType.R,RotationType.CounterClockWise,i,1);
                                moveDone = true;
                            }
                        }
                    }
                    //</editor-fold>
                    if(moveDone)
                        break;
                    //<editor-fold default-state="collapsed" desc="Check for piece in bottom">
                    for(int j = 0; j < 4; j++){
                        bottomCornerIndex = getBottomCornerIndex(j);

                        if(tempRubiksCube[j][8].getColor() == bottomColor && tempRubiksCube[(j+1)%4][6].getColor() == currentSideColor && tempRubiksCube[5][bottomCornerIndex].getColor() == nextSideColor ||
                           tempRubiksCube[j][8].getColor() == currentSideColor && tempRubiksCube[(j+1)%4][6].getColor() == nextSideColor && tempRubiksCube[5][bottomCornerIndex].getColor() == bottomColor ||
                           tempRubiksCube[j][8].getColor() == nextSideColor && tempRubiksCube[(j+1)%4][6].getColor() == bottomColor && tempRubiksCube[5][bottomCornerIndex].getColor() == currentSideColor){

                            if(i != j){
                                //Move out of place
                                solutionMovesAdd(MoveType.R,RotationType.ClockWise,i,1);
                                solutionMovesAdd(MoveType.U,RotationType.ClockWise,i,1);
                                solutionMovesAdd(MoveType.R,RotationType.CounterClockWise,i,1);
                            } else {
                                if(tempRubiksCube[j][8].getColor() == bottomColor && tempRubiksCube[(j+1)%4][6].getColor() == currentSideColor && tempRubiksCube[5][bottomCornerIndex].getColor() == nextSideColor ||
                                   tempRubiksCube[j][8].getColor() == nextSideColor && tempRubiksCube[(j+1)%4][6].getColor() == bottomColor && tempRubiksCube[5][bottomCornerIndex].getColor() == currentSideColor){
                                    //Move out of place
                                    solutionMovesAdd(MoveType.R,RotationType.ClockWise,i,1);
                                    solutionMovesAdd(MoveType.U,RotationType.ClockWise,i,1);
                                    solutionMovesAdd(MoveType.R,RotationType.CounterClockWise,i,1);
                                }
                            }
                        }
                    }
                    //</editor-fold>
                }
            }
        }
    }

    private void InsertSecondLayer(){
        boolean complete = false;

        while(!complete){
            complete = true;

            //Scan sides for completion
            for(int i = 0; i< 4; i++) {

                //Get surrounding colors
                Color currentSideColor = tempRubiksCube[i][4].getColor();
                Color previousSideColor = tempRubiksCube[(i + 3) % 4][4].getColor();
                Color nextSideColor = tempRubiksCube[(i + 1) % 4][4].getColor();

                if (tempRubiksCube[i][5].getColor() == currentSideColor && tempRubiksCube[(i + 1) % 4][3].getColor() == nextSideColor &&
                        tempRubiksCube[i][3].getColor() == currentSideColor && tempRubiksCube[(i+3)%4][5].getColor() == previousSideColor) {
                } else {
                    complete = false;

                    boolean moveDone = false;

                    //<editor-fold default-state="collapsed" desc="Check for piece in top">
                    for (int j = 0; j < 4; j++) {

                        //Check if found a fitting edge piece
                        if(tempRubiksCube[j][1].getColor() == currentSideColor){

                            //Gather information about the piece Color
                            int topEdgeIndex = getTopEdgeIndex(j);
                            Color topColor = tempRubiksCube[4][topEdgeIndex].getColor();


                            if(topColor == nextSideColor){

                                //Align to position
                                topLayerAlignment(j,getDistance(j,i));

                                insertIntoSecondLayerRight(i);
                                moveDone = true;
                            }else if(topColor == previousSideColor){

                                //Align to position
                                topLayerAlignment(j,getDistance(j,i));

                                insertIntoSecondLayerLeft(i);
                                moveDone = true;
                            }
                        }
                    }
                    //</editor-fold>
                    if(moveDone)
                        break;
                    //<editor-fold default-state="collapsed" desc="Check for piece in middle">
                    for(int j = 0; j < 4; j++){
                        if(j != i) {
                            if (tempRubiksCube[j][5].getColor() == currentSideColor && tempRubiksCube[(j + 1) % 4][3].getColor() == nextSideColor ||
                                tempRubiksCube[j][5].getColor() == nextSideColor && tempRubiksCube[(j+1)%4][3].getColor() == currentSideColor) {
                                insertIntoSecondLayerRight(j);
                                break;
                            }
                        } else {
                            if(tempRubiksCube[j][5].getColor() == nextSideColor && tempRubiksCube[(j+1)%4][3].getColor() == currentSideColor){
                                insertIntoSecondLayerRight(j);
                                break;
                            }
                        }
                    }
                    //</editor-fold>
                }
            }


        }
    }

    private void SecondCross(){
        boolean complete = false;

        while(!complete){
            //Get Color
            Color topColor = tempRubiksCube[4][4].getColor();

            //Check how many edge pieces are in place
            int goodPieces = 0;

            for(int i = 1; i<8; i+=2){
                if(tempRubiksCube[4][i].getColor() == topColor){
                    goodPieces++;
                }
            }

            //Do Moves to solve
            switch (goodPieces){
                case 0:
                    LTop(0);
                    break;
                case 2:
                    //<editor-fold desc="Decide if L or line shape">
                    boolean lineShape = false;
                    int lineSide = 0;

                    for(int i = 0; i <2; i++){
                        if(tempRubiksCube[4][1 + (2*i)].getColor() == topColor && tempRubiksCube[4][8 - 1 -(2*i)].getColor() == topColor){
                            lineShape = true;
                            lineSide = i + 1;
                            break;
                        }
                    }

                    if(lineShape)
                        lineTop(lineSide);
                    else {
                        for (int i = 0; i < 4; i++) {
                            int topL = 0, leftL = 0;

                            switch (i) {
                                case 0:
                                    topL = 1;
                                    leftL = 3;
                                    break;
                                case 1:
                                    topL = 3;
                                    leftL = 7;
                                    break;
                                case 2:
                                    topL = 7;
                                    leftL = 5;
                                    break;
                                case 3:
                                    topL = 5;
                                    leftL = 1;
                                    break;
                            }

                            if(tempRubiksCube[4][topL].getColor() == topColor && tempRubiksCube[4][leftL].getColor() == topColor){
                                LTop(i);
                            }
                        }
                    }
                    //</editor-fold>
                    break;
                case 4:
                    complete = true;
                    break;
            }
        }
    }

    private void OLL() {
        boolean complete = false;

        while (!complete) {
            complete = true;

            //Get top Color
            Color topColor = tempRubiksCube[4][4].getColor();

            //<editor-fold desc="Check for Complete OLL">
            for (int j = 0; j < 9; j++) {
                if (tempRubiksCube[4][j].getColor() != topColor) {
                    complete = false;
                    break;
                }
            }
            //</editor-fold>

             if (!complete)
            //<editor-fold desc="Sune Check">
            for (int i = 0; i < 4; i++) {
                int misplacedEdge = 2;
                if (tempRubiksCube[i][misplacedEdge].getColor() == topColor &&
                        tempRubiksCube[(i + 1) % 4][misplacedEdge].getColor() == topColor &&
                        tempRubiksCube[(i + 2) % 4][misplacedEdge].getColor() == topColor) {
                    //R U R` U R 2U R`
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.ClockWise, i, 2);
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    // complete = true;
                    break;
                }
            }
            //</editor-fold>
            //27
             if (!complete)
            //<editor-fold desc="Anti-Sune Check">
            for (int i = 0; i < 4; i++) {
                int misplacedEdge = 0;
                if (tempRubiksCube[i][misplacedEdge].getColor() == topColor &&
                        tempRubiksCube[(i + 1) % 4][misplacedEdge].getColor() == topColor &&
                        tempRubiksCube[(i + 2) % 4][misplacedEdge].getColor() == topColor) {
                    //R` U` R U` R` 2U R
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.ClockWise, i, 2);
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    //complete = true;
                    break;
                }
            }
            //</editor-fold>
            //26
              if (!complete)
            //<editor-fold desc="2 Headlights">
            for (int i = 0; i < 4; i++) {
                int misplacedEdge1 = 0;
                int misplacedEdge2 = 2;
                if (tempRubiksCube[i][misplacedEdge1].getColor() == topColor &&
                        tempRubiksCube[i][misplacedEdge2].getColor() == topColor &&
                        tempRubiksCube[(i + 2) % 4][misplacedEdge1].getColor() == topColor &&
                        tempRubiksCube[(i + 2) % 4][misplacedEdge2].getColor() == topColor) {
                    //R 2U R` U` R U R` U` R U` R`
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.ClockWise, i, 2);
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    //  complete = true;
                    break;
                }
            }
            //</editor-fold>
            //21
              if (!complete)
            //<editor-fold desc="HeadLights">
            for (int i = 0; i < 4; i++) {
                int misplacedEdgeLeft1 = 0; // BACK ASWELL
                int misplacedEdgeLeft2 = 2; // FRONT ASWELL
                if (tempRubiksCube[(i + 3) % 4][misplacedEdgeLeft1].getColor() == topColor &&
                        tempRubiksCube[(i + 3) % 4][misplacedEdgeLeft2].getColor() == topColor &&
                        tempRubiksCube[i][misplacedEdgeLeft2].getColor() == topColor &&
                        tempRubiksCube[(i + 2) % 4][misplacedEdgeLeft1].getColor() == topColor) {

                    if (tempRubiksCube[4][0].getColor() != topColor &&
                            tempRubiksCube[4][2].getColor() != topColor &&
                            tempRubiksCube[4][6].getColor() != topColor &&
                            tempRubiksCube[4][8].getColor() != topColor) {
                        //R 2U 2R U` 2R U` 2R 2U R
                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                        solutionMovesAdd(MoveType.U, RotationType.ClockWise, i, 2);
                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 2);
                        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, i, 1);
                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 2);
                        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, i, 1);
                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 2);
                        solutionMovesAdd(MoveType.U, RotationType.ClockWise, i, 2);
                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                        //   complete = true;
                        break;
                    }
                }
            }
            //</editor-fold>
            //22
               if (!complete)
            //<editor-fold desc="Diamond">
            for (int i = 0; i < 4; i++) {
                int misplacedEdge = 0;
                int misplacedEdge1 = 2;
                if (tempRubiksCube[i][misplacedEdge].getColor() == topColor &&
                        tempRubiksCube[(i + 1) % 4][misplacedEdge1].getColor() == topColor) {
                    //R`F R B` R` F` R B
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.F, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.B, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.F, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.B, RotationType.ClockWise, i, 1);
                    //  complete = true;
                    break;
                }
            }
            //</editor-fold>
            //25
               if (!complete)
            //<editor-fold desc="SuperMan">
            for (int i = 0; i < 4; i++) {
                int misplacedEdge1 = 0;
                int misplacedEdge2 = 2;
                if (tempRubiksCube[i][misplacedEdge1].getColor() == topColor &&
                        tempRubiksCube[i][misplacedEdge2].getColor() == topColor) {
                    //R D R` 2U R D` R` 2U R`
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 2);
                    solutionMovesAdd(MoveType.D, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.ClockWise, i, 2);
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.D, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.U, RotationType.ClockWise, i, 2);
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    //  complete = true;
                    break;
                }
            }
            //</editor-fold>
            //23
              if (!complete)
            //<editor-fold desc="Eyes to the side">
            for (int i = 0; i < 4; i++) {
                int misplacedEdge = 2;
                int misplacedEdge1 = 0;
                if (tempRubiksCube[i][misplacedEdge].getColor() == topColor &&
                        tempRubiksCube[(i + 2) % 4][misplacedEdge1].getColor() == topColor) {
                    // R' F' L F R F' L' F
                    solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.F, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.L, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.F, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.R, RotationType.ClockWise, i, 1);
                    solutionMovesAdd(MoveType.F, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.L, RotationType.CounterClockWise, i, 1);
                    solutionMovesAdd(MoveType.F, RotationType.ClockWise, i, 1);
                    // complete = true;
                    break;
                }
            }
            //</editor-fold>
            //24
        }
    }

    private void Headlights() {
        boolean complete = false;

        while (!complete) {
            //Check for headlights
            int headlightCount = 0;
            int headlightIndex = 0;
            for (int i = 0; i < 4; i++) {
                if (tempRubiksCube[i][0].getColor() == tempRubiksCube[i][2].getColor()) {
                    headlightCount++;
                    headlightIndex = i;
                }
                if (headlightCount > 1)
                    break;
            }

            if(headlightCount<2)
                headlightSetup((headlightIndex + 1)%4);
            else complete = true;
        }
    }

    private void headlightSetup(int sideToPreform){
        solutionMovesAdd(MoveType.R, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise,sideToPreform,1);
        solutionMovesAdd(MoveType.F, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.ClockWise, sideToPreform, 2);
        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.F, RotationType.CounterClockWise, sideToPreform, 1);
    }

    private void  PLL() {
        if(!isCubeSolved()) {
            //<editor-fold desc="Check for U Perm">
            for (int i = 0; i < 4; i++) {
                Color leftColor = tempRubiksCube[i][0].getColor();
                Color middleColor = tempRubiksCube[i][1].getColor();
                Color rightColor = tempRubiksCube[i][2].getColor();

                //Check for a complete side
                if (leftColor == middleColor && middleColor == rightColor) {
                    int indexToPreform = (i + 2) % 4;
                    //Check direction to do U Perm
                    if (tempRubiksCube[(i + 2) % 4][1].getColor() == tempRubiksCube[(i + 1) % 4][0].getColor()) {
                        //Ub Perm

                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, indexToPreform, 2);
                        solutionMovesAdd(MoveType.U, RotationType.ClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.U, RotationType.ClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.U, RotationType.ClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, indexToPreform, 1);
                        return;
                    } else {
                        //Ua Perm

                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, indexToPreform, 1);

                        solutionMovesAdd(MoveType.U, RotationType.ClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.U, RotationType.ClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, indexToPreform, 1);
                        solutionMovesAdd(MoveType.R, RotationType.ClockWise, indexToPreform, 2);
                        return;
                    }
                }
            }
            //</editor-fold>

            //<editor-fold desc="Z or H Perm"
            if (tempRubiksCube[2][1].getColor() == tempRubiksCube[0][0].getColor()) {
                solutionMovesAdd(MoveType.M, RotationType.ClockWise, 0, 2);
                solutionMovesAdd(MoveType.U, RotationType.ClockWise, 0, 1);

                solutionMovesAdd(MoveType.M, RotationType.ClockWise, 0, 2);
                solutionMovesAdd(MoveType.U, RotationType.ClockWise, 0, 2);

                solutionMovesAdd(MoveType.M, RotationType.ClockWise, 0, 2);
                solutionMovesAdd(MoveType.U, RotationType.ClockWise, 0, 1);

                solutionMovesAdd(MoveType.M, RotationType.ClockWise, 0, 2);
                return;
            } else {
                int indexToPreform = 0;

                if (tempRubiksCube[0][1].getColor() != tempRubiksCube[1][0].getColor()) {
                    indexToPreform++;
                }


                solutionMovesAdd(MoveType.M, RotationType.ClockWise, indexToPreform, 2);
                solutionMovesAdd(MoveType.U, RotationType.ClockWise, indexToPreform, 1);

                solutionMovesAdd(MoveType.M, RotationType.ClockWise, indexToPreform, 2);
                solutionMovesAdd(MoveType.U, RotationType.ClockWise, indexToPreform, 1);

                solutionMovesAdd(MoveType.M, RotationType.CounterClockWise, indexToPreform, 1);
                solutionMovesAdd(MoveType.U, RotationType.ClockWise, indexToPreform, 2);

                solutionMovesAdd(MoveType.M, RotationType.ClockWise, indexToPreform, 2);
                solutionMovesAdd(MoveType.U, RotationType.ClockWise, indexToPreform, 2);

                solutionMovesAdd(MoveType.M, RotationType.CounterClockWise, indexToPreform, 1);
                return;
            }
            //</editor-fold
        }
    }

    private void lineTop(int sideToPreform){
        solutionMovesAdd(MoveType.F, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.F, RotationType.CounterClockWise, sideToPreform, 1);
    }

    private void LTop(int sideToPreform){
        solutionMovesAdd(MoveType.F, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.F, RotationType.CounterClockWise, sideToPreform, 1);
    }

    private void insertIntoSecondLayerRight(int sideToPreform){
        solutionMovesAdd(MoveType.U, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.R, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.F, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.F, RotationType.ClockWise, sideToPreform, 1);
    }

    private void insertIntoSecondLayerLeft(int sideToPreform){
        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.L, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.L, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.F, RotationType.ClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, sideToPreform, 1);
        solutionMovesAdd(MoveType.F, RotationType.CounterClockWise, sideToPreform, 1);
    }

    private int getDistance(int currentPos, int finalPos){
        return Math.floorMod(currentPos-finalPos,4);
    }

    private void topLayerAlignment(int j, int distance) {
        switch (distance) {
            case 0:
                break;
            case 1:
                solutionMovesAdd(MoveType.U, RotationType.ClockWise, j, 1);
                break;
            case 2:
                solutionMovesAdd(MoveType.U, RotationType.ClockWise, j, 2);
                break;
            case 3:
                solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, j, 1);
                break;
        }
    }

    private int getBottomEdgeIndex(int side){
        switch (side) {
            case 0:
                return 1;
            case 1:
                return 5;
            case 2:
                return 7;
            case 3:
                return 3;
            default:
                return -1;
        }
    }

    private int getTopEdgeIndex(int side){
        switch (side) {
            case 0:
                return 7;
            case 1:
                return 5;
            case 2:
                return 1;
            case 3:
                return 3;
            default:
                return -1;
        }
    }

    private int getBottomCornerIndex(int side) {
        switch (side) {
            case 0:
                return 2;
            case 1:
                return 8;
            case 2:
                return 6;
            case 3:
                return 0;
            default:
                return -1;
        }
    }

    private int getTopCornerIndex(int side) {
        switch (side) {
            case 0:
                return 8;
            case 1:
                return 2;
            case 2:
                return 0;
            case 3:
                return 6;
            default:
                return -1;
        }
    }

    private boolean isCubeSolved(){
        for(int i = 0; i < 6; i++){
            Color centerColor = tempRubiksCube[i][4].getColor();
            for(int j =0; j<9;j++){
                if(centerColor != tempRubiksCube[i][j].getColor())
                    return false;
            }
        }
        return true;
    }

    private void lineUpCube() {
        Color centerZero = tempRubiksCube[0][4].getColor();
        Color topLayerZero = tempRubiksCube[0][0].getColor();
        if (centerZero != topLayerZero) {
            for (int i = 1; i < 4; i++) {
                topLayerZero = tempRubiksCube[i][0].getColor();
                if (topLayerZero == centerZero) {
                    switch (i) {
                        case 1:
                            solutionMovesAdd(MoveType.U, RotationType.ClockWise, 0, 1);
                            break;
                        case 2:
                            solutionMovesAdd(MoveType.U, RotationType.ClockWise, 0, 2);
                            break;
                        case 3:
                            solutionMovesAdd(MoveType.U, RotationType.CounterClockWise, 0, 1);
                            break;
                    }
                    break;
                }
            }
        }
    }

    private void solutionMovesAdd(MoveType moveType, RotationType rotationType, int layerNum, int timesToAdd){ // will add moves to list and apply on temp cube
        for (int i = 0; i < timesToAdd; i++) {
            System.out.println(moveType.toString() + " " + rotationType.toString());

            solutionMoves.add(moveType);
            solutionMovesRotation.add(rotationType);
            solutionLayerNumber.add(layerNum);

            LayerColor layerColor = null;
            //<editor-fold desc="Layer Number Switch">

            //<editor-fold desc="Layer Number Switch">
            switch (layerNum) {
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
                    break;

            }
            //</editor-fold>
            rotateLayer(moveType,layerColor,rotationType);
        }

    }

    //Temporary cube
    public void rotateLayer(MoveType moveType, LayerColor layerColor, RotationType rotationType) {
        currentLayer.setLayer(tempRubiksCube);
        currentLayer.chooseMove(layerColor,rotationType,moveType);
        tempRubiksCube = currentLayer.getSticker();
    }
}