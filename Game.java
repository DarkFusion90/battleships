import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.Scanner;

public class Game {
    Player player1;
    Player player2;

    // #region description = "List of ships allowed in the game"

    // Ship(length, maxQuantity, shipName)
    // Total Ships on board: 1+2+2+3+4 = 10
    public Ship carrier = new Ship(5, 1, "Carrier");
    public Ship battleship = new Ship(4, 2, "Battleship");
    public Ship cruiser = new Ship(3, 2, "Cruiser");
    public Ship submarine = new Ship(3, 2, "Submarine");
    public Ship destroyer = new Ship(2, 3, "Destroyer");
    // #endregion

    static public List<Ship> shipList = new ArrayList<>();
    public Scanner keyboard = new Scanner(System.in);
    final String NOTYETSHOT = "-", MISS = "0", HIT = "H", SUNK = "X", SHIP = "S";

    Game() {

        do {
            player1 = new Player();
            player2 = new Player();
            initializeShipList();

            System.out.println("\n\nWelcome to the epic game: Battleships!");
            System.out.println("Choose any one of the following: ");
            boolean invalidInput = true;
            String choice = null;

            while (invalidInput) {
                System.out.println("\n(1). Player vs Player\n(2). Player vs Computer");
                choice = keyboard.nextLine();
                invalidInput = !choice.equals("1") && !choice.equals("2");
            }

            if (choice.equals("1"))
                PlayerVsPlayer();
            else
                PlayerVsComputer();

            System.out.println("Press Y to play again. Else press any other key...");
            choice = keyboard.nextLine();

            if (!choice.equalsIgnoreCase("Y"))
                break;
        } while (true);
    }

    void initializeShipList() {
        shipList.add(carrier);
        shipList.add(battleship);
        shipList.add(cruiser);
        shipList.add(submarine);
        shipList.add(destroyer);
    }

    void PlayerVsComputer() {
        System.out.println("\nSsshhh! Old Grumpy Computer is sleeping! Come back later ;)\n");
    }

    void PlayerVsPlayer() {
        System.out.println("\n\nPlayer 1's turn to input: ");
        playerInput(player1);
        System.out.println("Input for Player 1 completed...\nDisplaying grid: \n");
        Game.sleep(1000);
        player1.showGrid();

        System.out.println("\n\n\n\nPlayer 2's turn to input: ");
        playerInput(player2);
        System.out.println("Input for Player 2 completed...\nDisplaying grid: \n");
        Game.sleep(1000);
        player2.showGrid();

        System.out.println("\nWe'll have a toss between the two players to decide who gets to shoot first:");
        System.out
                .println("HEAD signifies that Player 1 wins the toss.\nTAIL signifies that Player 2 wins the toss.\n");
        System.out.println("Press <ENTER> to continue: ");
        keyboard.nextLine();
        System.out.println("Performing Toss...");
        int tossResult = performToss();
        Game.sleep(3000);

        System.out.println("Toss result: " + (tossResult == 0 ? "HEAD" : "TAIL"));
        Player turn = tossResult == 0 ? player1 : player2;

        // displayShips(playerName);

        // continue shooting and switching turns until any one of the player(s) is
        // defeated
        do {

            System.out.println("\n" + (turn.equals(player1) ? "Player 1's" : "PLayer 2's") + " turn to shoot: \n");

            while (true) {
                Coordinate shotCoordinate = inputCoordinates(9, 9);
                String response = shoot(turn, shotCoordinate);
                if (response.equals("valid")) {
                    System.out.println("Press <ENTER> to continue...");
                    keyboard.nextLine();
                    break;
                } else {
                    System.out.println("\nEnter the co-ordinates again...\n");
                }
            }

            if (player1.isDefeated()) {
                System.out.println("All ships of Player 1 has been sunk...");
                System.out.println("Winner: Player 2\nPress <ENTER> to continue...");
                keyboard.nextLine();

                System.out.println("Displaying Player 1's Grid: ");
                player1.showGrid();
                Game.sleep(1000);

                System.out.println("Displaying Player 2's Grid: ");
                player2.showGrid();
                Game.sleep(1000);
                break;
            }

            if (player2.isDefeated()) {
                System.out.println("All ships of Player 2 has been sunk...");
                System.out.println("Winner: Player 1\nPress <ENTER> to continue...");
                keyboard.nextLine();

                System.out.println("Displaying Player 2's Grid: ");
                player2.showGrid();
                Game.sleep(1000);

                System.out.println("Displaying Player 1's Grid: ");
                player1.showGrid();
                Game.sleep(1000);
                break;
            }

            turn = switchTurn(turn);

        } while (true);

        System.out.println("\nWe've come to the end of the game...\n");
    }

    /**
     *
     * @param currentPlayer The Player instance of the current player
     */
    void playerInput(Player currentPlayer) {
        for (Ship ship : Game.shipList) {
            // Example: "2 Cruiser ships of length 4"
            int length = ship.getShipLength(), quantity = ship.getMaxShipQuantity();
            String name = ship.getShipName();

            System.out.println("\nYou need to enter: ");
            System.out.println("" + quantity + " " + name + " ship(s) of length: " + length + "\n");
            List<List<Coordinate>> currentShipList = new ArrayList<>();
            while (quantity-- > 0) {
                List<Coordinate> currentShip = addShip(length, currentPlayer);
                currentShipList.add(currentShip);
                addShipToGrid(currentPlayer, currentShip);

                System.out.println("Succesfully created ship: " + name + "\nQuantity Remaining: " + quantity + "\n");
                Game.sleep(750);
            }
            currentPlayer.addShipToFleet(name, currentShipList);
        }
    }

    /**
     * @param length        Length of the ship to be added
     * @param currentPlayer The Player object of the current player
     */
    List<Coordinate> addShip(int length, Player currentPlayer) {
        final int HORIZONTAL = 1, VERTICAL = 2;
        List<Coordinate> shipCoordinates = new ArrayList<>();

        Coordinate rootCoordinate = new Coordinate(0, 0);
        int horizontalOrVertical = -1;

        do {
            System.out.println("Choose orientation: \n(1). Horizontal\n(2). Vertical");
            String temporary = keyboard.nextLine();

            try {
                horizontalOrVertical = Integer.parseInt(temporary);
            } catch (NumberFormatException nfe) {
                continue;
            }

            if (horizontalOrVertical > 0 && horizontalOrVertical < 3)
                break;

        } while (true);

        if (horizontalOrVertical == HORIZONTAL)
            rootCoordinate = inputCoordinates(10 - length, 9);
        else if (horizontalOrVertical == VERTICAL)
            rootCoordinate = inputCoordinates(9, 10 - length);

        System.out.println("\nEntered cordinates: [" + rootCoordinate.getX() + "," + rootCoordinate.getY() + "]");

        while (true) {
            List<Coordinate> tempShip = new ArrayList<>();
            tempShip.clear();

            for (int i = 0; i < length; i++) {
                switch (horizontalOrVertical) {
                case HORIZONTAL:
                    tempShip.add(new Coordinate(rootCoordinate.getX() + i, rootCoordinate.getY()));
                    break;
                case VERTICAL:
                    tempShip.add(new Coordinate(rootCoordinate.getX(), rootCoordinate.getY() + i));
                    break;
                }
            }

            boolean validShip = true;
            // if the list of ships of the current player is empty
            // skip checking possible overlapping of ships in the grid
            if (currentPlayer.fleetList.isEmpty()) {
                shipCoordinates = tempShip;
                break;
            }

            /*
             * if the entered ship coordinates overlap with any of the coordinates of the
             * currentPlayer's ships in the fleet, it is not a valid Ship and thus, we retry
             * adding shipCoordinates. Also, inform the user accordingly
             */

            for (Map.Entry<String, List<List<Coordinate>>> ships : currentPlayer.fleetList.entrySet()) {
                for (List<Coordinate> curentShipCoords : ships.getValue()) {
                    for (Coordinate coord : curentShipCoords) {
                        if (containsCoordinate(tempShip, coord)) {
                            validShip = false;

                            // gets the first matching coordinate using Stream API
                            Coordinate firstMatchingCoord = tempShip.stream().filter(e -> e.equalsCoord(coord))
                                    .findFirst().get();

                            // print the first matching coordinate
                            System.out.println("There's a ship already present in [" + firstMatchingCoord.getX() + ","
                                    + firstMatchingCoord.getY() + "]");
                            System.out.println("Please enter the coordinates again\n");
                            // addShip again until valid Ship coord is entered
                            return addShip(length, currentPlayer);
                        }
                    }
                }
            }

            if (validShip) {
                shipCoordinates = tempShip;
                break;
            }
        }
        return shipCoordinates;
    }

    String shoot(Player currentplayer, Coordinate shotCoordinate) {
        Player opponentPlayer = getOpponentPlayer(currentplayer);
        String coordinateValue = getValueInCoord(opponentPlayer, shotCoordinate);
        if (coordinateValue.equals(NOTYETSHOT)) {
            System.out.println("That's a Miss!");
            updateGrid(opponentPlayer, shotCoordinate, MISS);
            return "valid";
        } else if (coordinateValue.equals(MISS)) {
            System.out.println("You've already shot there! It was a miss.");
            return "invalid";
        } else if (coordinateValue.equals(HIT)) {
            System.out.println("You've already shot there! It was a hit.");
            return "invalid";
        } else if (coordinateValue.equals(SUNK)) {
            System.out.println("You've already shot there! That's one of the ships you sunk.");
            return "invalid";
        }
        // coordinateValue.equals(SHIP);
        else {
            System.out.println("Thats a Hit!");
            updateGrid(opponentPlayer, shotCoordinate, HIT);
            return "valid";
        }
    }

    /**
     *
     * @param maximumX The maximum X-Coordinate allowed to entered
     * @param maximumY The maximum Y-Coordinate allowed to entered
     * @return Coordinate object with the entered co-ordinate value(X and Y)
     */
    Coordinate inputCoordinates(int maximumX, int maximumY) {
        int X, Y;

        while (true) {
            System.out.print("Enter X Co-ordinate (0-" + maximumX + "): ");
            String temporary = keyboard.nextLine();
            try {
                X = Integer.parseInt(temporary);
            } catch (NumberFormatException nfe) {
                continue;
            }
            // if input is correct, stop trying and exit the loop
            if (X >= 0 && X <= maximumX)
                break;
        }

        while (true) {
            System.out.print("Enter Y Co-ordinate (0-" + maximumY + "): ");
            String temporary = keyboard.nextLine();

            try {
                Y = Integer.parseInt(temporary);
            } catch (NumberFormatException nfe) {
                continue;
            }
            // if input is correct, stop trying and exit the loop
            if (Y >= 0 && Y <= maximumY)
                break;
        }

        return new Coordinate(X, Y);
    }

    /**
     * 
     * @param player     The current player playing the game
     * @param coordinate The coordinate whose content needs to be determined
     * @return The content in the Coordinate (NOTYETSHOT, HIT, MISS, SUNK or SHIP)
     */
    String getValueInCoord(Player currentPlayer, Coordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();
        return currentPlayer.grid[x][y];
    }

    /**
     *
     * @param invokingList The list to check for coordinates to match the next
     *                     parameter
     * @param coordinate   The coordinate to be matched with the listof coordinates
     *                     (invokingList)
     * @return True if atleast one element e is present in invokingList such that
     *         coordinate == e. Else, returns false
     */
    boolean containsCoordinate(List<Coordinate> invokingList, Coordinate coordinate) {
        for (Coordinate coord : invokingList) {
            if (Coordinate.checkIfEqual(coord, coordinate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param currentPlayer   The current player playing the game
     * @param shipCoordinates The coordinate(s) where ships need to be added
     */
    void addShipToGrid(Player currentPlayer, List<Coordinate> shipCoordinates) {
        for (Coordinate coordinate : shipCoordinates) {
            int x = coordinate.getX();
            int y = coordinate.getY();
            currentPlayer.grid[x][y] = SHIP;
        }
    }

    /**
     * 
     * @param currentPlayer The current player playing the game
     * @param coordinate    The coordinate to be updated
     * @param value         The value with which the coordinate needs to be updated
     */
    void updateGrid(Player currentPlayer, Coordinate coordinate, String value) {
        int x = coordinate.getX();
        int y = coordinate.getY();
        currentPlayer.grid[x][y] = value;
    }

    /**
     * 
     * @param currentPlayer The current player's Player object
     * @return The opponent player's Player object
     */
    Player getOpponentPlayer(Player currentPlayer) {
        return currentPlayer == player1 ? player2 : player1;
    }

    /**
     * This function performs a toss using the 'Random' class by randomly choosing a
     * value in a particular range.
     * 
     * @return The toss result (HEAD or TAIL)
     */
    int performToss() {
        final int HEADS = 0, TAILS = 1;
        Random rand = new Random();
        int randInt = rand.nextInt(9);
        if (randInt >= 0 && randInt < 5)
            return HEADS;
        else
            return TAILS;
    }

    /**
     * 
     * @param currentPlayer The current Player
     * @return The opponent's Player object
     */
    Player switchTurn(Player currentPlayer) {
        return currentPlayer.equals(player1) ? player2 : player1;
    }

    static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }

    void displayShips(Player currentPlayer) {
        System.out.println("\n\nDisplaying Ships: ");
        for (Map.Entry<String, List<List<Coordinate>>> ships : player1.fleetList.entrySet()) {
            System.out.println(ships.getKey() + ": ");
            for (List<Coordinate> shipCoordinates : ships.getValue()) {
                System.out.print("[   ");
                for (Coordinate coord : shipCoordinates) {
                    System.out.print("[" + coord.getX() + "," + coord.getY() + "]  ");
                }
                System.out.println("   ]");
            }
        }
    }

    public static void main(String args[]) {
        new Game();
    }
}
