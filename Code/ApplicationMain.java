import java.util.Scanner;

public class ApplicationMain {
    public static final Tile NULL_TILE = new Tile(Integer.MAX_VALUE); // Using null tile as a different tile to avoid exceptions.
    public static boolean firstTurn = true;

    public static void main(String[] args) throws CloneNotSupportedException {
        Scanner sc = new Scanner(System.in);
        SimplifiedOkeyGame game = new SimplifiedOkeyGame();

        System.out.print("Please enter your name: ");
        String playerName = sc.next();

        game.setPlayerName(0, playerName);
        game.setPlayerName(1, "John");
        game.setPlayerName(2, "Jane");
        game.setPlayerName(3, "Ted");

        game.createTiles();
        game.shuffleTiles();
        game.distributeTilesToPlayers();

        // developer mode is used for seeing the computer players hands, to be used for
        // debugging
        System.out.print("Play in developer's mode with other player's tiles visible? (Y/N): ");
        char devMode = sc.next().charAt(0);
        boolean devModeOn = devMode == 'Y';

        boolean gameContinues = true;
        int playerChoice = -1;

        while (gameContinues) {
            System.out.println();
            int currentPlayer = game.getCurrentPlayerIndex();
            System.out.println(game.getCurrentPlayerName() + "'s turn.");

            if (currentPlayer == 0) {
                // this is the human player's turn
                game.displayCurrentPlayersTiles();
                game.displayDiscardInformation();

                System.out.println("What will you do?");

                if (!firstTurn) {
                    // after the first turn, player may pick from tile stack or last player's
                    // discard
                    System.out.println("1. Pick From Tiles");
                    System.out.println("2. Pick From Discard");
                } else {
                    // on first turn the starting player does not pick up new tile
                    System.out.println("1. Discard Tile");
                }

                System.out.print("Your choice: ");
                playerChoice = getTwoChoices(sc);

                // after the first turn we can pick up
                if (!firstTurn) {

                    if (playerChoice == 1) {
                        System.out.println("You picked up: " + game.getTopTile());
                        firstTurn = false;
                    } else if (playerChoice == 2) {
                        System.out.println("You picked up: " + game.getLastDiscardedTile());
                    }

                    // display the hand after picking up new tile
                    game.displayCurrentPlayersTiles();
                } else {
                    // after first turn it is no longer the first turn
                    firstTurn = false;
                }

                gameContinues = !game.didGameFinish() && game.hasMoreTileInStack();

                if (gameContinues) {
                    // if game continues we need to discard a tile using the given index by the
                    // player
                    System.out.println("Which tile you will discard?");
                    System.out.print("Discard the tile in index.");
                    playerChoice = getPlayerChoice(sc);

                    // TODO: make sure the given index is correct, should be 0 <= index <= 14
                    // @CxTurkO

                    game.discardTile(playerChoice);
                    game.passTurnToNextPlayer();
                } else {
                    if (!game.didGameFinish()) {
                        // if we finish the hand we win
                        System.out.println("Congratulations, you win!");
                    } else {
                        // TODO: the game ended with no more tiles in the stack @rifatarifogluIK
                        // determine the winner based on longest chain lengths of the players
                        // use getPlayerWithHighestLongestChain method of game for this task
                        Player[] winners = game.getPlayerWithHighestLongestChain();
                        if (winners.length == 1) { // If winner is only one player
                            System.out.println("Winner: " + winners[0].getName());
                        } else { // Multiple winners
                            String output = "Winners: ";
                            for (Player player : winners) {
                                output += player.getName() + ", ";
                            }
                            System.out.println(output.substring(0, output.length() - 2));
                        }
                    }
                }
            } else {
                // this is the computer player's turn
                if (devModeOn) {
                    game.displayCurrentPlayersTiles();
                }

                // computer picks a tile from tile stack or other player's discard
                game.pickTileForComputer();

                gameContinues = !game.didGameFinish() && game.hasMoreTileInStack();

                if (gameContinues) {
                    // if game did not end computer should discard
                    game.discardTileForComputer();
                    game.passTurnToNextPlayer();
                } else {
                    if (!game.didGameFinish()) {
                        // current computer character wins
                        System.out.println(game.getCurrentPlayerName() + " wins.");
                    } else {
                        // TODO: the game ended with no more tiles in the stack @rifatarifogluIK
                        // determine the winner based on longest chain lengths of the players
                        // use getPlayerWithHighestLongestChain method of game for this task
                        Player[] winners = game.getPlayerWithHighestLongestChain();
                        if (winners.length == 1) { // If winner is only one player
                            System.out.println("Winner: " + winners[0].getName());
                        } else { // Multiple winners
                            String output = "Winners: ";
                            for (Player player : winners) {
                                output += player.getName() + ", ";
                            }
                            System.out.println(output.substring(0, output.length() - 2));
                        }
                    }
                }
            }
        }

        sc.close();
    }

    /**
     * A method that prompts the user to make a choice between 0 and 14 and returns
     * the choice
     * 
     * @param The Scanner object to read user input
     * @return The user's choice that an integer between 0 and 14
     */
    public static int getPlayerChoice(Scanner scanner) {
        int choice = -1;
        boolean isValidChoice = false;

        while (!isValidChoice) {
            System.out.print("Enter your choice (0 to 14): ");
            if (scanner.hasNextInt()) { // check the input whether integer or not
                int input = scanner.nextInt();
                if (input >= 0 && input <= 14) {
                    choice = input;
                    isValidChoice = true;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Please enter a valid integer.");
                scanner.next(); 
            }
        }

        return choice;
    }

    public static int getTwoChoices(Scanner in) {
        int choice = -1;
        System.out.print("Enter your choice (From above): ");
        while (!in.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            in.next();
        }
        if (firstTurn) {
            switch (in.nextInt()) {
                case 1:
                    choice = 1;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    choice = getTwoChoices(in); // for loop
            }
        } else {
            switch (in.nextInt()) {
                case 1:
                    choice = 1;
                    break;
                case 2:
                    choice = 2;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    choice = getTwoChoices(in); // for loop
            }
        }
        return choice;
    }

}