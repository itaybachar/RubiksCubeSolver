package cube;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Sticker {

    private Color color;
    private Box sticker;

    public Sticker(Color color, double stickerSize) {
        this.color = color;
        sticker = new Box(stickerSize, stickerSize, 0.1);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Box getSticker() {
        sticker.setMaterial(new PhongMaterial(color));
        return sticker;
    }
}
