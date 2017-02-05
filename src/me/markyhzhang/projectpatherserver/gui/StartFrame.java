package me.markyhzhang.projectpatherserver.gui;

import me.markyhzhang.projectpatherserver.PatherServer;
import me.markyhzhang.projectpatherserver.gui.customcomponents.CustomButton;
import me.markyhzhang.projectpatherserver.gui.listeners.ExitButtonListener;
import me.markyhzhang.projectpatherserver.gui.listeners.StartButtonListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Yi Han (Mark) Zhang
 *
 * This class is the frame that greets the user
 * when the user starts the program. It will ask
 * the user for the server's port and map size.
 */
public class StartFrame extends JFrame{

    /**
     * The text field for port
     */
    private JTextField portField;

    /**
     * A JSpinner for the map size
     * 20-50
     */
    private JSpinner mapSizeSpinner;

    /**
     * used for window dragging, these are the location of the mouse
     */
    private int posX=0,posY=0;

    /**
     * The constructor for the start frame
     * @param patherServer Main class's instance
     */
    public StartFrame(PatherServer patherServer) {
        //sets the size of frame
        setSize(400, 400);
        //spawn in center
        setLocationRelativeTo(null);
        setTitle("Pather Server by Mark Zhang");
        setResizable(false);
        //border layout
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
        //transparent
        setBackground(new Color(0,0,0,0));

        //creates a JPanel
        JPanel panel = new JPanel();
        //boxed layout center Y_AXIS aligned
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //black transparent
        panel.setBackground(new Color(0,0,0,200));

        //Title of the program
        JLabel title = new JLabel("Pather Server");
        //color white
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        //initializes the port JTextField with custom colors and borders
        portField = new JTextField(" Port (between 5000~6000)");
        portField.setMaximumSize(new Dimension(235,35));
        portField.setFont(new Font("Serif", Font.BOLD, 20));
        portField.setBackground(new Color(30, 33, 35));
        portField.setBorder(null);
        portField.setForeground(Color.WHITE);
        portField.setSelectionColor(Color.ORANGE);
        portField.setCaretColor(new Color(63, 219, 255));

        //this will remove the default "Port (between 5000~6000)" text that were in the field to empty so the user can input their own
        portField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e){
                if (portField.getText().replace(" ", "").equalsIgnoreCase("Port(between5000~6000)"))
                    portField.setText("");
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (portField.getText().replace(" ", "").equalsIgnoreCase(""))
                    portField.setText(" Port (between 5000~6000)");
            }
        });

        //the size of map panel
        JPanel sizePanel = new JPanel();
        sizePanel.setMaximumSize(new Dimension(500,50));
        sizePanel.setBackground(new Color(0,0,0,0));

        //Size of map label
        JLabel sizeLabel = new JLabel("Size of map: ");
        sizeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        sizeLabel.setForeground(Color.WHITE);

        //initializes the map spinner
        mapSizeSpinner = new JSpinner();
        //set range of spinner
        mapSizeSpinner.setModel(new SpinnerNumberModel(30,20,50,1));
        //set the format
        mapSizeSpinner.setEditor(new JSpinner.NumberEditor(mapSizeSpinner,"##"));
        mapSizeSpinner.setMaximumSize(new Dimension(45,30));
        //remove border
        mapSizeSpinner.setBorder(null);
        mapSizeSpinner.setFont(new Font("Serif", Font.BOLD, 25));

        //make the field uneditable by keyboard only by the buttons
        JFormattedTextField txt = ((JSpinner.NumberEditor) mapSizeSpinner.getEditor()).getTextField();
        ((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

        //set the color of the spinner
        Component c = mapSizeSpinner.getEditor().getComponent(0);
        c.setForeground(Color.WHITE);
        c.setBackground(Color.BLACK);

        //add the spinner to the sizePanel
        sizePanel.add(sizeLabel);
        sizePanel.add(mapSizeSpinner);

        //Creates a custom start button with StartButtonListener linked
        CustomButton startButton = new CustomButton("Start", 200, 35, 30);
        startButton.addActionListener(new StartButtonListener(this, patherServer));

        //this is to prevent from auto selecting on name field
        JLabel dummyLabel = new JLabel();

        //add all of the components with spacing applied to the main panel
        panel.add(dummyLabel);
        panel.add(Box.createRigidArea(new Dimension(0,50)));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0,35)));
        panel.add(portField);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(sizePanel);
        panel.add(Box.createRigidArea(new Dimension(0,30)));
        panel.add(startButton);

        //initializes the bottom panel for credit and exit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0,0,0,0));

        //credit JLabel
        JLabel credit = new JLabel("By: Yi Han (Mark) Zhang");
        bottomPanel.add(credit, BorderLayout.LINE_START);

        //Custom exit button with ExitButtonListener linked to it
        CustomButton exitButton = new CustomButton("Exit", 40,20,15);
        exitButton.addActionListener(new ExitButtonListener());
        bottomPanel.add(exitButton, BorderLayout.LINE_END);

        //add the main panel to the center of screen
        add(panel, BorderLayout.CENTER);
        //add the bottom panel to the bottom of the screen
        add(bottomPanel, BorderLayout.PAGE_END);
        setVisible(true);

        //make the focus on to the dummy label
        dummyLabel.requestFocus();
    }

    /**
     * Getter for the size of the map from the spinner
     * @return integer
     */
    public int getMapSize(){
        return (Integer) mapSizeSpinner.getValue();
    }

    /**
     * Getter for the port of the map
     * @return String
     */
    public String getPort(){
        return portField.getText();
    }

}
