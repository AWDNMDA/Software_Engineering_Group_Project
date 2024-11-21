package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoardSaver {
    private static final String SAVE_DIRECTORY = "boards/";
    private static final String DEFAULT_FILENAME = "board_v";

    static {
        File dir = new File(SAVE_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdir(); // Create directory if it doesn't exist
        }
    }

    /**
     * Save a board with a user-defined or auto-generated name.
     *
     * @param board  The board to save.
     * @param name   Optional name for the file; if null, generates a unique name.
     */
    public static void saveBoard(Board board, String name) {
        String filename = name != null && !name.isBlank()
                ? SAVE_DIRECTORY + name + ".dat"
                : SAVE_DIRECTORY + generateUniqueFilename();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(board);
            System.out.println("Board saved successfully as: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving board: " + e.getMessage());
        }
    }

    /**
     * Display all saved boards.
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
     * Check if any saved boards exist.
     *
     * @return True if saved boards are available, otherwise false.
     */
    public static boolean hasSavedBoards() {
        return !getSavedBoardFiles().isEmpty();
    }

    /**
     * Get a saved board by index.
     *
     * @param index The index of the board.
     * @return The selected board, or a default board if the index is invalid.
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
     * Load a saved board based on user input.
     *
     * @param scanner The scanner for user input.
     * @return The selected or default board.
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
        scanner.nextLine(); // Consume newline
        return getBoard(choice);
    }

    /**
     * Helper method to load a board from a file.
     *
     * @param filename The filename of the board to load.
     * @return The loaded board, or null if an error occurs.
     */
    private static Board loadBoard(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_DIRECTORY + filename))) {
            return (Board) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading board: " + e.getMessage());
            return null;
        }
    }

    /**
     * List all saved board files.
     *
     * @return A list of saved board filenames.
     */
    private static List<String> getSavedBoardFiles() {
        File dir = new File(SAVE_DIRECTORY);
        String[] files = dir.list((d, name) -> name.endsWith(".dat"));
        return files != null ? List.of(files) : new ArrayList<>();
    }

    /**
     * Generate a unique filename based on the current time.
     *
     * @return A unique filename string.
     */
    private static String generateUniqueFilename() {
        long timestamp = System.currentTimeMillis();
        return DEFAULT_FILENAME + timestamp + ".dat";
    }
}
