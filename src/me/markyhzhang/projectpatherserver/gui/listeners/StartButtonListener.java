package me.markyhzhang.projectpatherserver.gui.listeners;

import me.markyhzhang.projectpatherserver.PatherServer;
import me.markyhzhang.projectpatherserver.gui.MainFrame;
import me.markyhzhang.projectpatherserver.gui.StartFrame;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This class will initialize and start the server
 * when the error checking of the server port is valid
 */
public class StartButtonListener implements ActionListener{

    /**
     * StartFrame instance
     */
    private StartFrame startFrame;

    /**
     * Main class instance
     */
    private PatherServer patherServer;

    /**
     * Constructor of this class
     * @param startFrame StartFrame
     * @param patherServer PatherServer instance (main class)
     */
    public StartButtonListener(StartFrame startFrame, PatherServer patherServer){
        this.startFrame = startFrame;
        this.patherServer = patherServer;
    }

    /**
     * Triggered when the start button is pressed
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //gets the port
        String portStr = startFrame.getPort();

        //declare port variable
        int port;

        //error checks for the port input
        try{
            //read in the input port
            port = Integer.parseInt(portStr);
            //check if inputted port is in valid range
            if (5000>port||port>6000){
                JOptionPane.showMessageDialog(null,"Integer with ranged from 5000 ~ 6000 only!", "ERROR", JOptionPane.ERROR_MESSAGE);
                //set port to -1 so that it will take in port input again
                port=-1;
                return;
            }
        }catch (NumberFormatException ex){
            //this executes when the inputted value isn't a valid integer
            JOptionPane.showMessageDialog(null,"Integer with ranged from 5000 ~ 6000 only!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //gets the map size
        int mapSize = startFrame.getMapSize();

        //generate the map and then start the server
        patherServer.generateMap(mapSize);
        patherServer.start(port);

        //starts the server map over view with player's info frame
        new MainFrame(patherServer);

        //close this start frame
        startFrame.dispose();

    }

}
