package cube;

import javafx.scene.paint.Color;

import java.util.LinkedList;

public class SolutionSet {
    private LinkedList<SolutionMove> solutionMoves;
    private int iterator;

    public SolutionSet(){
        solutionMoves = new LinkedList<>();
        iterator = 0;
    }

    public void addMove(MoveType moveType, RotationType rotationType, Integer layerNum, LayerColor layerColor){
        solutionMoves.add(new SolutionMove(moveType,rotationType,layerNum,layerColor));
    }

    public void resetSet(){
        solutionMoves.clear();
        iterator = 0;
    }

    public SolutionMove getNextMove() {
        if (hasNext()) {
            iterator++;
            return solutionMoves.get(iterator - 1);
        } else return null;
    }

    public SolutionMove getPreviousMove() {
        if (hasPrev()) {
            iterator--;
            return solutionMoves.get(iterator);
        } else return null;
    }


    public LinkedList<SolutionMove> getSolutionMoves() {
        return solutionMoves;
    }

    public boolean hasNext(){
        return (iterator < solutionMoves.size() && solutionMoves.size() != 0);
    }

    public boolean hasPrev(){
        return iterator > 0;
    }
}
