package com.healthcare.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Generic repository class for managing collections of objects with file persistence.
 *
 * @param <T> the type of object managed by this repository
 */
public class DataRepository<T> {
    private List<T> items;

    /**
     * Constructor for DataRepository.
     */
    public DataRepository() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the repository.
     *
     * @param item the item to add
     */
    public void add(T item) {
        items.add(item);
    }

    /**
     * Retrieves all items from the repository.
     *
     * @return a defensive copy of the items list
     */
    public List<T> getAll() {
        return new ArrayList<>(items);
    }

    /**
     * Saves all items to a file. Each item's toString() is written as one line.
     *
     * @param filename the file to save to
     */
    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (T item : items) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    /**
     * Loads items from a file using a parser function.
     *
     * @param filename the file to load from
     * @param parser   function to parse each line into an object
     */
    public void loadFromFile(String filename, Function<String, T> parser) {
        items.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T item = parser.apply(line);
                if (item != null) {
                    items.add(item);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename + ". Starting with empty repository.");
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }
}
