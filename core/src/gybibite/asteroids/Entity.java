package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;

public abstract class Entity {
	/** An ID for checking entity types */
	int id;
	Texture tex;
	Sprite sprite;
	/** Clones for screen wrapping */
	Sprite[] clones = new Sprite[8];
	/** Polygon object for controlling the hitbox */
	Polygon hitbox;
	/** Constants for window size */
	protected static final int S_WIDTH = Asteroids.S_WIDTH, S_HEIGHT = Asteroids.S_HEIGHT;
	/** Positional variables */
	float x, y, vx, vy, rot, delta;
	/** Hitbox definition */
	float[] hb;

	/**
	 * The primary constructor for creating a new entity
	 * @param scale How to scale the texture for the entity
	 * @param id What ID to set (0: Player | 1: Bullet | 2: Asteroid | 3: UFO)
	 * @param tex The texture to use (assets directory)
	 */
	Entity(float scale, int id, Texture tex) {
		this.id = id;
		
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

		// Keep track of this entity in the entity list
		Asteroids.addEntity(this);
	}

	/**
	 * Returns the center of the sprite's X position, since libGDX won't draw the
	 * sprite centered on the coordinate
	 * @param x X Position
	 */
	float centerX(float x) {
		return x - sprite.getWidth() / 2;
	}

	/**
	 * Returns the center of the sprite's Y position, since libGDX won't draw the
	 * sprite centered on the coordinate
	 * @param y Y Position
	 */
	float centerY(float y) {
		return y - sprite.getHeight() / 2;
	}

	/** Get the entity's velocity, in array format */
	float[] getV() {
		return new float[] { vx, vy };
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

	/**
	 * Just tells the sprite batch to draw the ship
	 * @param batch The sprite batch to be passed to the draw command
	 */
	public void render(SpriteBatch batch) {
		delta = Gdx.graphics.getDeltaTime();
		// Set sprite location
		sprite.setPosition(centerX(x), centerY(y));
		posClones();
		// Tell the sprite batch to draw
		sprite.draw(batch);
		for (Sprite s : clones) {
			s.setRotation(rot);
			s.draw(batch);
		}
	}

	/**
	 * Disposes of any "Disposable" objects 
	 * @see com.badlogic.gdx.utils#Disposable
	 */
	public void dispose() {
		tex.dispose();
	}

	/** Removes the entity when necessary */
	public void kill() {
		Asteroids.destroyEntity(this);
		dispose();
	}

	/** Defines how the entity interacts with the world every server cycle */
	abstract void tick();
	
	/** Defines the hitbox vertices */
	abstract void setHitbox();
	
	/** Draws the hitbox */
	void drawHB() {
		Asteroids.s.begin(ShapeType.Line);
		Asteroids.s.setColor(1, 1, 0, 1);
		Asteroids.s.polygon(hitbox.getTransformedVertices());
		Asteroids.s.end();
	}

	/** Some verbose stuff */
	public String toString() {
		return "-----------------"
				+ "\n" + "Entity: " + this.getClass().getSimpleName() + " at " + Asteroids.entities.indexOf(this, false)
				+ "\n" + "Velocity: (" + vx + ", " + vy + ")"
				+ "\n" + "Position: (" + x + ", " + y + ")";
	}
	
	public class JavaClass extends Abstract implements Interface, InterfaceTwo {}
	
	public abstract class Abstract {}

	public interface Interface {}
	
	public interface InterfaceTwo {}
}

