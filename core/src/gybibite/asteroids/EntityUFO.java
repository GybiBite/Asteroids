package gybibite.asteroids;

import com.badlogic.gdx.math.Polygon;

public final class EntityUFO extends Entity implements Enemy {
  
  boolean isSmall;
  static final float SMALL_UFO_SCALE = 1;
  static final float UFO_SCALE = 1;

  /**
   * @param scale
   * @param isSmall
   */
  public EntityUFO(boolean isSmall) {
    super(isSmall ? SMALL_UFO_SCALE : UFO_SCALE, isSmall ? Asteroids.ufoSmallTex : Asteroids.ufoTex);

    this.isSmall = isSmall;
  }

  @Override
  void tick(float delta) {
    x += vx * delta;
    y += vy * delta;
    
    
  }
  
  void shootAtPlayer(EntityPlayer plr) {
    
  }

  @Override
  void setHitbox() {
    // TODO Auto-generated method stub

  }

  @Override
  Polygon getHitbox() {
    // TODO Auto-generated method stub
    return null;
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
