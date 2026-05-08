package edu.kings;

import java.util.HashMap;

/**
 * Represents the "Savior of Humanity" spaceship world.
 * The player awakens from cryofreeze and must navigate
 * the ship, collecting keycards and restoring systems to save humanity.
 *
 * @author Cody Aniche-Farrell
 * @version 2026.05.05
 */
public class World {

    /** All rooms in the spaceship, stored by lowercase name. */
    private HashMap<String, Room> rooms;

    /**
     * Constructs the world and initializes all ship rooms.
     */
    public World() {
        rooms = new HashMap<>();
        createRooms();
    }

    /**
     * Retrieves a room by name (case‑insensitive).
     *
     * @param name The room name.
     * @return The matching Room, or null if not found.
     */
    public Room getRoom(String name) {
        if (name == null) return null;
        return rooms.get(name.toLowerCase());
    }

    /** Adds a room to the world. */
    private void addRoom(Room room) {
        rooms.put(room.getName().toLowerCase(), room);
    }

    /** Connects two rooms with a directional door. */
    private void createDoor(Room from, String direction, Room to) {
        from.setExit(direction, new Door(to));
    }

    /**
     * Creates all rooms and connects them based on the map.
     */
    private void createRooms() {

        // Create rooms
        Room cryopod     = new Room("Cryopod Room", "You awaken from cryofreeze after a century of sleep. Frost still clings to your pod. Dim lights flicker overhead.");
        Room cafeteria   = new Room("Cafeteria", "The once-bustling crew cafeteria lies silent. Food trays float in zero‑G.");
        Room observation = new Room("Observation Deck", "Massive glass panels reveal endless space. The faint hum of the ship’s engines echoes faintly.");
        Room weapons     = new Room("Weapons Bay", "Weapons control panels blink with warnings. Red lights flash: ARMAMENT SYSTEMS OFFLINE.");
        Room medical     = new Room("Medical Bay", "Dusty cryo‑beds line the wall. A med‑bot hums idly, waiting for commands.");
        Room oxygen      = new Room("O2 Control", "The oxygen chamber smells of metal and ozone. Levels fluctuate dangerously — this needs repair.");
        Room shields     = new Room("Shields/Ship Weapons", "Reinforced panels line this room. A console reads: 'Shields 43% — Priority Maintenance Required.'");
        Room engine      = new Room("Engine Room/Reactor Core", "Pulsating light from the reactor core bathes the room in blue. Power conduits stretch in all directions.");
        Room science     = new Room("Science Lab", "Beakers float mid‑air. Experiments from a century ago lie frozen in time.");
        Room crew        = new Room("Crew Quarters", "Personal belongings drift through the air — echoes of a vanished crew.");
        Room storage      = new Room("Storage", "A cluttered cargo bay. You might find useful supplies or keycards here.");
        Room comms       = new Room("Communications", "A static‑filled console flickers ‘SPACECOM LINK OFFLINE’. Reboot needed.");

        // Add rooms to map
        addRoom(cryopod);
        addRoom(cafeteria);
        addRoom(observation);
        addRoom(weapons);
        addRoom(medical);
        addRoom(oxygen);
        addRoom(shields);
        addRoom(engine);
        addRoom(science);
        addRoom(crew);
        addRoom(storage);
        addRoom(comms);

        // Connections (approximating your diagram)

        // Cryopod Room -> Cafeteria
        createDoor(cryopod, "east", cafeteria);
        createDoor(cafeteria, "west", cryopod);

        // Cafeteria <-> Observation Deck
        createDoor(cafeteria, "east", observation);
        createDoor(observation, "west", cafeteria);

        // Cafeteria <-> Weapons
        createDoor(cafeteria, "south", weapons);
        createDoor(weapons, "north", cafeteria);

        // Weapons <-> Medical Bay
        createDoor(weapons, "east", medical);
        createDoor(medical, "west", weapons);

        // Weapons <-> Engine Room
        createDoor(weapons, "south", engine);
        createDoor(engine, "north", weapons);

        // Medical <-> O2
        createDoor(medical, "south", oxygen);
        createDoor(oxygen, "north", medical);

        // O2 <-> Shields
        createDoor(oxygen, "east", shields);
        createDoor(shields, "west", oxygen);

        // O2 <-> Storage
        createDoor(oxygen, "south", storage);
        createDoor(storage, "north", oxygen);

        // Engine Room <-> Science Lab
        createDoor(engine, "west", science);
        createDoor(science, "east", engine);

        // Science Lab <-> Communications
        createDoor(science, "south", comms);
        createDoor(comms, "north", science);

        // Engine Room <-> Crew Quarters
        createDoor(engine, "south", crew);
        createDoor(crew, "north", engine);

        // Crew Quarters <-> Storage
        createDoor(crew, "east", storage);
        createDoor(storage, "west", crew);
    
        // Lock some key areas that require keycards
        engine.getExit("north").setLocked(true);     // Weapons -> Engine Room
        oxygen.getExit("east").setLocked(true);      // O2 -> Shields
        science.getExit("south").setLocked(true);    // Science Lab -> Communications

        // Add note: keys needed
        /*
         * Engine Room/Reactor Core: needs Blue Keycard
         * Shields/Ship Weapons: needs Red Keycard
         * Communications: needs Green Keycard
         */

    }
} 
