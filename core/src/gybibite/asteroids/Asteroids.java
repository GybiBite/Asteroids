package gybibite.asteroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public final class Asteroids extends Game {

  public static final int S_WIDTH = 800;
  public static final int S_HEIGHT = 600;

  public static Texture shipTex;
  public static Texture bulletTex;
  public static Texture astTexSmall;
  public static Texture astTexMed;
  public static Texture astTexLarge;
  public static Texture ufoTex;
  public static Texture ufoSmallTex;
  
  public static Sound laserSfx;
  public static Sound deathSfx;
  public static Sound lifeSfx;

  static boolean debug;

  static ScreenAdapter menu;
  static ScreenAdapter game;
  static ScreenAdapter options;

  public Asteroids(String[] arg) {
    for (String element : arg) {
      if (element.contentEquals("-d") || element.contentEquals("--debug")) { // Handle debug argument
        debug = true;
      }
    }
  }

  @Override
  public void create() {
    Gdx.graphics.setWindowedMode(S_WIDTH, S_HEIGHT);
    Gdx.graphics.setResizable(false);
    menu = new MenuUI(this);
    game = new GameUI(this);
    options = new OptionsUI(this);

    shipTex = new Texture("ship.png");
    bulletTex = new Texture("bullet.png");
    astTexSmall = new Texture("asteroid_small.png");
    astTexMed = new Texture("asteroid_medium.png");
    astTexLarge = new Texture("asteroid_large.png");
    ufoTex = new Texture("ufo.png");
    ufoSmallTex = new Texture("ufo_small.png");
    laserSfx = Gdx.audio.newSound(Gdx.files.internal("laser.wav"));
    deathSfx = Gdx.audio.newSound(Gdx.files.internal("death.wav"));
    lifeSfx = Gdx.audio.newSound(Gdx.files.internal("gain_life.wav"));
    
    setScreen(menu);
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0.05f, 0.1f, 1); // Set background color
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
    super.render(); // Make sure that the active screen renders

    if (debug) {
      // Write FPS to the window's title bar
      Gdx.graphics
          .setTitle("Asteroids - FPS: " + Math.floor(1 / Gdx.graphics.getDeltaTime() * 10) / 10);
    }
  }

  public void switchScreen(final int s) {
    switch (s) {
      case 0:
        setScreen(menu);
        break;
      case 1:
        setScreen(game);
        break;
      case 2:
        setScreen(options);
        break;
      default:
        setScreen(menu);
    }
  }

  @Override
  public void dispose() {
    // There's nothing to dispose of in this class.
    // When this class is disposed, the program exits.
  }


  public static boolean isDebug() {
    return debug;
  }
}
