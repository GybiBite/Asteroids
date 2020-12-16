package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityPlayer extends Entity {

	static boolean rightPressed, leftPressed, upPressed, downPressed;

	static final float ACCEL = 2.4f;
	static final float RSPEED = 3;

	EntityPlayer(SpriteBatch batch, float scale) {
		super(batch, scale, new Texture("ship.png"));

		x = S_WIDTH / 2;
		y = S_HEIGHT / 2;
	}

	/** Checks if certain keys are being held down */
	public void checkInput() {
		rightPressed = (Gdx.input.isKeyPressed(Keys.RIGHT) | Gdx.input.isKeyPressed(Keys.D));
		leftPressed = (Gdx.input.isKeyPressed(Keys.LEFT) | Gdx.input.isKeyPressed(Keys.A));
		upPressed = (Gdx.input.isKeyPressed(Keys.UP) | Gdx.input.isKeyPressed(Keys.W));
		downPressed = (Gdx.input.isKeyPressed(Keys.DOWN) | Gdx.input.isKeyPressed(Keys.S));
	}

	public void moveEntity() {
		delta = Gdx.graphics.getDeltaTime();

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
			vx += -(float) (Math.sin(Math.toRadians(rot))) * ACCEL;
		} else if (!upPressed) {
			vy -= vy / 100;
			vx -= vx / 100;
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

	/** Verbose info */
	public String toString() {
		return "Velocity: (" + vx + ", " + vy + ")" + "\n" + "Position: (" + x + ", " + y + ")";
	}
}