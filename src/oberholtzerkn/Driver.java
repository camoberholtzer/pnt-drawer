
/*
 * CS 2852 - 021
 * Spring 2016
 * Lab 3 - Connect the Dots Generator Revisited
 * Name: Kyra Oberholtzer
 * Date: 3/15/2016
 */

package oberholtzerkn;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;

public class Driver extends JFrame {


    private int points;
    private File file;
    ReadFile newFile = new ReadFile();


    /**
     * Constructs the JFrame when called by the Driver constructor
     */
    public void init(){

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /*
        Creates the buttons, labels, and the textField
        Only instantiates the textField
        */
        JButton select, dots, lines, both, time;
        JLabel orginalPoints, finalPoints;
        JTextField finalNumPoints = new JTextField();

        //Sets the text of the first label
        orginalPoints = new JLabel("Points in original file: " + String.valueOf(points));

        //Instantiates the select button to select a file on click
        select = new JButton("Select File");
        select.addActionListener(new ActionListener() {
            /**
             * Private inner class of the select button. Waits for the button to be
             * clicked. It then Calls the JFileChooser and then sets the file object
             * to the selected file. Then sends the file to the ReadFile class and the
             * read method.
             * @param e clicking of the select button
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                Path path = null;

                //Opens the file chooser
                JFileChooser jfc = new JFileChooser();
                jfc.setAcceptAllFileFilterUsed(true);

                int returnValue = jfc.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION)
                    path = jfc.getSelectedFile().toPath();

                //Sets the file object to the selected file
                file = path.toFile();

                //Sends the selected file to the class ReadFile and into the read method
                //Sets the number of points to the number returned by the read method
                points = newFile.read(file);
                orginalPoints.setText("Points in original file: " + String.valueOf(points));
            }
        });

        //Instantiates a JPanel that will hold the select button and the originalPoints label
        JPanel panel1 = new JPanel();
        //Sets the layout of the JPanel
        panel1.setLayout(new GridLayout(2, 1));
        //Adds the buttons to the JPanel
        panel1.add(select);
        panel1.add(orginalPoints);

        //Instantiates the Dots! button to plot the desired amount of dots on click
        dots = new JButton("Dots!");
        dots.addActionListener(new ActionListener() {
            /**
             * Private inner class of the Dots! button ActionListener's actionPerformed method.
             * Waits for the button to be clicked.
             * </br>
             * Checks to see if the user input a desired amount of dots. If so the method
             * calls to plot the number of desired dots. If not the method calls to plot
             * the original pnt file.
             * @param e clicking of the Dots! button
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //User input check
                if(finalNumPoints != null) {
                    //Converts input to an int
                    int numPoints = Integer.parseInt(finalNumPoints.getText());
                    //Calls to plot desired number of dots
                    newFile.plotDots(numPoints);
                    repaint();
                }
                else{
                    //Calls to plot original number of dots
                    newFile.plotDots(points);
                    repaint();
                }
            }
        });

        //Instantiates the Lines! button to plot the desired amount of lines on click
        lines = new JButton("Lines!");
        lines.addActionListener(new ActionListener() {
            /**
             * Private inner class of the Lines! button ActionListener's actionPerformed method.
             * Waits for the button to be clicked.
             * </br>
             * Checks to see if the user input a desired amount of dots. If so the method
             * calls to plot the number of desired lines. If not the method calls to plot
             * the original pnt file.
             * @param e clicking of the Lines! button
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //User input check
                if(finalNumPoints != null) {
                    //Converts input to an int
                    int numPoints = Integer.parseInt(finalNumPoints.getText());
                    //Calls to plot desired number of lines
                    newFile.plotLines(numPoints);
                    repaint();
                }
                else{
                    //Calls to plot original number of lines
                    newFile.plotLines(points);
                    repaint();
                }
            }
        });

        /*
        Instantiates a JPanel that will hold the finalPoints label, finalNumPoints textField,
        and the Dots! and Lines! buttons
        */
        JPanel panel2 = new JPanel();
        //Sets the layout of the JPanel
        panel2.setLayout(new GridLayout(2, 2));
        //Sets the text of the finalPoints label
        finalPoints = new JLabel("Number of Dots: ");
        //Adds the components to the JFrame
        panel2.add(finalPoints);
        panel2.add(finalNumPoints);
        panel2.add(dots);
        panel2.add(lines);

        //Instantiates the Both! button to plot the desired amount of dots and lines on click
        both = new JButton("Both!");
        both.addActionListener(new ActionListener() {
            /**
             * Private inner class of the Both! button ActionListener's actionPerformed method.
             * Waits for the button to be clicked.
             * </br>
             * Checks to see if the user input a desired amount of dots. If so the method
             * calls to plot the number of desired dots and lines. If not the method calls to plot
             * the original pnt file.
             * @param e clicking of the Both! button
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //User input check
                if (finalNumPoints != null) {
                    //Converts input to an int
                    int numPoints = Integer.parseInt(finalNumPoints.getText());
                    //Calls to plot desired number of dots and lines
                    newFile.plotBoth(numPoints);
                    repaint();
                } else {
                    //Calls to plot original number of lines
                    newFile.plotBoth(points);
                    repaint();
                }
            }
        });

        //Instantiates the Both! button to plot the desired amount of dots and lines on click
        time = new JButton("Time!");
        time.addActionListener(new ActionListener() {
            /**
             * Private inner class of the Time! button ActionListener's actionPerformed method.
             * Waits for the button to be clicked.
             * </br>
             * @param e clicking of the Time! button
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numPoints = Integer.parseInt(finalNumPoints.getText());
                    newFile.getBenchmarking(numPoints);
                }catch(NumberFormatException exception){
                    JOptionPane.showMessageDialog(null, "Please input a desired number of dots.");
                }
            }
        });


        //Creates a layout to be added to the JFrame containing the Dots! and Lines! buttons
        GridLayout buttonLayout = new GridLayout(1, 2);
        //Add the buttons to the layout
        buttonLayout.addLayoutComponent("Dots!", dots);
        buttonLayout.addLayoutComponent("Lines!", lines);

        //Add the two panels and the Both! button to the JFrame
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayout(1,2));
        panel3.add(both);
        panel3.add(time);
        add(panel3, BorderLayout.SOUTH);



    }

    /**
     * Instantiates the Driver object. Calls the init method to set up
     * the JFrame. Then calls the pack method to resize the window.
     */
    public Driver (){

        init();
        pack();
    }

    /**
     * Creates an instance of the Driver class to display the GUI.
     * @param args
     */
    public static void main(String[] args){
        Driver d = new Driver();
    }
}
