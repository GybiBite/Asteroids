package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;

public class EntityPlayer extends Entity {

	boolean rightPressed, leftPressed, upPressed, downPressed, firePressed;

	static final float ACCEL = 4.8f;
	static final float DECEL = 100f; // Lower is better (divisor of velocity)
	/** How fast does the ship rotate (degrees per tick button held down) */
	static final float RSPEED = 3f;
	static final float MAX_SPEED = 175f;

	EntityPlayer(float scale) {
		super(scale, 0, new Texture("ship.png"));

		x = S_WIDTH / 2;
		y = S_HEIGHT / 2;

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
		}
		if (leftPressed) {
			rot = (rot + RSPEED) % 360;
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
			new EntityBullet(2f, this);
		}

		// Prevent velocity from getting infinitely smaller
		vx = (float) (Math.floor(vx * 10000) / 10000);
		vy = (float) (Math.floor(vy * 10000) / 10000);

		if (downPressed && Asteroids.isVerbose()) {
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

		hitbox.setRotation(rot);
		hitbox.setPosition(x, y);
	}

	void checkHit() {
		for (int i = 0; i < Asteroids.getEntities().size; i++) {
			if (Asteroids.getEntities().items[i].getId() == 2) {
				if (Asteroids.overlaps(hitbox, (Circle) Asteroids.getEntities().items[i].getHitbox())) {
					Asteroids.getEntities().items[i].kill();
					break;
				}
			}
		}
	}

	@Override
	void setHitbox() {
		hb = new float[] { 0, sprite.getHeight() / 1.3f, sprite.getWidth() / 1.3f, -sprite.getHeight() / 2.6f, 0,
				-sprite.getHeight() / 1.3f, -sprite.getWidth() / 1.3f, -sprite.getHeight() / 2.6f };

		hitbox = new Polygon(hb);
	}

	Polygon getHitbox() {
		return hitbox;
	}
}