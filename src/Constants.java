import java.util.ArrayList;

public class Constants {
    private static final int window_width = 1200; // the width of the main window
    private static final int window_height = 700; // the height of the main window
    private static final int menu_width = 200; // width of the menu section of the screen
    private static final int menu_height = window_height; // height of the menu section of the screen
    private static final int simulation_width = window_width - menu_width; // the width of the simulation part of the main window
    private static final int simulation_height = window_height; // the height of the simulation part of the main window

    private static final int default_mass = 50; // the default mass of a planet
    private static final int default_colour = 100; // the default colour component of a planet
    private static final int radius_constant = 8; // constant used when calculating the radius of a planet from it's mass
    private static final double mass_constant = Math.pow(10, 24); // relates simulation mass scales to astronomical mass scales
    private static final double time_constant = Math.pow(10, 5); // relates simulation time scales to astronomical time scales
    private static final double length_constant = Math.pow(10, 9); // relates simulation length scales to astronomical length scales
    private static final double motion_constant = time_constant / length_constant; // constant used in calculating the motion of a planet
    private static final double G = 6.67 * Math.pow(10, -11);
    private static final double combined_grav_contsant = G * mass_constant / (Math.pow(length_constant, 2)); // constant used in calculating the force between two planets
    private static final double initialVelConst = Math.sqrt(mass_constant * G / length_constant) / length_constant; // constant used for calculating the initial velocity of a planet
    private static final double stellar_mass = 2*Math.pow(10, 6);

    // visual constants
    private static final int margin_width = 10;
    private static final int toggle_width = 50;
    private static final int toggle_height = 50;
    private static final int toggle_x = menu_width / 2 - toggle_width / 2;
    private static final int button_width = 100;
    private static final int button_height = 50;
    private static final int button_x = menu_width / 2 - button_width / 2;
    private static final int slider_width = 50;
    private static final int slider_height = 100;
    private static final int slider_x = menu_width / 2 - slider_width / 2;
    private static final int chart_width = menu_width - margin_width;
    private static final int chart_height = 100;
    private static final int chart_x = menu_width / 2 - chart_width / 2;
    private static final int chart_size = 200;
    private static final int text_box_width = 100;
    private static final int text_box_height = 30;
    private static final int text_box_x = menu_width / 2 - text_box_width / 2;
    private static final double velocity_range = 0.0001;
    private static final double acceleration_range = 0.02;
    private static final int path_delay = 5;
    private static final int path_size = 25;

    private static final int image_buffer = 150;

    // getters
    public static int getWindow_height() {
        return window_height;
    }
    public static int getWindow_width() {
        return window_width;
    }
    public static int getSimulation_width() { return simulation_width; }
    public static int getSimulation_height() {
        return simulation_height;
    }
    public static int getMenu_width() {
        return menu_width;
    }
    public static int getMenu_height() {
        return menu_height;
    }

    public static int getRadius_constant() {
        return radius_constant;
    }
    public static int getDefault_mass() { return default_mass; }
    public static int getDefault_colour() { return default_colour; }
    public static double getMotion_constant() {
        return motion_constant;
    }
    public static double getMass_constant() {
        return mass_constant;
    }
    public static double getTime_constant() {
        return time_constant;
    }
    public static double getLength_constant() {
        return length_constant;
    }
    public static double getCombined_grav_contsant() {
        return combined_grav_contsant;
    }
    public static double getInitialVelConst() { return initialVelConst; }
    public static double getStellar_mass() { return stellar_mass; }

    public static int getMargin_width() { return margin_width; }
    public static int getToggle_width() {
        return toggle_width;
    }
    public static int getToggle_height() {
        return toggle_height;
    }
    public static int getToggle_x() { return toggle_x; }
    public static int getButton_width() {
        return button_width;
    }
    public static int getButton_height() {
        return button_height;
    }
    public static int getButton_x() { return button_x; }
    public static int getSlider_width() {
        return slider_width;
    }
    public static int getSlider_height() {
        return slider_height;
    }
    public static int getSlider_x() { return slider_x; }
    public static int getChart_height() {
        return chart_height;
    }
    public static int getChart_width() {
        return chart_width;
    }
    public static int getChart_x() {
        return chart_x;
    }
    public static int getChart_size() {
        return chart_size;
    }
    public static int getText_box_height() {
        return text_box_height;
    }
    public static int getText_box_width() {
        return text_box_width;
    }
    public static int getText_box_x() {
        return text_box_x;
    }
    public static double getVelocity_range() {
        return velocity_range;
    }
    public static double getAcceleration_range() {
        return acceleration_range;
    }
    public static int getPath_delay() {
        return path_delay;
    }
    public static int getPath_size() { return path_size; }
    public static int getImage_buffer() {
        return image_buffer;
    }
}
