package edu.ntnu.iir.bidata.filehandling;

import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.PlayingPiece;
import edu.ntnu.iir.bidata.model.PlayingPieceType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of PlayerFileReader that reads players from a CSV file.
 */
public class PlayerFileReaderCSV implements PlayerFileReader {
    @Override
    public List<Player> readPlayers(Path filePath) throws IOException {
        List<Player> players = new ArrayList<>();
        
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    throw new IOException("Invalid player data format: " + line);
                }
                
                String name = parts[0].trim();
                String pieceTypeStr = parts[1].trim();
                
                // Convert the piece type string to enum
                PlayingPieceType pieceType;
                try {
                    pieceType = PlayingPieceType.valueOf(pieceTypeStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IOException("Invalid playing piece type: " + pieceTypeStr, e);
                }
                
                PlayingPiece playingPiece = new PlayingPiece(pieceType);
                Player player = new Player(name, playingPiece);
                players.add(player);
            }
        }
        
        return players;
    }
}
