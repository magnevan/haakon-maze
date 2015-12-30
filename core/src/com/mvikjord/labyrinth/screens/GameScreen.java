package com.mvikjord.labyrinth.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mvikjord.labyrinth.LabyrinthGame;
import com.mvikjord.labyrinth.algorithms.Maze;
import com.mvikjord.labyrinth.algorithms.MazeMaker;
import com.mvikjord.labyrinth.sprites.Tile;

/**
 * Created by Magne on 27-Dec-15.
 */
public class GameScreen implements Screen {

  private final LabyrinthGame game;
  private final OrthographicCamera gamecam;
  private final Viewport viewport;
  private final Texture tileTexture;
  private final Tile[][] tiles;

  private final int WIDTH, HEIGHT;
  public static final int TEXTURE_SIZE = 15;
  public static final int TOUCH_LEEWAY = 5;

  private final Maze maze;

  private Tile lastTouched = null;

  public GameScreen(LabyrinthGame game) {
    this.game = game;

    WIDTH = Gdx.graphics.getWidth() / TEXTURE_SIZE;
    HEIGHT = Gdx.graphics.getHeight() / TEXTURE_SIZE;

    gamecam = new OrthographicCamera();
    viewport = new FitViewport(WIDTH * TEXTURE_SIZE, HEIGHT * TEXTURE_SIZE, gamecam);

    gamecam.position.set(WIDTH * TEXTURE_SIZE / 2f, HEIGHT * TEXTURE_SIZE / 2f, 0);
    viewport.apply();

    maze = MazeMaker.recursiveBacktracking(WIDTH, HEIGHT);

    tileTexture = new Texture("tile.png");
    tiles = new Tile[WIDTH][HEIGHT];
    for (int i = 0; i < WIDTH; i++) {
      for (int j = 0; j < HEIGHT; j++) {
        tiles[i][j] = new Tile(tileTexture);
        tiles[i][j].setX(i * TEXTURE_SIZE);
        tiles[i][j].setY(j * TEXTURE_SIZE);
      }
    }

    maze.setTiles(tiles);
    maze.setStart(0, 0);
  }

  private void handleInput() {
    if (!Gdx.input.isTouched()) {
      lastTouched = null;
    } else {
      Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
      gamecam.unproject(pos);
      int i = (int)(pos.x / TEXTURE_SIZE);
      int j = (int)(pos.y / TEXTURE_SIZE);

      if (i >= 0 && j >= 0 && i < WIDTH && j < HEIGHT) {
        Tile touchedTile = tiles[i][j];

        if (touchedTile != lastTouched) {
          lastTouched = touchedTile;
          maze.gotoIfPossible(i, j);
        }
      }
    }
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    handleInput();

    Gdx.gl.glClearColor(1, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    game.batch.setProjectionMatrix(gamecam.combined);

    game.batch.begin();
    for (int i = 0; i < WIDTH; i++)
      for (int j = 0; j < HEIGHT; j++) {
        tiles[i][j].draw(game.batch);
      }
    game.batch.end();

  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {

  }
}
