package ch.ceff.libgdx.PixelArt.screen.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ch.ceff.libgdx.colorbynumbers.config.GameConfig;
import ch.ceff.libgdx.colorbynumbers.model.Grid;

public class GameController {



    private Color[] palette = {
            new Color(0xBDCAD2FF),
            new Color(0x92251EFF),
            new Color(0xE68127FF),
            new Color(0xF7D594FF),
            new Color(0xF6B4A8FF),
            new Color(0xE65662FF)
    };

    //Pallette des 6 cercles de couleurs
    private ColorCircle[] colorPalette;

    private int selectedColor;

    private Grid mosaic;

    private Grid white;

    private Rectangle zoneOutils;

    public GameController() {
        white = new Grid(12, 2, 20, 15, 2);
        white.setContent(grenadine);


        mosaic = new Grid(12, 2, 20, 15, 2);
        mosaic.setContent(grenadine);

        zoneOutils = new Rectangle(
                mosaic.getX(),
                mosaic.getY(),
                mosaic.getWidth() * mosaic.getCellSize(),
                mosaic.getHeight() * mosaic.getCellSize()
        );

        colorPalette = new ColorCircle[palette.length];
        initColorPallette();
    }

    private void initColorPallette() {
        //Automatisation des cercles
        for (int i = 0; i < colorPalette.length; i++) {
            colorPalette[i] = new ColorCircle(
                    GameConfig.COLOR_CIRCLE_1_X_POS + (i % 3) * GameConfig.COLOR_CIRCLE_SPACE_BETWEEN, //( i & 3) le modulo il fait une remise à l'origine de la valeur, là on veut trois par ligne
                    GameConfig.COLOR_CIRCLE_1_Y_POS - (i / 3) * GameConfig.COLOR_CIRCLE_SPACE_BETWEEN,
                    i + 1 // Valeur de la couleur, +1 psk on commence de à compter de 0
            );
        }
    }


    /* Traiter un événemnt clic...*/
    public boolean touchDown(Vector2 touchPos, int pointer, int button) {
        if (zoneOutils.contains(touchPos)) {
            //demander à outils de nous retourner la valeur contenue dans la case
            //Identifier une valeur d'erreur --> Sentinelle (-1)
            int valeur = white.getValeur(touchPos);
            if (valeur != -1) {
                if (valeur < 10) {
                    if (valeur == selectedColor) {
                        white.setValeur(touchPos, valeur + 10);
//                    System.out.println(valeur);
                    }
                }
                return true;
            }
        }

        for (int i = 0; i < colorPalette.length; i++) {
            if (colorPalette[i].getCircle().contains(touchPos)) {
                selectedColor = colorPalette[i].getValue();
                //On efface toutes les précédentes valeur true pour selected
                for (int j = 0; j < colorPalette.length; j++) {
                    colorPalette[j].setSelected(false);
                }
                colorPalette[i].setSelected(true);
                return true;
            }
        }
        return false;
    }

    public Color[] getPalette() {
        return palette;
    }

    public Grid getMosaic() {
        return mosaic;
    }

    public Grid getWhite() {
        return white;
    }

    public ColorCircle[] getColorPalette() {
        return colorPalette;
    }

    public int getSelectedColor() {
        return selectedColor;
    }
}