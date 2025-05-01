package edu.ntnu.iir.bidata.model;

import edu.ntnu.iir.bidata.model.tileaction.GoToJailAction;
import edu.ntnu.iir.bidata.model.tileaction.JailAction;
import edu.ntnu.iir.bidata.model.tileaction.MoveAction;
import edu.ntnu.iir.bidata.model.tileaction.TileAction;
import edu.ntnu.iir.bidata.utils.RandomMath;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Factory class responsible for creating different Board configurations.
 * This class provides methods to create various types of game boards like
 * empty boards, Snakes and Ladders boards, and Monopoly boards with
 * appropriate tile configurations and styling.
 */
public class BoardFactory {

  /**
   * Creates a randomly generated Snakes and Ladders board with specified dimensions.
   * The board will contain randomly placed ladders and snakes.
   *
   * <p>This method generates up to 5 ladders and 5 snakes, placing them in valid positions
   * on the board. A valid ladder position must not be in the last row, and a valid snake
   * position must not be in the first row. The method ensures that ladder and snake positions
   * do not overlap.</p>
   *
   * @param columns Number of columns in the board (must be at least 3)
   * @param rows    Number of rows in the board (must be at least 3)
   * @return A configured board with random ladder and snake placements
   * @throws IllegalArgumentException if columns or rows are less than 3
   */
  public static Board randomSnakesAndLadders(int columns, int rows) {
    // the random generation doesn't work for columns < 3 and rows < 3
    if (columns < 3 || rows < 3) {
      throw new IllegalArgumentException("Columns and rows must be at least 3.");
    }

    List<Tile> tiles = BoardTileLayout.snakesAndLadders(columns, rows);
    Board board = new Board(tiles);

    // create a list of positions not yet used by a ladder or snake
    List<Integer> freePositions = new ArrayList<>();
    for (int i = 1; i <= columns * rows - 1; i++) {
      freePositions.add(i);
    }

    int ladderCount = 5;
    int snakeCount = 5;
    // generate ladders
    for (int i = 0; i < ladderCount; i++) {
      int start = RandomMath.randomPick(freePositions.stream().filter(
              p -> 1 < p && p < columns * (rows - 1)
      ).toList()).orElse(-1);
      int end = RandomMath.randomPick(freePositions.stream().filter(
              p -> (start - start % columns) + columns < p && p < columns * rows - 1
      ).toList()).orElse(-1);

      // if no valid positions are found, skip this ladder
      if (start == -1 || end == -1) {
        continue;
      }

      // create the ladder action
      MoveAction ladderAction = new MoveAction(start, end);
      board.getTile(start).setAction(ladderAction);

      // remove the used positions from the list
      freePositions.remove(Integer.valueOf(start));
      freePositions.remove(Integer.valueOf(end));
    }

    // snakes
    for (int i = 0; i < snakeCount; i++) {
      int start = RandomMath.randomPick(freePositions.stream().filter(
              p -> columns + 1 < p && p < columns * rows - 1
      ).toList()).orElse(-1);
      int end = RandomMath.randomPick(freePositions.stream().filter(
              p -> 0 < p && p < start - start % columns
      ).toList()).orElse(-1);

      // if no valid positions are found, skip this ladder
      if (start == -1 || end == -1) {
        continue;
      }

      // create the snake action
      MoveAction snakeAction = new MoveAction(start, end);
      board.getTile(start).setAction(snakeAction);

      // remove the used positions from the list
      freePositions.remove(Integer.valueOf(start));
      freePositions.remove(Integer.valueOf(end));
    }

    board.setName("Random board");
    colorSnakesAndLaddersBoard(board);
    return board;
  }

  /**
   * Creates a standard Snakes and Ladders board with predefined ladder and snake positions.
   *
   * <p>This method creates a 10x10 board with classic ladder and snake placements.
   * The board includes 8 ladders and 6 snakes at fixed positions that create
   * a balanced game experience. The ladders and snakes are colored appropriately
   * to distinguish them visually on the board.</p>
   *
   * @return A standard Snakes and Ladders board with fixed configuration
   */
  public static Board normalSnakesAndLadders() {
    List<Tile> tiles = BoardTileLayout.snakesAndLadders(10, 10);
    Board board = new Board(tiles);

    // ladders
    addMoveAction(board, 2, 38);
    addMoveAction(board, 4, 14);
    addMoveAction(board, 8, 30);
    addMoveAction(board, 21, 42);
    addMoveAction(board, 28, 76);
    addMoveAction(board, 50, 67);
    addMoveAction(board, 71, 92);
    addMoveAction(board, 88, 99);

    // snakes
    addMoveAction(board, 34, 6);
    addMoveAction(board, 32, 10);
    addMoveAction(board, 48, 26);
    addMoveAction(board, 62, 18);
    addMoveAction(board, 88, 24);
    addMoveAction(board, 95, 56);

    board.setName("Normal board");
    colorSnakesAndLaddersBoard(board);
    return board;
  }

  /**
   * Creates a Chinese variant of Snakes and Ladders with special rules.
   * In this variant, most tiles lead back to the starting position.
   *
   * <p>You have to roll double 6 every time to win</p>
   *
   * @return A Chinese-style Snakes and Ladders board
   */
  public static Board chineseSnakesAndLadders() {
    int columns = 20;
    int rows = 20;
    List<Tile> tiles = BoardTileLayout.snakesAndLadders(columns, rows);
    Board board = new Board(tiles);

    // snakes
    for (int i = 1; i < columns * rows; i++) {
      if ((i - 1) % 12 == 0) {
        continue;
      }
      addMoveAction(board, i, 1);
    }

    board.setName("Chinese board");
    colorSnakesAndLaddersBoard(board);
    return board;
  }

  /**
   * Helper method to add move actions to a board's tiles.
   *
   * <p>This utility method creates a MoveAction with the specified start and end positions
   * and attaches it to the appropriate tile on the board. It's used for creating
   * both ladder actions (where end > start) and snake actions (where end < start).</p>
   *
   * @param board The board to add the move action to
   * @param start The starting tile position
   * @param end   The ending tile position
   */
  private static void addMoveAction(Board board, int start, int end) {
    MoveAction action = new MoveAction(start, end);
    board.getTile(start).setAction(action);
  }

  /**
   * Applies color styling to a Snakes and Ladders board.
   * Ladders are colored green and snakes are colored red.
   *
   * <p>This method iterates through all tiles on the board and applies
   * appropriate color styling based on the tile's action:
   * <ul>
   *   <li>Ladders (actions where end > start) are colored green (#228B22)</li>
   *   <li>Snakes (actions where end < start) are colored red (#ec4a27)</li>
   * </ul>
   * </p>
   *
   * @param board The board to apply coloring to
   */
  private static void colorSnakesAndLaddersBoard(Board board) {
    // color the snakes and ladders
    for (Tile tile : board.getTiles()) {
      if (tile.getAction() instanceof MoveAction action) {
        if (action.getStart() < action.getEnd()) {
          tile.getStyling().setColor("#228B22");
        } else {
          tile.getStyling().setColor("#ec4a27");
        }
      }
    }
  }

  /**
   * Creates a standard Monopoly board with all the classic tiles and styling.
   *
   * <p>This method configures a complete Monopoly board with:
   * <ul>
   *   <li>Corner tiles (GO, Jail, Free Parking, Go to Jail)</li>
   *   <li>Property tiles with appropriate styling and rotation</li>
   *   <li>Utility tiles (Electric Company, Water Works)</li>
   *   <li>Railroad tiles</li>
   *   <li>Community Chest and Chance tiles</li>
   *   <li>Tax tiles</li>
   * </ul>
   * </p>
   *
   * <p>Each tile is configured with appropriate image paths and rotations based on
   * its position on the board. Special action tiles like Jail and Go to Jail
   * are also assigned their appropriate actions.</p>
   *
   * @return A fully configured standard Monopoly board
   */
  public static Board standardMonopoly() {
    List<Tile> tiles = BoardTileLayout.monopoly();
    Board board = new Board(tiles);
    board.setName("Monopoly board");

    // Define tile styling configurations in a concise format
    List<Map<String, Object>> stylingConfigs = List.of(
            // corner tiles
            Map.of(
                    "tileIds", List.of(0),
                    "imagePath", "images/games/monopoly/tiles/start.png",
                    "rotations", List.of(270.0)
            ),
            Map.of(
                    "tileIds", List.of(10),
                    "imagePath", "images/games/monopoly/tiles/just_visiting.png",
                    "rotations", List.of(0.0),
                    "special_tile", new JailAction()
            ),
            Map.of(
                    "tileIds", List.of(20),
                    "imagePath", "images/games/monopoly/tiles/parking.png",
                    "rotations", List.of(90.0)
            ),
            Map.of(
                    "tileIds", List.of(30),
                    "imagePath", "images/games/monopoly/tiles/go_to_jail.png",
                    "rotations", List.of(180.0),
                    "special_tile", new GoToJailAction()
            ),
            // non property tiles
            Map.of(
                    "tileIds", List.of(2, 17, 33),
                    "imagePath", "images/games/monopoly/tiles/community_chest.png",
                    "rotations", List.of(0.0, 90.0, 270.0)
            ),
            Map.of(
                    "tileIds", List.of(7, 22, 36),
                    "imagePath", "images/games/monopoly/tiles/chance.png",
                    "rotations", List.of(0.0, 180.0, 270.0)
            ),
            Map.of(
                    "tileIds", List.of(4),
                    "imagePath", "images/games/monopoly/tiles/income tax.png",
                    "rotations", List.of(0.0)
            ),
            Map.of(
                    "tileIds", List.of(38),
                    "imagePath", "images/games/monopoly/tiles/luxury tax.png",
                    "rotations", List.of(270.0)
            ),
            Map.of(
                    "tileIds", List.of(5, 15, 25, 35),
                    "imagePath", "images/games/monopoly/tiles/reading railroad.png",
                    "rotations", List.of(0.0, 90.0, 180.0, 270.0)
            ),
            Map.of(
                    "tileIds", List.of(12),
                    "imagePath", "images/games/monopoly/tiles/electric company.png",
                    "rotations", List.of(90.0)
            ),
            Map.of(
                    "tileIds", List.of(28),
                    "imagePath", "images/games/monopoly/tiles/water works.png",
                    "rotations", List.of(180.0)
            ),
            // property tiles
            Map.of(
                    "tileIds", List.of(1),
                    "imagePath", "images/games/monopoly/tiles/med avenue.png",
                    "rotations", List.of(0.0)
            ),
            Map.of(
                    "tileIds", List.of(3),
                    "imagePath", "images/games/monopoly/tiles/baltic avnue.png",
                    "rotations", List.of(0.0)
            ),
            Map.of(
                    "tileIds", List.of(6),
                    "imagePath", "images/games/monopoly/tiles/oriental avenue.png",
                    "rotations", List.of(0.0)
            ),
            Map.of(
                    "tileIds", List.of(8),
                    "imagePath", "images/games/monopoly/tiles/vermont avenue.png",
                    "rotations", List.of(0.0)
            ),
            Map.of(
                    "tileIds", List.of(9),
                    "imagePath", "images/games/monopoly/tiles/connecticut avenue.png",
                    "rotations", List.of(0.0)
            ),
            Map.of(
                    "tileIds", List.of(11),
                    "imagePath", "images/games/monopoly/tiles/charles place.png",
                    "rotations", List.of(90.0)
            ),
            Map.of(
                    "tileIds", List.of(13),
                    "imagePath", "images/games/monopoly/tiles/states avenue.png",
                    "rotations", List.of(90.0)
            ),
            Map.of(
                    "tileIds", List.of(14),
                    "imagePath", "images/games/monopoly/tiles/virginia avenue.png",
                    "rotations", List.of(90.0)
            ),
            Map.of(
                    "tileIds", List.of(16),
                    "imagePath", "images/games/monopoly/tiles/james places.png",
                    "rotations", List.of(90.0)
            ),
            Map.of(
                    "tileIds", List.of(18),
                    "imagePath", "images/games/monopoly/tiles/tenesee avenue.png",
                    "rotations", List.of(90.0)
            ),
            Map.of(
                    "tileIds", List.of(19),
                    "imagePath", "images/games/monopoly/tiles/new york avenue.png",
                    "rotations", List.of(90.0)
            ),
            Map.of(
                    "tileIds", List.of(21),
                    "imagePath", "images/games/monopoly/tiles/kentucky avenue.png",
                    "rotations", List.of(180.0)
            ),
            Map.of(
                    "tileIds", List.of(23),
                    "imagePath", "images/games/monopoly/tiles/indiana avenue.png",
                    "rotations", List.of(180.0)
            ),
            Map.of(
                    "tileIds", List.of(24),
                    "imagePath", "images/games/monopoly/tiles/illinois avenue.png",
                    "rotations", List.of(180.0)
            ),
            Map.of(
                    "tileIds", List.of(26),
                    "imagePath", "images/games/monopoly/tiles/atlantic avenue.png",
                    "rotations", List.of(180.0)
            ),
            Map.of(
                    "tileIds", List.of(27),
                    "imagePath", "images/games/monopoly/tiles/ventnor avenue.png",
                    "rotations", List.of(180.0)
            ),
            Map.of(
                    "tileIds", List.of(29),
                    "imagePath", "images/games/monopoly/tiles/marvin gardens.png",
                    "rotations", List.of(180.0)
            ),
            Map.of(
                    "tileIds", List.of(31),
                    "imagePath", "images/games/monopoly/tiles/pacific avenue.png",
                    "rotations", List.of(270.0)
            ),
            Map.of(
                    "tileIds", List.of(32),
                    "imagePath", "images/games/monopoly/tiles/north carolina avenue.png",
                    "rotations", List.of(270.0)
            ),
            Map.of(
                    "tileIds", List.of(34),
                    "imagePath", "images/games/monopoly/tiles/pennsylvania avenue.png",
                    "rotations", List.of(270.0)
            ),
            Map.of(
                    "tileIds", List.of(37),
                    "imagePath", "images/games/monopoly/tiles/park place.png",
                    "rotations", List.of(270.0)
            ),
            Map.of(
                    "tileIds", List.of(39),
                    "imagePath", "images/games/monopoly/tiles/boardwalk.png",
                    "rotations", List.of(270.0)
            )
    );

    // Apply styling to tiles
    for (Map<String, Object> config : stylingConfigs) {
      List<Integer> tileIds = (List<Integer>) config.get("tileIds");
      String imagePath = (String) config.get("imagePath");
      List<Double> rotations = (List<Double>) config.get("rotations");

      for (int i = 0; i < tileIds.size(); i++) {
        Tile tile = board.getTile(tileIds.get(i));
        TileStyling styling = new TileStyling();
        styling.setImagePath(imagePath);
        styling.setImageRotation(rotations.get(i));
        tile.setStyling(styling);
        if (config.containsKey("special_tile")) {
          tile.setAction((TileAction) config.get("special_tile"));
        }
      }
    }

    return board;
  }

  /**
   * Gets all available boards for the specified game type.
   *
   * <p>For Snakes and Ladders, this returns three board variants:
   * <ul>
   *   <li>A random board (10x10)</li>
   *   <li>A normal board with classic configuration</li>
   *   <li>A Chinese variant</li>
   * </ul>
   * </p>
   *
   * <p>For Monopoly, this returns the standard monopoly board.</p>
   *
   * @param gameType The type of game to get boards for
   * @return A list of available boards for the specified game type,
   * or an empty list if the game type is not supported
   */
  public static List<Board> getAllBoardsForGameType(GameType gameType) {
    return switch (gameType) {
      case GameType.SNAKES_AND_LADDERS -> List.of(
              randomSnakesAndLadders(10, 10),
              normalSnakesAndLadders(),
              chineseSnakesAndLadders()
      );
      case GameType.MONOPOLY -> List.of(
              standardMonopoly()
      );
    };
  }
}
