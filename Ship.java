class Ship {
    int maxQuantity, shipLength;
    String shipName;

    /**
     * @param length   The length of the invoking ship
     * @param quantity The maximum number of the invoking ship's quantity allowed on
     *                 the board
     * @param name     The name of the ship
     */
    Ship(int length, int quantity, String name) {
        this.maxQuantity = quantity;
        this.shipLength = length;
        this.shipName = name;
    }

    /**
     * @return The invoking ship's length
     */
    int getShipLength() {
        return this.shipLength;
    }

    /**
     * @return The maximum number of the invoking ship's quantity allowed on the
     *         board
     */
    int getMaxShipQuantity() {
        return this.maxQuantity;
    }

    /**
     * @return The name of the invoking ship
     */
    String getShipName() {
        return this.shipName;
    }

}
