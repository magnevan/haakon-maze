package com.mvikjord.labyrinth;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mvikjord.labyrinth.screens.GameScreen;

public class LabyrinthGame extends Game {
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();

    setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

  @Override
  public void dispose() {
    super.dispose();
    batch.dispose();
  }
}
