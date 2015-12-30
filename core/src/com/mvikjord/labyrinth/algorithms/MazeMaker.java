package com.mvikjord.labyrinth.algorithms;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntArray;

/**
 * Created by Magne on 28-Dec-15.
 */
public class MazeMaker {
  private static int[] dx = new int[]{ 0,  0,  1, -1};
  private static int[] dy = new int[]{ -1, 1,  0,  0};


  private static int[] maskbit = new int[]{1, 2, 4, 8};
  private static int[] maskbit_reverse = new int[]{2, 1, 8, 4};
  public static Maze recursiveBacktracking(int width, int height) {
    int[][] mask = new int[width][height];

    recursiveBacktrackingInternal(0, 0, mask, width, height);

    Maze maze = new Maze(width, height);
    maze.setPassages(mask);
    obfuscate(maze, 0.02f);

    return maze;
  }

  private static void recursiveBacktrackingInternal(int x, int y, int[][] mask, int width, int height) {
    IntArray directions = new IntArray(new int[]{0, 1, 2, 3});
    directions.shuffle();

    for (int d : directions.items) {
      int nx = x + dx[d];
      int ny = y + dy[d];
      if (!within(nx, ny, width, height) || mask[nx][ny] != 0) continue;
      mask[nx][ny] |= maskbit_reverse[d];
      mask[x][y] |= maskbit[d];
      recursiveBacktrackingInternal(nx, ny, mask, width, height);
    }
  }

  private static void obfuscate(Maze maze, float breakchance) {
    for (int x = 0; x < maze.width; x++) {
      for (int y = 0; y < maze.height; y++) {
        for (int i = 0; i < 4; i++) {
          float rnjesus = MathUtils.random(0f, 1f);
          if (rnjesus < breakchance) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (!within(nx, ny, maze.width, maze.height)) continue;
            maze.mask[x][y] |= maskbit[i];
            maze.mask[x + dx[i]][y + dy[i]] |= maskbit_reverse[i];
          }
        }
      }
    }
  }

  private static boolean within(int x, int y, int width, int height) {
    return x >= 0 && y >= 0 && x < width && y < height;
  }
}
