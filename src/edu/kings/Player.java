package edu.kings;

public class Player {

    private Room currentRoom;

    public Player(Room newcurrentRoom) {
        currentRoom = newcurrentRoom; 
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room newRoom) {
        this.currentRoom = newRoom;
    }
}
