package gybibite.asteroids;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.TimeUtils;

public final class EntityPlayer extends Entity {

  boolean upPressed, downPressed, leftPressed, rightPressed, firePressed, diePressed;

  static final float ACCEL = 5f;
  static final float DECEL = 150f; // Lower is quicker decel (divisor of velocity) //100
  /** How fast does the ship rotate (degrees per tick button held down) */
  static final float RSPEED = 4f;
  static final float MAX_SPEED = 250f;
  static final long INVUL_TIME = 2000;
  static final long BULLET_CHARGE_TIME = 600;
  static final int MAX_BULLET_COUNT = 4;
  
  static final int DEATH_PARTICLE_COUNT = 16;

  /** Which player the current entity is (1, 2, etc..) */
  final int player;

  final ParticleEmitter thrusterFire;
  final ParticleEmitter deathPieces;
  
  long invulTimer, bulletTimer;
  boolean isInvul = true;
  
  int score = 0;
  int bulletsRemaining = 0;

  EntityPlayer(float scale, int player) {
    super(scale, Asteroids.shipTex);
    
    invulTimer = bulletTimer = TimeUtils.millis();
    bulletsRemaining = 4;

    x = Asteroids.S_WIDTH / 2;
    y = Asteroids.S_HEIGHT / 2;

    this.player = player;

    setHitbox();

    this.thrusterFire =
        new ParticleEmitter(2f, new Color(0.98f, 0.686f, 0.05f, 1f), 90f, 3f, 250f, 50f);
    this.deathPieces =
        new ParticleEmitter(3f, new Color(0.3f, 0.9f, 1f, 1f), 359f, 7f, 1500f, 200f);

  }

  @Override
  /** Convert array of pressed buttons into individual flags */
  public void checkInput(boolean[] buttons) {
    if (player == 1) {
      upPressed = buttons[0];
      downPressed = buttons[1];
      leftPressed = buttons[2];
      rightPressed = buttons[3];
      firePressed = buttons[4];
      diePressed = buttons[5];
    }
  }

  @Override
  public void tick(float delta) {
    
    if (TimeUtils.timeSinceMillis(invulTimer) >= INVUL_TIME) {
      sprite.setAlpha(1);
      isInvul = false;
    } else {
      sprite.setAlpha(0.5f);
    }
    
    if(TimeUtils.timeSinceMillis(bulletTimer) >= BULLET_CHARGE_TIME && bulletsRemaining < MAX_BULLET_COUNT) {
      bulletsRemaining++;
      bulletTimer = TimeUtils.millis();
    }

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

      thrusterFire.emit((float) thrustPos[0], (float) thrustPos[1], -rot, 2);

    } else if (!upPressed) {
      vy -= vy / DECEL;
      vx -= vx / DECEL;
    }

    if (firePressed) {
      if(bulletsRemaining > 0) {
        Asteroids.laserSfx.play((float) GameUI.volume / 6);
        new EntityBullet(x, y, rot, true);
//        new EntityUFO(true);
        bulletsRemaining--;
      }
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

  double[] getThrustorPos() {
    return new double[] {
        hitbox.getTransformedVertices()[4],
        hitbox.getTransformedVertices()[5]
    };
  }

  @Override
  void notifyHit(final Entity e) {
    if (TimeUtils.timeSinceMillis(invulTimer) >= INVUL_TIME) {
      if (e instanceof EntityBullet) {
        if (!((EntityBullet) e).isFriendly()) {
          die();
        }
      }
    } else {
      die();
    }
  }

  
  @Override
  void setHitbox() {
    hb = new float[] {
        0, sprite.getHeight() / 1.3f,
        sprite.getWidth() / 1.3f, -sprite.getHeight() / 2.6f,
        0, -sprite.getHeight() / 1.3f,
        -sprite.getWidth() / 1.3f, -sprite.getHeight() / 2.6f
        };

    hitbox = new Polygon(hb);
  }

  @Override
  Polygon getHitbox() {
    return hitbox;
  }

  @Override
  void die() {
    for (int i = 0; i < DEATH_PARTICLE_COUNT; i++) {
      deathPieces.emit(x, y, 0, 0.2f);
    }
    Asteroids.deathSfx.play((float) GameUI.volume / 6);
    
    GameUI.lives--;
    
    delete();
  }
}
