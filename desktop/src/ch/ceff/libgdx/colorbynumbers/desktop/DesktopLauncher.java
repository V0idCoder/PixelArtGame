package ch.ceff.libgdx.colorbynumbers.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import ch.ceff.libgdx.colorbynumbers.ColorByNumbers;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setWindowedMode(1024, 768);
		config.setTitle("ColorByNumbers");
		config.setResizable(false);

		new Lwjgl3Application(new ColorByNumbers(), config);
	}
}

