
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import controlP5.*;

import static java.lang.Thread.sleep;

public class MainClass extends PApplet {
    ArrayList<Planet> all_planets = new ArrayList<Planet>(); // an array that stores every planet in the program
    PGraphics simulation_surface; // surface containing the simulation
    PGraphics menu_surface; // surface containing the menu
    PImage starry_background; // image of the starry background
    PImage help_image, physics_image; // images for the help and physics menu
    Star central_star; // the central star of the simulation
    Planet current_planet; // the planet currently being displayed in the planet menu

    ControlP5 cp5; // used to access the ControlP5 library
    Preferences Pref = new Preferences(); // holds the users preferences
    Group main_menu; // group which contains the main_menu's widgets
    Group planet_menu; // group which contains the planet_menu's widgets
    Group physics_menu; // physics menu
    Group help_menu; // help menu
    boolean play_simulation = true,
            show_vectors = false,
            display_questions = false,
            show_path = true; // variables corresponding to toggles in the menus
    ArrayList<Integer> red = new ArrayList<Integer>(); // the colour red
    float mass_slider, initial_velocity; // the value of the mass slider, and the initial_velocity slider
    boolean placing_planet = false; // is true if the planet is being placed
    Chart displacement, velocity, acceleration; // define the charts
    Slider mass_slider_object; // the actual mass_slider object
    Slider initial_velocity_slider_object; // the actual initial_velocity_slider object
    Toggle play_simulation_toggle; // the actual toggle object
    Textfield text_box; // the actual text box object
    int gameLoop = 0; // holds how many game loops there have been
    int graphTimer = 0; // used to stop lag caused by graphs
    boolean initialising_planet = false; // is true when planets initial stats are being determined
    boolean prev_play_simulation; // holds the value play_simulation before a planet was placed

    public static void main(String[] args) {
        PApplet.main("MainClass", args); // starts the program
    }

    // these three functions are from the processing library
    public void setup() {
        starry_background = loadImage("Images//Starry_background.png"); // import the starry background

        simulation_surface = createGraphics(Constants.getSimulation_width(), Constants.getSimulation_height()); // create the simulation surface
        menu_surface = createGraphics(Constants.getMenu_width(), Constants.getMenu_height()); // create the menu surface
        central_star = new Star(new Vector(0, 0), 2 * Math.pow(10, 6)); // create the central star
        cp5 = new ControlP5(this); // used to access the controlP5 library

        main_menu = cp5.addGroup("main menu") // create the main menu
            .setPosition(Constants.getSimulation_width(), 0)
            .setWidth(Constants.getMenu_width())
            .setHeight(Constants.getMenu_height());
        planet_menu = cp5.addGroup("planet menu") // create the planet menu
            .setPosition(Constants.getSimulation_width(), 0)
            .setWidth(Constants.getMenu_width())
            .setHeight(Constants.getMenu_height())
            .hide();
        physics_menu = cp5.addGroup("physics menu") // create the physics menu
            .setPosition(Constants.getSimulation_width(), 0)
            .setWidth(Constants.getMenu_width())
            .setHeight(Constants.getMenu_width())
            .hide();
        help_menu = cp5.addGroup("help menu") // create the physics menu
            .setPosition(Constants.getSimulation_width(), 0)
            .setWidth(Constants.getMenu_width())
            .setHeight(Constants.getMenu_width())
            .hide();

        create_main_menu(); // create the widgets of the main menu
        create_planet_menu(); // create the widgets of the planet menu
        create_physics_menu(); // create the widgets of the physics menu
        create_help_menu(); // create the widgets of the help menu
        main_menu.show(); // show the main menu

        red.add(255); // create the colour red.
        red.add(0);
        red.add(0);

        // This code is used to eliminate the lag spike
        display_stellar_object(new Vector(0, 0), 0, red, false, "just some text");
    }
    public void settings() {
        size(Constants.getWindow_width(), Constants.getWindow_height()); // sets the size of the main window
    }
    public void draw() {
        update_preferences(); // update the preferences
        draw_background(); // draw the background
        display_all_objects();

        // update simulation if it isn't paused
        if (Pref.getPlay_simulation()) {
            gameLoop = (gameLoop + 1) % Constants.getPath_delay();
            update_planets(); // update the planets

            // check if the path is shown, and there has been a sufficient number of game loops.
            if (gameLoop == 0) {
                // every 5 game loops
                for (Planet planet: all_planets) {
                    if (Pref.getShow_path()) {
                        planet.updatePath();
                    } else {
                        planet.clearPath();
                    }
                }
            }
        }

        // check if mouse has been pressed
        if (mousePressed) {
            // check if the planet is being placed
            if (placing_planet) {
                // check if the cursor is inside the simulation
                if (mouseX < Constants.getSimulation_width() && mouseY < Constants.getSimulation_height()) {
                    // convert to mouses coordinates to planet's coordinates
                    Vector planetPos = new Vector(mouseX - Constants.getSimulation_width() / 2,
                                                  mouseY - Constants.getSimulation_height() / 2);

                    // create the planet
                    Planet new_planet = create_planet(planetPos, new Vector(0, 0), Constants.getDefault_mass());
                    placing_planet = false;

                    // store the simulation's previous state
                    prev_play_simulation = play_simulation_toggle.getState();

                    // pause the simulation
                    play_simulation_toggle.setValue(false);

                    initialising_planet = true; // tell the program a planet is being initialised

                    // open the planet menu
                    current_planet = new_planet;
                    current_planet.setSelected(true);
                    open_planet_menu();
                }
            } else if (!initialising_planet) {
                check_planet_clicked(mouseX, mouseY);
            }
        }

        // check if a planet is being placed
        if (placing_planet) {
            // display 'mock' planet at mouse pointer
            Vector planetPos = new Vector(mouseX - Constants.getSimulation_width() / 2,
                                          mouseY - Constants.getSimulation_height() / 2);
            display_stellar_object(planetPos, 15, red, false, "");
        }

        graphTimer ++;
        // check if the planet menu is visible
        if (planet_menu.isVisible() && graphTimer % 5 == 0) {
            update_planet_menu();
        }

        // check if the help menu is visible
        if (help_menu.isVisible()) {
            update_help_menu();
        }

        // check if the physics menu is visible
        if (physics_menu.isVisible()) {
            update_physics_menu();
        }

        image(simulation_surface, 0, 0); // render the simulation surface
        if (physics_menu.isVisible() || help_menu.isVisible()) {
            image(menu_surface, Constants.getSimulation_width() - Constants.getImage_buffer(), 0); // render the menu surface
        } else {
            image(menu_surface, Constants.getSimulation_width(), 0); // render the menu surface
        }
    }

    // one time use functions
    public void create_main_menu() {
        int number_of_buttons = 5;
        int margin = Constants.getMargin_width();
        int spacing = (Constants.getMenu_height() - 2 * margin) / (number_of_buttons + 1);

        create_button(main_menu, "add_planet", Constants.getButton_x(), spacing);
        create_button(main_menu, "physics_menu", Constants.getButton_x(), 2 * spacing);
        create_button(main_menu, "help_menu", Constants.getButton_x(),  3 * spacing);

        play_simulation_toggle = create_toggle(main_menu, "play_simulation", Constants.getToggle_x(), 4 * spacing);
        create_toggle(main_menu, "display_questions", Constants.getToggle_x(), 5 * spacing);
    }
    public void create_planet_menu() {
        int top = 10 + 50;
        int slider_height = Constants.getSlider_height();
        int margin = 3*Constants.getMargin_width();
        int graphs_top = top + margin/2 + slider_height;
        int chart_height = Constants.getChart_height();

        // create the text box
        text_box = cp5.addTextfield("name")
                        .setGroup(planet_menu)
                        .setPosition(Constants.getText_box_x(), 5)
                        .setSize(Constants.getText_box_width(), Constants.getText_box_height())
                        .setAutoClear(false);

        // create the mass_slider
        mass_slider_object = create_slider(planet_menu, "mass_slider", 15, top, 1, 500, Constants.getDefault_mass());

        // create the initial_velocity slider
        initial_velocity_slider_object = create_slider(planet_menu, "initial_velocity", 50 + Constants.getSlider_width(), top, (float)0.01, 1, 1);

        // create the close planet menu button
        create_button(planet_menu, "close_planet_menu", Constants.getButton_x(), 600);

        // create the graphs
        displacement = create_graph(planet_menu, "displacement", graphs_top + margin, -Constants.getSimulation_width() / 2, Constants.getSimulation_width() / 2);
        velocity = create_graph(planet_menu, "velocity",  graphs_top + 2 * margin + chart_height, (float) -Constants.getVelocity_range(), (float) Constants.getVelocity_range());
        acceleration = create_graph(planet_menu, "acceleration", graphs_top + 3*margin + 2*chart_height, (float) -Constants.getAcceleration_range(), (float) Constants.getAcceleration_range());
    }
    public void create_physics_menu() {
        int number_of_buttons = 3;
        int margin = Constants.getMargin_width();
        int spacing = (Constants.getMenu_height() - 2 * margin) / (number_of_buttons + 1);

        physics_image = loadImage("Images/physics_menu.png");

        create_toggle(physics_menu, "show_path", Constants.getButton_x() -  Constants.getButton_width() - 5*margin, Constants.getMenu_height() - 200);
        create_toggle(physics_menu, "show_vectors", Constants.getButton_x() + Constants.getButton_width() - 5*margin, Constants.getMenu_height() - 200);
        create_button(physics_menu, "close_physics_menu", Constants.getButton_x(), Constants.getMenu_height() - margin * 5 - Constants.getButton_height());
    }
    public void create_help_menu() {
        help_image = loadImage("Images/help_menu.jpeg");

        // create the close planet menu button
        create_button(help_menu, "close_help_menu", Constants.getButton_x(), 600);
    }

    // visual functions
    private void draw_background() {
        simulation_surface.beginDraw();
        simulation_surface.image(starry_background, 0, 0);
        simulation_surface.endDraw();

        menu_surface.beginDraw();
        menu_surface.background(150);
        menu_surface.endDraw();
        background(150);
    }
    private void display_stellar_object(Vector pos, double radius, ArrayList<Integer> colour, Boolean selected, String name) {
        // this functions displays a planet to the screen by rendering it to the 'simulation surface'
        simulation_surface.beginDraw();

        if (selected) {
            set_colour(red);
            simulation_surface.circle((float) pos.getX() + Constants.getSimulation_width() / 2,
                    (float) pos.getY() + Constants.getSimulation_height() / 2,
                    (float) radius + 5); // draw the red ring
        }
        set_colour(colour); // set the drawing colour the planets colour
        simulation_surface.circle((float) pos.getX() + Constants.getSimulation_width() / 2,
                (float) pos.getY() + Constants.getSimulation_height() / 2,
                (float) radius); // draw the planet as a circle

        simulation_surface.text(name, (float) (pos.getX() + Constants.getSimulation_width() / 2), (float) (pos.getY() + radius * 1.5 + Constants.getSimulation_height() / 2));

        simulation_surface.endDraw();
    }
    private void set_colour(ArrayList<Integer> colour) {
        // sets the colour of the simulation
        simulation_surface.stroke(colour.get(0), colour.get(1), colour.get(2));
        simulation_surface.fill(colour.get(0), colour.get(1), colour.get(2));
    }
    private void check_planet_clicked(float mouseX, float mouseY) {
        // make mouse's position into a vector and shift coordinate system
        Vector mouse_pos = new Vector(mouseX - Constants.getSimulation_width() / 2, mouseY - Constants.getSimulation_height() / 2);

        for (Planet planet : all_planets) {
            // loop through planets
            Vector r = planet.getPosition().subtract((mouse_pos)); // find vector joining planet to mouse
            double r_mag = r.mag(); // find length of that vector
            double radius = planet.getRadius(); // get planet's radius
            if (r_mag / radius < 1.3) {
                // check mouse within tolerance zone

                // deselect the current_planet
                if (!(current_planet == null)) {
                    current_planet.setSelected(false);
                }

                current_planet = planet; // select the new planet
                current_planet.setSelected(true);

                open_planet_menu(); // open the planet menu
            }
        }
    }
    private Toggle create_toggle(Group g, String toggle_var, int pos_x, int pos_y) {
        return cp5.addToggle(toggle_var) // create the button
                .setPosition(pos_x, pos_y) // set the position
                .setWidth(Constants.getToggle_width())
                .setHeight(Constants.getToggle_height()) // set the size
                .setValue(false) // set the default value
                .setMode(ControlP5.SWITCH) // required by the library
                .setColorBackground(0) // sets the background colour
                .setColorActive(-100) // sets the 'switch' colour
                .setColorCaptionLabel(0) // sets the colour of the caption
                .setGroup(g); // add the toggle to the group
    }
    private void create_button(Group g, String button_function, int pos_x, int pos_y) {
        cp5.addButton(button_function) // create the button
                .setPosition(pos_x, pos_y) // set the position
                .setSize(Constants.getButton_width(),
                        Constants.getButton_height()) // set the size
                .setColorBackground(50) // set the colour
                .setGroup(g); // set the group
    }
    private Slider create_slider(Group g, String slider_val, int pos_x, int pos_y, float low, float high, int default_val) {
        return cp5.addSlider(slider_val) // create the slider
                .setPosition(pos_x, pos_y) // set the position
                .setRange(low, high) // set range
                .setValue(default_val) // set value
                .setSize(Constants.getSlider_width(),
                        Constants.getSlider_height()) // set the size
                .setColorBackground(50) // set the colour
                .setGroup(g); // add to the group
    }
    private Chart create_graph(Group g, String name, int pos_y, float low, float high) {
        return cp5.addChart(name)
                .setPosition(Constants.getChart_x(), pos_y)
                .setSize(Constants.getChart_width(), Constants.getChart_height())
                .setRange(low, high)
                .setView(Chart.LINE)
                .setGroup(g)
                .addDataSet("x")
                .addDataSet("y")
                .setData("x", new float[Constants.getChart_size()])
                .setData("y", new float[Constants.getChart_size()]);
    }
    public void update_planet_menu() {
        // set the current planets mass to the slider value
        current_planet.setMass(mass_slider);

        // set the current planets name
        current_planet.setName(text_box.getText());

        // update the graphs if the simulation is played
        if (Pref.getPlay_simulation()) {
            update_graph(acceleration, current_planet.getAcceleration());
            update_graph(velocity, current_planet.getVelocity());
            update_graph(displacement, current_planet.getPosition());
        }
    }
    public void update_physics_menu() {
        menu_surface.beginDraw();
        menu_surface.image(physics_image, 0, 0, menu_surface.width, help_image.height);
        menu_surface.endDraw();

    }
    public void update_help_menu() {
        menu_surface.beginDraw();
        menu_surface.image(help_image, 0, 0, menu_surface.width, help_image.height);
        menu_surface.endDraw();
    }
    public void update_graph(Chart graph, Vector v) {
        graph.addFirst("x", (float) v.getX()); // add the newest x value
        graph.addFirst("y", (float) v.getY()); // add the newest y value
        graph.removeLast("x"); // remove the oldest x value
        graph.removeLast("y"); // remove the oldest y value
    }
    public void display_path(Queue<Vector> path, Vector pos) {
        simulation_surface.noFill(); // make it so the curve isn't filled in

        simulation_surface.beginDraw(); // begin drawing the curve
        simulation_surface.beginShape();

        // draw the curves through all the previous points
        for (Vector position: path) {
            // draw a curve through all the previous positions
            simulation_surface.curveVertex((float) (position.getX() + Constants.getSimulation_width() / 2), (float) (position.getY() + Constants.getSimulation_height() / 2));
        }
        // draw curve through current position
        simulation_surface.curveVertex((float) pos.getX() + Constants.getSimulation_width() / 2,
                (float) pos.getY() + Constants.getSimulation_height() / 2);

        simulation_surface.endShape();
        simulation_surface.endDraw(); // finish drawing the curve
    }

    // functions corresponding to buttons
    public void add_planet() {
        // switch the value of placing_planet
        if (placing_planet) {
            placing_planet = false;
        } else {
            placing_planet = true;
        }
    }
    public void help_menu() {
        open_help_menu();
    }
    public void physics_menu() { open_physics_menu(); }
    public void close_planet_menu() {
        main_menu.show(); // show the main menu
        planet_menu.hide(); // hide the planet menu
        current_planet.setSelected(false); // deselect the current planet

        if (initialising_planet) {
            // restart the simulation
            play_simulation_toggle.setValue(prev_play_simulation);

            // scale the current_planets initial velocity
            current_planet.scaleVelocity(initial_velocity);

            // tell the simulation that the planet has been created
            initialising_planet = false;
        }
    }
    public void open_planet_menu() {
        main_menu.hide(); // hide the main menu
        physics_menu.hide(); // hide the physics menu
        planet_menu.show(); // show the planet menu

        // reset the mass slider
        mass_slider_object.setValue((float) current_planet.getMass());

        // reset the text_box
        text_box.setText(current_planet.getName());

        // handle the initial_velocity_slider
        if (initialising_planet) {
            mass_slider_object.setPosition(15, mass_slider_object.getPosition()[1]);
            initial_velocity_slider_object.show();
            initial_velocity_slider_object.setValue(1);
        } else {
            mass_slider_object.setPosition(Constants.getSlider_x(), mass_slider_object.getPosition()[1]);
            initial_velocity_slider_object.hide();
        }

        // reset the graphs
        displacement.setData("x", new float[Constants.getChart_size()]);
        displacement.setData("y", new float[Constants.getChart_size()]);

        velocity.setData("x", new float[Constants.getChart_size()]);
        velocity.setData("y", new float[Constants.getChart_size()]);

        acceleration.setData("x", new float[Constants.getChart_size()]);
        acceleration.setData("y", new float[Constants.getChart_size()]);
    }
    public void close_physics_menu() {
        menu_surface.setSize(Constants.getMenu_width() + Constants.getImage_buffer(), Constants.getMenu_height());
        main_menu.show();
        physics_menu.hide();
    }
    public void open_physics_menu() {
        menu_surface.setSize(Constants.getMenu_width() + Constants.getImage_buffer(), Constants.getMenu_height());
        main_menu.hide();
        planet_menu.hide();
        physics_menu.show();
    }
    public void close_help_menu() {
        menu_surface.setSize(Constants.getMenu_width(), Constants.getMenu_height());
        main_menu.show();
        help_menu.hide();
    }
    public void open_help_menu() {
        menu_surface.setSize(Constants.getMenu_width() + Constants.getImage_buffer(), Constants.getMenu_height());
        main_menu.hide();
        help_menu.show();
    }

    // other functions
    private void update_planets() {
        // loop through all the planets, move and display them
        for (Planet planet: all_planets) {
            planet.move(); // move the planet
            planet.reset_acceleration(); // set its acceleration to 0

            // calculate the force between this planet and the central star
            Vector force = calculate_force(planet.getPosition(), planet.getMass(), central_star.getPosition(), central_star.getMass());
            planet.add_acceleration(force.scale(1 / planet.get_mass()));
        }

        // loop through all the planet pairs and calculate the forces
        for (int i = 0; i < all_planets.size() - 1; i++) {
            Planet p = all_planets.get(i);
            Vector pos = p.getPosition();
            double mass = p.getMass();

            for (int j = i + 1; j < all_planets.size(); j ++) {
                Planet p1 = all_planets.get(j);

                // calculate the force between the planets
                Vector force = calculate_force(pos, mass, p1.getPosition(), p1.getMass());
                p.add_acceleration(force.scale(1/p.get_mass()));
                p1.add_acceleration(force.scale(-1/p.get_mass()));
            }
        }
    }
    private void display_all_objects() {
        for (Planet planet: all_planets) {
            if (planet.check_in_sim()) {
                // display the planet if inside the simulation window
                display_stellar_object(planet.getPosition(), planet.getRadius(), planet.getColour(), planet.getSelected(), planet.getName());
                display_path(planet.getPath(), planet.getPosition());
            }
        }
        display_stellar_object(central_star.getPosition(), central_star.getRadius(), central_star.getColour(), false, ""); // display the central star
    }
    private void update_preferences() {
        Pref.setDisplay_questions(display_questions);
        Pref.setPlay_simulation(play_simulation);
        Pref.setShow_vectors(show_vectors);
        Pref.setShow_path(show_path);
    }
    private Planet create_planet(Vector position, Vector velocity, double mass) {
        // create a random colour
        ArrayList<Integer> colour = new ArrayList<Integer>();
        colour.add((int) random(0, 255));
        colour.add((int) random(0, 255));
        colour.add((int) random(0, 255));

        // create a planet and add it to the rest
        Planet new_planet = new Planet(position, velocity, mass, colour, central_star);
        all_planets.add(new_planet);

        // return the planet
        return new_planet;
    }
    private Vector calculate_force(Vector pos, double mass, Vector pos1, double mass1) {
        // find the vector r joining p to p1
        Vector r = pos1.subtract(pos);

        Vector r_hat = r.norm(); // find the unit vector in the direction r
        double r_mag = r.mag(); // find the distance between them

        double k = Constants.getCombined_grav_contsant();
        double scalar = k * mass * mass1 / (Math.pow(r_mag, 2)); // calculate the magnitude of the force

        Vector force = r_hat.scale(scalar); // create the force vector

        // add the forces to the planets. They go in opposite directions hence the minus 1 scalar
        return force;
    }
}