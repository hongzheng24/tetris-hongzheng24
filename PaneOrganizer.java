package tetris;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * The PaneOrganizer class instantiates the root pane, _gamePane, and bottomPane. It also sets up the quit button.
 */
public class PaneOrganizer {

    private BorderPane _root;
    private Pane _gamePane;

    /**
     * This constructor instantiates a BorderPane, instantiates a Pane, calls the setUpGame() method, and instantiates
     * the Game class while passing in _gamePane as an argument.
     */
    public PaneOrganizer(){

        _root = new BorderPane();
        _gamePane = new Pane();
        this.setUpGame();
        new Game(_gamePane);
    }

    /**
     * This is a helper method to help de-clutter the PaneOrganizer constructor. It instantiates the bottom pane, a
     * rectangle, and a quit button. It then graphically adds the objects, sets focus on _gamePane, and sets the location
     * of the panes within the root pane.
     */
    public void setUpGame() {

        Pane bottomPane = new Pane();

        Rectangle bottomRect = new Rectangle(Constants.SCENE_WIDTH, Constants.SQUARE_WIDTH);
        Button quitButton = new Button("Quit");
        bottomPane.getChildren().addAll(bottomRect, quitButton);
        quitButton.setOnAction(new ButtonHandler());

        _gamePane.setFocusTraversable(true);
        bottomPane.setFocusTraversable(false);
        _gamePane.requestFocus();

        _root.setCenter(_gamePane);
        _root.setBottom(bottomPane);
    }

    /**
     * This method returns the root pane and is called in the App class.
     */
    public BorderPane getRoot() {

        return _root;
    }

    /**
     * The ButtonHandler class is responsible for what happens when the quit button is pressed.
     */
    private class ButtonHandler implements EventHandler<ActionEvent> {

        /**
         * This method calls System.exit(0) and quits the game when the quit button is pressed.
         */
        @Override
        public void handle(ActionEvent event) {

            System.exit(0);
        }
    }
}