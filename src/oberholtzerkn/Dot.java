
/**
 * CS 2852 - 021
 * Spring 2016
 * Lab 2 - Connect the Dots Generator
 * Name: Kyra Oberholtzer
 * Date: 3/15/2016
 */

package oberholtzerkn;

public class Dot {

    //Creates the x and y coordinate objects of the dot
    private final double X;
    private final double Y;

    /**
     * Getter for the x coordinate of the current Dot object
     * @return the x coordinate of the current Dot object
     */
    public double getX(){
        return X;
    }

    /**
     * Getter for the y coordinate of the current Dot object
     * @return the y coordinate of the current Dot object
     */
    public double getY(){
        return Y;
    }

    /**
     * Constructor for the Dot class,
     * </br>
     * Instantiates the x and y coordinates of the current Dot
     * object to the x and y parameters.
     * @param x x coordinate of the current Dot object
     * @param y y coordinate of the current Dot object
     */
    public Dot (double x, double y){
        //Sets x to the x parameter
        X = x;
        //Sets y to the y parameter
        Y = y;
    }
}
