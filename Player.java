import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class Player {
    final int ROWS = 10, COLUMNS = 10;
    String[][] grid;
    final int totalShips;

    List<Coordinate> previousHitList = new ArrayList<>();
    List<Coordinate> previousMissList = new ArrayList<>();
    /*
     * Example: Map<"Cruiser" , List of Cruiser Ships<List of individual Cruiser
     * Ship's coordinates>>
     */
    Map<String, List<List<Coordinate>>> fleetList = new HashMap<>();

    Player() {
        this.grid = makeGrid();
        this.totalShips = 10;
    }

    String[][] makeGrid() {
        String grid[][] = new String[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                grid[i][j] = "-";
            }
        }
        return grid;
    }

    void showGrid() {

        printHyphens();
        StringBuffer str = new StringBuffer("");
        // printing the indices
        for (int i = 0; i < COLUMNS; i++) {
            str.append("| ").append(i).append(" ");
        }
        str.append("|   |");

        System.out.println(str.toString());
        printHyphens();

        for (int i = 0; i < ROWS; i++) {
            str = str.delete(0, str.length());
            for (int j = 0; j < COLUMNS; j++) {
                str.append("| ").append(this.grid[i][j]).append(" ");
            }
            str.append("| ").append(i).append(" |\n");
            System.out.print(str.toString());
            printHyphens();
        }
    }

    void printHyphens() {
        for (int i = 0; i < 45; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    void addShipToFleet(String shipName, List<List<Coordinate>> shipCoord) {
        this.fleetList.put(shipName, shipCoord);
    }

    boolean isDefeated() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                // S denotes a ship in the grid.
                // If the following condition is never true, it means that all ships in the grid
                // have been sunk
                // and the player invoking this function is defeated;
                if (this.grid[i][j].equals("S"))
                    return false;
            }
        }
        return true;
    }
}
