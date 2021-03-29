package tetris;

import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Blocks {

    private Pane _gamePane;
    private Timeline _timeline;
    private Game.KeyHandler _keyHandler;
    private Rectangle[] _gameArray;
    private Rectangle[][] _boardArray;
    private boolean _isOBlock;

    public Blocks(Pane gamePane, Timeline timeline, Game.KeyHandler keyHandler) {

        _gamePane = gamePane;
        _timeline = timeline;
        _keyHandler = keyHandler;
        _gameArray = new Rectangle[4];
        _boardArray = new Rectangle[22][12];
        _isOBlock = false;
        this.generateBorder();
        this.generateRandomBlock();
    }

    /**
     * This method moves a piece left one space only if the new location of the piece is not occupied by blocks. This
     * is called in the handle() method in KeyHandler when the left arrow key is pressed.
     */
    public void moveLeft() {

        if (this.checkCollision(-1, 0) == false) {

            for (int i = 0; i < 4; i++) {

                _gameArray[i].setX(_gameArray[i].getX() - Constants.SQUARE_WIDTH);
            }
        }
    }

    /**
     * Similarly to the moveLeft() method, this method moves a piece right one space only if the new location of the
     * piece is not already occupied by blocks. This is called in the handle() method in KeyHandler when the right
     * arrow key is pressed.
     */
    public void moveRight() {

        if (this.checkCollision(1, 0) == false) {

            for (int i = 0; i < 4; i++) {

                _gameArray[i].setX(_gameArray[i].getX() + Constants.SQUARE_WIDTH);
            }
        }
    }

    /**
     * Similarly to the moveLeft() and moveRight() methods, this method moves a piece down one space only if the new
     * location of the piece is not already occupied by blocks. When a piece stops falling, it is added to _boardArray.
     * This method is called in the handle() method in TimeHandler.
     */
    public void fall() {

        boolean fallCollision = this.checkCollision(0, 1);

        for (int i = 0; i < 4; i++) {

            int x = (int) (_gameArray[i].getX()/Constants.SQUARE_WIDTH);
            int y = (int) (_gameArray[i].getY()/Constants.SQUARE_WIDTH);

            if (!fallCollision) {

                _gameArray[i].setY(_gameArray[i].getY() + Constants.SQUARE_WIDTH);
            }

            if (fallCollision) {

                _boardArray[y][x] = _gameArray[i];
            }
        }

        if (fallCollision) {

            this.generateRandomBlock();
        }
    }

    /**
     * This method drops a piece until it cannot fall anymore. Using a for loop, the piece is moved down one square
     * for each iteration of the loop. However, if the new location of the piece is occupied, the loop is broken.
     */
    public void drop() {

        for (int j = 0; j < 21; j++) {

            boolean dropCollision = this.checkCollision(0, 1);

            if (dropCollision) {

                this.fall();
                break;
            }

            else if (!dropCollision) {

                this.fall();
            }
        }
    }

    public boolean checkCollision(int xDisplacement, int yDisplacement) {

        boolean collision = false;

        for (int i = 0; i < 4; i++) {

            //the coordinates of the piece on the 22x12 board.
            int x = (int) (_gameArray[i].getX()/Constants.SQUARE_WIDTH);
            int y = (int) (_gameArray[i].getY()/Constants.SQUARE_WIDTH);

            //checks to see if the new location of the piece is already occupied
            if (_boardArray[y + yDisplacement][x + xDisplacement] != null) {

                collision = true;
            }
        }

        return collision;
    }

    /**
     * This method rotates a piece 90 degrees counterclockwise only if the new location of the piece is within the
     * bounds of the board and if the new location is not already occupied by blocks. This method also only rotates a
     * piece if it is not the O-piece (square piece).
     */
    public void rotate() {

        boolean rotateCollision = false;

        if (!_isOBlock) {

            for (int i = 0; i < 4; i++) {

                //In the Constants class, the first pair of coordinates is the coordinates of the square used as the center of rotation
                double newXLocation = _gameArray[0].getX() - _gameArray[0].getY() + _gameArray[i].getY();
                double newYLocation = _gameArray[0].getY() + _gameArray[0].getX() - _gameArray[i].getX();

                //coordinates of the new location on the 22x12 board
                int x = (int) (newXLocation / Constants.SQUARE_WIDTH);
                int y = (int) (newYLocation / Constants.SQUARE_WIDTH);

                //checks to see if the new location of the piece is on the board and if it is already occupied
                if (newXLocation < 0 || newXLocation >= Constants.SCENE_WIDTH ||
                        newYLocation < 0 || newYLocation >= Constants.SCENE_HEIGHT ||
                        _boardArray[y][x] != null) {

                    rotateCollision = true;
                }
            }

            for (int i = 0; i < 4; i++) {

                double newXLocation = _gameArray[0].getX() - _gameArray[0].getY() + _gameArray[i].getY();
                double newYLocation = _gameArray[0].getY() + _gameArray[0].getX() - _gameArray[i].getX();

                if (!rotateCollision) {

                    _gameArray[i].setX(newXLocation);
                    _gameArray[i].setY(newYLocation);
                }
            }
        }
    }

    /**
     * This method clears full lines and moves rows above the cleared line down. It checks if each row is full using the
     * rowIsFull() method. If a row is full, it is logically and graphically removed, and rows above the cleared row
     * are logically and graphically moved down.
     */
    public void clearLine() {

        //checks each row for a completed line, checked top to bottom
        for (int i = 1; i < 21; i++) {

            while (this.rowIsFull(i) == true) {

                //graphically and logically removes the cleared line
                for (int j = 1; j < 11; j++) {

                    _gamePane.getChildren().remove(_boardArray[i][j]);
                }

                //graphically and logically moves down rows above the clear row
                for (int k = i; k > 1; k--) {

                    for (int l = 1; l < 11; l++) {

                        if (_boardArray[k][l] != null) {

                            _boardArray[k][l].setY(_boardArray[k][l].getY() + Constants.SQUARE_WIDTH);
                        }

                        _boardArray[k][l] = _boardArray[k - 1][l];
                    }
                }
            }
        }
    }

    /**
     * This method takes in an integer and checks to see if the corresponding row is full. The method returns the
     * rowIsFull variable. If a row is full the rowIsFull variable is true; if not, it is false.
     * When the method is called, the rowIsFull variable is true. If a single block in a row is null, the
     * rowIsFull variable becomes false. This method is used in the clearLine() method.
     */
    public boolean rowIsFull(int i) {

        boolean rowIsFull = true;

        for (int j = 0; j < 11; j++) {

            if (_boardArray[i][j] == null) {

                rowIsFull = false;
            }
        }

        return rowIsFull;
    }

    /**
     * This method creates a border of squares on the board.
     */
    public void generateBorder() {

        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 12; j++) {

                if (i == 0) {

                    _boardArray[i][j] = new Rectangle(j*Constants.SQUARE_WIDTH, 0, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
                    _boardArray[i][j].setFill(Color.GRAY);
                    _boardArray[i][j].setStroke(Color.BLACK);
                    _boardArray[i][j].setStrokeWidth(1.0);
                    _gamePane.getChildren().add(_boardArray[i][j]);
                }

                if (i == 21) {

                    _boardArray[i][j] = new Rectangle(j*Constants.SQUARE_WIDTH, i*Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
                    _boardArray[i][j].setFill(Color.GRAY);
                    _boardArray[i][j].setStroke(Color.BLACK);
                    _boardArray[i][j].setStrokeWidth(1.0);
                    _gamePane.getChildren().add(_boardArray[i][j]);
                }

                if (j == 0) {

                    _boardArray[i][j] = new Rectangle(0, i*Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
                    _boardArray[i][j].setFill(Color.GRAY);
                    _boardArray[i][j].setStroke(Color.BLACK);
                    _boardArray[i][j].setStrokeWidth(1.0);
                    _gamePane.getChildren().add(_boardArray[i][j]);
                }

                if (j == 11) {

                    _boardArray[i][j] = new Rectangle(j*Constants.SQUARE_WIDTH, i*Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
                    _boardArray[i][j].setFill(Color.GRAY);
                    _boardArray[i][j].setStroke(Color.BLACK);
                    _boardArray[i][j].setStrokeWidth(1.0);
                    _gamePane.getChildren().add(_boardArray[i][j]);
                }
            }
        }
    }

    /**
     * Using a for loop, this method creates a piece with 4 squares, adds it to _gameArray, adds it to _gamePane, and
     * sets the color, stroke color, and stroke width. This method takes in a double array of integers and a color as
     * arguments and is called in the methods that generate the 7 pieces.
     */
    public void generateBlock(int[][] coords, Color color) {

        for (int i = 0; i < 4; i++) {

            _gameArray[i] = new Rectangle(Constants.MID_SCENE_WIDTH + coords[i][0],
                    Constants.SQUARE_WIDTH + coords[i][1], Constants.SQUARE_WIDTH,
                    Constants.SQUARE_WIDTH);
            _gameArray[i].setFill(color);
            _gameArray[i].setStroke(Color.BLACK);
            _gameArray[i].setStrokeWidth(1.0);
            _gamePane.getChildren().add(_gameArray[i]);
        }

        this.gameOver();
    }

    public void gameOver() {

        boolean isGameOver = false;

        for (int i = 0; i < 4; i++) {

            //coordinates of the piece on the 22x12 board.
            int x = (int) (_gameArray[i].getX()/Constants.SQUARE_WIDTH);
            int y = (int) (_gameArray[i].getY()/Constants.SQUARE_WIDTH);

            if (_boardArray[y][x] != null) {

                isGameOver = true;
            }
        }

        if (isGameOver) {

            _timeline.stop();
            Label gameOverLabel = new Label("game over. do better <3");

            //Font font = Font.loadFont("C:Users/zheng/Fonts/PressStart2P-Regular", 45);
            //gameOverLabel.setFont(font);

            gameOverLabel.setFont(Font.font(20));

            gameOverLabel.setLayoutX(Constants.GAME_OVER_X);
            gameOverLabel.setLayoutY(Constants.LABEL_Y);
            _gamePane.getChildren().add(gameOverLabel);
            _gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
        }
    }

    /**
     * Using a random number generator, this method randomly generates a piece. This is called at the beginning of the
     * game and when a piece cannot fall anymore.
     */
    public void generateRandomBlock() {

        int integer = (int) (Math.random() * 7);
        switch (integer) {

            case 0:
                this.generateTBlock();
                break;
            case 1:
                this.generateIBlock();
                break;
            case 2:
                this.generateOBlock();
                break;
            case 3:
                this.generateJBlock();
                break;
            case 4:
                this.generateLBlock();
                break;
            case 5:
                this.generateSBlock();
                break;
            case 6:
                this.generateZBlock();
                break;
        }
    }

    /**
     * This method creates an I block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.CYAN. This is called in the generateRandomBlock() method.
     */
    public void generateIBlock() {

        _isOBlock = false;
        generateBlock(Constants.I_BLOCK_COORDS, Color.CYAN);
    }

    /**
     * This method creates a T block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.PURPLE. This is called in the generateRandomBlock() method.
     */
    public void generateTBlock() {

        _isOBlock = false;
        generateBlock(Constants.T_BLOCK_COORDS, Color.PURPLE);
    }

    /**
     * This method creates an O block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.YELLOW. This is called in the generateRandomBlock() method.
     */
    public void generateOBlock() {

        _isOBlock = true;
        generateBlock(Constants.O_BLOCK_COORDS, Color.YELLOW);
    }

    /**
     * This method creates a J block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.BLUE. This is called in the generateRandomBlock() method.
     */
    public void generateJBlock() {

        _isOBlock = false;
        generateBlock(Constants.J_BLOCK_COORDS, Color.BLUE);
    }

    /**
     * This method creates an L block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.ORANGE. This is called in the generateRandomBlock() method.
     */
    public void generateLBlock() {

        _isOBlock = false;
        generateBlock(Constants.L_BLOCK_COORDS, Color.ORANGE);
    }

    /**
     * This method creates an S block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.LIME. This is called in the generateRandomBlock() method.
     */
    public void generateSBlock() {

        _isOBlock = false;
        generateBlock(Constants.S_BLOCK_COORDS, Color.LIME);
    }

    /**
     * This method creates a Z block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.RED. This is called in the generateRandomBlock() method.
     */
    public void generateZBlock() {

        _isOBlock = false;
        generateBlock(Constants.Z_BLOCK_COORDS, Color.RED);
    }
}
