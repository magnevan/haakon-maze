package com.mvikjord.labyrinth.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mvikjord.labyrinth.screens.GameScreen;

/**
 * Created by Magne on 28-Dec-15.
 */
public class Tile extends Sprite {
  public boolean closed;
  public boolean explored;
  public int passages = 0;
  public boolean isGoal;

  private ShapeRenderer renderer = new ShapeRenderer();

  public static final int NORTH = 1, SOUTH = 2, EAST = 4, WEST = 8;

  public Tile(Texture texture) {
    super(texture);
    //passages = NORTH | SOUTH | EAST | WEST; //MathUtils.random(0, 15);
    passages = 0;
  }

  public void close() {
    closed = true;
    this.setColor(0.5f, 0.5f, 0.5f, 1f);
  }

  public void explore() {
    explored = true;
    this.setColor(0.8f, 0.8f, 0f, 1f);
  }

  public void setGoal() {
    //this.setColor(0f, 0.8f, 0f, 1f);
    isGoal = true;
  }

  @Override
  public void draw(Batch batch) {
    super.draw(batch);
    int s = GameScreen.TEXTURE_SIZE;
    renderer.setProjectionMatrix(batch.getProjectionMatrix());
    renderer.begin(ShapeRenderer.ShapeType.Filled);
    if (explored) {
      renderer.setColor(1f, 1f, 0f, 1f);
    } else if (isGoal) {
      renderer.setColor(0f, 0.8f, 0f, 1f);
    } else {
      renderer.setColor(1f, 1f, 1f, 1f);
    }
    renderer.rect(this.getX(), this.getY(), s, s);

    renderer.setColor(0f, 0f, 0f, 1f);
    if ((passages & NORTH) == 0) {
      renderer.rect(this.getX(), this.getY(), s, 1);
    }
    if ((passages & SOUTH) == 0) {
      renderer.rect(this.getX(), this.getY() + s - 1, s, 1);
    }
    if ((passages & WEST) == 0) {
      renderer.rect(this.getX(), this.getY(), 1, s);
    }
    if ((passages & EAST) == 0) {
      renderer.rect(this.getX() + s - 1, this.getY(), 1, s);
    }

    renderer.end();
  }
}
