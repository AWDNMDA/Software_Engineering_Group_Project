package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class for managing the saving and loading of game boards.
 * This class allows users to save boards, display saved boards, load specific boards,
 * and manage the saved board files.
 */
public class BoardSaver {
    private static final String SAVE_DIRECTORY = "boards/";
    private static final String DEFAULT_FILENAME = "board_v";

    // Static initializer block to ensure the save directory exists
    static {
        File dir = new File(SAVE_DIRECTORY);
        if (!dir.exists() && !dir.mkdir()) {
            System.out.println("Failed to create save directory.");
        }
    }

    /**
     * Saves a board to a file with a specified or auto-generated name.
     * @param board The board to save.
     * @param name  Optional name for the file. If null or blank, a unique name is generated.
     */
    public static void saveBoard(Board board, String name) {
        String filename = name != null && !name.isBlank()
                ? SAVE_DIRECTORY + name + ".dat"
                : SAVE_DIRECTORY + generateUniqueFilename();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(board);
            System.out.println("Board saved successfully as: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving board: " + e.getMessage());
        }
    }

    /**
     * Displays all saved board files.
     */
    public static void displayBoards() {
        List<String> savedBoards = getSavedBoardFiles();
        if (savedBoards.isEmpty()) {
            System.out.println("No saved boards available.");
            return;
        }

        System.out.println("Saved Boards:");
        for (int i = 0; i < savedBoards.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, savedBoards.get(i));
        }
    }

    /**
     * Checks if there are any saved boards.
     * @return True if saved boards exist, otherwise false.
     */
    public static boolean hasSavedBoards() {
        return !getSavedBoardFiles().isEmpty();
    }

    /**
     * Retrieves a saved board by its index.
     * @param index The index of the saved board.
     * @return The loaded board, or a default board if the index is invalid.
     */
    public static Board getBoard(int index) {
        List<String> savedBoards = getSavedBoardFiles();

        if (index < 1 || index > savedBoards.size()) {
            System.out.println("Invalid choice. Loading default board instead.");
            return new Board();
        }

        String filename = savedBoards.get(index - 1);
        return loadBoard(filename);
    }

    /**
     * Loads a board based on user input.
     * @param scanner The scanner for user input.
     * @return The loaded board or a default board if no saved boards are available.
     */
    public static Board loadBoard(Scanner scanner) {
        List<String> savedBoards = getSavedBoardFiles();
        if (savedBoards.isEmpty()) {
            System.out.println("No saved boards available. Loading default board.");
            return new Board();
        }

        displayBoards();
        System.out.println("Enter the number of the board to load:");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return getBoard(choice);
    }

    /**
     * Loads a board from a specific file.
     * @param filename The filename of the board to load.
     * @return The loaded board, or a default board if an error occurs.
     */
    private static Board loadBoard(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_DIRECTORY + filename))) {
            return (Board) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading board: " + e.getMessage());
            return new Board();
        }
    }

    /**
     * Retrieves a list of all saved board files.
     * @return A list of filenames of saved boards.
     */
    private static List<String> getSavedBoardFiles() {
        File dir = new File(SAVE_DIRECTORY);
        String[] files = dir.list((d, name) -> name.endsWith(".dat"));
        return files != null ? List.of(files) : new ArrayList<>();
    }

    /**
     * Generates a unique filename based on the current timestamp.
     * @return A unique filename string.
     */
    private static String generateUniqueFilename() {
        long timestamp = System.currentTimeMillis();
        return DEFAULT_FILENAME + timestamp + ".dat";
    }
}
