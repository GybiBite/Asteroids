package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

	SpriteBatch batch;
	static Sprite ship = new Sprite(new Texture("ship.png"));
	static boolean rightPressed, leftPressed, upPressed, downPressed;
	static float x, y, vx, vy, rot, delta;

	static final float ACCEL = 2.4f;
	static final float RSPEED = 3;

	Player(SpriteBatch batch) {
		this.batch = batch;
		ship.setScale(1.5f);
		x = Const.S_WIDTH.get() / 2;
		y = Const.S_HEIGHT.get() / 2;
	}

	/** Just tells the sprite batch to draw the ship */
	public void render() {
		ship.draw(batch);
	}

	/** Checks if certain keys are being held down */
	public void checkInput() {
		rightPressed = (Gdx.input.isKeyPressed(Keys.RIGHT) | Gdx.input.isKeyPressed(Keys.D));
		leftPressed = (Gdx.input.isKeyPressed(Keys.LEFT) | Gdx.input.isKeyPressed(Keys.A));
		upPressed = (Gdx.input.isKeyPressed(Keys.UP) | Gdx.input.isKeyPressed(Keys.W));
		downPressed = (Gdx.input.isKeyPressed(Keys.DOWN) | Gdx.input.isKeyPressed(Keys.S));
	}

	/** Handles changing the ship coords, in the event that keys are being held. */
	public void moveShip() {
		delta = Gdx.graphics.getDeltaTime();

		if (rightPressed) {
			rot = (rot - RSPEED) % 360;
			ship.setRotation(rot);
		}
		if (leftPressed) {
			rot = (rot + RSPEED) % 360;
			ship.setRotation(rot);
		}
		if (upPressed) {
			vy += (float) (Math.cos(Math.toRadians(rot))) * ACCEL;
			vx += -(float) (Math.sin(Math.toRadians(rot))) * ACCEL;
		} else if (!upPressed) {
			vy -= vy / 100;
			vx -= vx / 100;
		}

		vx = (float) (Math.floor(vx * 10000) / 10000);
		vy = (float) (Math.floor(vy * 10000) / 10000);

		// TODO: REMOVE LATER
		if (downPressed) {
			vy = 0;
			vx = 0;
		}

		x += vx * delta;
		y += vy * delta;

		if (x <= -ship.getWidth()) {
			x = Const.S_WIDTH.get() + ship.getWidth();
		} else if (x >= Const.S_WIDTH.get() + ship.getWidth()) {
			x = -ship.getWidth();
		}

		if (y <= -ship.getWidth()) {
			y = Const.S_HEIGHT.get() + ship.getHeight();
		} else if (y >= Const.S_HEIGHT.get() + ship.getHeight()) {
			y = -ship.getHeight();
		}

		ship.setX(x - ship.getWidth());
		ship.setY(y - ship.getHeight());
	}

	/** Verbose info */
	public String toString() {
		return "Velocity: (" + vx + ", " + vy + ")" + "\n" +
				"Position: (" + x + ", " + y + ")" + "\n" +
				Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight();
	}
}