import static java.lang.String.valueOf;

public class Vector {
    private double x, y;

    // constructor
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // other subroutines
    public Vector add(Vector v) {
        // adds two vectors together
        double x_new = x + v.getX();
        double y_new = y + v.getY();
        return new Vector (x_new, y_new);
    }
    public Vector subtract(Vector v) {
        // subtracts one vector from another
        double x_new = x - v.getX();
        double y_new = y - v.getY();
        return new Vector(x_new, y_new);
    }
    public Vector scale(double s) {
        // scale a vector by a constant
        double x_new = x * s;
        double y_new = y * s;
        return new Vector(x_new, y_new);
    }
    public Vector norm() {
        // return the normal vector of a vector
        return this.scale(1 / this.mag());
    }
    public Vector perp() {
        // generate random integer 0 or 1
        int n = (int) (Math.random() * (2));


        // create new vector components
        double new_x = y * (-1);
        double new_y = x;

        //create the new perpendicular vector
        Vector perp_vector = new Vector(new_x, new_y);

        // return the vector in a random direction
        return perp_vector.scale(Math.pow(-1, n));
    }
    public double mag() {
        // return the magnitude of a vector
        double mag_sqr = Math.pow(x, 2) + Math.pow(y, 2);
        return Math.pow(mag_sqr, 1.0/2);
    }
    public void print() {
        System.out.println(valueOf(x) + ", " + valueOf(y));
    }

    // getters
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }

    // setters
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
}
