public class Tile {
    
    int value;

    /*
     * creates a tile using the given value. False jokers are not included in this game.
     */
    public Tile(int value) {
        this.value = value;
    }

    /*
     * TODO: should check if the given tile t and this tile have the same value @Berkantmahir
     * return true if they are matching, false otherwise
     */
    public boolean matchingTiles(Tile t) {
        return t.value == value;
    }

    /*
     * TODO: should compare the order of these two tiles @SelimArslan1
     * return 1 if given tile has smaller in value
     * return 0 if they have the same value
     * return -1 if the given tile has higher value
     */
    public int compareTo(Tile t) {
       return 0;
    }

    /*
     * TODO: should determine if this tile and given tile can form a chain together @CxTurkO
     * this method should check the difference in values of the two tiles
     * should return true if the absoulute value of the difference is 1 (they can form a chain)
     * otherwise, it should return false (they cannot form a chain)
     */
    public boolean canFormChainWith(Tile t) {
        // Calculate the difference in values between this tile and the given tile
        int valueDifference = Math.abs(this.value - t.getValue());
        
        // If the difference is 1, it means the tiles are consecutive and can form a chain
        // Return true if they can form a chain, otherwise return false
        return valueDifference == 1;
    }

    public String toString() {
        return "" + value;
    }

    public int getValue() {
        return value;
    }

}
