package tetris;

import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * This is the Blocks class. It instantiates the arrays, generates squares used for making the border and the pieces,
 * generates the aforementioned border and pieces, defines methods for the movement of pieces, defines methods for the
 * clearing of lines, and checks to see if the game is lost. In addition, this class also instantiates extra squares
 * and polygons that are used to decorate the blocks. Blocks has an association with the Game class so Blocks can
 * access _gamePane, _timeline, and _keyHandler. These are used to graphically add shapes, stop the timeline, and stop
 * the KeyHandler, respectively.
 */
public class Blocks {

    private Pane _gamePane;
    private Timeline _timeline;
    private Game.KeyHandler _keyHandler;
    private Rectangle[] _blockArray;
    private Rectangle[] _blockArray2;
    private Polygon[] _accentArray;
    private Rectangle[][] _boardArray;
    private Rectangle[][] _boardArray2;
    private Polygon[][] _accentBoardArray;
    private boolean _isOBlock;

    /**
     * The Blocks constructor defines the instance variables, instantiates all of the arrays, and calls the method that
     * generates the border and the method that generates a random piece.
     */
    public Blocks(Pane gamePane, Timeline timeline, Game.KeyHandler keyHandler) {

        _gamePane = gamePane;
        _timeline = timeline;
        _keyHandler = keyHandler;
        _blockArray = new Rectangle[4];
        _blockArray2 = new Rectangle[4];
        _accentArray = new Polygon[4];
        _boardArray = new Rectangle[22][12];
        _boardArray2 = new Rectangle[22][12];
        _accentBoardArray = new Polygon[22][12];
        _isOBlock = false;
        this.generateBorder();
        this.generateRandomBlock();
    }

    /**
     * This method moves a piece left one space only if the new location of the piece is not occupied by blocks. This
     * is called in the handle() method in KeyHandler when the left arrow key is pressed. It uses the checkCollision()
     * method to check if the new location is occupied by other squares.
     */
    public void moveLeft() {

        if (this.checkCollision(-1, 0) == false) {

            for (int i = 0; i < 4; i++) {

                _blockArray[i].setX(_blockArray[i].getX() - Constants.SQUARE_WIDTH);
                _blockArray2[i].setX(_blockArray2[i].getX() - Constants.SQUARE_WIDTH);
                _accentArray[i].setLayoutX(_accentArray[i].getLayoutX() - Constants.SQUARE_WIDTH);
            }
        }
    }

    /**
     * Similarly to the moveLeft() method, this method moves a piece right one space only if the new location of the
     * piece is not already occupied by blocks. This is called in the handle() method in KeyHandler when the right
     * arrow key is pressed. It uses the checkCollision() method to check if the new location is occupied by other squares.
     */
    public void moveRight() {

        if (this.checkCollision(1, 0) == false) {

            for (int i = 0; i < 4; i++) {

                _blockArray[i].setX(_blockArray[i].getX() + Constants.SQUARE_WIDTH);
                _blockArray2[i].setX(_blockArray2[i].getX() + Constants.SQUARE_WIDTH);
                _accentArray[i].setLayoutX(_accentArray[i].getLayoutX() + Constants.SQUARE_WIDTH);
            }
        }
    }

    /**
     * Similarly to the moveLeft() and moveRight() methods, this method moves a piece down one space only if the new
     * location of the piece is not already occupied by blocks. When a piece stops falling, it is added to _boardArray.
     * This method is called in the handle() method in TimeHandler. It uses the checkCollision() method to check if the
     * new location is occupied by other squares.
     */
    public void fall() {

        boolean fallCollision = this.checkCollision(0, 1);

        for (int i = 0; i < 4; i++) {

            int x = (int) (_blockArray[i].getX()/Constants.SQUARE_WIDTH);
            int y = (int) (_blockArray[i].getY()/Constants.SQUARE_WIDTH);

            if (!fallCollision) {

                _blockArray[i].setY(_blockArray[i].getY() + Constants.SQUARE_WIDTH);
                _blockArray2[i].setY(_blockArray2[i].getY() + Constants.SQUARE_WIDTH);
                _accentArray[i].setLayoutY(_accentArray[i].getLayoutY() + Constants.SQUARE_WIDTH);
            }

            if (fallCollision) {

                _boardArray[y][x] = _blockArray[i];
                _boardArray2[y][x] = _blockArray2[i];
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
     * It checks for this using the checkCollision() method.
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

    /**
     * This method checks to see if the space a piece wants to move into is already occupied. It does this by checking
     * if the new location in the array is null or not. This method takes in two integers as arguments; one is the
     * xDisplacement, and one is yDisplacement. These two values are the differences between the coordinates the old
     * location of the piece and coordinates of the new location. It returns true if the new location is occupied and
     * returns false if not. This method is called in the moveLeft(), moveRight(), fall(), drop(), and gameOver() methods.
     */
    public boolean checkCollision(int xDisplacement, int yDisplacement) {

        for (int i = 0; i < 4; i++) {

            //the coordinates of the piece on the 22x12 board.
            int x = (int) (_blockArray[i].getX()/Constants.SQUARE_WIDTH);
            int y = (int) (_blockArray[i].getY()/Constants.SQUARE_WIDTH);

            //checks to see if the new location of the piece is already occupied
            if (_boardArray[y + yDisplacement][x + xDisplacement] != null) {

                return true;
            }
        }

        return false;
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
                double newXLocation = _blockArray[0].getX() - _blockArray[0].getY() + _blockArray[i].getY();
                double newYLocation = _blockArray[0].getY() + _blockArray[0].getX() - _blockArray[i].getX();

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

                double newXLocation = _blockArray[0].getX() - _blockArray[0].getY() + _blockArray[i].getY();
                double newYLocation = _blockArray[0].getY() + _blockArray[0].getX() - _blockArray[i].getX();

                if (!rotateCollision) {

                    _blockArray[i].setX(newXLocation);
                    _blockArray[i].setY(newYLocation);
                    _blockArray2[i].setX(newXLocation);
                    _blockArray2[i].setY(newYLocation);
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

        for (int j = 0; j < 11; j++) {

            if (_boardArray[i][j] == null) {

                return false;
            }
        }

        return true;
    }

    /**
     * This method creates a border of squares on the board using for loops, if statements, and the generateBorderBlock()
     * method.
     */
    public void generateBorder() {

        for (int i = 0; i < 22; i++) {

            for (int j = 0; j < 12; j++) {

                if (i == 0) {

                    this.generateBorderBlock(0, j, j*Constants.SQUARE_WIDTH, 0);
                }

                if (i == 21) {

                    this.generateBorderBlock(21, j, j*Constants.SQUARE_WIDTH, i*Constants.SQUARE_WIDTH);
                }

                if (j == 0) {

                    this.generateBorderBlock(i, 0, 0, i*Constants.SQUARE_WIDTH);
                }

                if (j == 11) {

                    this.generateBorderBlock(i, 11, j*Constants.SQUARE_WIDTH, i*Constants.SQUARE_WIDTH);
                }
            }
        }
    }

    /**
     * This method is ued in the generateBorder() method to create squares for the border. The method passes in array
     * indexes and square coordinates.
     */
    public void generateBorderBlock(int i, int j, int x, int y) {

        _boardArray[i][j] = this.generateSquare(x, y, Constants.SQUARE_WIDTH, Color.BLACK);
        _boardArray2[i][j] = this.generateSquare(x, y, Constants.COLOR_SQUARE_WIDTH, Color.DARKGRAY);
        _accentBoardArray[i][j] = this.generateAccentPolygon(x, y);
    }

    /**
     * This method passes in an x-coordinate, a y-coordinate, a side length, and a color and instantiates a square
     * based on these values. It also graphically adds the square. The method then returns this created square. This
     * method is used in the generateBorder() and the generateBlock() method.
     */
    public Rectangle generateSquare(int x, int y, double width, Color color) {

        Rectangle square = new Rectangle(x, y, width, width);
        square.setFill(color);
        _gamePane.getChildren().add(square);
        return square;
    }

    /**
     * This method instantiates a polygon based on coordinates defined in the Constants class. It then sets the location
     * of the polygon based on the x and y coordinates passed in as arguments. It also makes the polygon white and adds
     * it graphically. It returns this polygon. It is used in the generateBorder() and the generateBlock() method.
     * This polygon is used to decorate each square.
     */
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
     * arguments and is called in the generateRandomBlock() method. This method also calls the gameOver() method to
     * check if the game is lost.
     */
    public void generateBlock(int[][] coords, Color color) {

        for (int i = 0; i < 4; i++) {

            _blockArray[i] = this.generateSquare(Constants.MID_SCENE_WIDTH + coords[i][0],
                    Constants.SQUARE_WIDTH + coords[i][1], Constants.SQUARE_WIDTH, Color.BLACK);
            _blockArray2[i] = this.generateSquare(Constants.MID_SCENE_WIDTH + coords[i][0],
                    Constants.SQUARE_WIDTH + coords[i][1], Constants.COLOR_SQUARE_WIDTH, color);
            _accentArray[i] = this.generateAccentPolygon(Constants.MID_SCENE_WIDTH + coords[i][0],
                    Constants.SQUARE_WIDTH + coords[i][1]);
        }

        this.gameOver();
    }

    /**
     * This method uses the checkCollision() method to see if the location at which a piece generates is occupied. If
     * it is occupied, it stops the timeline, stops the KeyHandler, and adds a "game over" label. It is called in the
     * generateBlock() method.
     */
    public void gameOver() {

        if (this.checkCollision(0,0) == true) {

            _timeline.stop();
            Label gameOverLabel = new Label("game over. do better <3");
            gameOverLabel.setFont(Font.font(Constants.FONT_SIZE));
            gameOverLabel.setLayoutX(Constants.GAME_OVER_X);
            gameOverLabel.setLayoutY(Constants.LABEL_Y);
            _gamePane.getChildren().add(gameOverLabel);
            _gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
        }
    }

    /**
     * Using a random number generator, this method randomly generates a piece by calling the generateBlock() method and
     * passing in the corresponding coordinates. This is called at the beginning of the game or when a piece cannot
     * fall anymore. This method uses a factory pattern design.
     */
    public void generateRandomBlock() {

        _isOBlock = false;

        int integer = (int) (Math.random() * 7);
        switch (integer) {

            case 0:
                //generates a T block
                generateBlock(Constants.T_BLOCK_COORDS, Color.PLUM);
                break;
            case 1:
                //generates an I block
                generateBlock(Constants.I_BLOCK_COORDS, Color.PALETURQUOISE);
                break;
            case 2:
                //generates an O block
                _isOBlock = true;
                generateBlock(Constants.O_BLOCK_COORDS, Color.KHAKI);
                break;
            case 3:
                //generates a J block
                generateBlock(Constants.J_BLOCK_COORDS, Color.LIGHTSTEELBLUE);
                break;
            case 4:
                //generates an L block
                generateBlock(Constants.L_BLOCK_COORDS, Color.LIGHTSALMON);
                break;
            case 5:
                //generates an S block
                generateBlock(Constants.S_BLOCK_COORDS, Color.PALEGREEN);
                break;
            case 6:
                //generates a Z block
                generateBlock(Constants.Z_BLOCK_COORDS, Color.PINK);
                break;
        }
    }
}