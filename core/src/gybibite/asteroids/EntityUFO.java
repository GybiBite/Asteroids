package gybibite.asteroids;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public final class EntityUFO extends Entity/* implements Enemy */{
  
  boolean isSmall;
  static final float SMALL_UFO_SCALE = 1.5f;
  static final float UFO_SCALE = 2.25f;
  static final float SHOOT_DELAY = 500;
  static final float ZIG_DELAY = 1000;
  long shootTimer, zigZagTimer;
  static Array<Entity> entities = GameUI.entities;
  
  final ParticleEmitter deathPieces;

  /** Amount of particles to spawn upon death. */
  static final int DEATH_PARTICLE_COUNT = 16;

  /**
   * @param scale
   * @param isSmall
   */
  public EntityUFO(boolean isSmall) {
    super(isSmall ? SMALL_UFO_SCALE : UFO_SCALE, isSmall ? Asteroids.ufoSmallTex : Asteroids.ufoTex);

    this.isSmall = isSmall;
    
    x = 0;
    y = (float) Math.random() * Asteroids.S_HEIGHT;
    
    if(isSmall) {
      vx = 100;
      vy = 60;
    } else {
      vx = 75;
      vy = 40;
    }
    setHitbox();
    
    this.deathPieces =
        new ParticleEmitter(3f, new Color(1f, 1f, 1f, 1f), 359f, 7f, 1000f, 100f);
  }

  @Override
  void tick(float delta) {
    
    /* Behavioral differences */
    if (TimeUtils.timeSinceMillis(shootTimer) >= SHOOT_DELAY) {
      if (isSmall) {
        for (int i = 0; i < entities.size; i++) {
          if (entities.toArray()[i] instanceof EntityPlayer) {
            EntityPlayer e = (EntityPlayer) entities.toArray()[i];
            float diffX = x - e.x;
            float diffY = y - e.y;
            float angle = (float) Math.toDegrees(Math.atan(diffX / -diffY));
            if (diffY == Math.abs(diffY)) {
              angle += 180;
            }
            new EntityBullet(x, y, angle, false);
            Asteroids.laserSfx.play((float) GameUI.volume / 6);
            shootTimer = TimeUtils.millis();
          }
        }

      } else {
        new EntityBullet(x, y, (float) Math.floor(Math.random() * 360), false);
        Asteroids.laserSfx.play((float) GameUI.volume / 6);
        shootTimer = TimeUtils.millis();
      }
    }
    
    /* Movement differences */
    if (isSmall) {
      if (TimeUtils.timeSinceMillis(zigZagTimer) >= ZIG_DELAY / 2) {
        vy *= -1;
        zigZagTimer = TimeUtils.millis();
      }
    } else {
      if (TimeUtils.timeSinceMillis(zigZagTimer) >= ZIG_DELAY) {
        vy *= -1;
        zigZagTimer = TimeUtils.millis();
      }
    }

    x += vx * delta;
    y += vy * delta;

    if (x <= 0) {
      x = Asteroids.S_WIDTH;
    } else if (x >= Asteroids.S_WIDTH) {
      x = 0;
      y = (float) (Math.random() * Asteroids.S_HEIGHT);
    }

    if (y <= 0) {
      y = Asteroids.S_HEIGHT;
    } else if (y >= Asteroids.S_HEIGHT) {
      y = 0;
    }
    
    hitbox.setPosition(x, y);
  }

  @Override
  void setHitbox() {
    
    if(isSmall) {
      hb = new float[] {
          0, sprite.getHeight() - 7,
          sprite.getWidth() - 4, 0,
          0, -sprite.getHeight() + 8,
          -sprite.getWidth() + 4, 0
      };
    } else {
      hb = new float[] {
          0, sprite.getHeight() + 2,
          sprite.getWidth() + 1, 0,
          0, -sprite.getHeight() / 1.3f,
          -sprite.getWidth() - 1, 0
      };
    }

    hitbox = new Polygon(hb);
  }

  @Override
  Polygon getHitbox() {
    return hitbox;
  }

  @Override
  void notifyHit(Entity e) {
    if (e instanceof EntityBullet && ((EntityBullet) e).isFriendly()) {
      GameUI.addScore(isSmall ? 150 : 100);
      die();
    } else if (e instanceof EntityPlayer && !((EntityPlayer) e).isInvul) {
      die();
    }
  }

  @Override
  void die() {
    for (int i = 0; i < 10/* amount of death particles to emit */; i++) {
      deathPieces.emit(x, y, 0, 0.2f);
    }

    delete();
  }
}
