package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

public class EntityPlayer extends Entity {

	boolean rightPressed, leftPressed, upPressed, downPressed, firePressed;

	static final float ACCEL = 4.8f;
	static final float DECEL = 100f; // Lower is better (divisor of velocity)
	/** How fast does the ship rotate (degrees per tick button held down) */
	static final float RSPEED = 3f;
	static final float MAX_SPEED = 175f;
	
	static final float[] hb = new float[8];

	EntityPlayer(SpriteBatch batch, float scale) {
		super(batch, scale, new Texture("ship.png"));

		x = S_WIDTH / 2;
		y = S_HEIGHT / 2;
		
		hb[0] = 2; //TODO: I'll do this later.
		hb[0] = 2;
		
		new Polygon(hb);
		
		setHitbox();
	}

	/** Checks if certain keys are being held down */
	public void checkInput() {
		rightPressed = (Gdx.input.isKeyPressed(Keys.RIGHT) | Gdx.input.isKeyPressed(Keys.D));
		leftPressed = (Gdx.input.isKeyPressed(Keys.LEFT) | Gdx.input.isKeyPressed(Keys.A));
		upPressed = (Gdx.input.isKeyPressed(Keys.UP) | Gdx.input.isKeyPressed(Keys.W));
		downPressed = (Gdx.input.isKeyPressed(Keys.DOWN) | Gdx.input.isKeyPressed(Keys.S));
		firePressed = (Gdx.input.isButtonJustPressed(Buttons.LEFT));
	}

	@Override
	public void tick() {
		checkInput();

		// Check movement keys
		if (rightPressed) {
			rot = (rot - RSPEED) % 360;
			sprite.setRotation(rot);
		}
		if (leftPressed) {
			rot = (rot + RSPEED) % 360;
			sprite.setRotation(rot);
		}
		if (upPressed) {
			vy += (float) (Math.cos(Math.toRadians(rot))) * ACCEL;
			vx -= (float) (Math.sin(Math.toRadians(rot))) * ACCEL;

			vy = Math.min(Math.max(-MAX_SPEED, vy), MAX_SPEED);
			vx = Math.min(Math.max(-MAX_SPEED, vx), MAX_SPEED);

		} else if (!upPressed) {
			vy -= vy / DECEL;
			vx -= vx / DECEL;
		}
		
		if (firePressed) {
			new EntityBullet(batch, 2f, this, x, y);
		}

		// Prevent velocity from getting infinitely smaller
		vx = (float) (Math.floor(vx * 10000) / 10000);
		vy = (float) (Math.floor(vy * 10000) / 10000);

		// TODO: REMOVE LATER
		if (downPressed) {
			vy = 0;
			vx = 0;
		}

		// Increment position by velocity multiplied by the time since
		// last frame, for consistent movement
		x += vx * delta;
		y += vy * delta;

		if (x <= 0) {
			x = S_WIDTH;
		} else if (x >= S_WIDTH) {
			x = 0;
		}

		if (y <= 0) {
			y = S_HEIGHT;
		} else if (y >= S_HEIGHT) {
			y = 0;
		}
	}

	@Override
	void setHitbox() {
		hitbox = new Polygon();
	}
}