package gybibite.asteroids.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gybibite.asteroids.Asteroids;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
//		config.vSyncEnabled = false;
//		config.foregroundFPS = 0;

		// TODO: fix fps cap
		
		new LwjglApplication(new Asteroids(arg), config);
	}
}
