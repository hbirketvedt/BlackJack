package app;

import java.util.Collection;

public interface FileManager {

    void saveGame(String playername, int coins) throws IllegalArgumentException;

    int loadGame(String playername);

    void newSaveGame(String playername) throws IllegalArgumentException;

    void deleteSave(String playername) throws IllegalArgumentException;

    Collection<String> getSaveTitles();
}
