package me.markyhzhang.projectpatherserver.gui;

import me.markyhzhang.projectpatherserver.PatherServer;
import me.markyhzhang.projectpatherserver.PlayersManager;
import me.markyhzhang.projectpatherserver.gui.customcomponents.CustomButton;
import me.markyhzhang.projectpatherserver.gui.customcomponents.CustomPanel;
import me.markyhzhang.projectpatherserver.gui.listeners.ExitButtonListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This class will initialize and start the server
 * when the error checking of the server port is valid
 */
public class MainFrame extends JFrame implements Runnable{

    /**
     * CustomPanel instance
     */
    private CustomPanel panel;

    /**
     * Main class's instance
     */
    private PatherServer patherServer;

    /**
     * Used for window dragging, these are the location of the mouse
     */
    private int posX=0,posY=0;

    /**
     * Constructor for this MainFrame class
     * @param patherServer Main class's instance
     */
    public MainFrame(PatherServer patherServer){
        this.patherServer = patherServer;

        //gets the player manager
        PlayersManager playersManager = patherServer.getPlayersManager();

        //gets the map
        int[][] map = patherServer.getMap();

        //set the size of window depending on the map size
        setSize(map[0].length*20+200, map.length*20+25);
        //spawn in the center of screen
        setLocationRelativeTo(null);
        setTitle("ProjectPather by Mark Zhang");
        setResizable(false);
        //use border layout with 3 pixel vertical gap
        setLayout(new BorderLayout(0,3));

        //borderless and draggable
        setUndecorated(true);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                posX=e.getX();
                posY=e.getY();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent evt) {
                //sets frame position when mouse dragged
                setLocation (evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
            }
        });
        setBackground(new Color(0,0,0,0));

        //initialize the custom panel
        panel = new CustomPanel(playersManager, map);

        //Initialize the bottom panel for credit and the exit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0,0,0,0));

        //initialize the credit JLabel
        JLabel credit = new JLabel("By: Yi Han (Mark) Zhang");
        //appended it to the start of the panel
        bottomPanel.add(credit, BorderLayout.LINE_START);

        //initialized the stop button with an exitbuttonlistener
        CustomButton exitButton = new CustomButton("Stop", 40,20,15);
        exitButton.addActionListener(new ExitButtonListener());
        //ad it to the line end of this panel
        bottomPanel.add(exitButton, BorderLayout.LINE_END);

        //add the main panel to the frame at the center
        add(panel, BorderLayout.CENTER);
        //add the bottom panel to the page end of the frame
        add(bottomPanel, BorderLayout.PAGE_END);

        //make it visible
        setVisible(true);

        //start this thread
        new Thread(this).start();
    }

    /**
     * The update loop for the live game/maze
     * map overview as well as the player info
     * pane on the right hand side
     */
    @Override
    public void run() {
        //Maximum 60fps
        double delta = 1.0/60.0;
        // convert the time to seconds
        double nextTime = (double)System.nanoTime() / 1000000000.0;
        /*
         * This variable is used to ensure that the rendering and
         * the game logic doesn't go desync 0.5 second with each other
         */
        double maxTimeDiff = 0.5;

        //this variable keep track of how many frames are skipped
        int skippedFrames = 1;

        //this variable determines the maximum frames that can be skipped
        int maxSkippedFrames = 5;

        while(patherServer.isRunning()){
            //get the current time in seconds
            double currTime = (double) System.nanoTime() / 1000000000.0;

            //If the loop is fallen too much behind, render ASAP to prevent non-updating screen
            if ((currTime - nextTime) > maxTimeDiff) nextTime = currTime;

            //if we are a
            if (currTime >= nextTime) {//if the loop is behind or at the time to render

                // assign the time for the next update
                nextTime += delta;

                /*
                 * Render if the program got the game logic done early
                 * OR
                 * If the game logic is too slow that the loop is already
                 * 5 frames behind
                 */
                if ((currTime < nextTime) || (skippedFrames > maxSkippedFrames)) {
                    panel.repaint();
                    skippedFrames = 1;
                } else {
                    skippedFrames++;
                }
            } else {// if the loop is generating too fast make it sleep for the program to catch up
                // calculate the time to sleep
                int sleepTime = (int) (1000.0 * (nextTime - currTime));
                // sanity check
                if (sleepTime > 0) {
                    // sleep until the next update
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
