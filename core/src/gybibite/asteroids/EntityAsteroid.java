package gybibite.asteroids;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public final class EntityAsteroid extends Entity implements Enemy {

	int size;
	Circle hitbox;
	Circle[] cloneHitbox = new Circle[8];
	double rotSpeed;
	final ParticleEmitter deathPieces;

	public EntityAsteroid(int size, float x, float y) {
		super(setScale(size), setTexture(size));

		this.size = size;
		this.x = x;
		this.y = y;

		double speed = Math.random() * (0.9 - 0.15) + 0.15;

		rot = (float) ((speed * 720) - 360);
		rotSpeed = speed * 2;

		speed *= 200 - ((size - 1) * 20);

		vy += (float) (Math.cos(Math.toRadians(rot))) * speed;
		vx -= (float) (Math.sin(Math.toRadians(rot))) * speed;

		setHitbox();
		
		this.deathPieces = new ParticleEmitter(size + 3, new Color(0.66f, 0.66f, 0.66f, 1f), 359f, 7f, 1000f, 100f);
	}

	@Override
	void tick(final float delta) {
		x += vx * delta;
		y += vy * delta;

		rot += rotSpeed;

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

		hitbox.setPosition(x, y);
		for (int i = 0; i < cloneHitbox.length; i++) {
			cloneHitbox[i].setPosition(clones[i].getX() + sprite.getWidth() / 2,
					clones[i].getY() + sprite.getHeight() / 2);
		}
	}

	@Override
	void notifyHit(Entity e) {
		if (e instanceof EntityBullet || e instanceof EntityPlayer) {
			die();
		}
	}

	@Override
	void setHitbox() {
		posClones();
		if (size == 1 || size == 0) {
			hitbox = new Circle(x, y, sprite.getWidth());
			for (int i = 0; i < clones.length; i++) {
				cloneHitbox[i] = new Circle(clones[i].getX(), clones[i].getY(), sprite.getWidth());
			}
		} else {
			hitbox = new Circle(x, y, sprite.getWidth() * 1.625f);
			for (int i = 0; i < clones.length; i++) {
				cloneHitbox[i] = new Circle(clones[i].getX(), clones[i].getY(), sprite.getWidth() * 1.625f);
			}
		}
	}

	@Override
	void drawHB() {
		GameUI.drawCirc(hitbox);
		for (Circle c : cloneHitbox) {
			GameUI.drawCirc(c);
		}
	}

	static Texture setTexture(int size) {
		switch (size) {
		case 0:
			return Asteroids.astTexSmall;
		case 1:
			return Asteroids.astTexMed;
		case 2:
			return Asteroids.astTexLarge;
		default:
			return Asteroids.astTexMed;
		}
	}

	static float setScale(float size) {
		if (size == 0 || size == 1) {
			return 2.5f;
		} else {
			return 3.75f;
		}
	}

	@Override
	Circle getHitbox() {
		return hitbox;
	}

	@Override
	void die() {
		if (size > 0) {
			new EntityAsteroid(size - 1, x, y);
			new EntityAsteroid(size - 1, x, y);
		}
		for (int i = 0; i < 8/*amount of death particles to emit*/; i++) {
			deathPieces.emit(x, y, 0, 0.2f);
		}
		
		GameUI.score += 25 * (3 - size);
		
		delete();
	}
}
