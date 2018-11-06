package cube;

public enum MoveType {
    F(), //Front
    R(), //Right
    U(), //Up
    L(), //Left
    B(), //Back
    D(), //Down

    M(), //Middle (Same dir as L)
    E(), //Equatorial (Same dir as D)
    S(); //Standing (Same dir as F)

    public String toString() {
        switch (this) {
            case F:
                return "F";
            case R:
                return "R";
            case U:
                return "U";
            case L:
                return "L";
            case B:
                return "B";
            case D:
                return "D";
            case M:
                return "M";
            case E:
                return "E";
            case S:
                return "S";
            default:
                return "No Move";
        }
    }
}
