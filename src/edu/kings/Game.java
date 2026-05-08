package edu.kings;

/**
 * This class is the main class of the "Savior of HUmanity" application.
 * "Savior of Humanity" is a very simple, text based adventure game. Users must complete a number 
 * of tasks to repair their ship and 
 * explore space onward to SAVE HUMANITY!
 * 
 * @author Cody Aniche-Farrell
 */
public class Game {

    /** The world where the game takes place. */
    private World world;

    /** The player character. */
    private Player player;

    /** The player's score. */
    private int score = 0;

    /** The total number of turns taken. */
    private int total_number_of_turns = 0;
    /** Maximum number of turns before the ship's oxygen runs out. */
    private static final int MAX_TURNS_BEFORE_DEATH = 50;

    /** Whether the player has already lost. */
    private boolean hasLost = false;

    /**
     * Create the game and initialize its internal map.
     */
    /**
     * Create the game and initialize its internal map.
     */
    public Game() {
        world = new World();
        // Set the starting room to the Cryopod Room (the first room of your ship)
        player = new Player(world.getRoom("Cryopod Room"));
    }

    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        printWelcome();

        // Enter the main game loop. Here we repeatedly read commands and
        // execute them until the game is over.
        boolean wantToQuit = false;
        while (!wantToQuit && !hasLost) {
            Command command = Reader.getCommand();
            total_number_of_turns++;
            if (total_number_of_turns == MAX_TURNS_BEFORE_DEATH / 2) {
                Writer.println("Oxygen levels dropping. Systems failing — find the Reactor soon!");
            }
            wantToQuit = processCommand(command);

            // Check lose condition each turn if not already lost or quit
            if (!wantToQuit && checkLoss()) {
                System.exit(0);
            }
        }

        printGoodbye();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Helper methods for processing the commands

    /**
     * Given a command, process (that is: execute) the command.
     *
     * @param command
     *            The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if (command.isUnknown()) {
            Writer.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();

        switch (commandWord) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                break;
            case "look":
                look1();
                break;
            case "take":
                take1(command);
                break;
            case "inventory":
                Writer.println(player.toString());
                break;
            case "use":
                use(command);
                break;
            case "quit":
                wantToQuit = quit(command);
                break;
            default:
                Writer.println(commandWord + " is not implemented yet!");
        }
        return wantToQuit;
    }
    /** Uses a keycard to unlock doors in specific rooms. */
    private void use(Command command) {
        if (!command.hasSecondWord()) {
            Writer.println("Use what?");
            return;
        }

        String item = command.getRestOfLine().trim();

        if (!player.hasItem(item)) {
            Writer.println("You don’t have that item.");
            return;
        }

        Room room = player.getCurrentRoom();
        boolean unlocked = false;

        // Blue Keycard unlocks Weapons→Engine door
        if (item.equalsIgnoreCase("Blue Keycard") &&
            room.getName().equalsIgnoreCase("Weapons Bay")) {
            Door engineDoor = room.getExit("south");
            if (engineDoor != null && engineDoor.isLocked()) {
                engineDoor.setLocked(false);
                Writer.println("You swipe the Blue Keycard. The door to the Engine Room clicks open.");
                unlocked = true;
            }
        }

        // Red Keycard unlocks O2→Shields door
        else if (item.equalsIgnoreCase("Red Keycard") &&
                 room.getName().equalsIgnoreCase("O2 Control")) {
            Door shieldDoor = room.getExit("east");
            if (shieldDoor != null && shieldDoor.isLocked()) {
                shieldDoor.setLocked(false);
                Writer.println("You swipe the Red Keycard. Access to the Shield Room granted.");
                unlocked = true;
            }
        }

        // Green Keycard unlocks Science→Comms door
        else if (item.equalsIgnoreCase("Green Keycard") &&
                 room.getName().equalsIgnoreCase("Science Lab")) {
            Door commsDoor = room.getExit("south");
            if (commsDoor != null && commsDoor.isLocked()) {
                commsDoor.setLocked(false);
                Writer.println("You swipe the Green Keycard. Communication systems accessible!");
                unlocked = true;
            }
        }

        if (!unlocked) {
            Writer.println("That keycard doesn’t seem to work here.");
        }
    }


	/** Describes the current room and visible items. */
    private void look1() {
        Room room = player.getCurrentRoom();
        Writer.println("You are in " + room.getName() + ".");
        Writer.println(room.getDescription());

        // Add flavor: items in specific rooms
        switch (room.getName().toLowerCase()) {
            case "storage":
                Writer.println("You see a shimmering Blue Keycard here.");
                break;
            case "medical bay":
                Writer.println("A Red Keycard lies beside an inactive med-bot.");
                break;
            case "science lab":
                Writer.println("A Green Keycard floats near the research terminal.");
                break;
            default:
                Writer.println("There’s nothing special here.");
        }
    }

    /** Lets the player take items from certain rooms. */
    private void take1(Command command) {
        if (!command.hasSecondWord()) {
            Writer.println("Take what?");
            return;
        }

        String item = command.getRestOfLine();
        Room room = player.getCurrentRoom();
        boolean taken = false;

        if (room.getName().equalsIgnoreCase("Storage") && item.equalsIgnoreCase("Blue Keycard")) {
            player.addItem("Blue Keycard");
            Writer.println("You pick up the Blue Keycard!");
            taken = true;
        } else if (room.getName().equalsIgnoreCase("Medical Bay") && item.equalsIgnoreCase("Red Keycard")) {
            player.addItem("Red Keycard");
            Writer.println("You grab the Red Keycard!");
            taken = true;
        } else if (room.getName().equalsIgnoreCase("Science Lab") && item.equalsIgnoreCase("Green Keycard")) {
            player.addItem("Green Keycard");
            Writer.println("You take the Green Keycard!");
            taken = true;
        }

        if (!taken) {
            Writer.println("There’s nothing like that here.");
        }
    }

    /**
     * Try to go to one direction. If there is an exit, enter the new room,
     * otherwise print an error message.
     *
     * @param command
     *            The command to be processed.
     */
    /**
     * Try to go to one direction. If there is an exit,
     * enter the new room unless it’s locked.
     */
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            Writer.println("Go where?");
            return;
        }

        String direction = command.getRestOfLine();
        Door door = player.getCurrentRoom().getExit(direction);

        if (door == null) {
            Writer.println("There’s no passage that way.");
            return;
        }

        if (door.isLocked()) {
            Writer.println("The door is locked. You might need a keycard.");
            return;
        }

        player.setCurrentRoom(door.getDestination());
        printLocationInformation();

        // Check for victory after entering a new room
        if (checkVictory()) {
            System.exit(0);  // End the game after the victory message prints
        }
    }

    /**
     * Checks if the player has achieved the victory condition.
     * The player wins by entering the Engine Room after unlocking all keycard doors.
     */
    private boolean checkVictory() {
        Room current = player.getCurrentRoom();

        // Win condition: player is in Engine Room/Reactor Core
        // after having collected all three keycards.
        if (current.getName().equalsIgnoreCase("Engine Room/Reactor Core")) {
            if (player.hasItem("Blue Keycard") && player.hasItem("Red Keycard") && player.hasItem("Green Keycard")) {
                Writer.println();
                Writer.println("You insert all three keycards into the reactor console...");
                Writer.println("The ship's systems roar back to life!");
                Writer.println("Humanity will survive and you’ve reignited hope among the stars!");
                Writer.println();
                Writer.println("CONGRATULATIONS, YOU CAN NOW EXPLORE SPACE FOR A CURE TO SAVE HUMANITY!");
                score += 100;
                return true;
            }
        }

        return false;
    }
    /**
     * Checks if the ship has run out of oxygen and ends the game.
     */
    private boolean checkLoss() {
        if (total_number_of_turns >= MAX_TURNS_BEFORE_DEATH) {
            Writer.println();
            Writer.println("WARNING: Oxygen levels critical!");
            Writer.println("You collapse to the floor as the ship’s life‑support systems fail...");
            Writer.println("Darkness closes in as the Ecliptica drifts into the void.");
            Writer.println();
            Writer.println("YOU HAVE FAILED ");
            hasLost = true;
            return true;
        }
        return false;
    }


    /**
     * Prints out the current location and exits.
     */
    private void printLocationInformation() {
        Writer.println(player.getCurrentRoom().toString());
    }

    /**
     * Print out the closing message for the player.
     */
    private void printGoodbye() {
        Writer.println("Mission terminated. Humanity awaits the outcome of your voyage.");
        Writer.println("You have earned " + score + " points in " + total_number_of_turns + " turns.");
    }
    

    /**
     * Print out some help information. Here we print some stupid, cryptic
     * message and a list of the command words.
     */
    /**
     * Print out some help information and list of commands.
     */
    private void printHelp() {
        Writer.println("You are aboard the starship *Ecliptica*.");
        Writer.println("Explore rooms, find keycards, and restart the reactor to save humanity.");
        Writer.println();
        Writer.println("Available commands:");
        Writer.println("go <direction> - Move north, south, east, or west");
        Writer.println("look - Examine your surroundings");
        Writer.println("take <item> - Pick up an item in the room");
        Writer.println("use <item> - Use an item, like a keycard, to unlock a door");
        Writer.println("inventory - View items you’re carrying");
        Writer.println("help - Display this list again");
        Writer.println("quit - End the game");
    }


    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        Writer.println();
        Writer.println("Welcome aboard the starship *Ecliptica*.");
        Writer.println("You are humanity’s last hope — a lone explorer waking from cryofreeze.");
        Writer.println("Explore the ship, gather keycards, and restart the engines to save Earth.");
        Writer.println("Type 'help' for a list of commands.");
        Writer.println();
        printLocationInformation();
    }

    /**
     * "Quit" was entered. Check the rest of the command to see whether we
     * really quit the game.
     *
     * @param command
     *            The command to be processed.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        boolean wantToQuit = true;
        if (command.hasSecondWord()) {
            Writer.println("Quit what?");
            wantToQuit = false;
        }
        return wantToQuit;
    }
}