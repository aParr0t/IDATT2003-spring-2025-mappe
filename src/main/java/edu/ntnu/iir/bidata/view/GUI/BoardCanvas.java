package edu.ntnu.iir.bidata.view.GUI;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.Tile;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public abstract class BoardCanvas extends Canvas {
  protected final Board board;
  protected List<Player> players = List.of();

  public BoardCanvas(Board board) {
    this.board = board;
    // Set default size if not specified otherwise
    setWidth(500);
    setHeight(500);

    // Add listener to redraw when the canvas size changes
    widthProperty().addListener(observable -> draw());
    heightProperty().addListener(observable -> draw());

    draw();
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
    draw();
  }

  public abstract void draw();

  protected void drawPlayers(List<Player> players) {
    GraphicsContext gc = getGraphicsContext2D();

    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      Tile tile = board.getTile(player.getPosition());
      double tileWidth = tile.getWidth();
      double tileHeight = tile.getHeight();
      List<Point2D> offsets = List.of(
              new Point2D(0, 0),
              new Point2D(tileWidth / 2, 0),
              new Point2D(0, tileHeight / 2),
              new Point2D(tileWidth / 2, tileHeight / 2)
      );
      Point2D offset = offsets.get(i);
      Point2D playerPos = tile.getPosition().add(offset);
      double playerSize = tileWidth * 0.5 * getWidth();
      String imagePath = player.getPlayingPiece().getImagePath();
      Image image = new Image(imagePath);
      gc.drawImage(image, playerPos.getX() * getWidth(), playerPos.getY() * getHeight(), playerSize, playerSize);
    }
  }
}
