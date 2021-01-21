package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;

public class EntityPlayer extends Entity {

	boolean upPressed, downPressed, leftPressed, rightPressed, firePressed;

	static final float ACCEL = 6f;
	static final float DECEL = 100f; // Lower is quicker decel (divisor of velocity)
	/** How fast does the ship rotate (degrees per tick button held down) */
	static final float RSPEED = 3f;
	static final float MAX_SPEED = 218.75f;

	EntityPlayer(float scale) {
		super(scale, 0, new Texture("ship.png"));

		x = S_WIDTH / 2;
		y = S_HEIGHT / 2;

		setHitbox();
	}

	/** Checks if certain keys are being held down */
	public void checkInput(boolean[] buttons) {
//		rightPressed = (Gdx.input.isKeyPressed(Keys.RIGHT) | Gdx.input.isKeyPressed(Keys.D));
//		leftPressed = (Gdx.input.isKeyPressed(Keys.LEFT) | Gdx.input.isKeyPressed(Keys.A));
//		upPressed = (Gdx.input.isKeyPressed(Keys.UP) | Gdx.input.isKeyPressed(Keys.W));
//		downPressed = (Gdx.input.isKeyPressed(Keys.DOWN) | Gdx.input.isKeyPressed(Keys.S));
//		firePressed = (Gdx.input.isButtonJustPressed(Buttons.LEFT));
		
		upPressed = buttons[0];
		downPressed = buttons[1];
		leftPressed = buttons[2];
		rightPressed = buttons[3];
		firePressed = buttons[4];
	}

	@Override
	public void tick(float delta) {

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
			new EntityBullet(2.5f, this);
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
		for (int i = 0; i < GameUI.getEntities().size; i++) {
			if (GameUI.getEntities().items[i].getId() == 2) {
				if (GameUI.overlaps(hitbox, (Circle) GameUI.getEntities().items[i].getHitbox())) {
					GameUI.getEntities().items[i].delete();
					this.delete();
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

	@Override
	void die() {
		// TODO Auto-generated method stub
		
	}
}