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
    S(), //Standing (Same dir as F)
}
