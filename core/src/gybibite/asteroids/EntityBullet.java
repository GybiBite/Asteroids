package gybibite.asteroids;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.TimeUtils;

public class EntityBullet extends Entity  {

	Entity pl;
	float originX;
	float originY;
	/** Max bullet speed */
	static final float BULLET_SPEED = 500;
	/** How long does the bullet live before dying (ms) */
	static final float BULLET_TIME = 800;

	long age;

	public EntityBullet(float scale, Entity pl) {
		super(scale, Asteroids.bulletTex);
		this.pl = pl;
		this.originX = pl.getX();
		this.originY = pl.getY();
		this.rot = pl.getRot();

		x = originX;
		y = originY;

		vy = (float) (Math.cos(Math.toRadians(rot))) * BULLET_SPEED;
		vx = -(float) (Math.sin(Math.toRadians(rot))) * BULLET_SPEED;

		age = TimeUtils.millis();

		setHitbox();
	}

	@Override
	void tick(float delta) {
		// If the bullet has been alive as long as BULLET_TIME without hitting anything,
		// kill the bullet
		if (TimeUtils.timeSinceMillis(age) >= BULLET_TIME) {
			delete();
		}

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

	@Override
	void notifyHit(Entity e) {
		if (e instanceof EntityAsteroid) {
			die();
		}
	}

	@Override
	void setHitbox() {
		hb = new float[] { 0 - sprite.getWidth(), sprite.getHeight(), sprite.getWidth(), sprite.getHeight(),
				sprite.getWidth(), 0 - sprite.getHeight(), 0 - sprite.getWidth(), 0 - sprite.getHeight() };

		hitbox = new Polygon(hb);
	}

	@Override
	Polygon getHitbox() {
		return hitbox;
	}

	@Override
	void die() {
		delete();
	}
}