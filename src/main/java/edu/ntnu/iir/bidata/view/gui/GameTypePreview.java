package edu.ntnu.iir.bidata.view.gui;

import edu.ntnu.iir.bidata.model.GameType;

/**
 * A data class that represents a preview of a game type in the UI.
 * Contains the basic information needed to display a game type in the game selection interface.
 */
public record GameTypePreview(String name, String imagePath, GameType gameType) {}
