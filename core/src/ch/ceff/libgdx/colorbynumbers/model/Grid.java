package ch.ceff.libgdx.PixelArt.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Grid {
    //coordonées du coin inférieure gauche
    private float x;
    private float y;

    //dimensions du tableau
    private int width; //nombre de colonnes
    private int height; //nombre de lignes

    //dimension (carré) d'une cellule
    private int cellSize;

    //Contenu du tableau de grid
    private int[][] content;


    //Constructeur

    public Grid(float x, float y, int width, int height, int cellSize) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.content = new int[height][width];
    }

    //Initialiser le content à 0
//    private void init() {
//        for (int y = 0; y < content.length; y++) {
//            for (int x = 0; x < content[y].length; x++) {
//                content[y][x] = 0;
//            }
//            content[0][0] = 1;
//        }
//    }

    /**
     * Définir le contenu de Grid
     *
     * @param source ke contenu de la mosaique
     *               <p>
     *               Cette méthode permet de d'inverser verticalement la grénadine
     */
    public void setContent(int[][] source) {
        for(int y = source.length; y > 0; y--) { //y = 15
            for (int x = 0; x < source[y - 1].length; x++) {
                this.content[source.length - y][x] = source[y - 1][x];
            }
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int[][] getContent() {
        return content;
    }


    private void getXY(Vector2 position, int[] coords) {
        coords[0] = MathUtils.floor((position.x - x) / cellSize);
        coords[1] = MathUtils.floor((position.y - y) / cellSize);
    }

    public int getValeur(Vector2 touchPos) {
        int[] coords = new int[2];
        getXY(touchPos, coords);
        return getValeur(coords[0], coords[1]);
    }

    public int getValeur(int x, int y) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            return content[y][x];
        }
        return -1; //hors tableau
    }

    public void setValeur(Vector2 touchPos, int valeur) {
        int[] coords = new int[2];
        getXY(touchPos, coords);
        setValeur(coords[0], coords[1], valeur);
    }

    public void setValeur(int x, int y, int valeur) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            content[y][x] = valeur;
        }
    }
}
