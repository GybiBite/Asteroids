package gybibite.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Utility class to have a text "typewriter" effect where, when executed, the text will slowly form
 * character by character, such as when being actively typed out. After a short pause, it will
 * "untype" the characters until there is no more text on the screen.
 * 
 * @author GybiBite
 */
public class TypewriterTextAnim {

  /** Font object to draw the text with */
  private final BitmapFont font =
      new BitmapFont(Gdx.files.internal("fsex300.fnt"), Gdx.files.internal("fsex300.png"), false);

  /** X position of the text label */
  float x;
  /** Y position of the text label */
  float y;
  /** Full text to be displayed on the label */
  String label;
  /** How many characters per second will be typed out */
  float speed;
  /** How many seconds to pause before "untyping" the text */
  float pauseInt;

  private String temp = "";
  private long speedTimer;
  private boolean typing, untyping;

  /**
   * Creates a new typewriter text object at a certain point on the screen with a specific label and
   * speed
   */
  public TypewriterTextAnim(float x, float y, float scale, String label, float speed, float pauseInt) {
    this.x = x;
    this.y = y;
    this.label = label;
    this.speed = speed;
    this.pauseInt = pauseInt;
    font.getData().setScale(scale);
  }

  public void render(SpriteBatch sb) {
    if (typing) {
      addChars();
    } else if (untyping) {
      removeChars();
    } else if (TimeUtils.timeSinceMillis(speedTimer) >= pauseInt) {
      untyping = true;
    }
    
    sb.begin();
    font.draw(sb, temp, x, y, 0, Align.center, false);
    sb.end();
  }
  
  public void setLabel(String label) {
    this.label = label;
  }

  public void startTyping() {
    typing = true;
  }

  private void addChars() {
    if (temp.length() < label.length()) {
      if (TimeUtils.timeSinceMillis(speedTimer) >= speed) {
        temp = temp + label.charAt(temp.length());
        speedTimer = TimeUtils.millis();
      }
    } else {
      typing = false;
    }
  }

  private void removeChars() {
    if (temp.length() > 0) {
      if (TimeUtils.timeSinceMillis(speedTimer) >= speed) {
        temp = temp.substring(0, temp.length() - 1);
        speedTimer = TimeUtils.millis();
      }
    } else {
      untyping = false;
      speedTimer = TimeUtils.millis();
    }
  }
}
