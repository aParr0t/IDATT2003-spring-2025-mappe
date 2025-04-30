package edu.ntnu.iir.bidata.filehandling;

import edu.ntnu.iir.bidata.exceptions.CsvParsingException;
import edu.ntnu.iir.bidata.exceptions.FileNotFoundException;
import edu.ntnu.iir.bidata.exceptions.PlayerDataException;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.PlayingPiece;
import edu.ntnu.iir.bidata.model.PlayingPieceType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of PlayerFileReader that reads players from a CSV file.
 */
public class PlayerFileReaderCSV implements PlayerFileReader {
    @Override
    public List<Player> readPlayers(Path filePath) throws IOException, FileNotFoundException, CsvParsingException, PlayerDataException {
        List<Player> players = new ArrayList<>();
        
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    throw new CsvParsingException("Invalid player data format at line " + lineNumber + ": " + line);
                }
                
                String name = parts[0].trim();
                String pieceTypeStr = parts[1].trim();
                
                // Convert the piece type string to enum
                PlayingPieceType pieceType;
                try {
                    pieceType = PlayingPieceType.valueOf(pieceTypeStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new PlayerDataException("Invalid playing piece type at line " + lineNumber + ": " + pieceTypeStr, e);
                }
                
                PlayingPiece playingPiece = new PlayingPiece(pieceType);
                Player player = new Player(name, playingPiece);
                players.add(player);
            }
        } catch (NoSuchFileException e) {
            throw new FileNotFoundException(filePath, e);
        }
        
        return players;
    }
}
