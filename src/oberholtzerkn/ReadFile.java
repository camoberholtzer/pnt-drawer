
/*
 * CS 2852 - 021
 * Spring 2016
 * Lab 3 - Connect the Dots Generator Revisited
 * Name: Kyra Oberholtzer
 * Date: 3/15/2016
 */

package oberholtzerkn;

import edu.msoe.se1010.winPlotter.WinPlotter;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Read in the file from the driver class and then
 * plots desired number of  Dots and Lines
 * when called by the buttons in the GUI
 * @author Kyra Oberholtzer
 * @version 3/24/2016
 */
public class ReadFile extends WinPlotter{

    //List of dots in the file
    private List<Dot> points = new ArrayList<Dot>();

    //List of dots in the modified file
    private List<Dot> result;

    //Time it takes for benchmarking
    private long[] delta = new long[4];
    //delta[0]=getArrayTime
    //delta[1]=getLinkedTime
    //delta[2]=ArrayIterator
    //delta[3]=LinkedIterator


    /**
     * Accepts a list of dots, original, and copies the dots into a list,
     * result, that starts out empty.  The method then uses the technique described
     * in the lab assignment to remove all but the numDesired number of dots.
     * <br />
     * If fewer than numDesired dots are found in original, then a copy of all
     * the dots in original is returned.
     * @param original The list of dots read in from the data file
     * @param result An empty list that will contain the numDesired most critical
     *        dots from the original list
     * @param numDesired The number of dots desired in the resulting list, must be
     *        at least 2
     */
    public static long getDesiredDots(List<Dot> original, List<Dot> result, int numDesired) {
        long startTime = System.nanoTime();
        //Clears the array
        result.clear();
        //Sets the results array to the original array
        result.addAll(original);
        //Runs through the result array and removes elements until it contains the correct number of dots
        while (result.size() >= numDesired) {
            //Creates new array and sets equal to the result size
            double[] criticalValues = new double[result.size()];
            /*
            Sets the first element to the critical value
            Must be called separately because it has to reference the last element
             */

            criticalValues[0] = calcPoints(result.get(result.size() - 1), result.get(0), result.get(1));
            /*
            Loop that runs through the critical value array
            Each time it calls the calcPoints method and updates
            the critical value at index i to the value returned
             */
            for (int i = 1; i < result.size() - 1; i++) {
                criticalValues[i] = calcPoints(result.get(i - 1), result.get(i), result.get(i + 1));
            }
            /*
            Sets the last element to the critical value
            Must be called separately because it has to reference the first element
             */
            criticalValues[result.size() - 1] = calcPoints(result.get(result.size() - 2), result.get(result.size() - 1),
                    result.get(0));

            //index of the smallest value
            int smallest = 0;
            //smallest critical value
            double smallestCrit = 1000;
            //Loops through the critical value array to find the smallest critical value to remove
            for (int i = 0; i < criticalValues.length - 1; i++) {
                //Checks if the current element is smaller than the current smallest value
                if(criticalValues[i] < smallestCrit){
                    //Sets the smallest value to the critical value at the index
                    smallestCrit = criticalValues[i];
                    //Sets the index of smallest value
                    smallest = i;
                }
            }
            //Removes the smallest value from the results list
            result.remove(smallest);

        }
        long stopTime = System.nanoTime();
        return (stopTime - startTime);
    }

    /**
     * Accepts a list of dots, original, and copies the dots into a list,
     * result, that starts out empty.  The method then uses the technique described
     * in the lab assignment to remove all but the numDesired number of dots.
     * <br />
     * If fewer than numDesired dots are found in original, then a copy of all
     * the dots in original is returned.
     * @param original The list of dots read in from the data file
     * @param result An empty list that will contain the numDesired most critical
     *        dots from the original list
     * @param numDesired The number of dots desired in the resulting list, must be
     *        at least 3
     * @return The number of nanoseconds required to execute
     */
    public static long getDesiredDots2(List<Dot> original, Collection<Dot> result, int numDesired){
        long startTime = System.nanoTime();
        //Clears the array
        result.clear();
        //Sets the results array to the original array
        result.addAll(original);

        //Runs through the result array and removes elements until it contains the correct number of dots
        while (result.size() >= numDesired) {
            //Creates new array and sets equal to the result size
            double[] criticalValues = new double[result.size()];
            /*
            Sets the first element to the critical value
            Must be called separately because it has to reference the last element
             */
            Iterator<Dot> iterator = result.iterator();
            Dot first = iterator.next();
            Dot previous = first;
            Dot current = iterator.next();
            Dot second = current;
            Dot next = iterator.next();
            Dot removable = null;
            for (int i = 2; i < result.size()-1; i++){
                criticalValues[i-1] = calcPoints(previous, current, next);
                previous = current;
                current = next;
                next = iterator.next();
            }
            criticalValues[0] = calcPoints(next, first, second);
            criticalValues[result.size()-1] = calcPoints(current, next, first);

            iterator = result.iterator();
            removable = iterator.next();
            //smallest critical value
            double smallestCrit = criticalValues[0];
            //Loops through the critical value array to find the smallest critical value to remove
            for (int i = 0; i < criticalValues.length - 1; i++) {
                Dot step = iterator.next();
                //Checks if the current element is smaller than the current smallest value
                if(criticalValues[i] < smallestCrit){
                    //Sets the smallest value to the critical value at the index
                    smallestCrit = criticalValues[i];
                    //Sets the object of smallest value
                    removable = step;
                }
            }
            result.remove(removable);

        }
        long stopTime = System.nanoTime();
        return (stopTime - startTime);
    }

    /**
     * Takes the file instantiated in the constructor
     * and reads it in using the Scanner
     * @return the number of lines read in from the file
     */
    public int read(File file){

        //Resets the points array
        points = new ArrayList<Dot>();
        //Counts how many lines are read in
        int count = 0;

        try(Scanner in = new Scanner(file)){
            //Checks for a next line
            while(in.hasNextLine()){
                count ++;
                //Scans in next line
                String line = in.nextLine();
                //Splits the two doubles at the comma and adds to an array
                String[] a = line.split(",");
                //Parses the strings to doubles and adds the to the x and y coordinates
                double x = Double.parseDouble(a[0]);
                double y = Double.parseDouble(a[1]);
                //Creates new dot with coordinates x and y
                points.add(new Dot(x, y));
            }
        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }

        //Plots the dots in ArrayList points
        plotDots(count);
        //returns the number of lines
        return count;
    }

    /**
     * Executes both the plotDots and plotLines methods
     * @param numPoints the number of points the user wants in the picture
     */
    public void plotBoth(int numPoints) throws IllegalArgumentException{
        //resizes the window
        this.setWindowSize(1000, 1000);
        this.setPlotBoundaries(0, 0, 1.0, 1.0);
        //erases any previous marks
        this.erase();
        //Checks if numPoints is equal to the original size of the file
        if(numPoints == points.size()) {
            //Steps through the points ArrayList and plots each point
            for (int i = 0; i < points.size(); i++) {
                double x = points.get(i).getX();
                double y = points.get(i).getY();
                this.drawPoint(x, y);
            }
        }
        //Calls rangeCheck for numPoints
        else if(rangeCheck(numPoints)) {
            //Calls getDesiredDots to remove as many dots as required by numPoints
            runTests(numPoints);
            //Steps through the new results array updated from getDesiredDots and plots each point
            for (int i = 0; i < result.size(); i++) {
                double x = result.get(i).getX();
                double y = result.get(i).getY();
                this.drawPoint(x, y);
            }
        }

        //Checks if numPoints is equal to the original size of the file
        if(numPoints == points.size()){
            //Steps through the points ArrayList and plots each line
            this.moveTo(points.get(0).getX(), points.get(0).getY());
            for(int i = 1; i < points.size(); i++) {
                double x = points.get(i).getX();
                double y = points.get(i).getY();
                this.drawTo(x, y);
                this.moveTo(x, y);
            }
            this.drawTo(points.get(0).getX(), points.get(0).getY());
        }
        //Calls rangeCheck for numPoints
        else if(rangeCheck(numPoints)){
            //Calls getDesiredDots to remove as many dots as required by numPoints
            runTests(numPoints);
            //Steps through the new results array updated from getDesiredDots and plots each line
            this.moveTo(result.get(0).getX(), result.get(0).getY());
            for(int i = 1; i < result.size(); i++) {
                double x = result.get(i).getX();
                double y = result.get(i).getY();
                this.drawTo(x, y);
            }
            this.drawTo(result.get(0).getX(), result.get(0).getY());
        }
    }

    /**
     * Called by button click event on button Dots! Takes in the number of points
     * input by the user and calls method getDesiredDots which then updates the result
     * array with the new dots and plots them using the WinPlotter drawPoint method.
     * @param numPoints number of points the user wants in the picture
     */
    public void plotDots(int numPoints) throws IllegalArgumentException{
        //resizes the window
        this.setWindowSize(1000, 1000);
        this.setPlotBoundaries(0, 0, 1.0, 1.0);
        //erases any previous marks
        this.erase();
        //Checks if numPoints is equal to the original size of the file
        if(numPoints == points.size()) {
            //Steps through the points ArrayList and plots each dot
            for (int i = 0; i < points.size(); i++) {
                double x = points.get(i).getX();
                double y = points.get(i).getY();
                this.drawPoint(x, y);
            }
        }
        //Calls rangeCheck for numPoints
        else if(rangeCheck(numPoints)) {
            //Calls getDesiredDots to remove as many dots as required by numPoints
            runTests(numPoints);
            //Steps through the new results array updated from getDesiredDots and plots each dot
            for (int i = 0; i < result.size(); i++) {
                double x = result.get(i).getX();
                double y = result.get(i).getY();
                this.drawPoint(x, y);
            }
        }
    }

    /**
     * Called by button click event on button Lines! Takes in the number of points
     * input by the user and calls method getDesiredDots which then updates the result
     * array with the new dots and plots the lines using the WinPlotter's moveTo
     * and drawTo methods.
     * @param numPoints number of points the user wants in the picture
     */
    public void plotLines(int numPoints) throws IllegalArgumentException{
        //resizes the window
        this.setWindowSize(1000, 1000);
        this.setPlotBoundaries(0, 0, 1.0, 1.0);
        //erases any previous marks
        this.erase();
        //Checks if numPoints is equal to the original size of the file
        if(numPoints == points.size()){
            //Steps through the points ArrayList and plots each point
            this.moveTo(points.get(0).getX(), points.get(0).getY());
            for(int i = 1; i < points.size(); i++) {
                double x = points.get(i).getX();
                double y = points.get(i).getY();
                this.drawTo(x, y);
            }
            this.drawTo(points.get(0).getX(), points.get(0).getY());
        }
        //Calls rangeCheck for numPoints
        else if(rangeCheck(numPoints)){
            //Calls getDesiredDots to remove as many dots as required by numPoints
            runTests(numPoints);
            //Steps through the new results array updated from getDesiredDots and plots each line
            this.moveTo(result.get(0).getX(), result.get(0).getY());
            for(int i = 1; i < result.size(); i++) {
                double x = result.get(i).getX();
                double y = result.get(i).getY();
                this.drawTo(x, y);
            }
            this.drawTo(result.get(0).getX(), result.get(0).getY());
        }
    }

    /**
     * Calls the desired point methods. Used for the time measurements
     * and the dot calculating
     * @param numPoints the number of desired dots
     */
    public void runTests(int numPoints){
        result = new ArrayList<Dot>();
        delta[0] = getDesiredDots(points, result, numPoints);
        delta[2] = getDesiredDots2(points, result, numPoints);

        result = new LinkedList<Dot>();
        delta[3] = getDesiredDots2(points, result, numPoints);
        delta[1] = getDesiredDots(points, result, numPoints);
    }

    /**
     * Returns the output of the time calculations
     * @param numPoints the number of desired dots
     */
    public void getBenchmarking(int numPoints){
        runTests(numPoints);
        String out;
        out = "Indexed ArrayList: " + benchToString(delta[0]) + "\n Indexed LinkedList: " + benchToString(delta[1]) +
                "\n Iterated ArrayList: " + benchToString(delta[2]) + "\n Iterated LinkedList: " + benchToString(delta[3]);
        JOptionPane.showMessageDialog(null, out);
    }

    /**
     * Converts the time from nanoseconds to hours
     * mintues and seconds and returns the values in the
     * time format hh:mm:ss.ssss
     * @param time for the methods to run
     * @return output string
     */
    public String benchToString(long time){
        String out;
        int hr = 00;
        int min = 00;

        String hour = "00";
        String minute = "00";
        String second;

        DecimalFormat df1 = new DecimalFormat("00");

        if (time >= 3.6e12){
            hr = (int) (time/3.6e12);
            time -= (double)hr * (3.6e12);
            hour = df1.format(hr);
        }
        if (time >= 6e10){
            min = (int) (time/6e10);
            time -= (double)min * (6e10);
            minute = df1.format(min);
        }
        DecimalFormat df2 = new DecimalFormat("00.0000");
        second = df2.format(time*1e-9);

        out = hour + ":" + minute + ":" + second;

        return out;
    }

    /**
     * Constructor, instantiates file object
     *
     */
    public ReadFile(){

    }

    /**
     * Calculates the distance between two dots
     * passed in by the calcPoints method
     * @param one the first dot
     * @param two the second dot
     * @return the distance between the two dots
     */
    public static double calculateDistance(Dot one, Dot two){
        double x1 = one.getX();
        double x2 = two.getX();
        double y1 = one.getY();
        double y2 = two.getY();

        return (Math.abs(Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2))));
    }

    /**
     * Accepts three points and uses the given critical value equation
     * //critVal2 = d12 + d23 - d13
     * to find the most crucial dots in the pnt file
     * @param one the first dot
     * @param two the second dot
     * @param three the third dot
     * @return the critical value of the dots as calculated in the above equation
     */
    public static double calcPoints(Dot one, Dot two, Dot three){
        double criticalValue = calculateDistance(one, two) + calculateDistance(two, three) -
                calculateDistance(one, three);

        return criticalValue;
    }

    /**
     * Checks to see if the number of points desired is within the range of the points
     * the pnt file. If the desired points are greater than or less than the existing
     * number of points the method throws and handles an IOException.
     * @return if the desired number of points is within the range
     */
    public Boolean rangeCheck(int numPoints) throws IllegalArgumentException{
        if (numPoints > points.size() || numPoints < 2 || points.size() < 3) {
            throw new IllegalArgumentException("Number of points desired is greater than or less than the number of points in " +
                        "the file.");
        }
        return true;
    }


}
