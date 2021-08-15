package gybibite.asteroids;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;

public class EntityPlayer extends Entity {

	boolean upPressed, downPressed, leftPressed, rightPressed, firePressed, diePressed;

	static final float ACCEL = 5f; //6
	static final float DECEL = 150f; // Lower is quicker decel (divisor of velocity) //100
	/** How fast does the ship rotate (degrees per tick button held down) */
	static final float RSPEED = 3f;
	static final float MAX_SPEED = 250f; //218.75

	/** Which player the current entity is (1, 2, etc..) */
	final int player;

	final ParticleEmitter thrusterFire;
	final ParticleEmitter deathPieces;

	EntityPlayer(float scale, int player) {
		super(scale, Asteroids.shipTex);

		x = S_WIDTH / 2;
		y = S_HEIGHT / 2;

		this.player = player;

		setHitbox();

//		atlas = new TextureAtlas(Gdx.files.internal("ship_death.pack"));
//		deathAnim = new Animation<TextureRegion>(0.05f, atlas.getRegions(), Animation.PlayMode.NORMAL);

		this.thrusterFire = new ParticleEmitter(2f, new Color(0.98f, 0.686f, 0.05f, 1f), 90f, 3f, 250f, 50f);
		this.deathPieces = new ParticleEmitter(3f, new Color(0.3f, 0.9f, 1f, 1f), 359f, 7f, 1500f, 200f);
		
	}

	@Override
	/** Convert array of pressed buttons into individual flags */
	public void checkInput(boolean[] buttons) {
		if (player == 1) {
			upPressed    = buttons[0];
			downPressed  = buttons[1];
			leftPressed  = buttons[2];
			rightPressed = buttons[3];
			firePressed  = buttons[4];
			diePressed   = buttons[5];
		}
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
				
				double[] thrustPos = getThrustorPos();

				thrusterFire.emit((float)thrustPos[0], (float)thrustPos[1], -rot, 2);

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

			if (Asteroids.isDebug()) {
				if (downPressed) {
					vy = 0;
					vx = 0;
				}
				if (diePressed) {
					die();
				}
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
	
	double[] getThrustorPos() {
		return new double[] { hitbox.getTransformedVertices()[4], hitbox.getTransformedVertices()[5] };
	}

	@Override
	void notifyHit(Entity e) {
		if (e instanceof EntityAsteroid && !dying) {
			die();
		}
	}

	@Override
	void setHitbox() {
		hb = new float[] { 0, sprite.getHeight() / 1.3f,
				sprite.getWidth() / 1.3f, -sprite.getHeight() / 2.6f,
				0, -sprite.getHeight() / 1.3f,
				-sprite.getWidth() / 1.3f, -sprite.getHeight() / 2.6f };

		hitbox = new Polygon(hb);
	}

	@Override
	Polygon getHitbox() {
		return hitbox;
	}

	@Override
	void die() {
		for (int i = 0; i < 16/*amount of death particles to emit*/; i++) {
			deathPieces.emit(x, y, 0, 0.2f);
		}
		delete();
	}
}