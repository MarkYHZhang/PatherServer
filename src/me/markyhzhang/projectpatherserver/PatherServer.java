package me.markyhzhang.projectpatherserver;

import me.markyhzhang.projectpatherserver.gui.StartFrame;

import java.awt.Point;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This is the main class of the server, it
 * implements runnable for the server socket
 * to accept players without blocking thread
 */
public class PatherServer implements Runnable{

    /**
     * Either the server is running or not
     */
    private boolean running = true;

    /**
     * Initialize the player manager object
     */
    private PlayersManager playersManager = new PlayersManager();

    /**
     * Declare the DataSender object
     */
    private DataSender dataSender;

    /**
     * Declare the ServerSockert object
     */
    private ServerSocket serverSocket;

    /**
     * The map of the game/maze in 2d integer array
     */
    private int[][] map;

    /**
     * The map compressed into 1D string for sending to players
     */
    private String mapStr = "";

    /**
     * Available spawning points for the player
     * wound the out-ter paths of the map
     */
    private ArrayList<Point> availableSpawningPoints = new ArrayList<>();

    /**
     * The main method of this program
     * @param args String[]
     */
    public static void main(String[] args) {
        //starts a instance of this program
        new PatherServer().init();
    }

    /**
     * Initialization method for this instance
     */
    private void init(){
        //greet the user with the start frame
        new StartFrame(this);
    }

    /**
     * This method will start the server's dataSender
     * as well as its server socket for player acceptances
     * @param port integer
     */
    public void start(int port){
        //initialize the server socket
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //set running flag to true
        running = true;
        //Runs DataSender class in another thread
        dataSender = new DataSender(this);
        new Thread(dataSender).start();
        //Runs server console in another thead
        new Thread(this).start();
    }

    /**
     * This method will generate a map for
     * this round of the game
     * @param sizeOfMap integer
     */
    public void generateMap(int sizeOfMap){
        //initializes the map generator and generates the map
        MapGenerator mapGenerator = new MapGenerator(sizeOfMap, sizeOfMap);
        map = mapGenerator.generate();

        //constructing the mapstr to be sent to players
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i==1||j==1||i==map.length-2||j==map.length-2){
                    if (map[i][j]==0){
                        availableSpawningPoints.add(new Point(i,j));
                    }
                }
                mapStr+=map[i][j];
            }
            mapStr+="|";
        }
    }

    /**
     * Getter for the PlayersManager
     * @return PlayersManager
     */
    public PlayersManager getPlayersManager() {
        return playersManager;
    }

    /**
     * Getter for the map string
     * @return String
     */
    String getMapStr() {
        return mapStr;
    }

    /**
     * Getter for the map int[][] array
     * @return int[][]
     */
    public int[][] getMap() {
        return map;
    }

    /**
     * Getter for the running boolean
     * @return boolean
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Getter for a random valid spawning point on the map
     * @return Point
     */
    Point getRandomSpawnPoint(){
        return availableSpawningPoints.get((int)(Math.random() * availableSpawningPoints.size()));
    }

    /**
     * Getter for the data sender
     * @return DataSender
     */
    DataSender getDataSender() {
        return dataSender;
    }

    /**
     * This method is triggered when this thread
     * is started. It will accept new players/clients
     * and handles them on their own ClientHandler thread
     */
    @Override
    public void run() {
        try{
            System.out.println("Server running... Accepting clients.");
            //This while loop continue accepts new clients
            while (running) {
                //get the client
                Socket client = serverSocket.accept();
                //start a ClientHandler on another thread
                new Thread(new ClientHandler(client, this)).start();
            }
        }catch (Exception e){
            System.out.println("Server closed.");
        }
    }
}
