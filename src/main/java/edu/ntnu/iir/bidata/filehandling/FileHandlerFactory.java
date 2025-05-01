package edu.ntnu.iir.bidata.filehandling;

/**
 * Factory for creating file readers and writers.
 */
public class FileHandlerFactory {
  private FileHandlerFactory() {
    // Private constructor to prevent instantiation
  }

  /**
   * Creates a BoardFileReader for reading board files.
   *
   * @return a BoardFileReader
   */
  public static BoardFileReader createBoardFileReader() {
    return new BoardFileReaderGson();
  }

  /**
   * Creates a BoardFileWriter for writing board files.
   *
   * @return a BoardFileWriter
   */
  public static BoardFileWriter createBoardFileWriter() {
    return new BoardFileWriterGson();
  }

  /**
   * Creates a PlayerFileReader for reading player files.
   *
   * @return a PlayerFileReader
   */
  public static PlayerFileReader createPlayerFileReader() {
    return new PlayerFileReaderCsv();
  }

  /**
   * Creates a PlayerFileWriter for writing player files.
   *
   * @return a PlayerFileWriter
   */
  public static PlayerFileWriter createPlayerFileWriter() {
    return new PlayerFileWriterCsv();
  }
}
