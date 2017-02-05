package me.markyhzhang.projectpatherserver;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This class is responsible for sending
 * and broadcasting info to all clients
 * sychronized.
 */

public class DataSender implements Runnable{

    /**
     * Pather server instance
     */
    private PatherServer instance;

    /**
     * get players from the main class PatherServer
     */
    private PlayersManager players;

    /**
     * String map from pather server main class
     */
    private String mapStr;

    /**
     * initialization of uuidQueue queue
     */
    private Queue<UUID> uuidQueue = new LinkedList<UUID>();

    /**
     * initialization of new player queue
     */
    private Queue<Player> addPlayers = new LinkedList<Player>();

    /**
     * initialization of left player queue
     */
    private Queue<Player> removePlayers = new LinkedList<Player>();

    @Override
    /*
     * Implementation method for being a Runnable type
     * Runs at 200 iteration per second while run is true
     */
    public void run() {
        while (instance.isRunning()){
            try {
                sync();
                Thread.sleep(5); //200 tps (tick per second)
                //remove or add player to prevent ConcurrentModificationException
                updatePlayerList();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("stopped");
    }

    /**
     * The constructor for this class
     * @param instance PatherServer main class
     */
    DataSender(PatherServer instance){
        this.instance = instance;
        players = instance.getPlayersManager();
        mapStr = instance.getMapStr();
    }

    /**
     * This method sends/syncs information to every single online
     * players. Information include the location, status, name, id
     * and health of every single player. Also map information at
     * first packet
     */
    private void sync(){
        //Loop through every single player connected
        for(UUID id : players.getPlayers()){
            //get the player object
            Player player = players.getPlayer(id);
            String output = "";

            /*
            when the newly connected player uuidQueue queue isn't empty
            and the current player's uuidQueue matches to the first uuidQueue
            in the queue then send the map data as well as the uuidQueue
            assigned for the player to the client.
            */
            if(!uuidQueue.isEmpty()&&player.getId().equals(uuidQueue.peek())){
                Point randomLoc = instance.getRandomSpawnPoint();
                output+= uuidQueue.poll();
                output+="#";
                output+=randomLoc.x+","+randomLoc.y;
                output+="#";
                output+=mapStr;
                player.setX(randomLoc.x);
                player.setY(randomLoc.y);
                output+="#";
            }

            /*
             * Loop through every single player and update them with
             * other player's information
             *
             * Data format:
             * DIED/NORMAL|ATTACKING/NORMAL|DAMAGING/NORMAL|uuidQueue|playerName|PlayerTypeEnum|x|y|health
             *
             * % sign is used to differentiate multiple players, example:
             * NORMAL|ATTACKING|NORMAL|b039d756-3377-11e6-ac61-9e71128cae77|Mark|ALPHA|650.0|100.0|100.0%NORMAL|NORMAL|DAMAING|cf2fccce-3377-11e6-ac61-9e71128cae77|Mango|BETA|75.0|15.0|100.0
             */
            for (UUID target : players.getPlayers()) {
                //get target player object
                Player targetPlayer = players.getPlayer(target);
                String data = "";
                //add information to data accordingly
                if (!targetPlayer.isAlive()) data += "DIED|";
                else data += "NORMAL|";
                if (targetPlayer.isAttacking()) data += "ATTACKING|";
                else data += "NORMAL|";
                if (targetPlayer.isDamaging()&&targetPlayer.isAlive()) data += "DAMAGING|";
                else data += "NORMAL|";
                data += targetPlayer.getId().toString() + "|";
                data += targetPlayer.getName() + "|";
                data += targetPlayer.getType().getStr() + "|";
                data += targetPlayer.getX() + "|";
                data += targetPlayer.getY() + "|";
                data += targetPlayer.getHealth();
                //add data into the final output string
                output += data;
                output += "%";
            }
            //sends the information to player
            send(player, output);
        }
    }


    /**
     * The purpose of this is to prevent ConcurrentModificationException
     * Removes and Adds player to the players hashmap after making sure
     * nothing is modifying the hashmap
     */
    private void updatePlayerList(){
        while (!addPlayers.isEmpty()){
            Player p = addPlayers.poll();
            players.addPlayer(p);
        }
        while (!removePlayers.isEmpty()){
            players.removePlayer(removePlayers.poll().getId());
        }
    }


    /**
     * The purpose of this is to prevent ConcurrentModificationException
     * add player to addPlayers queue to be added also send the uuid
     * @param p Player
     */
    void addPlayer(Player p){
        addPlayers.add(p);
        sendUUID(p.getId());
    }

    /**
     * The purpose of this is to prevent ConcurrentModificationException
     * add player to removePlayers queue waiting to be removed
     * @param p Player
     */
    void removePlayer(Player p){
        removePlayers.add(p);
    }

    /**
     * Appends the argument uuid to the
     * uuid Queue
     * @param id UUID
     */
    private void sendUUID(UUID id){
        uuidQueue.add(id);
    }

    /**
     * Send player string information
     * @param p Player
     * @param str String
     */
    private void send(Player p, String str){
        if (p!=null)
            p.getPrintStream().println(str);
    }
}
