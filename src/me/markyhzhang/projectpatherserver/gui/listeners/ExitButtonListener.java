package me.markyhzhang.projectpatherserver.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This class a exit button listener
 * that halts the program when pressed
 */
public class ExitButtonListener implements ActionListener {

    /**
     * Halt the entire server program when the
     * button is triggered
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}