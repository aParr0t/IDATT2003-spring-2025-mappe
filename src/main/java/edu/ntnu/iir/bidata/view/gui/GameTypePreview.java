package edu.ntnu.iir.bidata.view.gui;

import edu.ntnu.iir.bidata.model.GameType;

/**
 * @param gameType this should be an enum or an object
 */
public record GameTypePreview(String name, String imagePath, GameType gameType) {
}
