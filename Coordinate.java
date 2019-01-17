class Coordinate {
    private int X, Y;

    /**
     * 
     * @param x The abscissa (x - coordinate) of the Coordinate object
     * @param y The ordinate (y - coordinate) of the Coordinate object
     */
    Coordinate(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    /**
     * 
     * @return The abscissa (x - coordinate) of the Coordinate object
     */
    int getX() {
        return this.X;
    }

    /**
     * 
     * @return The ordinate (y - coordinate) of the Coordinate object
     */
    int getY() {
        return this.Y;
    }

    /**
     * 
     * @param coord1 Coordinate object to be compared
     * @param coord2 Another Coordinate object to be compared
     * @return If both coord1 and coord2 hold the same X and Y values
     */
    static boolean checkIfEqual(Coordinate coord1, Coordinate coord2) {
        return coord1.equalsCoord(coord2);
    }

    public boolean equalsCoord(Coordinate coordinate) {
        return (this.X == coordinate.X) && (this.Y == coordinate.Y);
    }
}
