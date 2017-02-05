package me.markyhzhang.projectpatherserver;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This is the main class of the server, it
 * implements runnable for the server socket
 * to accept players without blocking thread
 */
class MapGenerator {

    /**
     * The 2D integer array of the maze
     */
    private int[][] maze;

    /**
     * The row number of the map
     */
    private int row;

    /**
     * The column number of the map
     */
    private int col;

    /**
     * A collection of direction point
     * vectors in all four directions
     */
    private ArrayList<Point> directions = new ArrayList<Point>() {{
        add(new Point(-2,0));//up
        add(new Point(2,0));//down
        add(new Point(0,-2));//left
        add(new Point(0,2));//right
    }};

    /**
     * The constructor of this map generator
     * @param row integer
     * @param col integer
     */
    MapGenerator(int row, int col){
        maze = new int[row-2][col-2];
        this.row = row-2;
        this.col = col-2;
    }

    /**
     * This method is the helper method of the
     * DPS recursive map generation algorithm with
     * room generation added to it.
     */
    int[][] generate() {

        //Calculate the size of the map
        int mapSize = row*col;

        //number counter for the map sectioning
        int cnt = 0;

        /*
         * Initializes the map in to 3 sections with different
         * walls. 3 and 4 and 5 are all wall IDs.
         */
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (cnt<mapSize/3)
                    maze[i][j] = 3;
                else if (cnt<mapSize/3*2)
                    maze[i][j] = 4;
                else
                    maze[i][j] = 5;
                cnt++;
            }
        }

        //Generate the maze with recursive method
        dfs(0, 0);

        /*
         * Initializes the 2D map array to be both horizontally
         * and vertically 2 bigger than the maze length for
         * border generation.
         */
        int[][] map = new int[maze.length+2][maze[0].length+2];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                //adds border (ID 9) to the map all four edges of the square
                if (i==0 || i==map.length-1 || j==0 || j==map[0].length-1){
                    map[i][j]=9;
                }
                //otherwise keep the original
                else map[i][j]=maze[i-1][j-1];
            }
        }

        /*
         * This part of the code generates random rooms
         * at random locations with random sizes on to the map
         */
        //generates row/4 rooms
        for (int i = 0; i < row/4; i++) {
            //the x and y are randomized with a ranange
            int x = (int) (Math.random() * (row-6)) + 2;
            int y = (int) (Math.random() * (col-5)) + 2;
            //size is randomized as well with a rande
            int size = (int) (Math.random() * 4) + 2;
            //build the room
            for (int j = x; j < x+size; j++) {
                for (int k = y; k < y+size; k++) {
                    map[j][k] = 1;
                }
            }
        }

        /*
         * Center room generation. It considers two cases.
         * even row and odd row (Note that only rows are considered
         * since the map is a square).
         */
        if ((row+2) % 2 ==0) {//even
            //finds the center row and column and apply the center id of 2
            for (int i = (row+2-1)/2-1; i < (row+2-1)/2-1+4; i++) {
                for (int j = (col+2-1)/2-1; j < (row+2-1)/2-1+4; j++) {
                    map[i][j]=2;
                }
            }
        }else{//odd
            //finds the center row and column and apply the center id of 2
            for (int i = (row+2)/2-1; i < (row+2)/2-1+4; i++) {
                for (int j = (col+2)/2-1; j < (row+2)/2-1+4; j++) {
                    map[i][j]=2;
                }
            }
        }

        //return the fully generated map with rooms
        return map;
    }

    /**
     * This method generates the maze/game map
     * by using a Graph theory technique
     * called Depth First Search (namely DFS). It works
     * by applying the following logic:
     *
     * 1. looping through the four random directions.
     * 2. check if the new point is with in the maze
     *    and such that it hasn't been visited.
     * 3. Set that point to be visited and the point
     *    right before it. Since we want there to be
     *    walls.
     * 4. sleep for 0.025 second before doing the next
     *    generation, so it won't generate based on the
     *    computer's speed.
     * 5. Recursively run this method on the new point
     *    that the was just generated.
     *
     * @param r integer row
     * @param c integer column
     */
    private void dfs(int r, int c) {
        //Randomize direction
        Collections.shuffle(directions);
        Point[] randomDirections = directions.toArray(new Point[4]);

        // Examine each direction
        for (Point randomDirection : randomDirections) {
            //gets the row and column direction vector
            int rVect = randomDirection.x;
            int cVect = randomDirection.y;

            //creates the new point
            int newR = r + rVect;
            int newC = c + cVect;

            //check if the new point is in the map and that it is not visited
            if (newR >= 0 && newR < row && newC >= 0 && newC < col && maze[newR][newC] != 0) {
                //set the two points in that direction to be visited
                maze[newR][newC] = 0;
                maze[r + rVect / 2][c + cVect / 2] = 0;

                //recursively call the method on these new point
                dfs(newR, newC);
            }
        }
    }
}
