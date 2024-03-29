package gybibite.asteroids;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import gybibite.asteroids.ParticleEmitter.Particle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public final class GameUI extends ScreenAdapter {

  static SpriteBatch batch;
  static ShapeRenderer s;
  Asteroids g;
  static Array<Entity> entities = new Array<Entity>(new Entity[0]);
  static Array<Particle> particles = new Array<Particle>(new Particle[0]);

  private final BitmapFont font =
      new BitmapFont(Gdx.files.internal("fsex300.fnt"), Gdx.files.internal("fsex300.png"), false);

  /** Stores every sprite that will just be a static play on the screen to indicate a life */
  static Sprite[] livesDisp; // This size can be modified to change max amount of "lives" shown.

  private final int NEXTLVL_DELAY = 3000;
  private final int STAR_COUNT = 300;
  private final static int NEW_LIFE_SCORE = 4000;
  private static double ufoSpawnDelay = 15000;
  long nextLvlTimer = -1;
  private int level = 0;
  private boolean gameHasEnded = false;
  static int score;
  private boolean enemiesStillAlive, playerStillAlive;
  private int[] tempAstSpawnPos;
  private long ufoSpawnTimer;
  static int volume = 3;
  
  static boolean useArrowKeys;
  static boolean killKeyPressed;
  static boolean isUfoAlive;

  Random rand = new Random();
  static CollisionDetector collide = new CollisionDetector(entities);
  EntityPlayer playerOne;
  
  ParticleEmitter stars = new ParticleEmitter(1, new Color(255, 255, 255, 1), 360, 400, Long.MAX_VALUE, 0);
  
  private static final TypewriterTextAnim LEVEL_TEXT = new TypewriterTextAnim(Asteroids.S_WIDTH / 2, Asteroids.S_HEIGHT - 50, 0.4f, "IfYouSeeThisTheresABug", 50f, 3000f);
  private static final TypewriterTextAnim GAME_OVER = new TypewriterTextAnim(Asteroids.S_WIDTH / 2, Asteroids.S_HEIGHT / 2, 0.6f, "Game Over", 100f, Float.MAX_VALUE);

  /** Amount of remaining lives (or "respawns") for the player */
  public static int lives = 0;

  GameUI(Asteroids g) {
    this.g = g;

    font.setColor(1, 1, 1, 1);

    font.getData().setScale(0.25f);
  }

  @Override
  public void show() {
    livesDisp = new Sprite[10];
    gameHasEnded = false;
    lives = 3;
    score = 0;
    level = 0;
    ufoSpawnTimer = TimeUtils.millis();
    batch = new SpriteBatch();
    s = new ShapeRenderer();
    new EntityPlayer(1.875f, 1); // Creates a new player (ship), while passing the sprite batch so it can render

    for (int i = 0; i < livesDisp.length; i++) {
      livesDisp[i] = new Sprite(Asteroids.shipTex);
      livesDisp[i].setScale(1.2f);

      livesDisp[i].setPosition(30 + (i * 20), Asteroids.S_HEIGHT - 70);
      livesDisp[i].setRotation(-45);
    }
    
    for(int i = 0; i < STAR_COUNT; i++) {
      stars.emit(Asteroids.S_WIDTH/2, Asteroids.S_HEIGHT/2, 0, 0);
    }
  }

  @Override
  public void render(float delta) {
    tick(delta); 

    batch.begin(); // Begins the sprite batch. Required for libGDX
    for (Entity e : entities) {
      e.render(batch);
    }

    font.draw(batch, ("" + score), 30, Asteroids.S_HEIGHT - 30, 2, Align.left, false);
    
    if(gameHasEnded) {
      font.draw(batch, ("Score: " + score), Asteroids.S_WIDTH / 2, Asteroids.S_HEIGHT / 2 - 50, 0, Align.center, false);
      
      if(Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
        dispose();
        g.switchScreen(0);
        GAME_OVER.reset();
      }
    }

    for (int i = 0; i < Math.min(10, lives); i++) {
      livesDisp[i].draw(batch);
    }
    batch.end(); // Ends the sprite batch. Required for libGDX

    LEVEL_TEXT.render(batch);
    GAME_OVER.render(batch);
    
    if (Asteroids.isDebug()) {
      for (Entity e : entities) {
        Gdx.app.log("ENTITYLIST", e.toString()); // Print entity info
        s.begin(ShapeType.Line); // Begin the shape renderer for type Line
        Gdx.gl.glLineWidth(1);
        s.setColor(1, 1, 0, 1); // Set the line color to yellow
        e.drawHB(); // Draw the polygon representing the polygon
        s.end(); // End the shape renderer
      }
      Gdx.app.log("ENTITYLIST", "===== END OF FRAME =====");
    }
  }

  public void tick(float delta) {
    killKeyPressed = Gdx.input.isKeyJustPressed(Keys.F) && Asteroids.debug;
    enemiesStillAlive = playerStillAlive = isUfoAlive = false;
    
    /*
     * Because of an apparent bug with iterators, this has to be a classic "for loop" and not
     * "Object o : Array" and it looks ugly as shit
     * 
     * PS: iterators are awful :)
     */
    for (int i = 0; i < entities.size; i++) {
      // If current entity tick is on a player, check user input
      if (entities.toArray()[i] instanceof EntityPlayer) {
        if (playerOne == null) {
          playerOne = (EntityPlayer) entities.toArray()[i];
        }

        entities.toArray()[i].checkInput(new boolean[] {
            useArrowKeys? Gdx.input.isKeyPressed(Keys.UP) : Gdx.input.isKeyPressed(Keys.W),
            useArrowKeys? Gdx.input.isKeyPressed(Keys.DOWN) : Gdx.input.isKeyPressed(Keys.S),
            useArrowKeys? Gdx.input.isKeyPressed(Keys.LEFT) : Gdx.input.isKeyPressed(Keys.A),
            useArrowKeys? Gdx.input.isKeyPressed(Keys.RIGHT) : Gdx.input.isKeyPressed(Keys.D),
            /* 4 */ Gdx.input.isButtonJustPressed(Buttons.LEFT),
            /* 5 */ Gdx.input.isKeyJustPressed(Keys.ALT_RIGHT)});

        playerStillAlive = true;
      }
      // If current entity is considered an Enemy, tell the game enemies are still alive
      if (entities.toArray()[i] instanceof Enemy) {
        enemiesStillAlive = true;
      }
      
      if (entities.toArray()[i] instanceof EntityUFO) {
        isUfoAlive = true;
      }
    }

    for (Particle p : particles) {
      p.tick(s);
    }
    for (Entity e : entities) {
      e.tick(delta);
      collide.checkForCollisions();
      
      if(killKeyPressed) {
        entities.clear();
      }
    }

    if (!playerStillAlive && !gameHasEnded) {
      if (nextLvlTimer == -1) {
        nextLvlTimer = TimeUtils.millis();
      } else if (TimeUtils.timeSinceMillis(nextLvlTimer) >= NEXTLVL_DELAY) {
        nextLvlTimer = -1;
        if (lives >= 0) {
          new EntityPlayer(1.875f, 1);
        } else {
          System.out.println("reached death");
          for(Entity e : entities) {
            e.die();
          }
          gameHasEnded = true;
          GAME_OVER.startTyping();
//          dispose();
//          g.switchScreen(0);
          
          
        }
      }
    } else if (!enemiesStillAlive && !gameHasEnded) {
      if (nextLvlTimer == -1) {
        level++;
        nextLvlTimer = TimeUtils.millis();
        LEVEL_TEXT.setLabel("Level " + level);
        LEVEL_TEXT.startTyping();
      } else if (TimeUtils.timeSinceMillis(nextLvlTimer) >= NEXTLVL_DELAY) {
        nextLvlTimer = -1;
        for(int i = 0; i < level + 2; i++) {
          tempAstSpawnPos = getSafeAsteroidSpawnPos(playerOne);
          new EntityAsteroid(2, tempAstSpawnPos[0], tempAstSpawnPos[1]);
        }
      }
    }
    
    if(TimeUtils.timeSinceMillis(ufoSpawnTimer) > ufoSpawnDelay) {
      if(isUfoAlive) {
        ufoSpawnTimer = TimeUtils.millis();
      }
      else {
      ufoSpawnDelay = (20 + (Math.random() * 30)) * 1000;
      ufoSpawnTimer = TimeUtils.millis();
      
      new EntityUFO((2/level) < Math.random());
      }
    }
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {
    dispose();
  }

  @Override
  public void dispose() {
    for (Entity e : entities) {
      e.dispose();
    }

    entities.clear();
    particles.clear();
  }

  public static void addEntity(Entity e) {
    entities.add(e);
  }

  public static void addParticle(Particle p) {
    particles.add(p);
  }

  public static void destroyEntity(Entity e) {
    if (entities.contains(e, true)) {
      entities.removeValue(e, true);
    }
  }

  public static void destroyParticle(Particle p) {
    if (particles.contains(p, true)) {
      particles.removeValue(p, true);
    }
  }

  public static Array<Entity> getEntities() {
    return entities;
  }
  
  public int[] getSafeAsteroidSpawnPos(final EntityPlayer plr) {
    int xPos, yPos;
    
    do {
      xPos = rand.nextInt(Asteroids.S_WIDTH);
    } while (Math.abs(xPos - plr.getX()) < 250);
    
    do {
      yPos = rand.nextInt(Asteroids.S_HEIGHT);
    } while (Math.abs(yPos - plr.getY()) < 250);
    
    return new int[] {xPos, yPos};
  }
  
  public static void addScore(int score) {
    for (int i = GameUI.score + 1; i <= GameUI.score + score; i++) {
      if (GameUI.score != 0 && i % NEW_LIFE_SCORE == 0) {
        lives += 1;
        Asteroids.lifeSfx.play((float) GameUI.volume / 6);
      }
    }
    GameUI.score += score;
  }

  public static void drawPoly(float[] vert) {
    s.polygon(vert);
  }

  public static void drawCirc(Circle c) {
    s.circle(c.x, c.y, c.radius);
  }
}
