package me.markyhzhang.projectpatherserver;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This class manages all the players. It encapsulates
 * a ConcurrentHashMap to prevent ConcurrentModificationException
 * that contains all the players as value and their UUID as keys
 * to the ConcurrentHashMap
 */
public class PlayersManager {

    /**
     * The ConcurrentHashMap that stores ther player
     * as value and UUID as keys to it
     */
    private ConcurrentHashMap<UUID, Player> players;

    /**
     * Constructor for this class
     */
    PlayersManager(){
        players = new ConcurrentHashMap<>();
    }

    /**
     * Adds a player to the hashmap
     * @param player Player object
     */
    void addPlayer(Player player){
        players.put(player.getId(), player);
    }

    /**
     * Return player by their UUID
     * @param id UUID
     * @return Player object
     */
    Player getPlayer(UUID id){
        return players.get(id);
    }

    /**
     * Size of player list
     * @return integer size
     */
    public int size(){
        return players.size();
    }

    /**
     * Removes the player by their UUID
     * @param id UUID
     */
    void removePlayer(UUID id){
        players.remove(id);
    }

    /**
     * Getter for all the player UUID
     * objects in a Set
     * @return Set<UUID>
     */
    Set<UUID> getPlayers(){
        return players.keySet();
    }

    /**
     * Getter for the players Collection
     * that contains all the player objects
     * @return Collection<Player>
     */
    public Collection<Player> getPlayersList(){
        return players.values();
    }
}
