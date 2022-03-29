package tetris;

public class Constants {
     // TODO: fill this class in with any more constants you might need!

    // width of each square
    public static final int SQUARE_WIDTH = 25;
    public static final double COLOR_SQUARE_WIDTH = 21.875; //26.25
    public static final double ACCENT_WIDTH = 3.125; //3.75

    // coordinates for squares in each tetris piece, the first pair of coordinates is the coordinates of the square used as the center of rotation
    public static final int[][] I_BLOCK_COORDS = { {0, 2*SQUARE_WIDTH}, {0, 0}, {0, SQUARE_WIDTH}, {0, 3*SQUARE_WIDTH} };
    public static final int[][] T_BLOCK_COORDS = { {0, SQUARE_WIDTH}, {-1*SQUARE_WIDTH, 0}, {-1*SQUARE_WIDTH, SQUARE_WIDTH}, {-1*SQUARE_WIDTH, 2*SQUARE_WIDTH} };
    public static final int[][] O_BLOCK_COORDS = { {0, SQUARE_WIDTH}, {0, 0}, {-SQUARE_WIDTH, 0}, {-SQUARE_WIDTH, SQUARE_WIDTH} };
    public static final int[][] J_BLOCK_COORDS = { {-SQUARE_WIDTH, SQUARE_WIDTH}, {0, 0}, {-SQUARE_WIDTH, 0}, {-SQUARE_WIDTH, 2*SQUARE_WIDTH} };
    public static final int[][] L_BLOCK_COORDS = { {0, SQUARE_WIDTH}, {0, 0}, {-SQUARE_WIDTH, 0}, {0, 2*SQUARE_WIDTH} };
    public static final int[][] S_BLOCK_COORDS = { {0, SQUARE_WIDTH}, {-SQUARE_WIDTH, 0}, {-SQUARE_WIDTH, SQUARE_WIDTH}, {0, 2*SQUARE_WIDTH} };
    public static final int[][] Z_BLOCK_COORDS = { {0, SQUARE_WIDTH}, {0,0}, {-SQUARE_WIDTH, SQUARE_WIDTH}, {-SQUARE_WIDTH, 2*SQUARE_WIDTH} };

    // coordinates of the polygons that decorate each square
    public static final double[] ACCENT_POLYGON_COORDS = { 0, 0, ACCENT_WIDTH, 0, ACCENT_WIDTH, ACCENT_WIDTH, 3*ACCENT_WIDTH, ACCENT_WIDTH,
            3*ACCENT_WIDTH, 2*ACCENT_WIDTH, 2*ACCENT_WIDTH, 2*ACCENT_WIDTH, 2*ACCENT_WIDTH, 3*ACCENT_WIDTH, ACCENT_WIDTH, 3*ACCENT_WIDTH,
            ACCENT_WIDTH, ACCENT_WIDTH, 0, ACCENT_WIDTH};

    public static final int SCENE_WIDTH = 12 * SQUARE_WIDTH;
    public static final int SCENE_HEIGHT = 23 * SQUARE_WIDTH;
    public static final int MID_SCENE_WIDTH = SCENE_WIDTH/2;
    public static final double GAME_OVER_X = SCENE_WIDTH/4.25;
    public static final double PAUSED_X = SCENE_WIDTH/2.375;
    public static final int LABEL_Y = SCENE_HEIGHT/2 - SQUARE_WIDTH;
    public static final int FONT_SIZE = 15;
}
