package me.markyhzhang.projectpatherserver.gui.customcomponents;

import me.markyhzhang.projectpatherserver.Player;
import me.markyhzhang.projectpatherserver.PlayersManager;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This class a custom JPanel class that enables the
 * the window to have a custom map with player on it
 * with a info list to the right pane
 */
public class CustomPanel extends JPanel {

    /**
     * PlayersManager instance
     */
    private PlayersManager playersManager;

    /**
     * The game/maze map
     */
    private int[][] map;

    /**
     * The colors that represent the four
     * characters in game
     * <p>
     * They are, purple-ish pink, blue
     * yellow and green, respectively
     */
    private static final Color[] COLOR = {
            new Color(216, 113, 219),
            new Color(0, 150, 255),
            new Color(255, 251, 108),
            new Color(18, 182, 0),
    };

    /**
     * The constructor for this class
     *
     * @param playersManager PlayersManager
     * @param map            int[][]
     */
    public CustomPanel(PlayersManager playersManager, int[][] map) {
        this.playersManager = playersManager;
        this.map = map;

        //sets the background to transparent black
        setBackground(new Color(0, 0, 0, 200));
    }

    /**
     * This method overrides its parent method and adds the
     * maze map and the player info list to it
     *
     * @param g Graphic
     */
    @Override
    protected void paintComponent(Graphics g) {
        //Call to super method for all other necessary functions
        super.paintComponent(g);

        /*
         * This block of the code paints the map depending on the
         * location type there are: normal floor (while), center (yellow),
         * room (green) and black for walls. They are listed in
         * respective order below
         */
        //sets the color the white
        g.setColor(new Color(236, 240, 241, 230));
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 0) {//normal floor
                    g.fillRect(i * 20, j * 20, 20, 20);
                } else if (map[i][j] == 2) {//center
                    g.setColor(new Color(241, 196, 15, 230));
                    g.fillRect(i * 20, j * 20, 20, 20);
                    g.setColor(new Color(236, 240, 241, 230));
                } else if (map[i][j] == 1) {//room
                    g.setColor(new Color(0, 121, 88, 255));
                    g.fillRect(i * 20, j * 20, 20, 20);
                    g.setColor(new Color(236, 240, 241, 230));
                } else {//wall
                    g.setColor(new Color(0, 0, 0, 255));
                    g.fillRect(i * 20, j * 20, 20, 20);
                    g.setColor(new Color(236, 240, 241, 230));
                }
            }
        }

        /*
         * This block of code draws the player info
         * frame on the right side of the panel
         */
        g.setColor(new Color(0, 132, 255));//blue
        g.fillRect(getWidth() - 200, 0, 5, getHeight());
        g.fillRect(getWidth() - 5, 0, 5, getHeight());
        g.fillRect(getWidth() - 200, 0, 200, 5);
        g.fillRect(getWidth() - 200, getHeight() - 5, 200, 5);
        g.fillRect(getWidth() - 195, 55, 190, 3);

        //sets the color to white and font to serif and draws the currently online players
        g.setColor(new Color(248, 255, 253));
        g.setFont(new Font("Serif", Font.BOLD, 30));
        g.drawString(playersManager.size() + " Players", getWidth() - 190, 40);

        //sets the font to 15 for player list drawing
        g.setFont(new Font("Serif", Font.BOLD, 15));

        //counter of player for drawing spacing purpose
        int playerCnt = 0;

        //drawn player count so that the undisplayed counter could be calculated
        int drawnPlayerCnt = 0;

        //player and player info painting
        for (Player player : playersManager.getPlayersList()) {
            //gets the player name
            String name = player.getName();

            //calculate for the location to draw
            int initDrawX = getWidth() - 192;
            int initDrawY = 85 + playerCnt * 100;

            //transparent color initialization
            Color color = new Color(0, 0, 0, 0);
            //sets color to depending on the player type
            switch (player.getType()) {
                case ALPHA:
                    color = COLOR[0];
                    break;
                case BETA:
                    color = COLOR[1];
                    break;
                case GAMMA:
                    color = COLOR[2];
                    break;
                case DELTA:
                    color = COLOR[3];
            }

            //if the current player can be drawn completely on the screen without going off the screen
            if (initDrawY + 78 < getHeight() - 5) {
                drawnPlayerCnt++;
                //draws the player information on to the screen
                g.setColor(color);
                g.setFont(new Font("Serif", Font.BOLD, 18));
                g.drawString(name, initDrawX, initDrawY);
                g.setFont(new Font("Serif", Font.BOLD, 14));
                g.setColor(Color.YELLOW);
                g.drawString("x: " + player.getX(), initDrawX + 10, initDrawY + 20);
                g.drawString("y: " + player.getY(), initDrawX + 10, initDrawY + 35);
                //pink-ish red
                g.setColor(new Color(255, 127, 178));
                g.drawString("Health: " + ((int) player.getHealth()), initDrawX + 10, initDrawY + 50);

                //if alive then drawn his/her status
                if (player.isAlive()) {
                    //if attacking draw attacking status
                    if (player.isAttacking()) {
                        g.setColor(Color.CYAN);
                        g.drawString("ATTACKING", initDrawX + 10, initDrawY + 65);
                    }
                    //if damaging draw damaging status
                    if (player.isDamaging()) {
                        g.setColor(Color.RED);
                        g.drawString("DAMAGING", initDrawX + 100, initDrawY + 65);
                    }
                } else {
                    //if not alive then drawn the DIED status
                    g.setColor(Color.RED);
                    g.setFont(new Font("Serif", Font.BOLD, 24));
                    g.drawString(">>DIED<<", initDrawX + 60, initDrawY);
                }
                //sets the color to white and draws the player divide
                g.setColor(new Color(248, 255, 253));
                g.fillRect(initDrawX - 3, initDrawY + 75, 190, 2);
            } else {
                /*
                 * If there aren't sufficient space for more player info
                 * then draw "n more players" where n is the number of undrawn players
                 */
                g.setColor(Color.WHITE);
                g.setFont(new Font("Serif", Font.BOLD, 20));
                g.drawString((playersManager.size() - drawnPlayerCnt) + " more players...", getWidth() - 190, getHeight() - 10);
            }
            //if the player is alive then draw his/her location on the map
            if (player.isAlive()) {
                //calculate for the x location on the map after scaling factor
                int x = (int) (player.getX() * 20 - 7.5);
                int y = (int) (player.getY() * 20 - 7.5);
                //sets the color to the player's character's color
                g.setColor(color);
                //use a 7.5 radius circle to represent the player
                g.fillOval(x, y, 15, 15);
                //if player is damaging draw small red circle
                if (player.isDamaging()) {
                    g.setColor(Color.RED);
                    g.fillOval(x + 3, y + 3, 10, 10);
                }
                //if player is attacking draw medium cyan circle
                if (player.isAttacking()) {
                    g.setColor(Color.CYAN);
                    g.fillOval(x + 5, y + 5, 5, 5);
                }
            }
            //update player cnt
            playerCnt++;
        }
    }
}
