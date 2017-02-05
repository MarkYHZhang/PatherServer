package me.markyhzhang.projectpatherserver;

import java.io.PrintStream;
import java.util.UUID;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This is the main class of the server, it
 * implements runnable for the server socket
 * to accept players without blocking thread
 */
public class Player{

    /**
     * Enum for player type with their
     * string names initialized with constructor
     */
    public enum PlayerType {
        ALPHA("ALPHA"), BETA("BETA"), GAMMA("GAMMA"), DELTA("DELTA");
        private final String str;
        PlayerType(String str) {
            this.str = str;
        }
        public String getStr(){
            return str;
        }
    }

    /**
     * The UUID of player
     */
    private UUID id;

    /**
     * Name of player
     */
    private String name;

    /**
     * Player character type
     */
    private PlayerType type;

    /**
     * Health for this player
     */
    private double health;

    /**
     * Boolean damaging flag
     */
    private boolean isDamaging;

    /**
     * Boolean attacking flag
     */
    private boolean isAttacking;

    /**
     * Double attackDamage of this player
     */
    private double attackDamage;

    /**
     * The X coordinate of the player
     */
    private double x;

    /**
     * The Y coordinate of the player
     */
    private double y;

    /**
     * The output stream to the player used in DataSender (broadcast purposes)
     */
    private PrintStream printStream;

    /**
     * The constructor for new players
     * @param printStream PrintStream (output stream)
     * @param id UUID of player
     * @param name String name of player
     * @param type String type of player
     * @param x double x
     * @param y double y
     */
    Player(PrintStream printStream, UUID id, String name, String type, double x, double y){
        this.name = name;
        this.type = PlayerType.valueOf(type);
        this.printStream = printStream;
        this.id = id;
        health = 100;
        isDamaging = false;
        isAttacking = false;
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for damaging flag
     * @return boolean
     */
    public boolean isDamaging() {
        return isDamaging;
    }

    /**
     * Setter damaging
     * @param isDamaging boolean
     */
    void setDamaging(boolean isDamaging) {
        this.isDamaging = isDamaging;
    }

    /**
     * Getter for is attacking
     * @return boolean
     */
    public boolean isAttacking() {
        return isAttacking;
    }

    /**
     * Setter for attacking
     * @param isAttacking boolean
     */
    void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    /**
     * Getter for UUID
     * @return UUID
     */
    UUID getId() {
        return id;
    }

    /**
     * Getter for name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for player type
     * @return PlayerType
     */
    public PlayerType getType() {
        return type;
    }

    /**
     * Getter for health
     * @return double
     */
    public double getHealth() {
        return health;
    }

    /**
     * Getter for if player is still alive
     * @return boolean
     */
    public boolean isAlive(){
        return getHealth() > 0;
    }

    /**
     * The amount of damage will be dealed to this player
     * @param amt double damage
     */
    void damage(double amt){
        health-=amt;
    }

    /**
     * The X coordinate of the player
     * @return double
     */
    public double getX(){
        return x;
    }

    /**
     * Setter for the X coordinate
     * @param x double x coordinate
     */
    void setX(double x){
        this.x = x;
    }

    /**
     * Getter for the Y coordinate
     * @return double y coordinate
     */
    public double getY(){
        return y;
    }

    /**
     * Setter for the Y coordinate
     * @param y
     */
    void setY(double y){
        this.y = y;
    }

    /**
     * Getter for the PrintStream of this player
     * @return PrintStream
     */
    PrintStream getPrintStream() {
        return printStream;
    }

    /**
     * Sets the attack damage of this player
     * @param attackDamage double
     */
    void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Getter for the attack damage
     * @return double attack damage
     */
    double getAttackDamage() {
        return attackDamage;
    }
}
