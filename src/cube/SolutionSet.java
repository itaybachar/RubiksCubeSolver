package cube;

import javafx.scene.paint.Color;

import java.util.LinkedList;

public class SolutionSet {
    private LinkedList<MoveType> solutionMoves;
    private LinkedList<RotationType> solutionMovesRotation;
    private LinkedList<Integer> solutionLayerNumber;
    private LinkedList<Color> solutionMoveLayerColor;

    public SolutionSet(){
        solutionMoves = new LinkedList<>();
        solutionMovesRotation = new LinkedList<>();
        solutionLayerNumber = new LinkedList<>();
        solutionMoveLayerColor = new LinkedList<>();
    }

    private void addMove(MoveType moveType, RotationType rotationType, Integer layerNum, Color layerColor){
        solutionMoves.add(moveType);
        solutionMovesRotation.add(rotationType);
        solutionLayerNumber.add(layerNum);
        solutionMoveLayerColor.add(layerColor);
    }

    public void resetSet(){
        solutionMoves.clear();
        solutionMovesRotation.clear();
        solutionLayerNumber.clear();
        solutionMoveLayerColor.clear();
    }

    public LinkedList<MoveType> getSolutionMoves() {
        return solutionMoves;
    }

    public LinkedList<RotationType> getSolutionMovesRotation() {
        return solutionMovesRotation;
    }

    public LinkedList<Integer> getSolutionLayerNumber() {
        return solutionLayerNumber;
    }

    public LinkedList<Color> getSolutionMoveLayerColor() {
        return solutionMoveLayerColor;
    }
}