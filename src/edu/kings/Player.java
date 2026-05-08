package edu.kings;

import java.util.ArrayList;

public class Player {

    private Room currentRoom;
    private ArrayList<String> inventory;

    public Player(Room startingRoom) {
        currentRoom = startingRoom;
        inventory = new ArrayList<>();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room newRoom) {
        this.currentRoom = newRoom;
    }

    /** Adds an item (like a keycard) to the player's inventory. */
    public void addItem(String item) {
        inventory.add(item.toLowerCase());
    }

    /** Checks if the player has an item. */
    public boolean hasItem(String item) {
        return inventory.contains(item.toLowerCase());
    }

    /** Returns a copy of the inventory for display. */
    public ArrayList<String> getInventory() {
        return new ArrayList<>(inventory);
    }

    @Override
    public String toString() {
        return "Inventory: " + inventory.toString();
    }
}

