package gybibite.asteroids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Asteroids extends ApplicationAdapter {
	SpriteBatch batch;
	Player pl;
	boolean verbose = false;

	// This is just here to parse arguments
	public Asteroids(String[] arg) {
		for(int i = 0; i < arg.length; i++) {
			if(arg[i].contentEquals("-v")) {
				verbose = true;
			}
		}
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		Gdx.graphics.setWindowedMode(800, 600); // Doesn't set window size, just scales :(
		pl = new Player(batch); // Creates a new player (ship), while passing the sprite batch so it can render
	}

	@Override
	public void render() {
		System.out.println("FPS: " + Math.floor(1 / Gdx.graphics.getDeltaTime() * 10) / 10);
		pl.checkInput();
		pl.moveShip();
		Gdx.gl.glClearColor(0, 0.05f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		pl.render();
		batch.end();
		
		if(verbose) {
			System.out.println(pl);
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
