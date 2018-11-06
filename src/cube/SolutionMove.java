package cube;

public class SolutionMove {
    private MoveType moveType;
    private RotationType rotationType;
    private Integer layerNum;
    private LayerColor layerColor;

    public SolutionMove(MoveType moveType, RotationType rotationType, Integer layerNum, LayerColor layerColor){
        this.moveType = moveType;
        this.rotationType = rotationType;
        this.layerNum = layerNum;
        this.layerColor = layerColor;
    }

    public void flipRotation(){
        if(rotationType == RotationType.ClockWise){
            rotationType = RotationType.CounterClockWise;
        } else rotationType = RotationType.ClockWise;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public RotationType getRotationType() {
        return rotationType;
    }

    public Integer getLayerNum() {
        return layerNum;
    }

    public LayerColor getLayerColor() {
        return layerColor;
    }
}
