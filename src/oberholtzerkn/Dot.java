
/*
 * CS 2852 - 021
 * Spring 2016
 * Lab 3 - Connect the Dots Generator Revisited
 * Name: Kyra Oberholtzer
 * Date: 3/15/2016
 */

package oberholtzerkn;

public class Dot {

    //Creates the x and y coordinate objects of the dot
    private double x, y;

    /**
     * Getter for the x coordinate of the current Dot object
     * @return the x coordinate of the current Dot object
     */
    public double getX(){
        return x;
    }

    /**
     * Getter for the y coordinate of the current Dot object
     * @return the y coordinate of the current Dot object
     */
    public double getY(){
        return y;
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
        this.x = x;
        //Sets y to the y parameter
        this.y = y;
    }
}
