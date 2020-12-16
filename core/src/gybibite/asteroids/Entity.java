package gybibite.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {
	SpriteBatch batch;
	Texture tex;
	Sprite sprite;
	Sprite[] clones = new Sprite[8];
	protected static final int S_WIDTH = Asteroids.S_WIDTH;
	protected static final int S_HEIGHT = Asteroids.S_HEIGHT;
	static float x, y, vx, vy, rot, delta;

	Entity(SpriteBatch batch, float scale, Texture tex) {
		this.batch = batch;
		this.tex = tex;
		sprite = new Sprite(tex);
		
		// Set all "clones" to the same texture
		for (int i = 0; i < 8; i++) {
			clones[i] = new Sprite(tex);
		}

		// Apply scaling
		sprite.setScale(scale);
		for (Sprite s : clones) {
			s.setScale(scale);
		}
	}

	/**
	 * Returns the center of the sprite's X position, since libGDX won't draw the
	 * sprite centered on the coordinate
	 */
	float centerX(float x) {
		return x - sprite.getWidth() / 2;
	}

	/**
	 * Returns the center of the sprite's Y position, since libGDX won't draw the
	 * sprite centered on the coordinate
	 */
	float centerY(float y) {
		return y - sprite.getHeight() / 2;
	}

	/** Positions "clones" to emulate screen texture wrapping */
	void posClones() {
		clones[0].setPosition(centerX(x) - S_WIDTH, centerY(y) - S_HEIGHT); // Top right
		clones[1].setPosition(centerX(x), centerY(y) - S_HEIGHT); // Top middle
		clones[2].setPosition(centerX(x) + S_WIDTH, centerY(y) - S_HEIGHT); // Top left
		clones[3].setPosition(centerX(x) - S_WIDTH, centerY(y)); // Middle right
		clones[4].setPosition(centerX(x) + S_WIDTH, centerY(y)); // Middle left
		clones[5].setPosition(centerX(x) - S_WIDTH, centerY(y) + S_HEIGHT); // Bottom right
		clones[6].setPosition(centerX(x), centerY(y) + S_HEIGHT); // Bottom middle
		clones[7].setPosition(centerX(x) + S_WIDTH, centerY(y) + S_HEIGHT); // Bottom left
	}

	/** Just tells the sprite batch to draw the ship */
	public void render() {
		posClones();
		// Set sprite location
		sprite.setPosition(centerX(x), centerY(y));
		sprite.draw(batch);
		for (Sprite s : clones) {
			s.setRotation(rot);
			s.draw(batch);
		}
	}

	/** Necessary for libGDX memory management */
	public void dispose() {
		tex.dispose();
	}

	/** In each subclass, will be used to determine how the entity moves */
	abstract void moveEntity();
}
