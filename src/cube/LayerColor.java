package cube;

public enum LayerColor {
    Red(),
    Green(),
    Orange(),
    Blue(),
    Yellow(),
    White();


    public String toString() {
        switch (this) {
            case Red:
                return "Red";
            case Green:
                return "Green";
            case Orange:
                return "Orange";
            case Blue:
                return "Blue";
            case Yellow:
                return "Yellow";
            case White:
                return "White";
            default:
                return "No Color";
        }
    }
}
