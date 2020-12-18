package gybibite.asteroids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Asteroids extends ApplicationAdapter {
	SpriteBatch batch;
	static Array<Entity> entities = new Array<Entity>();

	boolean verbose = false;

	public static final int S_WIDTH = 640, S_HEIGHT = 480;

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
		batch = new SpriteBatch();
		Gdx.graphics.setWindowedMode(800, 600); // Doesn't set window res, just scales :(
		new EntityPlayer(batch, 1.5f); // Creates a new player (ship), while passing the sprite batch so it can render
	}

	@Override
	public void render() {
		Gdx.graphics.setTitle("Asteroids - FPS: " + Math.floor(1 / Gdx.graphics.getDeltaTime() * 10) / 10);
		Gdx.gl.glClearColor(0, 0.05f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		for (Entity e : entities) {
			e.tick();
			e.render();
		}
		batch.end();

		if (verbose) {
			for (Entity e : entities) {
				System.out.println(e);
			}
		}
	}

	@Override
	public void dispose() {
		batch.dispose();

		for (Entity e : entities) {
			e.dispose();
		}
	}

	public static void addEntity(Entity e) {
		entities.add(e);
	}

	public static void destroyEntity(Entity e) {
		if (entities.contains(e, true)) {
			entities.removeValue(e, true);
		} else {
			System.out.println("ERROR: " + e + " not found in entity list! Skipping removal...");
		}

	}
}