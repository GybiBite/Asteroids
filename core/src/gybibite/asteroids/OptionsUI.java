package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public final class OptionsUI extends ScreenAdapter {

  SpriteBatch sb;
  ShapeRenderer sr;
  Asteroids g;
  Array<Button> main = new Array<>(new Button[0]);
  
  private final BitmapFont font =
      new BitmapFont(Gdx.files.internal("fsex300.fnt"), Gdx.files.internal("fsex300.png"), false);
  
  boolean useArrowKeys = false;

  public OptionsUI(Asteroids g) {
    sb = new SpriteBatch();
    sr = new ShapeRenderer();
    this.g = g;
    
    font.setColor(1, 1, 1, 1);

    main.add(
        new Button(400, 100, 175, 40, "<- Return", sb, sr).setClickEvent(() -> g.switchScreen(0))
    );
    
    main.add(
        new Button(550, 250, 150, 40, "W|A|S|D", sb, sr).setClickEvent(() -> {
          GameUI.useArrowKeys = !GameUI.useArrowKeys;
          if(GameUI.useArrowKeys) {
            main.toArray()[1].setLabel("^|<|V|>");
          }
          else {
            main.toArray()[1].setLabel("W|A|S|D");
          }
        })
    );
    
    main.add(
        new Button(550, 300, 150, 40, "No", sb, sr).setEnabled(false)
    );
    main.add(
        new Button(550, 350, 150, 40, Asteroids.debug ? "Enabled" : "Disabled", sb, sr).setClickEvent(() -> {
          Asteroids.debug = !Asteroids.debug;
          
          if(Asteroids.debug) {
            main.toArray()[3].setLabel("Enabled");
          } else {
            main.toArray()[3].setLabel("Disabled");
            Gdx.graphics.setTitle("Asteroids");
          }
        }).setEnabled(Asteroids.debug)
    );
    main.add(
        new Button(550, 400, 150, 40, "Max", sb, sr).setClickEvent(() -> {
          switch (GameUI.volume) {
            case 0:
              main.toArray()[4].setLabel("Min");
              GameUI.volume++;
              break;
            case 1:
              main.toArray()[4].setLabel("Med");
              GameUI.volume++;
              break;
            case 2:
              main.toArray()[4].setLabel("Max");
              GameUI.volume++;
              break;
            case 3:
              main.toArray()[4].setLabel("Off");
              GameUI.volume = 0;
              break;
            default:
              main.toArray()[4].setLabel("Off");
              GameUI.volume = 0;
          }
        })
    );
  }
  
  @Override
  public void render(float delta) {
    for (Button b : main) {
      b.render();
      b.checkHover(Gdx.input.getX(), Gdx.input.getY(), Gdx.input.isButtonJustPressed(Buttons.LEFT));
    }
    
    sb.begin();
    font.getData().setScale(0.4f);
    font.draw(sb, "In-game volume:", 450, 415, 0, Align.right, false);
    font.draw(sb, "Debug/dev mode:", 450, 365, 0, Align.right, false);
    font.draw(sb, "Publish scores:", 450, 315, 0, Align.right, false);
    font.draw(sb, "Control scheme:", 450, 265, 0, Align.right, false);
    sb.end();
  }
}
