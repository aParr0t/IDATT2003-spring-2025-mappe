package edu.ntnu.iir.bidata.filehandling;

import com.google.gson.*;
import edu.ntnu.iir.bidata.exceptions.DirectoryCreationException;
import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Tile;
import edu.ntnu.iir.bidata.model.TileStyling;
import edu.ntnu.iir.bidata.model.tileaction.TileAction;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Implementation of BoardFileWriter that writes boards to a JSON file using Gson.
 */
public class BoardFileWriterGson implements BoardFileWriter {
  @Override
  public void writeBoard(Board board, Path filePath) throws IOException {
    // Ensure the parent directory exists
    try {
      FileUtils.ensureDirectoryExists(filePath.getParent());
    } catch (DirectoryCreationException e) {
      throw new IOException("Failed to create directory for file: " + filePath, e);
    }

    // Create a Gson instance with the TileAction adapter and pretty printing
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(TileAction.class, new TileActionAdapter())
            .setPrettyPrinting()
            .create();

    // Create the JSON object for the board
    JsonObject boardJson = new JsonObject();

    // Add the board name
    boardJson.addProperty("name", board.getName());

    // Add the tiles
    JsonArray tilesJson = new JsonArray();
    List<Tile> tiles = board.getTiles();

    for (Tile tile : tiles) {
      JsonObject tileJson = new JsonObject();

      // Add the tile ID
      tileJson.addProperty("id", tile.getId());

      // Add the next tile ID if available
      if (tile.getNextTile() != null) {
        tileJson.addProperty("nextTile", tile.getNextTile().getId());
      }

      // Add the position
      Point2D position = tile.getPosition();
      if (position != null) {
        JsonObject positionJson = new JsonObject();
        positionJson.addProperty("x", position.getX());
        positionJson.addProperty("y", position.getY());
        tileJson.add("position", positionJson);
      }

      // Add the size
      tileJson.addProperty("width", tile.getWidth());
      tileJson.addProperty("height", tile.getHeight());

      // Add the styling if available
      TileStyling styling = tile.getStyling();
      if (styling != null) {
        JsonObject stylingJson = new JsonObject();

        if (styling.getColor() != null) {
          stylingJson.addProperty("color", styling.getColor());
        }

        if (styling.getImagePath() != null) {
          stylingJson.addProperty("imagePath", styling.getImagePath());
        }

        stylingJson.addProperty("imageRotation", styling.getImageRotation());

        tileJson.add("styling", stylingJson);
      }

      // Add the action if available
      TileAction action = tile.getAction();
      if (action != null) {
        JsonElement actionJson = gson.toJsonTree(action);
        tileJson.add("action", actionJson);
      }

      tilesJson.add(tileJson);
    }

    boardJson.add("tiles", tilesJson);

    // Write the JSON to the file
    String json = gson.toJson(boardJson);
    Files.writeString(filePath, json);
  }
}
