import java.util.Arrays;
import java.util.Random;

public class SimplifiedOkeyGame {

    private Player[] players;
    private Tile[] tiles;
    private int tileCount;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public SimplifiedOkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // four copies of each value, no jokers
        for (int i = 1; i <= 26; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i);
            }
        }

        tileCount = 104;
    }

    /*
     * TODO: distributes the starting tiles to the players @CxTurkO
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles, this method assumes the tiles are already
     * shuffled
     */
    public void distributeTilesToPlayers() {
        // Starting player gets 15 tiles
        for (int i = 0; i < 15; i++) {
            players[0].addTile(tiles[--tileCount]);

        }

        // Other players get 14 tiles each
        for (int i = 1; i < players.length; i++) {
            for (int j = 0; j < 14; j++) {
                players[i].addTile(tiles[--tileCount]);
            }
        }

        for (int i = 1; i < 4; i++) {
            players[i].getTiles()[14] = ApplicationMain.NULL_TILE;
        }
    }

    /*
     * TODO: get the last discarded tile for the current player @Berkantmahir
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we
     * picked
     */
    public String getLastDiscardedTile() {
        players[currentPlayerIndex].addTile(lastDiscardedTile);
        return lastDiscardedTile.toString();
    }

    /*
     * TODO: get the top tile from tiles array for the current player @SelimArslan1
     * that tile is no longer in the tiles array (this simulates picking up the top
     * tile)
     * and it will be given to the current player
     * returns the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {

        Tile currentTile = tiles[tileCount - 1];
        tiles[tileCount - 1] = null;
        tileCount--;
        players[currentPlayerIndex].addTile(currentTile);
        return currentTile.toString();
    }

    /**
     * TODO: should randomly shuffle the tiles array before game starts @CxTurkO
     * @Fisher-Yates method for shuffle
     */
    public void shuffleTiles() {
        
        Random random = new Random();

        // loop through the tiles array
        for (int i = 0; i < tileCount; i++) {
            int j = i + random.nextInt(tileCount - i);
            Tile temp = tiles[i];
            tiles[i] = tiles[j];
            tiles[j] = temp;
        }
    }

    /*
     * TODO: check if game still continues, should return true if current
     * player @Berkantmahir
     * finished the game. use checkWinning method of the player class to determine
     */
    public boolean didGameFinish() {
        for (Player p : players) {
            if (p.checkWinning()) {
                return true;
            }
        }
        return false;
    }

    /*
     * TODO: finds the player who has the highest number for the longest
     * chain @rifatarifogluIK
     * if multiple players have the same length may return multiple players
     */
    public Player[] getPlayerWithHighestLongestChain() {
        boolean[] winner = new boolean[players.length];
        int biggest = Integer.MIN_VALUE;
        int count = 0;

        for (int i = 0; i < players.length; i++) {
            if (biggest < players[i].findLongestChain()) {
                Arrays.fill(winner, false);
                winner[i] = true;
                biggest = players[i].findLongestChain();
                count = 1;
            } else if (biggest == players[i].findLongestChain()) {
                winner[i] = true;
                count++;
            }
        }

        Player[] winnerPlayers = new Player[count];

        for (int i = 0, j = 0; i < players.length; i++) {
            if (winner[i]) {
                winnerPlayers[j++] = players[i];
            }
        }

        return winnerPlayers;
    }

    /*
     * checks if there are more tiles on the stack to continue the game
     */
    public boolean hasMoreTileInStack() {
        return tileCount != 0;
    }

    /*
     * TODO: pick a tile for the current computer player using one of the
     * following: @rifatarifogluIK - @SelimArslan1
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * you should check if getting the discarded tile is useful for the computer
     * by checking if it increases the longest chain length, if not get the top tile
     */
    public void pickTileForComputer() throws CloneNotSupportedException {
        Player currentPlayer = players[currentPlayerIndex];
        Player imaginePlayer = (Player) currentPlayer.clone();
        Tile temp = new Tile(lastDiscardedTile.getValue());
        String pickedTile = "";

        imaginePlayer.addTile(temp);
        int imagineChainLength = imaginePlayer.findLongestChain();

        if (imagineChainLength > currentPlayer.findLongestChain()) {
            pickedTile = getLastDiscardedTile();
        } else {
            pickedTile = getTopTile();
        }
        System.out.println(currentPlayer.getName() + " picked " + pickedTile);
    }

    /*
     * TODO: Current computer player will discard the least useful
     * tile. @SelimArslan1 - @rifatarifogluIK
     * you may choose based on how useful each tile is
     */
    public void discardTileForComputer() {
        
        boolean isDone = false;

        for (int i = 0; i < 15 && !isDone; i++) {
            for (int j = i + 1; j < 15 && !isDone; j++) {
                if (players[currentPlayerIndex].getTiles()[i]
                        .compareTo(players[currentPlayerIndex].getTiles()[j]) == 0) {
                    discardTile(i);
                    isDone = true;
                }
            }
        }
        if (!isDone) {
            int intervalCount = 13;
            int intervalLength = 14;
            int[] counter = new int[intervalCount];

            for (Tile tile : players[currentPlayerIndex].getTiles()) {
                for (int i = 0; i < intervalCount; i++) {
                    if (tile.getValue() >= i + 1 && tile.getValue() < i + 1 + intervalLength) {
                        counter[i]++;
                    }
                }
            }
            int mostFilled = maxIndex(counter);
            int leastFilled = minIndex(counter);

            if (mostFilled > leastFilled) {
                discardTile(leastFilled);
            } else {
                discardTile(leastFilled + 13);
            }
        }
    }

    private static int maxIndex(int[] arr) {
        int max = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    private static int minIndex(int[] arr) {
        int min = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;

    }

    /*
     * TODO: discards the current player's tile at given index @Berkantmahir
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        lastDiscardedTile = players[currentPlayerIndex].getTiles()[tileIndex];

        System.out.println(players[currentPlayerIndex].getName() + " discarded " + lastDiscardedTile.getValue());

        for (int i = tileIndex; i < players[currentPlayerIndex].getTiles().length - 1; i++) {
            players[currentPlayerIndex].getTiles()[i] = players[currentPlayerIndex].getTiles()[i + 1];
        }

        players[currentPlayerIndex].getTiles()[14] = ApplicationMain.NULL_TILE;
    }

    public void displayDiscardInformation() {
        if (lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if (index >= 0 && index <= 3) {

            players[index] = new Player(name);

            for (int i = 0; i < 15; i++) {
                players[index].getTiles()[i] = ApplicationMain.NULL_TILE;
            }

        }
    }

}
