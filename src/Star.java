import java.util.ArrayList;

public class Star extends StellarObject {
    public Star(Vector position, double mass) {
        // instantiate the planet's mass
        this.mass = Constants.getStellar_mass();

        // create the star's orange colour.
        this.colour.add(255);
        this.colour.add(100);
        this.colour.add(0);

        // instantiate the star's position
        this.position = position;

        // calculate the star's radius
        calculate_radius();
    }
}
