package com.mvikjord.labyrinth.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mvikjord.labyrinth.LabyrinthGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.width = 1000;
    config.height = 1000;
    config.resizable = false;
		new LwjglApplication(new LabyrinthGame(), config);
	}
}
