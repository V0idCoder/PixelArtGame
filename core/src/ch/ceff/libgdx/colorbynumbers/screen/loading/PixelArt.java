package ch.ceff.libgdx.colorbynumbers.screen.loading;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PixelArt {
    package ch.ceff.libgdx.colorbynumbers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ch.ceff.libgdx.colorbynumbers.screen.loading.LoadingScreen;

    public class ColorByNumbers extends Game {
        //L'objet responsable du dessine des formes géométrique
        private ShapeRenderer renderer; //[Step 1] Only one object for one application
        //L'objet responsable du chargment des ressources
        private AssetManager assetManager;
        //L'objet responsable du dessin des images bitmap
        private SpriteBatch batch;

        //1) Cette méthode est applée au démarrage
        @Override
        public void create() {
            //Construction de gros consommateurs
            renderer = new ShapeRenderer(); // [Step 2]
            assetManager = new AssetManager();
            batch = new SpriteBatch();

            //Change the windows --> Loading Screen
            setScreen(new LoadingScreen());
        }

        //Cette méthode est exécutée 60 x / seconde --> 60 FPS --> GPU (it's working with OpenGL)
        @Override
        public void render() {
            super.render();
        }

        // Cette méthode est applée a la fin du prg
        @Override
        public void dispose() {
            super.dispose();
            //Cette méthode détruit les gros consommateurs
            renderer.dispose(); // [Step 3]
            assetManager.dispose();
            batch.dispose();
        }

        //Méthodes getter pour obtenir les objets
        public ShapeRenderer getRenderer() {
            return renderer;
        }

        public AssetManager getAssetManager() {
            return assetManager;
        }

        public SpriteBatch getBatch() {
            return batch;
        }
    }

}
