package cube;

import javafx.scene.paint.Color;

public class Layer {
    Sticker[][] sticker;
    Color[][] oldColor = new Color[6][9];

    public Layer() {
    }

    public void setLayer(Sticker[][] sticker){
        this.sticker = sticker;
    }

    private void redFace(RotationType direction) {
        switch (direction) {
            case ClockWise:
                //todo: rotate the front face
                sticker[0][0].setColor(oldColor[0][6]);
                sticker[0][1].setColor(oldColor[0][3]);
                sticker[0][2].setColor(oldColor[0][0]);
                sticker[0][3].setColor(oldColor[0][7]);
                sticker[0][4].setColor(oldColor[0][4]);
                sticker[0][5].setColor(oldColor[0][1]);
                sticker[0][6].setColor(oldColor[0][8]);
                sticker[0][7].setColor(oldColor[0][5]);
                sticker[0][8].setColor(oldColor[0][2]);

                //todo: rotate edges

                //todo: top
                sticker[4][6].setColor(oldColor[3][8]);
                sticker[4][7].setColor(oldColor[3][5]);
                sticker[4][8].setColor(oldColor[3][2]);
                //todo: right side
                sticker[1][0].setColor(oldColor[4][6]);
                sticker[1][3].setColor(oldColor[4][7]);
                sticker[1][6].setColor(oldColor[4][8]);
                //todo: bottom
                sticker[5][0].setColor(oldColor[1][6]);
                sticker[5][1].setColor(oldColor[1][3]);
                sticker[5][2].setColor(oldColor[1][0]);
                //todo: left side
                sticker[3][2].setColor(oldColor[5][0]);
                sticker[3][5].setColor(oldColor[5][1]);
                sticker[3][8].setColor(oldColor[5][2]);
                break;

            case CounterClockWise:
                //todo: rotate the front face
                sticker[0][0].setColor(oldColor[0][2]);
                sticker[0][1].setColor(oldColor[0][5]);
                sticker[0][2].setColor(oldColor[0][8]);
                sticker[0][3].setColor(oldColor[0][1]);
                sticker[0][4].setColor(oldColor[0][4]);
                sticker[0][5].setColor(oldColor[0][7]);
                sticker[0][6].setColor(oldColor[0][0]);
                sticker[0][7].setColor(oldColor[0][3]);
                sticker[0][8].setColor(oldColor[0][6]);

                //todo: rotate edges

                //todo: top
                sticker[4][6].setColor(oldColor[1][0]);
                sticker[4][7].setColor(oldColor[1][3]);
                sticker[4][8].setColor(oldColor[1][6]);
                //todo: right side
                sticker[1][0].setColor(oldColor[5][2]);
                sticker[1][3].setColor(oldColor[5][1]);
                sticker[1][6].setColor(oldColor[5][0]);
                //todo: bottom
                sticker[5][0].setColor(oldColor[3][2]);
                sticker[5][1].setColor(oldColor[3][5]);
                sticker[5][2].setColor(oldColor[3][8]);
                //todo: left side
                sticker[3][2].setColor(oldColor[4][8]);
                sticker[3][5].setColor(oldColor[4][7]);
                sticker[3][8].setColor(oldColor[4][6]);
                break;
        }
    }

    private void greenFace(RotationType direction) {

        switch (direction) {
            case ClockWise:
                //todo: rotate the front face
                sticker[1][0].setColor(oldColor[1][6]);
                sticker[1][1].setColor(oldColor[1][3]);
                sticker[1][2].setColor(oldColor[1][0]);
                sticker[1][3].setColor(oldColor[1][7]);
                sticker[1][4].setColor(oldColor[1][4]);
                sticker[1][5].setColor(oldColor[1][1]);
                sticker[1][6].setColor(oldColor[1][8]);
                sticker[1][7].setColor(oldColor[1][5]);
                sticker[1][8].setColor(oldColor[1][2]);

                //todo: rotate edges

                //todo: top
                sticker[4][8].setColor(oldColor[0][8]);
                sticker[4][5].setColor(oldColor[0][5]);
                sticker[4][2].setColor(oldColor[0][2]);
                //todo: right side
                sticker[2][0].setColor(oldColor[4][8]);
                sticker[2][3].setColor(oldColor[4][5]);
                sticker[2][6].setColor(oldColor[4][2]);
                //todo: bottom
                sticker[5][2].setColor(oldColor[2][6]);
                sticker[5][5].setColor(oldColor[2][3]);
                sticker[5][8].setColor(oldColor[2][0]);
                //todo: left side
                sticker[0][2].setColor(oldColor[5][2]);
                sticker[0][5].setColor(oldColor[5][5]);
                sticker[0][8].setColor(oldColor[5][8]);
                break;

            case CounterClockWise:
                //todo: rotate the front face
                sticker[1][0].setColor(oldColor[1][2]);
                sticker[1][1].setColor(oldColor[1][5]);
                sticker[1][2].setColor(oldColor[1][8]);
                sticker[1][3].setColor(oldColor[1][1]);
                sticker[1][4].setColor(oldColor[1][4]);
                sticker[1][5].setColor(oldColor[1][7]);
                sticker[1][6].setColor(oldColor[1][0]);
                sticker[1][7].setColor(oldColor[1][3]);
                sticker[1][8].setColor(oldColor[1][6]);

                //todo: rotate edges

                //todo: top
                sticker[4][8].setColor(oldColor[2][0]);
                sticker[4][5].setColor(oldColor[2][3]);
                sticker[4][2].setColor(oldColor[2][6]);
                //todo: right side
                sticker[2][0].setColor(oldColor[5][8]);
                sticker[2][3].setColor(oldColor[5][5]);
                sticker[2][6].setColor(oldColor[5][2]);
                //todo: bottom
                sticker[5][2].setColor(oldColor[0][2]);
                sticker[5][5].setColor(oldColor[0][5]);
                sticker[5][8].setColor(oldColor[0][8]);
                //todo: left side
                sticker[0][2].setColor(oldColor[4][2]);
                sticker[0][5].setColor(oldColor[4][5]);
                sticker[0][8].setColor(oldColor[4][8]);
                break;

        }
    }

    private void orangeFace(RotationType direction) {
        switch (direction) {
            case ClockWise:
                //todo: rotate the front face
                sticker[2][0].setColor(oldColor[2][6]);
                sticker[2][1].setColor(oldColor[2][3]);
                sticker[2][2].setColor(oldColor[2][0]);
                sticker[2][3].setColor(oldColor[2][7]);
                sticker[2][4].setColor(oldColor[2][4]);
                sticker[2][5].setColor(oldColor[2][1]);
                sticker[2][6].setColor(oldColor[2][8]);
                sticker[2][7].setColor(oldColor[2][5]);
                sticker[2][8].setColor(oldColor[2][2]);

                //todo: rotate edges

                //todo: top
                sticker[4][2].setColor(oldColor[1][8]);
                sticker[4][1].setColor(oldColor[1][5]);
                sticker[4][0].setColor(oldColor[1][2]);
                //todo: right side
                sticker[3][0].setColor(oldColor[4][2]);
                sticker[3][3].setColor(oldColor[4][1]);
                sticker[3][6].setColor(oldColor[4][0]);
                //todo: bottom
                sticker[5][8].setColor(oldColor[3][6]);
                sticker[5][7].setColor(oldColor[3][3]);
                sticker[5][6].setColor(oldColor[3][0]);
                //todo: left side
                sticker[1][2].setColor(oldColor[5][8]);
                sticker[1][5].setColor(oldColor[5][7]);
                sticker[1][8].setColor(oldColor[5][6]);
                break;

            case CounterClockWise:
                //todo: rotate the front face
                sticker[2][0].setColor(oldColor[2][2]);
                sticker[2][1].setColor(oldColor[2][5]);
                sticker[2][2].setColor(oldColor[2][8]);
                sticker[2][3].setColor(oldColor[2][1]);
                sticker[2][4].setColor(oldColor[2][4]);
                sticker[2][5].setColor(oldColor[2][7]);
                sticker[2][6].setColor(oldColor[2][0]);
                sticker[2][7].setColor(oldColor[2][3]);
                sticker[2][8].setColor(oldColor[2][6]);

                //todo: rotate edges

                //todo: top
                sticker[4][2].setColor(oldColor[3][0]);
                sticker[4][1].setColor(oldColor[3][3]);
                sticker[4][0].setColor(oldColor[3][6]);
                //todo: right side
                sticker[3][0].setColor(oldColor[5][6]);
                sticker[3][3].setColor(oldColor[5][7]);
                sticker[3][6].setColor(oldColor[5][8]);
                //todo: bottom
                sticker[5][8].setColor(oldColor[1][2]);
                sticker[5][7].setColor(oldColor[1][5]);
                sticker[5][6].setColor(oldColor[1][8]);
                //todo: left side
                sticker[1][2].setColor(oldColor[4][0]);
                sticker[1][5].setColor(oldColor[4][1]);
                sticker[1][8].setColor(oldColor[4][2]);
                break;

        }
    }

    private void blueFace(RotationType direction) {
        switch (direction) {
            case ClockWise:
                //todo: rotate the front face
                sticker[3][0].setColor(oldColor[3][6]);
                sticker[3][1].setColor(oldColor[3][3]);
                sticker[3][2].setColor(oldColor[3][0]);
                sticker[3][3].setColor(oldColor[3][7]);
                sticker[3][4].setColor(oldColor[3][4]);
                sticker[3][5].setColor(oldColor[3][1]);
                sticker[3][6].setColor(oldColor[3][8]);
                sticker[3][7].setColor(oldColor[3][5]);
                sticker[3][8].setColor(oldColor[3][2]);

                //todo: rotate edges

                //todo: top
                sticker[4][0].setColor(oldColor[2][8]);
                sticker[4][3].setColor(oldColor[2][5]);
                sticker[4][6].setColor(oldColor[2][2]);
                //todo: right side
                sticker[0][0].setColor(oldColor[4][0]);
                sticker[0][3].setColor(oldColor[4][3]);
                sticker[0][6].setColor(oldColor[4][6]);
                //todo: bottom
                sticker[5][6].setColor(oldColor[0][6]);
                sticker[5][3].setColor(oldColor[0][3]);
                sticker[5][0].setColor(oldColor[0][0]);
                //todo: left side
                sticker[2][2].setColor(oldColor[5][6]);
                sticker[2][5].setColor(oldColor[5][3]);
                sticker[2][8].setColor(oldColor[5][0]);
                break;

            case CounterClockWise:
                //todo: rotate the front face
                sticker[3][0].setColor(oldColor[3][2]);
                sticker[3][1].setColor(oldColor[3][5]);
                sticker[3][2].setColor(oldColor[3][8]);
                sticker[3][3].setColor(oldColor[3][1]);
                sticker[3][4].setColor(oldColor[3][4]);
                sticker[3][5].setColor(oldColor[3][7]);
                sticker[3][6].setColor(oldColor[3][0]);
                sticker[3][7].setColor(oldColor[3][3]);
                sticker[3][8].setColor(oldColor[3][6]);

                //todo: rotate edges

                //todo: top
                sticker[4][0].setColor(oldColor[0][0]);
                sticker[4][3].setColor(oldColor[0][3]);
                sticker[4][6].setColor(oldColor[0][6]);
                //todo: right side
                sticker[0][0].setColor(oldColor[5][0]);
                sticker[0][3].setColor(oldColor[5][3]);
                sticker[0][6].setColor(oldColor[5][6]);
                //todo: bottom
                sticker[5][6].setColor(oldColor[2][2]);
                sticker[5][3].setColor(oldColor[2][5]);
                sticker[5][0].setColor(oldColor[2][8]);
                //todo: left side
                sticker[2][2].setColor(oldColor[4][6]);
                sticker[2][5].setColor(oldColor[4][3]);
                sticker[2][8].setColor(oldColor[4][0]);
                break;

        }
    }

    private void yellowFace(RotationType direction) {
        switch (direction) {
            case ClockWise:
                //todo: rotate the front face
                sticker[4][0].setColor(oldColor[4][6]);
                sticker[4][1].setColor(oldColor[4][3]);
                sticker[4][2].setColor(oldColor[4][0]);
                sticker[4][3].setColor(oldColor[4][7]);
                sticker[4][4].setColor(oldColor[4][4]);
                sticker[4][5].setColor(oldColor[4][1]);
                sticker[4][6].setColor(oldColor[4][8]);
                sticker[4][7].setColor(oldColor[4][5]);
                sticker[4][8].setColor(oldColor[4][2]);

                //todo: rotate edges

                //todo: top
                sticker[2][2].setColor(oldColor[3][2]);
                sticker[2][1].setColor(oldColor[3][1]);
                sticker[2][0].setColor(oldColor[3][0]);
                //todo: right side
                sticker[1][2].setColor(oldColor[2][2]);
                sticker[1][1].setColor(oldColor[2][1]);
                sticker[1][0].setColor(oldColor[2][0]);
                //todo: bottom
                sticker[0][0].setColor(oldColor[1][0]);
                sticker[0][1].setColor(oldColor[1][1]);
                sticker[0][2].setColor(oldColor[1][2]);
                //todo: left side
                sticker[3][0].setColor(oldColor[0][0]);
                sticker[3][1].setColor(oldColor[0][1]);
                sticker[3][2].setColor(oldColor[0][2]);
                break;

            case CounterClockWise:
                //todo: rotate the front face
                sticker[4][0].setColor(oldColor[4][2]);
                sticker[4][1].setColor(oldColor[4][5]);
                sticker[4][2].setColor(oldColor[4][8]);
                sticker[4][3].setColor(oldColor[4][1]);
                sticker[4][4].setColor(oldColor[4][4]);
                sticker[4][5].setColor(oldColor[4][7]);
                sticker[4][6].setColor(oldColor[4][0]);
                sticker[4][7].setColor(oldColor[4][3]);
                sticker[4][8].setColor(oldColor[4][6]);

                //todo: rotate edges

                //todo: top
                sticker[2][2].setColor(oldColor[1][2]);
                sticker[2][1].setColor(oldColor[1][1]);
                sticker[2][0].setColor(oldColor[1][0]);
                //todo: right side
                sticker[1][2].setColor(oldColor[0][2]);
                sticker[1][1].setColor(oldColor[0][1]);
                sticker[1][0].setColor(oldColor[0][0]);
                //todo: bottom
                sticker[0][0].setColor(oldColor[3][0]);
                sticker[0][1].setColor(oldColor[3][1]);
                sticker[0][2].setColor(oldColor[3][2]);
                //todo: left side
                sticker[3][0].setColor(oldColor[2][0]);
                sticker[3][1].setColor(oldColor[2][1]);
                sticker[3][2].setColor(oldColor[2][2]);
                break;

        }
    }

    private void whiteFace(RotationType direction) {
        switch (direction) {
            case ClockWise:
                //todo: rotate the front face
                sticker[5][0].setColor(oldColor[5][6]);
                sticker[5][1].setColor(oldColor[5][3]);
                sticker[5][2].setColor(oldColor[5][0]);
                sticker[5][3].setColor(oldColor[5][7]);
                sticker[5][4].setColor(oldColor[5][4]);
                sticker[5][5].setColor(oldColor[5][1]);
                sticker[5][6].setColor(oldColor[5][8]);
                sticker[5][7].setColor(oldColor[5][5]);
                sticker[5][8].setColor(oldColor[5][2]);

                //todo: rotate edges

                //todo: top
                sticker[0][6].setColor(oldColor[3][6]);
                sticker[0][7].setColor(oldColor[3][7]);
                sticker[0][8].setColor(oldColor[3][8]);
                //todo: right side
                sticker[1][6].setColor(oldColor[0][6]);
                sticker[1][7].setColor(oldColor[0][7]);
                sticker[1][8].setColor(oldColor[0][8]);
                //todo: bottom
                sticker[2][6].setColor(oldColor[1][6]);
                sticker[2][7].setColor(oldColor[1][7]);
                sticker[2][8].setColor(oldColor[1][8]);
                //todo: left side
                sticker[3][6].setColor(oldColor[2][6]);
                sticker[3][7].setColor(oldColor[2][7]);
                sticker[3][8].setColor(oldColor[2][8]);
                break;

            case CounterClockWise:
                //todo: rotate the front face
                sticker[5][0].setColor(oldColor[5][2]);
                sticker[5][1].setColor(oldColor[5][5]);
                sticker[5][2].setColor(oldColor[5][8]);
                sticker[5][3].setColor(oldColor[5][1]);
                sticker[5][4].setColor(oldColor[5][4]);
                sticker[5][5].setColor(oldColor[5][7]);
                sticker[5][6].setColor(oldColor[5][0]);
                sticker[5][7].setColor(oldColor[5][3]);
                sticker[5][8].setColor(oldColor[5][6]);

                //todo: rotate edges

                //todo: top
                sticker[0][6].setColor(oldColor[1][6]);
                sticker[0][7].setColor(oldColor[1][7]);
                sticker[0][8].setColor(oldColor[1][8]);
                //todo: right side
                sticker[1][6].setColor(oldColor[2][6]);
                sticker[1][7].setColor(oldColor[2][7]);
                sticker[1][8].setColor(oldColor[2][8]);
                //todo: bottom
                sticker[2][6].setColor(oldColor[3][6]);
                sticker[2][7].setColor(oldColor[3][7]);
                sticker[2][8].setColor(oldColor[3][8]);
                //todo: left side
                sticker[3][6].setColor(oldColor[0][6]);
                sticker[3][7].setColor(oldColor[0][7]);
                sticker[3][8].setColor(oldColor[0][8]);
                break;

        }
    }

    //Extra Moves (M,E) Standing will be recycled with M,

    private void middleRedFacing(RotationType direction) {
        switch (direction) {
            case ClockWise:
                sticker[0][1].setColor(oldColor[4][1]);
                sticker[0][4].setColor(oldColor[4][4]);
                sticker[0][7].setColor(oldColor[4][7]);

                sticker[4][1].setColor(oldColor[2][7]);
                sticker[4][4].setColor(oldColor[2][4]);
                sticker[4][7].setColor(oldColor[2][1]);

                sticker[2][1].setColor(oldColor[5][7]);
                sticker[2][4].setColor(oldColor[5][4]);
                sticker[2][7].setColor(oldColor[5][1]);

                sticker[5][1].setColor(oldColor[0][1]);
                sticker[5][4].setColor(oldColor[0][4]);
                sticker[5][7].setColor(oldColor[0][7]);
                break;

            case CounterClockWise:
                sticker[0][1].setColor(oldColor[5][1]);
                sticker[0][4].setColor(oldColor[5][4]);
                sticker[0][7].setColor(oldColor[5][7]);

                sticker[4][1].setColor(oldColor[0][1]);
                sticker[4][4].setColor(oldColor[0][4]);
                sticker[4][7].setColor(oldColor[0][7]);

                sticker[2][1].setColor(oldColor[4][7]);
                sticker[2][4].setColor(oldColor[4][4]);
                sticker[2][7].setColor(oldColor[4][1]);

                sticker[5][1].setColor(oldColor[2][7]);
                sticker[5][4].setColor(oldColor[2][4]);
                sticker[5][7].setColor(oldColor[2][1]);
                break;
        }
    }

    private void middleGreenFacing(RotationType direction) {
        switch (direction) {
            case ClockWise:
                sticker[1][1].setColor(oldColor[4][3]);
                sticker[1][4].setColor(oldColor[4][4]);
                sticker[1][7].setColor(oldColor[4][5]);

                sticker[4][3].setColor(oldColor[3][7]);
                sticker[4][4].setColor(oldColor[3][4]);
                sticker[4][5].setColor(oldColor[3][1]);

                sticker[3][7].setColor(oldColor[5][5]);
                sticker[3][4].setColor(oldColor[5][4]);
                sticker[3][1].setColor(oldColor[5][3]);

                sticker[5][3].setColor(oldColor[1][7]);
                sticker[5][4].setColor(oldColor[1][4]);
                sticker[5][5].setColor(oldColor[1][1]);
                break;

            case CounterClockWise:
                sticker[1][1].setColor(oldColor[5][5]);
                sticker[1][4].setColor(oldColor[5][4]);
                sticker[1][7].setColor(oldColor[5][3]);

                sticker[4][3].setColor(oldColor[1][1]);
                sticker[4][4].setColor(oldColor[1][4]);
                sticker[4][5].setColor(oldColor[1][7]);

                sticker[3][7].setColor(oldColor[4][3]);
                sticker[3][4].setColor(oldColor[4][4]);
                sticker[3][1].setColor(oldColor[4][5]);

                sticker[5][3].setColor(oldColor[3][1]);
                sticker[5][4].setColor(oldColor[3][4]);
                sticker[5][5].setColor(oldColor[3][7]);
                break;
        }
    }

    private void equatorialRedFacing(RotationType direction) {
        switch (direction) {
            case ClockWise:
                sticker[0][3].setColor(oldColor[3][3]);
                sticker[0][4].setColor(oldColor[3][4]);
                sticker[0][5].setColor(oldColor[3][5]);

                sticker[3][3].setColor(oldColor[2][3]);
                sticker[3][4].setColor(oldColor[2][4]);
                sticker[3][5].setColor(oldColor[2][5]);

                sticker[2][3].setColor(oldColor[1][3]);
                sticker[2][4].setColor(oldColor[1][4]);
                sticker[2][5].setColor(oldColor[1][5]);

                sticker[1][3].setColor(oldColor[0][3]);
                sticker[1][4].setColor(oldColor[0][4]);
                sticker[1][5].setColor(oldColor[0][5]);
                break;

            case CounterClockWise:
                sticker[0][3].setColor(oldColor[1][3]);
                sticker[0][4].setColor(oldColor[1][4]);
                sticker[0][5].setColor(oldColor[1][5]);

                sticker[3][3].setColor(oldColor[0][3]);
                sticker[3][4].setColor(oldColor[0][4]);
                sticker[3][5].setColor(oldColor[0][5]);

                sticker[2][3].setColor(oldColor[3][3]);
                sticker[2][4].setColor(oldColor[3][4]);
                sticker[2][5].setColor(oldColor[3][5]);

                sticker[1][3].setColor(oldColor[2][3]);
                sticker[1][4].setColor(oldColor[2][4]);
                sticker[1][5].setColor(oldColor[2][5]);
                break;
        }
    }

    public void chooseMove(LayerColor layerColor, RotationType direction, MoveType moveType) {

        int redFace = 0, greenFace = 1, orangeFace = 2, blueFace = 3, yellowFace = 4, whiteFace = 5;
        int layerNum = 0;

        //<editor-fold desc="Color to Int">
        switch (layerColor){
            case Red:
                break;
            case Green:
                layerNum = 1;
                break;
            case Orange:
                layerNum = 2;
                break;
            case Blue:
                layerNum = 3;
                break;
            case Yellow:
                layerNum = 4;
                break;
            case White:
                layerNum = 5;
        }
        //</editor-fold>

        //<editor-fold desc="Set Old Colors">
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                oldColor[i][j] = sticker[i][j].getColor();
            }
        }
        //</editor-fold>

        //<editor-fold desc="All Moves on red Face">
        if (layerNum == blueFace && moveType == MoveType.R ||
                layerNum == greenFace && moveType == MoveType.L ||
                layerNum == yellowFace && moveType == MoveType.D ||
                layerNum == whiteFace && moveType == MoveType.U ||
                layerNum == redFace && moveType == MoveType.F ||
                layerNum == orangeFace && moveType == MoveType.B) {
            redFace(direction);
            return;
        }
        //</editor-fold>

        //<editor-fold desc="All Moves on green Face">
        if (layerNum == redFace && moveType == MoveType.R ||
                layerNum == orangeFace && moveType == MoveType.L ||
                layerNum == yellowFace && moveType == MoveType.R ||
                layerNum == whiteFace && moveType == MoveType.R ||
                layerNum == greenFace && moveType == MoveType.F ||
                layerNum == blueFace && moveType == MoveType.B) {
            greenFace(direction);
            return;
        }
        //</editor-fold>

        //<editor-fold desc="All Moves on orange Face">
        if (layerNum == greenFace && moveType == MoveType.R ||
                layerNum == blueFace && moveType == MoveType.L ||
                layerNum == yellowFace && moveType == MoveType.U ||
                layerNum == whiteFace && moveType == MoveType.D ||
                layerNum == orangeFace && moveType == MoveType.F ||
                layerNum == redFace && moveType == MoveType.B) {
            orangeFace(direction);
            return;
        }
        //</editor-fold>

        //<editor-fold desc="All Moves on blue Face">
        if (layerNum == orangeFace && moveType == MoveType.R ||
                layerNum == redFace && moveType == MoveType.L ||
                layerNum == yellowFace && moveType == MoveType.L ||
                layerNum == whiteFace && moveType == MoveType.L ||
                layerNum == blueFace && moveType == MoveType.F ||
                layerNum == greenFace && moveType == MoveType.B) {
            blueFace(direction);
            return;
        }
        //</editor-fold>

        //<editor-fold desc="All Moves on yellow Face">
        if (layerNum == redFace && moveType == MoveType.U ||
                layerNum == greenFace && moveType == MoveType.U ||
                layerNum == orangeFace && moveType == MoveType.U ||
                layerNum == blueFace && moveType == MoveType.U ||
                layerNum == yellowFace && moveType == MoveType.F ||
                layerNum == whiteFace && moveType == MoveType.B) {
            yellowFace(direction);
            return;
        }
        //</editor-fold>

        //<editor-fold desc="All Moves on white Face">
        if (layerNum == redFace && moveType == MoveType.D ||
                layerNum == greenFace && moveType == MoveType.D ||
                layerNum == orangeFace && moveType == MoveType.D ||
                layerNum == blueFace && moveType == MoveType.D ||
                layerNum == whiteFace && moveType == MoveType.F ||
                layerNum == yellowFace && moveType == MoveType.B) {
            whiteFace(direction);
            return;
        }
        //</editor-fold>

        //Extra Moves

        //<editor-fold desc="Middle Facing Red">
        if(layerNum == redFace && moveType == MoveType.M ||
                layerNum == orangeFace && moveType == MoveType.M ||
                layerNum == yellowFace && moveType == MoveType.M ||
                layerNum == whiteFace && moveType == MoveType.M ||
                layerNum == greenFace && moveType == MoveType.S ||
                layerNum == blueFace && moveType == MoveType.S){

            //Flip Rotation
            if(layerNum == orangeFace || layerNum == greenFace){
                if(direction == RotationType.ClockWise){
                    direction = RotationType.CounterClockWise;
                } else direction = RotationType.ClockWise;
            }

            middleRedFacing(direction);
            return;
        }
        //</editor-fold>

        //<editor-fold desc="Middle Facing Green">
        if(layerNum == greenFace && moveType == MoveType.M ||
                layerNum == blueFace && moveType == MoveType.M ||
                layerNum == yellowFace && moveType == MoveType.E ||
                layerNum == whiteFace && moveType == MoveType.E ||
                layerNum == redFace && moveType == MoveType.S ||
                layerNum == orangeFace && moveType == MoveType.S){

            //Flip Rotation
            if(layerNum == whiteFace || layerNum == orangeFace){
                if(direction == RotationType.ClockWise){
                    direction = RotationType.CounterClockWise;
                } else direction = RotationType.ClockWise;
            }

            middleGreenFacing(direction);
            return;
        }
        //</editor-fold>

        //<editor-fold desc="Equatorial Facing Red">
        if(layerNum == redFace && moveType == MoveType.E ||
                layerNum == greenFace && moveType == MoveType.E ||
                layerNum == orangeFace && moveType == MoveType.E ||
                layerNum == blueFace && moveType == MoveType.E ||
                layerNum == yellowFace && moveType == MoveType.S ||
                layerNum == whiteFace && moveType == MoveType.S){

            //Flip Rotation
            if(layerNum == yellowFace){
                if(direction == RotationType.ClockWise){
                    direction = RotationType.CounterClockWise;
                } else direction = RotationType.ClockWise;
            }

            equatorialRedFacing(direction);
            return;
        }
        //</editor-fold>
    }


    public Sticker[][] getSticker() {
        return sticker;
    }
}
