package gybibite.asteroids;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.TimeUtils;

public final class EntityBullet extends Entity {

  /** X spawn point for the bullet. */
  private final float originX;
  /** Y spawn point for the bullet. */
  private final float originY;
  /** Max bullet speed. */
  static final float BULLET_SPEED = 500;
  /** How long does the bullet live before dying (ms). */
  static final float BULLET_TIME = 800;
  /** How long the bullet has been alive for. */
  private long age;

  /**
   * Create a new EntityBullet object<br>
   * To be used when a play or UFO shoots.
   *
   * @param scale The scale of the entity (size)
   * @param pl The entity that shot the bullet
   */
  public EntityBullet(final float scale, final Entity pl) {
    super(scale, Asteroids.bulletTex);
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
  void tick(final float delta) {
    // Kill bullet if alive for longer than allowed
    if (TimeUtils.timeSinceMillis(age) >= BULLET_TIME) {
      delete();
    }

    x += vx * delta;
    y += vy * delta;

    if (x <= 0) {
      x = Asteroids.S_WIDTH;
    } else if (x >= Asteroids.S_WIDTH) {
      x = 0;
    }

    if (y <= 0) {
      y = Asteroids.S_HEIGHT;
    } else if (y >= Asteroids.S_HEIGHT) {
      y = 0;
    }

    hitbox.setRotation(rot);
    hitbox.setPosition(x, y);
  }

  @Override
  void notifyHit(final Entity e) {
    if (e instanceof EntityAsteroid) {
      die();
    }
  }

  @Override
  void setHitbox() {
    hb = new float[] {
        0 - sprite.getWidth(), sprite.getHeight(),
        sprite.getWidth(), sprite.getHeight(),
        sprite.getWidth(), 0 - sprite.getHeight(),
        0 - sprite.getWidth(), 0 - sprite.getHeight()
        };

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
