package tetris;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * The Game class sets up the timeline, defines the TimeHandler class, defines the KeyHandler class, and creates a
 * label for when the game is paused. Game has an association with the PaneOrganizer class so that Game can access
 * _gamePane.
 */
public class Game {

    private Pane _gamePane;
    private Blocks _blocks;
    private KeyHandler _keyHandler;
    private Timeline _timeline;
    private boolean _pauseGame;
    private Label _pauseLabel;

    /**
     * This constructor instantiates KeyHandler, calls the setUpPauseLabel() and the setUpTimeline()
     * methods, and instantiates the Blocks class and passes in _gamePane, _timeline, and _keyHandler.
     */
    public Game(Pane gamePane) {

        _gamePane = gamePane;
        _keyHandler = new KeyHandler();
        _pauseGame = false;
        this.setUpPauseLabel();
        _gamePane.addEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
        this.setUpTimeline();
        _blocks = new Blocks(_gamePane, _timeline, _keyHandler);
    }

    /**
     * This method instantiates KeyFrame, Timeline, and calls play() on _timeline. It sets up and starts the animation.
     */
    public void setUpTimeline() {

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), new TimeHandler());
        _timeline = new Timeline(keyFrame);
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
    }

    /**
     * This method creates the pause label and sets the location and font size. This label will be used in the pause()
     * method. It is only instantiated once; if it is instantiated in the pause method, the method will not work correctly.
     */
    public void setUpPauseLabel() {

        _pauseLabel = new Label("paused");
        _pauseLabel.setFont(Font.font(Constants.FONT_SIZE));
        _pauseLabel.setLayoutX(Constants.PAUSED_X);
        _pauseLabel.setLayoutY(Constants.LABEL_Y);
    }

    /**
     * The KeyHandler class is responsible for what happens when keys are pressed. The pause() method is defined in
     * this method so that it can be called.
     */
    public class KeyHandler implements EventHandler<KeyEvent> {

        /**
         * This method uses a switch statement to decide what will happen when a key is pressed. Using an if statement,
         * the method also stops keys other than the P key from working when the game is paused.
         */
        @Override
        public void handle(KeyEvent e) {

            KeyCode keyPressed = e.getCode();

            if (!_pauseGame) {

                switch (keyPressed) {

                    case LEFT:
                        _blocks.moveLeft();
                        break;
                    case RIGHT:
                        _blocks.moveRight();
                        break;
                    case DOWN:
                        _blocks.fall();
                        break;
                    case UP:
                        _blocks.rotate();
                        break;
                    case SPACE:
                        _blocks.drop();
                        break;
                }
            }

            if (keyPressed == KeyCode.P){

                    this.pause();
            }

            e.consume();
        }

        /**
         * Using if statements and a boolean variable, this method pauses/unpauses the game and adds/remove the pause
         * label.
         */
        public void pause() {

            if (!_pauseGame) {

                _timeline.stop();
                _gamePane.getChildren().add(_pauseLabel);
                _pauseGame = true;
            }

            else if (_pauseGame) {

                _timeline.play();
                _gamePane.getChildren().remove(_pauseLabel);
                _pauseGame = false;
            }
        }
    }

    /**
     * The TimeHandler class is responsible for the animation of the game.
     */
    private class TimeHandler implements EventHandler<ActionEvent> {

        /**
         * This method calls the fall() and clearLine() methods.
         */
        @Override
        public void handle(ActionEvent event) {

            _blocks.fall();
            _blocks.clearLine();
        }
    }

}
