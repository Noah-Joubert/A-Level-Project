import processing.core.PGraphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.String.valueOf;
import processing.core.PApplet;

public class Planet extends StellarObject{
    private Vector velocity,
            acceleration = new Vector (0, 0),
            force = new Vector(0, 0);
    private boolean selected;
    private Queue<Vector> path;
    private String name = "";

    public Planet(Vector position, Vector velocity, double mass, ArrayList<Integer> colour, Star s) {
        super();
        // Validate that the mass is greater than 0
        if (mass > 0) {
            this.mass = mass;
        } else {
            this.mass = Constants.getDefault_mass();
        }

        // validate the colour of the planet
        for (int i = 0; i < 3; i++) {
            // each component of the colour must be between 0 and 250
            if (colour.get(i) <= 255 && colour.get(i) >= 0) {
                this.colour.add(colour.get(i));
            } else {
                this.colour.add(Constants.getDefault_colour());
            }
        }

        // instantiate the planets position, velocity and force
        this.position = position;
        this.velocity = velocity;

        // calculate the planets radius
        calculate_radius();

        // provide the star with initial velocity
        initial_velocity(s);

        // instantiate the path queue
        path = new LinkedList<>();
    }

    // movement methods
    public void move() {
        velocity = velocity.add(acceleration.scale(Constants.getMotion_constant())); // update velocity
        position = position.add(velocity.scale(Constants.getTime_constant())); // update position
    }
    public void reset_acceleration() {
        // sets the planets acceleration and force to 0
        force = new Vector (0,0);
        acceleration = new Vector(0, 0);
    }
    public void add_acceleration(Vector a) {
        // change the acceleration acting on a planet
        acceleration = acceleration.add(a);
    }
    private void initial_velocity(Star s) {
        // find the chord joining the planet to the star
        Vector r = position.subtract(s.getPosition());
        Vector r_perp = r.norm().perp();

        // find the magnitude of the initial velocity
        double scalar = Math.sqrt(s.getMass()/ r.mag());
        double v_mag = scalar * Constants.getInitialVelConst();

        Vector initial_velocity = r_perp.scale(v_mag / 1);
        this.velocity = initial_velocity;
    }
    public void updatePath() {
        // add a new position
        if (in_sim) {
            path.add(position);

            // remove the oldest position if the queue is full
            if (path.size() >= Constants.getPath_size()) {
                path.remove();
            }
        } else {
            // remove the oldest position
            if (path.size() > 0) {
                path.remove();
            }
        }
    }
    public void clearPath() { path.clear(); }
    public boolean getSelected() {
        return selected;
    }
    public Vector getVelocity() { return velocity; }
    public Vector getAcceleration() { return acceleration; }
    public Queue<Vector> getPath() { return path; }
    public String getName() { return name; }
    public double get_mass(){ return mass; }
    public void setSelected(Boolean selected_in) {
        selected = selected_in;
    }
    public void setMass(double mass_in) {
        mass = mass_in;
        calculate_radius();
    }
    public void setName(String name_in) {
        this.name = name_in;
    }
    public void scaleVelocity(double scalar) {
        this.velocity = this.velocity.scale(scalar);
    }
}
