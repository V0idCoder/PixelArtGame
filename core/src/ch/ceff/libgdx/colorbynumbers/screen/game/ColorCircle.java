package ch.ceff.libgdx.PixelArt.screen.game;

import com.badlogic.gdx.math.Circle;

import ch.ceff.libgdx.colorbynumbers.config.GameConfig;

public class ColorCircle {
    private int value; // 1, 2, 3, 4, 5, 6,
    private Circle circle;
    private boolean selected;

    public ColorCircle(float x, float y, int value) {
        this.circle = new Circle(x, y, GameConfig.COLOR_CIRCLE_RADIUS);
        this.value = value;
        selected = false;
    }

    public Circle getCircle() {
        return circle;
    }

    public int getValue() {
        return value;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
