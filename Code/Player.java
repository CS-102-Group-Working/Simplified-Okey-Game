public class Player implements Cloneable {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the
                           // game
    }

    /*
     * TODO: checks this player's hand to determine if this player is
     * winning @Berkantmahir
     * the player with a complete chain of 14 consecutive numbers wins the game
     * note that the player whose turn is now draws one extra tile to have 15 tiles
     * in hand,
     * and the extra tile does not disturb the longest chain and therefore the
     * winning condition
     * check the assigment text for more details on winning condition
     */
    public boolean checkWinning() {
        return findLongestChain() >= 14;
    }

    /*
     * TODO: used for finding the longest chain in this player hand @SelimArslan1
     * this method should iterate over playerTiles to find the longest chain
     * of consecutive numbers, used for checking the winning condition
     * and also for determining the winner if tile stack has no tiles
     */
    public int findLongestChain() {
        int length = this.playerTiles.length;
        int longestChain = 0;
        int currentChain = 1;

        for(int i = 0; i < length - 1; i++) {
            if(this.playerTiles[i].getValue() == this.playerTiles[i + 1].getValue() - 1) {
                currentChain++;
            } else if(!(this.playerTiles[i].getValue() == this.playerTiles[i + 1].getValue())) {
                currentChain = 1;
            }
            if(currentChain > longestChain) {
                longestChain = currentChain;
            }
        }
        return longestChain;
    }

    /*
     * TODO: removes and returns the tile in given index position @Berkantmahir
     */
    public Tile getAndRemoveTile(int index) {
        Tile toBeRemoved = getTiles()[index];
        playerTiles[index] = null;

        for (int i = index; i < numberOfTiles; i++) {
            playerTiles[i] = playerTiles[i + 1];
        }

        return toBeRemoved;
    }

    /*
     * TODO: adds the given tile to this player's hand keeping the ascending
     * order @rifatarifogluIK
     * this requires you to loop over the existing tiles to find the correct
     * position,
     * then shift the remaining tiles to the right by one
     */
    public void addTile(Tile t) {
        boolean isPutted = false;

        for (int i = playerTiles.length - 1; i > 0 && !isPutted; i--) {
            if (playerTiles[i - 1].compareTo(t) > 0) {
                playerTiles[i] = playerTiles[i - 1];
            } else {
                playerTiles[i] = t;
                isPutted = true;
            }
        }

        if (!isPutted) {
            playerTiles[0] = t;
        }
    }

    /*
     * finds the index for a given tile in this player's hand
     */
    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if (playerTiles[i].matchingTiles(t)) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }
    /*
     * Additional method that returns the index of a tile with the same value as the parameter 
     */
    public int findPositionOfTile(int value) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if (playerTiles[i].getValue() == value) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    /*
     * displays the tiles of this player
     */
    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < 14; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
