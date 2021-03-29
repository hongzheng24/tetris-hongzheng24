package tetris;

import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import jdk.nashorn.internal.ir.ReturnNode;
import sun.rmi.runtime.NewThreadAction;

public class Blocks {

    private Pane _gamePane;
    private Timeline _timeline;
    private Game.KeyHandler _keyHandler;
    private Rectangle[] _gameArray;
    private Rectangle[] _gameArray2;
    private Polygon[] _accentArray;
    private Rectangle[][] _boardArray;
    private Rectangle[][] _boardArray2;
    private Polygon[][] _accentBoardArray;
    private boolean _isOBlock;

    public Blocks(Pane gamePane, Timeline timeline, Game.KeyHandler keyHandler) {

        _gamePane = gamePane;
        _timeline = timeline;
        _keyHandler = keyHandler;
        _gameArray = new Rectangle[4];
        _gameArray2 = new Rectangle[4];
        _accentArray = new Polygon[4];
        _boardArray = new Rectangle[22][12];
        _boardArray2 = new Rectangle[22][12];
        _accentBoardArray = new Polygon[22][12];
        _isOBlock = false;
        this.generateBorder();
        this.generateRandomBlock();
        //this.generateIBlock();
    }

    /**
     * This method moves a piece left one space only if the new location of the piece is not occupied by blocks. This
     * is called in the handle() method in KeyHandler when the left arrow key is pressed.
     */
    public void moveLeft() {

        if (this.checkCollision(-1, 0) == false) {

            for (int i = 0; i < 4; i++) {

                _gameArray[i].setX(_gameArray[i].getX() - Constants.SQUARE_WIDTH);
                _gameArray2[i].setX(_gameArray2[i].getX() - Constants.SQUARE_WIDTH);
                _accentArray[i].setLayoutX(_accentArray[i].getLayoutX() - Constants.SQUARE_WIDTH);
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
                _gameArray2[i].setX(_gameArray2[i].getX() + Constants.SQUARE_WIDTH);
                _accentArray[i].setLayoutX(_accentArray[i].getLayoutX() + Constants.SQUARE_WIDTH);
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
                _gameArray2[i].setY(_gameArray2[i].getY() + Constants.SQUARE_WIDTH);
                _accentArray[i].setLayoutY(_accentArray[i].getLayoutY() + Constants.SQUARE_WIDTH);
            }

            if (fallCollision) {

                _boardArray[y][x] = _gameArray[i];
                _boardArray2[y][x] = _gameArray2[i];
                _accentBoardArray[y][x] = _accentArray[i];
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
                    _gameArray2[i].setX(newXLocation);
                    _gameArray2[i].setY(newYLocation);
                    _accentArray[i].setLayoutX(newXLocation);
                    _accentArray[i].setLayoutY(newYLocation);
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

                    _gamePane.getChildren().removeAll(_boardArray[i][j], _boardArray2[i][j], _accentBoardArray[i][j]);
                    _boardArray[i][j] = null;
                    _boardArray2[i][j] = null;
                    _accentBoardArray[i][j] = null;
                }

                //graphically and logically moves down rows above the clear row
                for (int k = i; k > 1; k--) {

                    for (int l = 1; l < 11; l++) {

                        if (_boardArray[k][l] != null) {

                            _boardArray[k][l].setY(_boardArray[k][l].getY() + Constants.SQUARE_WIDTH);
                            _boardArray2[k][l].setY(_boardArray2[k][l].getY() + Constants.SQUARE_WIDTH);
                            _accentBoardArray[k][l].setLayoutY(_accentBoardArray[k][l].getLayoutY() + Constants.SQUARE_WIDTH);

                        }

                        _boardArray[k][l] = _boardArray[k - 1][l];
                        _boardArray2[k][l] = _boardArray2[k - 1][l];
                        _accentBoardArray[k][l] = _accentBoardArray[k - 1][l];
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

                    _boardArray[i][j] = this.generateSquare(j*Constants.SQUARE_WIDTH, 0,
                            Constants.SQUARE_WIDTH, Color.BLACK);
                    this.generateSquare(j*Constants.SQUARE_WIDTH, 0,
                            Constants.COLOR_SQUARE_WIDTH, Color.DARKGRAY);
                    this.generateAccentPolygon(j*Constants.SQUARE_WIDTH, 0);
                }

                if (i == 21) {

                    _boardArray[i][j] = this.generateSquare(j*Constants.SQUARE_WIDTH, i*Constants.SQUARE_WIDTH,
                            Constants.SQUARE_WIDTH, Color.BLACK);
                    this.generateSquare(j*Constants.SQUARE_WIDTH, i*Constants.SQUARE_WIDTH,
                            Constants.COLOR_SQUARE_WIDTH, Color.DARKGRAY);
                    this.generateAccentPolygon(j*Constants.SQUARE_WIDTH, i*Constants.SQUARE_WIDTH);
                }

                if (j == 0) {

                    _boardArray[i][j] = this.generateSquare(0, i*Constants.SQUARE_WIDTH,
                            Constants.SQUARE_WIDTH, Color.BLACK);
                    this.generateSquare(0, i*Constants.SQUARE_WIDTH,
                            Constants.COLOR_SQUARE_WIDTH, Color.DARKGRAY);
                    this.generateAccentPolygon(0, i*Constants.SQUARE_WIDTH);
                }

                if (j == 11) {

                    _boardArray[i][j] = this.generateSquare(j*Constants.SQUARE_WIDTH, i*Constants.SQUARE_WIDTH,
                            Constants.SQUARE_WIDTH, Color.BLACK);
                    this.generateSquare(j*Constants.SQUARE_WIDTH, i*Constants.SQUARE_WIDTH,
                            Constants.COLOR_SQUARE_WIDTH, Color.DARKGRAY);
                    this.generateAccentPolygon(j*Constants.SQUARE_WIDTH, i*Constants.SQUARE_WIDTH);
                }
            }
        }
    }

    public Rectangle generateSquare(int x, int y, double width, Color color) {

        Rectangle square = new Rectangle(x, y, width, width);
        square.setFill(color);
        _gamePane.getChildren().add(square);
        return square;
    }

    public Polygon generateAccentPolygon(int x, int y) {

        Polygon polygon = new Polygon(Constants.ACCENT_POLYGON_COORDS);
        polygon.setLayoutX(x);
        polygon.setLayoutY(y);
        polygon.setFill(Color.WHITE);
        _gamePane.getChildren().add(polygon);
        return polygon;
    }

    /**
     * Using a for loop, this method creates a piece with 4 squares, adds it to _gameArray, adds it to _gamePane, and
     * sets the color, stroke color, and stroke width. This method takes in a double array of integers and a color as
     * arguments and is called in the methods that generate the 7 pieces.
     */
    public void generateBlock(int[][] coords, Color color) {

        for (int i = 0; i < 4; i++) {

            _gameArray[i] = this.generateSquare(Constants.MID_SCENE_WIDTH + coords[i][0],
                    Constants.SQUARE_WIDTH + coords[i][1], Constants.SQUARE_WIDTH, Color.BLACK);
            _gameArray2[i] = this.generateSquare(Constants.MID_SCENE_WIDTH + coords[i][0],
                    Constants.SQUARE_WIDTH + coords[i][1], Constants.COLOR_SQUARE_WIDTH, color);
            _accentArray[i] = this.generateAccentPolygon(Constants.MID_SCENE_WIDTH + coords[i][0],
                    Constants.SQUARE_WIDTH + coords[i][1]);
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
            gameOverLabel.setFont(Font.font(15));
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
        generateBlock(Constants.I_BLOCK_COORDS, Color.PALETURQUOISE);
    }

    /**
     * This method creates a T block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.PURPLE. This is called in the generateRandomBlock() method.
     */
    public void generateTBlock() {

        _isOBlock = false;
        generateBlock(Constants.T_BLOCK_COORDS, Color.PLUM);
    }

    /**
     * This method creates an O block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.YELLOW. This is called in the generateRandomBlock() method.
     */
    public void generateOBlock() {

        _isOBlock = true;
        generateBlock(Constants.O_BLOCK_COORDS, Color.KHAKI);
    }

    /**
     * This method creates a J block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.BLUE. This is called in the generateRandomBlock() method.
     */
    public void generateJBlock() {

        _isOBlock = false;
        generateBlock(Constants.J_BLOCK_COORDS, Color.LIGHTSTEELBLUE);
    }

    /**
     * This method creates an L block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.ORANGE. This is called in the generateRandomBlock() method.
     */
    public void generateLBlock() {

        _isOBlock = false;
        generateBlock(Constants.L_BLOCK_COORDS, Color.LIGHTSALMON);
    }

    /**
     * This method creates an S block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.LIME. This is called in the generateRandomBlock() method.
     */
    public void generateSBlock() {

        _isOBlock = false;
        generateBlock(Constants.S_BLOCK_COORDS, Color.PALEGREEN);
    }

    /**
     * This method creates a Z block using the generateBlock method, passing in coordinates defined in the Constants
     * class and Color.RED. This is called in the generateRandomBlock() method.
     */
    public void generateZBlock() {

        _isOBlock = false;
        generateBlock(Constants.Z_BLOCK_COORDS, Color.PINK);
    }
}
