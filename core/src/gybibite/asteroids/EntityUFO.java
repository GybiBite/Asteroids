package gybibite.asteroids;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public final class EntityUFO extends Entity implements Enemy {
  
  boolean isSmall;
  static final float SMALL_UFO_SCALE = 1.5f;
  static final float UFO_SCALE = 2.25f;
  static final float SHOOT_DELAY = 500;
  static final float ZIG_DELAY = 1000;
  long shootTimer, zigZagTimer;
  static Array<Entity> entities = GameUI.entities;

  /**
   * @param scale
   * @param isSmall
   */
  public EntityUFO(boolean isSmall) {
    super(isSmall ? SMALL_UFO_SCALE : UFO_SCALE, isSmall ? Asteroids.ufoSmallTex : Asteroids.ufoTex);

    this.isSmall = isSmall;
    
    x = 300;
    y = (float) Math.random() * Asteroids.S_HEIGHT;
    
    if(isSmall) {
      vx = 100;
      vy = 60;
    } else {
      vx = 75;
      vy = 40;
    }
    setHitbox();
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
            shootTimer = TimeUtils.millis();
          }
        }

      } else {
        new EntityBullet(x, y, (float) Math.floor(Math.random() * 360), false);
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
    // TODO Auto-generated method stub

  }

  @Override
  void die() {
    // TODO Auto-generated method stub

  }
}
