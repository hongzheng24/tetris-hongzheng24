package tetris;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class PaneOrganizer {

    private BorderPane _root;
    private Pane _gamePane;

    public PaneOrganizer(){

        _root = new BorderPane();
        _gamePane = new Pane();
        this.setUpGame();
        new Game(_gamePane);
    }

    public void setUpGame() {

        Pane bottomPane = new Pane();

        Rectangle bottomRect = new Rectangle(Constants.SCENE_WIDTH, Constants.SQUARE_WIDTH);
        Button quitButton = new Button("Quit");
        bottomPane.getChildren().addAll(bottomRect, quitButton);
        quitButton.setOnAction(new MoveHandler());

        _gamePane.setFocusTraversable(true);
        bottomPane.setFocusTraversable(false);
        _gamePane.requestFocus();

        _root.setCenter(_gamePane);
        _root.setBottom(bottomPane);
    }

    public BorderPane getRoot() {

        return _root;
    }

    private class MoveHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            System.exit(0);
        }
    }
}