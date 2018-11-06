package cube;

public enum RotationType {
    ClockWise(),
    CounterClockWise();

    public String toString(){
        switch (this){
            case ClockWise:
                return "Clockwise";
            case CounterClockWise:
                return "Counter-Clockwise";
                default:
                    return "No Rotation";
        }
    }
}
