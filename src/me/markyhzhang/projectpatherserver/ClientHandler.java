package me.markyhzhang.projectpatherserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This is the main class of the server, it
 * implements runnable for the server socket
 * to accept players without blocking thread
 */

class ClientHandler implements Runnable {

    //Socket for the client
    private Socket client;

    //instance of main class
    private PatherServer instance;

    //PlayersManager from main class instance
    private PlayersManager players;

    //constructor that takes in the socket for the client
    ClientHandler(Socket client, PatherServer instance){
        this.client = client;
        this.instance = instance;
        players = instance.getPlayersManager();
    }

    /**
     * Implementation method for being a Runnable type
     * This method takes in information/packets that are
     * sent by the client to the server and process them
     * into server-understandable information
     */
    @Override
    public void run() {
        try {
            //collision distance squared
            double collisionDist = 1.5*1.5;

            //Displays the Player information with its IP
            System.out.println("A Player connected from " + client.getInetAddress().getHostName());

            //Initializes a PrintStream object for the current client for sending informations to the client.
            PrintStream out = new PrintStream(client.getOutputStream());

            //Initializes a BufferedReader object for reading packets from the client.
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));

            //set the running variable as true
            boolean flag = true;

            //Declare a dummy Player object for the current Client.
            Player player = null;

            /*
            This while loop will stop when flag or the server stops
            This is the main content/function of the method.

            Player-to-Server Packet format:
            - keep live packet: KEEPLIVE (the purpose of this packet is to for the client to continue to receive
            information about other players even when the player is not moving and not attacking [idling])
            - initialization packet: init,x,y,playerName,PlayerTypeEnum
            - location update packet: update,x,y
            - attack packet: attack,x,y,damage(Double type)
             */
            while (flag && instance.isRunning()) {
                try {
                    String str = buf.readLine();
                    if (str==null) continue;
                    //Split info by (comma) into a String array
                    String[] info = str.split(",");
                    //get the packet type
                    String option = info[0];

                    if (option.equals("bye")) {
                        System.out.println("Player " + player.getName() + " left the game.");
                        //remove player from DataSender list
                        instance.getDataSender().removePlayer(player);
                        players.removePlayer(player.getId());
                        //Stop this loop
                        flag = false;
                    }

                    switch (option) {
                        case "init":
                            //create new UUID for the player
                            UUID uuid = UUID.randomUUID();
                            //get the name of player
                            String name = info[1];
                            //get the PlayerType enum type of the player
                            String character = info[2];
                            //Creates a player object for this newly joined client
                            player = new Player(out, uuid, name, character, -1, -1);

                            //Add this player to the DataSender client list
                            instance.getDataSender().addPlayer(player);
                            break;
                        case "update": {
                            //get location variable
                            double x = Double.parseDouble(info[1]);
                            double y = Double.parseDouble(info[2]);
                            //update player location
                            player.setX(x);
                            player.setY(y);
                            //set attacking state false
                            player.setAttacking(false);
                            break;
                        }
                        case "attack": {
                            //get location variable
                            double x = Double.parseDouble(info[1]);
                            double y = Double.parseDouble(info[2]);
                            //set attacking state to true
                            player.setAttacking(true);

                            //update player location
                            player.setX(x);
                            player.setY(y);

                            //read in double damage information
                            player.setAttackDamage(Double.parseDouble(info[3]));
                            break;
                        }
                    }

                    /*
                     * This block of code is handling player collision
                     * for attacking and damaging feature of the game
                     *
                     * It is set to that the collision distance to be 1.5 radius.
                     * This means that the player can be damaged or effectively attacking
                     * when they are 1.5 block away from other player
                     */
                    //if player isn't null
                    if (player != null) {
                        //if player is alive
                        if (player.isAlive()) {
                            boolean attackedFlag = false;
                            //loop through all the players
                            for (UUID id : players.getPlayers()) {
                                Player target = players.getPlayer(id);
                                //if this player is other player and is attacking and is alive
                                if (!target.getId().equals(player.getId()) && target.isAttacking() && target.isAlive()) {
                                    //get location of both player
                                    double targetX = target.getX();
                                    double targetY = target.getY();
                                    double thisX = player.getX();
                                    double thisY = player.getY();
                                    //using the circle formula see if they collide
                                    if (Math.pow((targetX - thisX), 2) + Math.pow(targetY - thisY, 2) <= collisionDist) {
                                        //set add on the current player
                                        player.setDamaging(true);
                                        player.damage(target.getAttackDamage());
                                        attackedFlag = true;
                                    }
                                }
                            }
                            if (!attackedFlag) player.setDamaging(false);
                        } else {
                            player.setDamaging(false);
                        }
                    }
                } catch (SocketException e) {
                    //If a SocketException is thrown that means this player left the game
                    if (player!=null) {
                        System.out.println("Player " + player.getName() + " left the game.");
                        //remove player from DataSender list
                        instance.getDataSender().removePlayer(player);
                        players.removePlayer(player.getId());
                    }else{
                        System.out.println("A Player failed to connect");
                    }
                    //Stop this loop
                    flag = false;
                }
            }
            //closes the input/output stream
            client.close();
        }catch (Exception e){
            /*
            if the server is still running and error is thrown
            /means something went wrong.
            */
            if (instance.isRunning()) {
                System.out.println("Wow! Wut was that");
                e.printStackTrace();
            }
        }
    }
}