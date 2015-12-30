package com.mvikjord.labyrinth.algorithms;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Queue;
import com.mvikjord.labyrinth.screens.GameScreen;
import com.mvikjord.labyrinth.sprites.Tile;

/**
 * Created by Magne on 28-Dec-15.
 */
public class Maze {
  public final int width, height;
  public int[][] mask;
  public boolean[][] explored;

  private Tile[][] tiles;

  public int cx, cy;

  public Maze(int width, int height) {
    this.width = width;
    this.height = height;
    mask = new int[width][height];
    explored = new boolean[width][height];
  }

  public void setPassages(int[][] mask) {
    this.mask = mask;
  }

  public void setStart(int x, int y) {
    explored[x][y] = true;
    tiles[x][y].explore();

    int[] goal = furthestPointFrom(x, y);
    tiles[goal[0]][goal[1]].setGoal();
  }

  public void setTiles(Tile[][] tiles) {
    for (int x = 0; x < width; x++)
      for (int y = 0; y < height; y++)
        tiles[x][y].passages = mask[x][y];
    this.tiles = tiles;
  }

  public void gotoIfPossible(int i, int j) {
    if (explored[i][j]) {
      return;
    }

    IntArray path = bfsPath(i, j);
    if (path != null && path.size <= GameScreen.TOUCH_LEEWAY) {
      for (int c : path.items) {
        int[] pos = decode(c);
        explored[pos[0]][pos[1]] = true;
        tiles[pos[0]][pos[1]].explore();
      }
    }

  }

  private IntArray bfsPath(int x, int y) {
    boolean[][] seen = new boolean[width][height];
    int[][] previous = new int[width][height];
    int[] dx = new int[]{ 0,  0,  1, -1};
    int[] dy = new int[]{ -1, 1,  0,  0};
    int[] dirmask = new int[]{1, 2, 4, 8};

    seen[x][y] = true;
    Queue<Integer> q = new Queue<Integer>();
    q.addFirst(encode(x, y));
    while (q.size > 0) {
      int[] pos = decode(q.removeFirst());

      if (explored[pos[0]][pos[1]]) {
        IntArray path = new IntArray();
        int c = previous[pos[0]][pos[1]];
        int[] npos = decode(c);

        while (npos[0] != x || npos[1] != y) {
          path.add(c);
          c = previous[npos[0]][npos[1]];
          npos = decode(c);
        }

        path.add(encode(x, y));
        return path;
      }

      for (int i = 0; i < 4; i++) {
        int nx = pos[0] + dx[i];
        int ny = pos[1] + dy[i];
        if (!within(nx, ny)) continue;

        if (seen[nx][ny]) continue;
        if ((mask[pos[0]][pos[1]] & dirmask[i]) == 0) continue;

        previous[nx][ny] = encode(pos[0], pos[1]);
        seen[nx][ny] = true;
        q.addLast(encode(nx, ny));
      }
    }

    return null;
  }

  private int[] furthestPointFrom(int x, int y) {
    boolean[][] seen = new boolean[width][height];
    int[] dx = new int[]{ 0,  0,  1, -1};
    int[] dy = new int[]{ -1, 1,  0,  0};
    int[] dirmask = new int[]{1, 2, 4, 8};
    int[] lastpos = new int[]{x, y};

    seen[x][y] = true;
    Queue<Integer> q = new Queue<Integer>();
    q.addFirst(encode(x, y));
    while (q.size > 0) {
      int[] pos = decode(q.removeFirst());
      lastpos = pos;

      for (int i = 0; i < 4; i++) {
        int nx = pos[0] + dx[i];
        int ny = pos[1] + dy[i];

        if (!within(nx, ny)) continue;
        if (seen[nx][ny]) continue;
        if ((mask[pos[0]][pos[1]] & dirmask[i]) == 0) continue;

        seen[nx][ny] = true;
        q.addLast(encode(nx, ny));
      }
    }

    return lastpos;
  }

  private int encode(int i, int j) {
    if (width > height)
      return i * width + j;
    else
      return j * height + i;
  }

  private int[] decode(int c) {
    int i, j;
    if (width > height) {
      j = c % width;
      i = (c - j) / width;
    } else {
      i = c % height;
      j = (c - i) / height;
    }

    return new int[]{i, j};
  }

  private boolean within(int x, int y) {
    return x >= 0 && y >= 0 && x < width && y < height;
  }
}
