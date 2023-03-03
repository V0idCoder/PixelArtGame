package ch.ceff.libgdx.PixelArt.screen.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import ch.ceff.libgdx.colorbynumbers.ColorByNumbers;
import ch.ceff.libgdx.colorbynumbers.screen.game.GameScreen;

public class LoadingScreen extends ScreenAdapter {
    private AssetManager assetManager;

    public LoadingScreen() {
        this.assetManager = ((PixelArt) Gdx.app.getApplicationListener()).getAssetManager();
        //Le chargement de ressources est un processus asynchrone (+ ou - de temps)
        this.assetManager.load("fonts/roboto_light_12.fnt", BitmapFont.class);
        this.assetManager.load("fonts/roboto_light_32.fnt", BitmapFont.class);
        this.assetManager.load("fonts/wicked_mouse_32.fnt", BitmapFont.class);
    }

    @Override
    public void render(float delta) {
        //Définir la couleur de base [Step 1]
        Gdx.gl.glClearColor(0, 1, 1, 1); // alpha = opacity
        //Effacer l'écran [Step 2]
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (assetManager.update()) { //Tous les fihciers sont-ils chargés en mémoire
            //poser everytime la question si le user touche l'ecran (L'user a-t-il touché (tap) l'écran?)
            if (Gdx.input.isTouched()) { //polling --> Il pose des question en mode repeat
                //((Demo)Gdx.app.getApplicationListener()) --> Game
                ((PixelArt) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        }
    }

    // Clean Memory tools
    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
