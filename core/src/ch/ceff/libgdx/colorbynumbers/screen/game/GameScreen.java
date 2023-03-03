package ch.ceff.libgdx.colorbynumbers.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ch.ceff.libgdx.colorbynumbers.ColorByNumbers;
import ch.ceff.libgdx.colorbynumbers.config.GameConfig;

public class GameScreen extends ScreenAdapter {

    private GameController controller;

    //Objet d'affichage (px --> m)
    private Viewport gameViewport;

    //Objet de dessin des formes géométrique
    private ShapeRenderer renderer;

    //Objet de dessin de bitmap
    private SpriteBatch batch;

    //Fichiers fonts
    private BitmapFont roboto_light_16;
    private BitmapFont roboto_light_32;
    private BitmapFont wicked_mouse_32;

    //Viewport de texte --> HUD
    private Viewport hudViewport;

    //Cet objet vous donne les dimensions (px) d'un texte
    private GlyphLayout glyphLayout = new GlyphLayout();

    //Vecteur --> regrouper les coordonnées du clic pour les transformer (px -> m)
    private Vector2 touchPos;

    private Mosaic mosaic;


    //Nouveautés

    public GameScreen() {
        controller = new GameController();

        // L'écran principal 32m x 24m --> avec une caméra 2D //"FillViewport" il gere le ratio
        gameViewport = new FillViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, new OrthographicCamera());

        //On appele "ShapeRenderer" en utilisant "((Demo)Gdx.app.getApplicationListener())"
        renderer = ((ColorByNumbers) Gdx.app.getApplicationListener()).getRenderer();

        batch = ((ColorByNumbers) Gdx.app.getApplicationListener()).getBatch();
        //Création de l'objet BitmapFont depuis le fihcier ressource chargé (LoadingScreen)
        roboto_light_16 = ((ColorByNumbers) Gdx.app.getApplicationListener()).getAssetManager().get("fonts/roboto_light_12.fnt");
        roboto_light_32 = ((ColorByNumbers) Gdx.app.getApplicationListener()).getAssetManager().get("fonts/roboto_light_32.fnt");
        wicked_mouse_32 = ((ColorByNumbers) Gdx.app.getApplicationListener()).getAssetManager().get("fonts/wicked_mouse_32.fnt");
        //Activer l'option pour colorer la police
        roboto_light_16.getData().markupEnabled = true; // Color --> "[#RRVVBB]...."
        roboto_light_32.getData().markupEnabled = true;
        wicked_mouse_32.getData().markupEnabled = true;

        hudViewport = new ScreenViewport(new OrthographicCamera());

        mosaic = new Mosaic();

        touchPos = new Vector2();

        //Listener pour un événement
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touchPos.set(screenX, screenY);
                gameViewport.unproject(touchPos);
                if (controller.touchDown(touchPos, pointer, button)) {
                    return true; //Evénement traité : OK
                }
                return super.touchDown(screenX, screenY, pointer, button); //Evénement non traité --> passe plus loin
            }
        });
    }

    //Dès le moment ou il y a un Viewport --> il faut cette méthode
    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
        hudViewport.update(width, height);
    }


    @Override
    public void render(float delta) {
        //Définir la couleur de base [Step 1]
        Gdx.gl.glClearColor(1, 1, 1, 1); // alpha = opacity
        //Effacer l'écran [Step 2]
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Déclarer le viewport (Tout ce qui suit ça te concerne (gameViewport))
        gameViewport.apply(true); //ça va nous centrer la camera
        //Syncroniser le renderer par rapport aux réglages de la caméra
        renderer.setProjectionMatrix(gameViewport.getCamera().combined);


        //Filled
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        drawGrenadines();
        circles();

        renderer.end();

        // Line
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawGrids();

        renderer.end();

        //Ecrire du texte --> HUD
        //1) Activer le viewport de texte
        hudViewport.apply(true);
        //2) Adapter le batch à la caméra
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        //3) Dessiner le texte
        batch.begin();

        writeText_Numbers();

       numbers();
        batch.end();
    }


    private void drawGrenadines() {
        for (int y = 0; y < controller.getMosaic().getContent().length; y++) {
            for (int x = 0; x < controller.getMosaic().getContent()[y].length; x++) {
                //Little Mosaic----------------------------------------------------------------------------------------------------------------------
                renderer.setColor(controller.getPalette()[controller.getMosaic().getContent()[y][x] - 1]);
                renderer.rect(8 + x, 33 + y, 1, 1);

                //Big Mosaic ----------------------------------------------------------------------------------------------------------------------
                renderer.setColor(controller.getPalette()[controller.getMosaic().getContent()[y][x] - 1]);
                renderer.rect(controller.getMosaic().getX() + x * controller.getMosaic().getCellSize(),
                        controller.getMosaic().getY() + y * controller.getMosaic().getCellSize(),
                        controller.getMosaic().getCellSize(),
                        controller.getMosaic().getCellSize()
                );

                //White Square ----------------------------------------------------------------------------------------------------------------------
                if (controller.getWhite().getContent()[y][x] < 10) {
                    renderer.setColor(Color.WHITE);
                    renderer.rect(controller.getWhite().getX() + x * controller.getWhite().getCellSize(),
                            controller.getWhite().getY() + y * controller.getWhite().getCellSize(),
                            controller.getWhite().getCellSize(),
                            controller.getWhite().getCellSize()
                    );
                }
            }
        }
    }

    private void drawGrids() {
        renderer.setColor(Color.LIGHT_GRAY);
        // Little Mosaic Grid-------------------------------------------------------------------------------------------------------
        for (int x = 8; x <= 28; x++) {
            renderer.line(x, 33, x, 48);
        }
        for (int y = 33; y <= 48; y++) {
            renderer.line(8, y, 28, y);
        }

        // ColorGrid -----------------------------------------------------------------------------------------------------------
        renderer.setColor(Color.BLACK);
        for (int y = 0; y < controller.getMosaic().getHeight(); y++) {
            for (int x = 0; x < controller.getMosaic().getWidth(); x++) {
                renderer.rect(
                        controller.getMosaic().getX() + x * controller.getMosaic().getCellSize(),
                        controller.getMosaic().getY() + y * controller.getMosaic().getCellSize(),
                        controller.getMosaic().getCellSize(),
                        controller.getMosaic().getCellSize()
                );
            }
        }

    }

    private void writeText_Numbers() {
        //Affihchage du texte à 16m (x) et 10m (y) --> ATTENTION les coordonées doivent être en px
        //Il faut utiliser PPM !!
        //Les dimensions du texte :
        glyphLayout.setText(wicked_mouse_32, "Color By Numbers");
        wicked_mouse_32.draw(
                batch,
                "[#F6B4A8FF]Color By Numbers",
                42 * GameConfig.PPM - glyphLayout.width / 2, //px
                45 * GameConfig.PPM + glyphLayout.height / 2 //px
        );

        //WriteNumbers ------------------------------------------------------------------------------------------------------------------------------
        for (int y = 0; y < controller.getWhite().getContent().length; y++) {
            for (int x = 0; x < controller.getWhite().getContent()[y].length; x++) {
                if (controller.getWhite().getContent()[y][x] < 10) {

                    glyphLayout.setText(roboto_light_16, controller.getWhite().getContent()[y][x] + "");
                    roboto_light_16.draw(
                            batch,
                            "[#000000]" + controller.getWhite().getContent()[y][x],
                            (controller.getWhite().getX() + x * controller.getWhite().getCellSize() + controller.getWhite().getCellSize() / 2f) * GameConfig.PPM - glyphLayout.width / 2f,
                            (controller.getWhite().getY() + y * controller.getWhite().getCellSize() + controller.getWhite().getCellSize() / 2f) * GameConfig.PPM + glyphLayout.width / 2f
                    );
                }
            }
        }
    }

    private void drawLemon() {
        for (int y = 0; y < mosaic.getLemon().length; y++) {
            //pour une ligne --> y
            for (int x = 0; x < mosaic.getLemon()[y].length; x++) {
                renderer.setColor(mosaic.getPalette2()[mosaic.getLemon()[y][x] - 1]);
                renderer.rect(mosaic.getxx() + x, mosaic.getyy() - y, 1, 1);
            }
        }
    }

    private void numbers() {
        for (int i = 1; i <= controller.getPalette().length; i++) {
            glyphLayout.setText(roboto_light_32, ""+i);
            if (controller.getColorPalette()[i -1].isSelected()) {
                roboto_light_32.draw(
                        batch,
                        "[#FF0000]" + i,
                        (36 + (((i - 1) % 3) * 6)) * GameConfig.PPM - glyphLayout.width / 2f,
                        (41 - (((i - 1) / 3) * 6)) * GameConfig.PPM + glyphLayout.height / 2f
                );
            }else {
                roboto_light_32.draw(
                        batch,
                        "[#FFFFFF]" + i,
                        (36 + (((i - 1) % 3) * 6)) * GameConfig.PPM - glyphLayout.width / 2f,
                        (41 - (((i - 1) / 3) * 6)) * GameConfig.PPM + glyphLayout.height / 2f
                );
            }
        }
    }

    private void circles() {
        for (int i = 0; i < controller.getColorPalette().length; i++) {
            if (controller.getColorPalette()[i].isSelected()) {
                renderer.setColor(Color.RED);
                renderer.circle(
                        controller.getColorPalette()[i].getCircle().x,
                        controller.getColorPalette()[i].getCircle().y,
                        controller.getColorPalette()[i].getCircle().radius + 0.2f,
                        60
                );
            }
            renderer.setColor(controller.getPalette()[controller.getColorPalette()[i].getValue() - 1]);
            renderer.circle(
                    controller.getColorPalette()[i].getCircle().x,
                    controller.getColorPalette()[i].getCircle().y,
                    controller.getColorPalette()[i].getCircle().radius,
                    60
            );
        }
    }
//    private void drawCircle() {
//        renderer.setColor(controller.getPalette()[0]);
//        renderer.circle(35, 40, 2, 100);
//
//        renderer.setColor(controller.getPalette()[1]);
//        renderer.circle(41, 40, 2, 100);
//
//        renderer.setColor(controller.getPalette()[2]);
//        renderer.circle(47, 40, 2, 100);
//
//        renderer.setColor(controller.getPalette()[3]);
//        renderer.circle(35, 35, 2, 100);
//
//        renderer.setColor(controller.getPalette()[4]);
//        renderer.circle(41, 35, 2, 100);
//
//        renderer.setColor(controller.getPalette()[5]);
//        renderer.circle(47, 35, 2, 100);
//    }

//    private void drawCirleEdge() {
//        renderer.setColor(Color.RED);
//        renderer.circle(35, 40, 2, 100);
//
//        renderer.setColor(Color.RED);
//        renderer.circle(41, 40, 2, 100);
//
//        renderer.setColor(Color.RED);
//        renderer.circle(47, 40, 2, 100);
//
//        renderer.setColor(Color.RED);
//        renderer.circle(35, 35, 2, 100);
//
//        renderer.setColor(Color.RED);
//        renderer.circle(41, 35, 2, 100);
//
//        renderer.setColor(Color.RED);
//        renderer.circle(47, 35, 2, 100);
//    }

//    private void writeCircleNbrs() {
//        glyphLayout.setText(roboto_light_32, "1");
//        roboto_light_32.draw(
//                batch,
//                "[#FFFFFF]1",
//                35 * GameConfig.PPM - glyphLayout.width / 2, //px
//                40 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//        glyphLayout.setText(roboto_light_32, "2");
//        roboto_light_32.draw(
//                batch,
//                "[#FFFFFF]2",
//                41 * GameConfig.PPM - glyphLayout.width / 2, //px
//                40 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//        glyphLayout.setText(roboto_light_32, "3");
//        roboto_light_32.draw(
//                batch,
//                "[#FFFFFF]3",
//                47 * GameConfig.PPM - glyphLayout.width / 2, //px
//                40 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//        glyphLayout.setText(roboto_light_32, "4");
//        roboto_light_32.draw(
//                batch,
//                "[#FFFFFF]4",
//                35 * GameConfig.PPM - glyphLayout.width / 2, //px
//                35 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//        glyphLayout.setText(roboto_light_32, "5");
//        roboto_light_32.draw(
//                batch,
//                "[#FFFFFF]5",
//                41 * GameConfig.PPM - glyphLayout.width / 2, //px
//                35 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//        glyphLayout.setText(roboto_light_32, "6");
//        roboto_light_32.draw(
//                batch,
//                "[#FFFFFF]6",
//                47 * GameConfig.PPM - glyphLayout.width / 2, //px
//                35 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//    }

//    private void writeCircleNbrsRed() {
//
//        glyphLayout.setText(roboto_light_32, "1");
//        roboto_light_32.draw(
//                batch,
//                "[#FF0000]1",
//                35 * GameConfig.PPM - glyphLayout.width / 2, //px
//                40 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//        glyphLayout.setText(roboto_light_32, "2");
//        roboto_light_32.draw(
//                batch,
//                "[#FF0000]2",
//                41 * GameConfig.PPM - glyphLayout.width / 2, //px
//                40 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//        glyphLayout.setText(roboto_light_32, "3");
//        roboto_light_32.draw(
//                batch,
//                "[#FF0000]3",
//                47 * GameConfig.PPM - glyphLayout.width / 2, //px
//                40 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//        glyphLayout.setText(roboto_light_32, "4");
//        roboto_light_32.draw(
//                batch,
//                "[#FF0000]4",
//                35 * GameConfig.PPM - glyphLayout.width / 2, //px
//                35 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//        glyphLayout.setText(roboto_light_32, "5");
//        roboto_light_32.draw(
//                batch,
//                "[#FF0000]5",
//                41 * GameConfig.PPM - glyphLayout.width / 2, //px
//                35 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//
//        glyphLayout.setText(roboto_light_32, "6");
//        roboto_light_32.draw(
//                batch,
//                "[#FF0000]6",
//                47 * GameConfig.PPM - glyphLayout.width / 2, //px
//                35 * GameConfig.PPM + glyphLayout.height / 2 //px
//        );
//





    // Clean Memory tools
    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        roboto_light_16.dispose();
        roboto_light_32.dispose();
        wicked_mouse_32.dispose();
    }
}
