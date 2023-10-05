public class Preferences {
    // instantiate attributes
    private Boolean play_simulation;
    private Boolean show_vectors;
    private Boolean display_questions;
    private Boolean show_path;

    public void Preferences() {
        // set default values for attributes
        play_simulation = true;
        show_vectors = false;
        display_questions = false;
        show_path = true;
    }

    // getters
    public Boolean getShow_vectors() {
        return show_vectors;
    }
    public Boolean getPlay_simulation() {
        return play_simulation;
    }
    public Boolean getDisplay_questions() {
        return display_questions;
    }
    public Boolean getShow_path() { return show_path; }

    // setters
    public void setPlay_simulation(Boolean play_simulation) {
        this.play_simulation = play_simulation;
    }
    public void setShow_vectors(Boolean show_vectors) {
        this.show_vectors = show_vectors;
    }
    public void setDisplay_questions(Boolean display_questions) {
        this.display_questions = display_questions;
    }
    public void setShow_path(Boolean show_path) {
        this.show_path = show_path;
    }
}
