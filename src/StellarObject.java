import java.util.ArrayList;

public class StellarObject {
    // initialise the classes attributes
    protected Vector position;
    protected double mass; // stored as a multiple of 10^24
    protected ArrayList<Integer> colour = new ArrayList<Integer>(3); // planet's colour
    protected double radius; // stores the radius of the planet
    protected boolean in_sim; // indicates whether or not the planet is in the simulation window

    // other methods
    public boolean check_in_sim() {
        // checks whether a planet is inside the simulation window
        if (Math.abs(this.getX()) < Constants.getSimulation_width() / 2.0 + 100
                && Math.abs(this.position.getY()) <  Constants.getSimulation_height() / 2.0 + 100) {
            this.in_sim = true;
        } else {
            this.in_sim = false;
        }

        return this.in_sim;
    }
    public void calculate_radius() {
        this.radius = Constants.getRadius_constant() * Math.pow(this.mass, (1.0/6)); // radius = k * mass ^ (1/3)
    }

    // getters
    public double getX() {
        return this.position.getX();
    }
    public double getY() { return this.position.getY(); }
    public ArrayList<Integer> getColour() { return colour; }
    public double getRadius() { return radius; }
    public Vector getPosition() {
        return position;
    }
    public double getMass() {
        return mass;
    }

    // setters
}
