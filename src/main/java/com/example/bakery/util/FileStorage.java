package com.example.bakery.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileStorage - Utility class for reading and writing .txt data files.
 * Lecture Concepts: File I/O (BufferedReader, PrintWriter), Exception Handling (try-catch),
 * static methods, ArrayList with Generics.
 */
@Component
public class FileStorage {

    private static final String DATA_DIR = "data/";

    // Static initializer block - runs once when class is loaded (Lecture 05)
    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Initialize default data files if they don't exist
        initFile("users.txt");
        initFile("products.txt");
        initFile("orders.txt");
        initFile("cakes.txt");
        initFile("suppliers.txt");
        initFile("ingredients.txt");
        initFile("reviews.txt");
        initFile("purchase_orders.txt");
    }

    // Static helper method - shared across all instances (Lecture 05)
    private static void initFile(String filename) {
        File f = new File(DATA_DIR + filename);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.err.println("Could not create file: " + filename);
            }
        }
    }

    /**
     * Read all lines from a data file.
     * Exception Handling: try-catch-finally (Lecture 06)
     */
    public List<String> readAll(String filename) {
        List<String> lines = new ArrayList<>(); // ArrayList with Generics (Lecture 08)
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_DIR + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (FileNotFoundException e) {
            // Checked exception (Lecture 06)
            System.err.println("File not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename + " - " + e.getMessage());
        }
        return lines;
    }

    /**
     * Write all lines to a data file (overwrites).
     */
    public void writeAll(String filename, List<String> lines) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_DIR + filename))) {
            for (String line : lines) {
                pw.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + filename + " - " + e.getMessage());
        }
    }

    /**
     * Append a single line to a data file.
     */
    public void appendLine(String filename, String line) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_DIR + filename, true))) {
            pw.println(line);
        } catch (IOException e) {
            System.err.println("Error appending to file: " + filename + " - " + e.getMessage());
        }
    }

    /**
     * Generate next available ID from a file.
     * Static method - does not depend on object state (Lecture 05)
     */
    public static int generateId(List<String> lines) {
        int maxId = 0;
        for (String line : lines) {
            try {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                if (id > maxId) maxId = id;
            } catch (NumberFormatException e) {
                // Unchecked exception - skip invalid lines (Lecture 06)
            }
        }
        return maxId + 1;
    }
}
