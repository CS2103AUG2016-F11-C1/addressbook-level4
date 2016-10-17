package seedu.todo.controllers;

/**
 * Contains the logic required to appropriately interpret and process user input
 * from the views.
 * 
 * @author louietyj
 *
 */
public interface Controller {
    
    /**
     * Given a user input, returns a measure of confidence of whether the input
     * should be processed by the controller.
     * 
     * @param input
     *            User input
     * @return confidence A float in the range [0, 1] where 1 means extremely
     *         confident.
     */
    public float inputConfidence(String input);
    
    /**
     * Processes the user input.
     * 
     * @param input
     *            User input
     */
    public void process(String input);

}
