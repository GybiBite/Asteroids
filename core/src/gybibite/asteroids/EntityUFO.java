package gybibite.asteroids;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public final class EntityUFO extends Entity implements Enemy {
  
  boolean isSmall;
  static final float SMALL_UFO_SCALE = 1;
  static final float UFO_SCALE = 2.25f;
  static final float SHOOT_DELAY = 1000;
  long shootTimer;
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
      vx = 25;
    } else {
      vx = 10;
    }
    
    setHitbox();
  }

  @Override
  void tick(float delta) {
    if (TimeUtils.timeSinceMillis(shootTimer) >= SHOOT_DELAY) {
      if (isSmall) {
        for(int i = 0; i < entities.size; i++) {
          if (entities.toArray()[i] instanceof EntityPlayer) {
            EntityPlayer e = (EntityPlayer) entities.toArray()[i];
            float diffX = x - e.x;
            float diffY = y - e.y;
            float angle = (float) Math.toDegrees(Math.atan(diffX / -diffY));
            if(diffY == Math.abs(diffY)) {
              angle += 180;
            }
            new EntityBullet(x, y, angle, false);
            shootTimer = TimeUtils.millis();
            System.out.println("bullet shot i think idk");
          }
        }
        
      }
      else {
        new EntityBullet(x, y, (float) Math.floor(Math.random() * 360), false);
        shootTimer = TimeUtils.millis();
      }
    }
    hitbox.setPosition(x, y);
  }

  @Override
  void setHitbox() {
    if(isSmall) {
      
    } else {
      hb = new float[] {
          0, sprite.getHeight() + 2,
          sprite.getWidth() + 1, 0,
          0, -sprite.getHeight() / 1.3f,
          -sprite.getWidth() - 1, 0
      };
    }

    hitbox = new Polygon(hb);
    
    System.out.println(hitbox);
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
