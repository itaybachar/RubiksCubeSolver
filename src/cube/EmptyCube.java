package cube;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class EmptyCube {
    private Box[][] piece = new Box[3][9];
    private Group cube = new Group();

    private double pieceSize;
    private double pieceGap = 0.65;
    // SandyBrown
    public EmptyCube(double pieceSize) {
        this.pieceSize = pieceSize;
        createCube();
    }

    private void createCube() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                //todo: initialize the pieces
                piece[i][j] = new Box(pieceSize, pieceSize, pieceSize);
                piece[i][j].setMaterial(new PhongMaterial(Color.rgb(15,15,15)));
                cube.getChildren().add(piece[i][j]);
            }
        }

        //todo: Put the pieces in place
        setTopLayer();
        setMiddleLayer();
        setBottomLayer();
    }

    private void setTopLayer() {

        //todo: back three
        piece[0][0].setTranslateX(-pieceGap - pieceSize);
        piece[0][0].setTranslateZ(pieceGap + pieceSize);
        piece[0][0].setTranslateY(-pieceGap - pieceSize);

        piece[0][1].setTranslateZ(pieceGap + pieceSize);
        piece[0][1].setTranslateY(-pieceGap - pieceSize);

        piece[0][2].setTranslateX(pieceGap + pieceSize);
        piece[0][2].setTranslateZ(pieceGap + pieceSize);
        piece[0][2].setTranslateY(-pieceGap - pieceSize);

        //todo: middle three
        piece[0][3].setTranslateX(-pieceGap - pieceSize);
        piece[0][3].setTranslateY(-pieceGap - pieceSize);

        piece[0][4].setTranslateY(-pieceGap - pieceSize);

        piece[0][5].setTranslateX(pieceGap + pieceSize);
        piece[0][5].setTranslateY(-pieceGap - pieceSize);

        //todo: front three
        piece[0][6].setTranslateX(-pieceGap - pieceSize);
        piece[0][6].setTranslateZ(-pieceGap - pieceSize);
        piece[0][6].setTranslateY(-pieceGap - pieceSize);

        piece[0][7].setTranslateZ(-pieceGap - pieceSize);
        piece[0][7].setTranslateY(-pieceGap - pieceSize);

        piece[0][8].setTranslateX(pieceGap + pieceSize);
        piece[0][8].setTranslateZ(-pieceGap - pieceSize);
        piece[0][8].setTranslateY(-pieceGap - pieceSize);
    }

    private void setMiddleLayer() {
        //todo: back three
        piece[1][0].setTranslateX(-pieceGap - pieceSize);
        piece[1][0].setTranslateZ(pieceGap + pieceSize);

        piece[1][1].setTranslateZ(pieceGap + pieceSize);

        piece[1][2].setTranslateX(pieceGap + pieceSize);
        piece[1][2].setTranslateZ(pieceGap + pieceSize);

        //todo: middle three
        piece[1][3].setTranslateX(-pieceGap - pieceSize);

        piece[1][4].setTranslateY(-pieceGap - pieceSize);

        piece[1][5].setTranslateX(pieceGap + pieceSize);

        //todo: front three
        piece[1][6].setTranslateX(-pieceGap - pieceSize);
        piece[1][6].setTranslateZ(-pieceGap - pieceSize);

        piece[1][7].setTranslateZ(-pieceGap - pieceSize);

        piece[1][8].setTranslateX(pieceGap + pieceSize);
        piece[1][8].setTranslateZ(-pieceGap - pieceSize);
    }

    private void setBottomLayer() {
        //todo: back three
        piece[2][0].setTranslateX(-pieceGap - pieceSize);
        piece[2][0].setTranslateZ(pieceGap + pieceSize);
        piece[2][0].setTranslateY(pieceGap + pieceSize);

        piece[2][1].setTranslateZ(pieceGap + pieceSize);
        piece[2][1].setTranslateY(pieceGap + pieceSize);

        piece[2][2].setTranslateX(pieceGap + pieceSize);
        piece[2][2].setTranslateZ(pieceGap + pieceSize);
        piece[2][2].setTranslateY(pieceGap + pieceSize);

        //todo: middle three
        piece[2][3].setTranslateX(-pieceGap - pieceSize);
        piece[2][3].setTranslateY(pieceGap + pieceSize);

        piece[2][4].setTranslateY(pieceGap + pieceSize);

        piece[2][5].setTranslateX(pieceGap + pieceSize);
        piece[2][5].setTranslateY(pieceGap + pieceSize);

        //todo: front three
        piece[2][6].setTranslateX(-pieceGap - pieceSize);
        piece[2][6].setTranslateZ(-pieceGap - pieceSize);
        piece[2][6].setTranslateY(pieceGap + pieceSize);

        piece[2][7].setTranslateZ(-pieceGap - pieceSize);
        piece[2][7].setTranslateY(pieceGap + pieceSize);

        piece[2][8].setTranslateX(pieceGap + pieceSize);
        piece[2][8].setTranslateZ(-pieceGap - pieceSize);
        piece[2][8].setTranslateY(pieceGap + pieceSize);
    }

    public Group getCube() {
        cube.getChildren().clear();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {

                cube.getChildren().add(piece[i][j]);
            }
        }
        return cube;
    }

    public Box[][] getPiece() {
        return piece;
    }
}
