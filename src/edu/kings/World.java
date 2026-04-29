package edu.kings;

import java.util.HashMap;

/**
 * Represents the entire world for the "Campus of Kings" text adventure.
 * Creates all rooms and connects them with directional exits.
 *
 * @author Maria Jump
 * @version 2015.02.01
 *
 * Used with permission from Dr. Maria Jump at Northeastern University
 */
public class World {

    /** All rooms in the world, stored by lowercase name. */
    private HashMap<String, Room> rooms;
    

    /**
     * Constructs the world and initializes all rooms.
     */
    public World() {
        rooms = new HashMap<>();
        createRooms();
    }

    /**
     * Retrieves a room by name (case‑insensitive).
     *
     * @param name The name of the room.
     * @return The matching Room, or null if not found.
     */
    public Room getRoom(String name) {
        if (name == null) return null;
        return rooms.get(name.toLowerCase());
    }

    // --------------------------------------------------------------------
    // Private helper methods
    // --------------------------------------------------------------------

    /** Adds a room to the world. */
    private void addRoom(Room room) {
        rooms.put(room.getName().toLowerCase(), room);
    }

    /** Creates a north door. */
    private void createDoor(Room from, String direction, Room to) {
    	from.setExit(direction,new Door(to));
    	
    }

    /**
     * Creates all rooms and connects them.
     */
    private void createRooms() {

        // Create rooms
        Room outside       = new Room("Outside", "outside in the center of the King's College campus.");
        Room holyCross     = new Room("Holy Cross", "at one of two main dormitories on campus.");
        Room essef         = new Room("Essef", "at the other main dormitory on campus.");
        Room campusCenter  = new Room("Campus Center", "in the center of student activities on campus.");
        Room admin         = new Room("Admin", "in the oldest building on campus and home to the computer science department.");
        Room silvaOffice   = new Room("Silva's Office", "in Dr. Silva's office.");
        Room janoskiOffice = new Room("Janoski's Office", "in Dr. Janoski's office.");
        Room lab           = new Room("Computer Lab", "in the Computer Science and Math computing lab.");
        Room classroom     = new Room("Classroom", "in the classroom where the computer science classes are taught.");

        // Add rooms to world
        addRoom(outside);
        addRoom(holyCross);
        addRoom(essef);
        addRoom(campusCenter);
        addRoom(admin);
        addRoom(silvaOffice);
        addRoom(janoskiOffice);
        addRoom(lab);
        addRoom(classroom);

        // ------------------------------------------------------------
        // Create connections (bidirectional where appropriate)
        // ------------------------------------------------------------

        createDoor(essef, "south", outside);
        createDoor(outside, "north", essef); 
       

        createDoor(campusCenter, "east", outside); 
        createDoor(outside, "west", campusCenter); 
        

        createDoor(outside, "east", holyCross); 
        createDoor(holyCross, "west", outside); 
        

        createDoor(outside, "south", admin); 
        createDoor(admin, "north", outside); 
        

        createDoor(admin, "east", lab); 
        createDoor(lab, "west", admin); 
      
        createDoor(admin, "south", janoskiOffice); 
        createDoor(janoskiOffice, "north", admin); 
        

        createDoor(admin, "west", silvaOffice); 
        createDoor(silvaOffice, "east", janoskiOffice); 
       

        createDoor(lab, "south", classroom); 
        createDoor(classroom, "north", lab); 
       
    }
}