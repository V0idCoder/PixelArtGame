package ch.ceff.libgdx.PixelArt.config;

public class GameConfig {
    public static final int WIDTH = 1024; //px
    public static final int HEIGHT = 768; //px

    public static final int WORLD_WIDTH = 64; //m
    public static final int WORLD_HEIGHT = 48; //m

    //PPM = Pixels Per Meter --> nombre de pixels utilisés pour répresenter 1m.
    public static final float PPM = (float) WIDTH / WORLD_WIDTH; //PPM = 32

    public static final float COLOR_CIRCLE_RADIUS = 2;

    public static final float COLOR_CIRCLE_1_X_POS = 36;

    public static final float COLOR_CIRCLE_1_Y_POS = 41;

    public static final float COLOR_CIRCLE_SPACE_BETWEEN = 6;

    private GameConfig() {
    }
}
