package gybibite.asteroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class Asteroids extends Game {

	public static final int S_WIDTH = 800;
	public static final int S_HEIGHT = 600;

	static boolean verbose;

	static ScreenAdapter menu, game;

	// This is just here to parse arguments
	public Asteroids(String[] arg) {
		for (int i = 0; i < arg.length; i++) {
			if (arg[i].contentEquals("-v")) { // Handle verbose argument (shows some debug info)
				verbose = true;
			}
		}
	}

	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(800, 600);
		Gdx.graphics.setResizable(false);
		menu = new MenuUI(this);
		game = new GameUI(this);

		setScreen(menu);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0.05f, 0.1f, 1); // Set background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
		super.render(); // Make sure that the active screen renders

		if (verbose) {
			// Write FPS to the window's title bar
			Gdx.graphics.setTitle("Asteroids - FPS: " + Math.floor(1 / Gdx.graphics.getDeltaTime() * 10) / 10);
		}
	}

	public void switchScreen(int s) {
		switch (s) {
		case 0:
			setScreen(menu);
			break;
		case 1:
			setScreen(game);
			break;
		case 2:
			// TODO setScreen(options);
			break;
		default:
			setScreen(menu);
		}
	}

	@Override
	public void dispose() {
		// There's nothing to dispose of in this class.
	}

	public static boolean isVerbose() {
		return verbose;
	}
}