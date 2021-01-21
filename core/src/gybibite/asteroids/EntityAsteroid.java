package gybibite.asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public class EntityAsteroid extends Entity {

	int size;
	Circle hitbox;
	Circle[] cloneHitbox = new Circle[8];
	double rotSpeed;

	public EntityAsteroid(int size, float x, float y) {
		super(setScale(size), 2, setTexture(size));

		this.size = size;
		this.x = x;
		this.y = y;

		double speed = Math.random() * (0.9-0.15) + 0.15;

		rot = (float) ((speed * 720) - 360);
		rotSpeed = speed * 2;

		speed *= 100;

		vy += (float) (Math.cos(Math.toRadians(rot))) * speed;
		vx -= (float) (Math.sin(Math.toRadians(rot))) * speed;

		setHitbox();
	}

	@Override
	void tick(float delta) {
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

	void checkHit() {}

	@Override
	public void delete() {
		if (size > 0) {
			new EntityAsteroid(size - 1, x, y);
		}
		GameUI.destroyEntity(this);
		dispose();
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
			hitbox = new Circle(x, y, sprite.getWidth() * 1.3f);
			for (int i = 0; i < clones.length; i++) {
				cloneHitbox[i] = new Circle(clones[i].getX(), clones[i].getY(), sprite.getWidth() * 1.3f);
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
			return new Texture("asteroid_small.png");
		case 1:
			return new Texture("asteroid.png");
		case 2:
			return new Texture("asteroid_large.png");
		default:
			return new Texture("asteroid.png");
		}
	}

	static float setScale(float size) {
		if (size == 0 || size == 1) {
			return 2.5f;
		} else {
			return 3.75f;
		}
	}

	Circle getHitbox() {
		return hitbox;
	}

	@Override
	void die() {
		// TODO Auto-generated method stub
		
	}
}