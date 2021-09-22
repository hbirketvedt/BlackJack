package app;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class FileManagerImpl implements FileManager {

    public static String getPath() { 
        return System.getProperty("user.home") + "\\saves\\";
    }

    public void saveGame(String playername, int coins) throws IllegalArgumentException {
        if (playername.matches(".*[/?:|\"<>*\\s\\\\ ]+") || playername.isEmpty()) { // Symboler man ikke kan bruke til å lagre filer i windows
            throw new IllegalArgumentException("Invalid username");
        }
        if (coins < 5) { // Kan ikke spille med mindre enn 5 coins
            deleteSave(playername);
            return;
        }
        createSaveFile(playername); // thrower IllegalArgumentException herfra også i tilfelle den ikke klarer å lagre
        writeToSaveFile(playername, coins); // og herfra
        System.out.println("Game saved");
    }

    public int loadGame(String playername) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getPath() + playername));  // valgte å ikke ha /saves/ i resources fordi det da funker som en jar fil
            String kroner = reader.readLine();
            reader.close();
            try {
                return Integer.parseInt(kroner);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Error reading coins from file.");
                return 100; // velger å sette verdien til 100 dersom det skjer feil istedenfor å throwe en exception
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occured reading from file.");
            return 100;
        }
    }

    public void newSaveGame(String playername) throws IllegalArgumentException {
        String lowerCaseName = playername.toLowerCase();
        for (String existingName : getSaveTitles()) { // Windows registrerer upper og lowercase som samme
            if (lowerCaseName.matches(existingName.toLowerCase())) {
                throw new IllegalArgumentException("Username taken");
            }
        }
        saveGame(playername, 100);
    }

    public Collection<String> getSaveTitles() {
        Collection<String> filenames = new ArrayList<>();
        File directory = new File(getPath());
        if (directory.mkdir()) {
            System.out.println(getPath() +  " created");
        }
        File[] files = new File(getPath()).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                filenames.add(file.getName());
            }
        }
        return filenames;
    }

    public void deleteSave(String playername) throws IllegalArgumentException{
        File file = new File(getPath() + playername);
        if (file.delete()) {
            System.out.println("Save deleted");
        } else {
            System.out.println("Save not found so not deleted");
        }
    }

    private void createSaveFile(String playername) throws IllegalArgumentException {
        try {
            File save = new File(getPath() + playername);
            if (save.createNewFile()) {
                System.out.println("Savefile created");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("An error occured creating savefile");
        }
    }

    private void writeToSaveFile(String playername, int coins) throws IllegalArgumentException {
        try {
            FileWriter writer = new FileWriter(getPath() + playername);
            writer.write(String.valueOf(coins));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("An error occured writing to savefile");
        }
    }

}
