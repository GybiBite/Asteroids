package gybibite.asteroids;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public final class EntityAsteroid extends Entity implements Enemy {

  /** The size of the asteroid. */
  private int size;
  /** Alternate object for round hitbox. */
  private Circle hitbox;
  /** Hitbox array for the asteroid clones. */
  private Circle[] cloneHitbox = new Circle[CLONE_COUNT];
  double rotSpeed;
  final ParticleEmitter deathPieces;

  /** Amount of particles to spawn upon death. */
  static final int DEATH_PARTICLE_COUNT = 16;

  /** Creates a new asteroid object with a random direction.
   *
   * @param astSize The size of the asteroid (small, medium, large)
   * @param astX The X position to spawn the asteroid at
   * @param astY The Y position to spawn the asteroid at
   */
  public EntityAsteroid(final int astSize, final float astX, final float astY) {
    super(setScale(astSize), setTexture(astSize));

    this.size = astSize;
    this.x = astX;
    this.y = astY;

    double speed = Math.random() * (0.9 - 0.15) + 0.15;

    rot = (float) ((speed * 720) - 360);
    rotSpeed = speed * 2;

    speed *= 200 - ((astSize - 1) * 20);

    vy += (float) (Math.cos(Math.toRadians(rot))) * speed;
    vx -= (float) (Math.sin(Math.toRadians(rot))) * speed;

    setHitbox();

    this.deathPieces =
        new ParticleEmitter(astSize + 3, new Color(0.66f, 0.66f, 0.66f, 1f), 359f, 7f, 1000f, 100f);
  }

  @Override
  void tick(final float delta) {
    x += vx * delta;
    y += vy * delta;

    rot += rotSpeed;

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

    hitbox.setPosition(x, y);
    for (int i = 0; i < cloneHitbox.length; i++) {
      cloneHitbox[i].setPosition(clones[i].getX() + sprite.getWidth() / 2,
          clones[i].getY() + sprite.getHeight() / 2);
    }
  }

  @Override
  void notifyHit(final Entity e) {
    if (e instanceof EntityBullet) {
      GameUI.addScore(25 * (3 - size));
      die();
    } else if (e instanceof EntityPlayer && !((EntityPlayer) e).isInvul) {
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

  static Texture setTexture(final int size) {
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

  static float setScale(final float size) {
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
    for (int i = 0; i < 8/* amount of death particles to emit */; i++) {
      deathPieces.emit(x, y, 0, 0.2f);
    }

    delete();
  }
}
