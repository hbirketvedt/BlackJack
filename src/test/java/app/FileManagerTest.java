package app;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.awt.geom.IllegalPathStateException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class FileManagerTest {
    private final FileManager fileManager = new FileManagerImpl();

    public static String getBackupPath() {
        return System.getProperty("user.home") + "\\backupSavesForBlackJackApp\\";
    }

    @BeforeAll
    public static void backupSaves() {
        File backupDirectory = new File(getBackupPath());
        if (!backupDirectory.mkdir()) { // Sjekker at mappen ikke eksisterer fra før av så appen ikke kommer og sletter filer den ikke skulle
            throw new IllegalPathStateException("path for backup folder already exists: " + getBackupPath());
        }
        File source = new File(FileManagerImpl.getPath());
        File dest = new File(getBackupPath());
        try {
            FileUtils.copyDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void reverseFiles() {
        File source = new File(getBackupPath());
        File dest = new File(FileManagerImpl.getPath());
        try {
            FileUtils.copyDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File[] files = new File(getBackupPath()).listFiles();
        for (File file : files) {
            file.delete();
        }
        source.delete();
    }

    @BeforeEach
    @AfterEach
    public void emptyDirectory() {
        for (File file: new File(FileManagerImpl.getPath()).listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }

    }



    @Test
    public void testSaveTitles() {
        emptyDirectory();
        Assertions.assertTrue(fileManager.getSaveTitles().isEmpty(), "Please empty test directory before testing");
        fileManager.newSaveGame("Alice");
        fileManager.newSaveGame("Bob");
        fileManager.newSaveGame("Rob");
        Collection<String> testList = new ArrayList<>();
        testList.add("Alice");
        testList.add("Bob");
        testList.add("Rob");
        Assertions.assertEquals(testList, fileManager.getSaveTitles());
    }


    @Test
    public void testNewSaveGame() {
        fileManager.newSaveGame("test");
        Assertions.assertEquals(100, fileManager.loadGame("test"));
    }

    @Test
    public void testSaveGame() {
        fileManager.newSaveGame("testSaveGame");
        fileManager.saveGame("testSaveGame", 300);
        Assertions.assertEquals(300, fileManager.loadGame("testSaveGame"));
        fileManager.saveGame("testSaveGame", 456);
        Assertions.assertEquals(456, fileManager.loadGame("testSaveGame"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fileManager.saveGame("", 10));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fileManager.saveGame("   ", 10));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fileManager.saveGame("h/", 10));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fileManager.saveGame("?", 10));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fileManager.saveGame("*", 10));
    }

    @Test
    public void testDeletesSave() {
        fileManager.newSaveGame("testDeleteSave");
        fileManager.saveGame("testDeleteSave", 789);
        Assertions.assertEquals(789, fileManager.loadGame("testDeleteSave"));
        fileManager.deleteSave("testDeleteSave");
        Assertions.assertEquals(100, fileManager.loadGame("testDeleteSave"));
        fileManager.newSaveGame("testDeleteSave");
        fileManager.saveGame("testDeleteSave", 4);
        Assertions.assertEquals(100, fileManager.loadGame("testDeleteSave")); // Filen skal slettes når coins < 5, så loadGame kommer til å få en FileNotFoundException og dermed returnere standardverdi på 100
    }




}
