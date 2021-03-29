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

public class Game {

    private Pane _gamePane;
    private Blocks _blocks;
    private KeyHandler _keyHandler;
    private Timeline _timeline;
    private boolean _pauseGame;
    private Label _pauseLabel;

    public Game(Pane gamePane) {

        _gamePane = gamePane;
        _keyHandler = new KeyHandler();
        _pauseGame = false;
        this.setUpPauseLabel();
        _gamePane.addEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
        this.setupTimeline();
        _blocks = new Blocks(_gamePane, _timeline, _keyHandler);
    }

    public void setupTimeline() {

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
        _pauseLabel.setFont(Font.font(20));
        _pauseLabel.setLayoutX(Constants.PAUSED_X);
        _pauseLabel.setLayoutY(Constants.LABEL_Y);
    }

    public class KeyHandler implements EventHandler<KeyEvent> {

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

    private class TimeHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            _blocks.fall();
            _blocks.clearLine();
        }
    }

}
