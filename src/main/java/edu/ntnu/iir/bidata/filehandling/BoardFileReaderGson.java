package edu.ntnu.iir.bidata.filehandling;

import com.google.gson.*;
import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Tile;
import edu.ntnu.iir.bidata.model.TileStyling;
import edu.ntnu.iir.bidata.model.tileaction.TileAction;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of BoardFileReader that reads boards from a JSON file using Gson.
 */
public class BoardFileReaderGson implements BoardFileReader {
    @Override
    public Board readBoard(Path filePath) throws IOException {
        String json = Files.readString(filePath);
        
        // Create a Gson instance with the TileAction adapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TileAction.class, new TileActionAdapter())
                .create();
        
        // Parse the JSON
        JsonObject boardJson = JsonParser.parseString(json).getAsJsonObject();
        
        // Create a new board
        Board board = new Board();
        
        // Set the board name
        if (boardJson.has("name")) {
            board.setName(boardJson.get("name").getAsString());
        }
        
        // Parse the tiles
        if (boardJson.has("tiles")) {
            JsonArray tilesJson = boardJson.getAsJsonArray("tiles");
            List<Tile> tiles = new ArrayList<>();
            Map<Integer, Tile> tileMap = new HashMap<>();
            
            // First pass: create all tiles
            for (JsonElement tileElement : tilesJson) {
                JsonObject tileJson = tileElement.getAsJsonObject();
                int id = tileJson.get("id").getAsInt();
                
                Tile tile = new Tile(id);
                
                // Set position if available
                if (tileJson.has("position")) {
                    JsonObject positionJson = tileJson.getAsJsonObject("position");
                    double x = positionJson.get("x").getAsDouble();
                    double y = positionJson.get("y").getAsDouble();
                    tile.setPosition(new Point2D(x, y));
                }
                
                // Set size if available
                if (tileJson.has("width") && tileJson.has("height")) {
                    double width = tileJson.get("width").getAsDouble();
                    double height = tileJson.get("height").getAsDouble();
                    tile.setSize(width, height);
                }
                
                // Set styling if available
                if (tileJson.has("styling")) {
                    JsonObject stylingJson = tileJson.getAsJsonObject("styling");
                    TileStyling styling = new TileStyling();
                    
                    if (stylingJson.has("color")) {
                        styling.setColor(stylingJson.get("color").getAsString());
                    }
                    
                    if (stylingJson.has("imagePath")) {
                        styling.setImagePath(stylingJson.get("imagePath").getAsString());
                    }
                    
                    if (stylingJson.has("imageRotation")) {
                        styling.setImageRotation(stylingJson.get("imageRotation").getAsDouble());
                    }
                    
                    tile.setStyling(styling);
                }
                
                // Set action if available
                if (tileJson.has("action")) {
                    TileAction action = gson.fromJson(tileJson.get("action"), TileAction.class);
                    tile.setAction(action);
                }
                
                tiles.add(tile);
                tileMap.put(id, tile);
            }
            
            // Second pass: connect tiles
            for (JsonElement tileElement : tilesJson) {
                JsonObject tileJson = tileElement.getAsJsonObject();
                int id = tileJson.get("id").getAsInt();
                
                if (tileJson.has("nextTile")) {
                    int nextTileId = tileJson.get("nextTile").getAsInt();
                    Tile tile = tileMap.get(id);
                    Tile nextTile = tileMap.get(nextTileId);
                    
                    if (tile != null && nextTile != null) {
                        tile.setNextTile(nextTile);
                    }
                }
            }
            
            // Add all tiles to the board
            for (Tile tile : tiles) {
                board.addTile(tile);
            }
        }
        
        return board;
    }
}
