package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

	SpriteBatch batch;
	static Texture tShip;
	static Sprite ship;
	static Sprite[] clones = new Sprite[8];
	static boolean rightPressed, leftPressed, upPressed, downPressed;
	static float x, y, vx, vy, rot, delta;

	static final float ACCEL = 2.4f;
	static final float RSPEED = 3;
	static final float S_WIDTH = Const.S_WIDTH.get();
	static final float S_HEIGHT = Const.S_HEIGHT.get();

	Player(SpriteBatch batch) {
		tShip = new Texture("ship.png");
		ship = new Sprite(tShip);

		for (int i = 0; i < 8; i++) {
			clones[i] = new Sprite(tShip);
		}
		
		for(Sprite s : clones) {
			s.setScale(1.5f);
		}

		this.batch = batch;
		ship.setScale(1.5f);
		x = S_WIDTH / 2;
		y = S_HEIGHT / 2;
	}

	/** Just tells the sprite batch to draw the ship */
	public void render() {
		ship.draw(batch);
		for(Sprite s : clones) {
			s.draw(batch);
		}
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

		// Check movement keys
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

		for (Sprite s : clones) {
			s.setRotation(rot);
		}

		clones[0].setPosition(centerX(x) - S_WIDTH, centerY(y) - S_HEIGHT); // Top right
		clones[1].setPosition(centerX(x), centerY(y) - S_HEIGHT);           // Top middle
		clones[2].setPosition(centerX(x) + S_WIDTH, centerY(y) - S_HEIGHT); // Top left
		clones[3].setPosition(centerX(x) - S_WIDTH, centerY(y));            // Middle right
		clones[4].setPosition(centerX(x) + S_WIDTH, centerY(y));            // Middle left
		clones[5].setPosition(centerX(x) - S_WIDTH, centerY(y) + S_HEIGHT); // Bottom right
		clones[6].setPosition(centerX(x), centerY(y) + S_HEIGHT);           // Bottom middle
		clones[7].setPosition(centerX(x) + S_WIDTH, centerY(y) + S_HEIGHT); // Bottom left

		// Set sprite location
		ship.setPosition(centerX(x), centerY(y));
	}

	float centerX(float x) {
		return x - ship.getWidth() / 2;
	}
	
	float centerY(float y) {
		return y - ship.getHeight() / 2;
	}

	/** Verbose info */
	public String toString() {
		return "Velocity: (" + vx + ", " + vy + ")" + "\n" + "Position: (" + x + ", " + y + ")" + "\n"
				+ Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight();
	}

	public void dispose() {
		tShip.dispose();
	}
}