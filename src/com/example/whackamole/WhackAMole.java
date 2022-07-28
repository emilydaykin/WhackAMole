package com.example.whackamole;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class WhackAMole {
    // Instance variables
    int score;
    int molesLeft;
    int attemptsLeft;
    char[][] moleGrid;  // 2d array (matrix)

    public WhackAMole(int numAttempts, int gridDimension) {  // the CONSTRUCTOR (with params to initialise attributes)!
        moleGrid = new char[gridDimension][gridDimension];
        attemptsLeft = numAttempts;  // when initialised, attempts left = number of (total) attempts
        // First fill grid with '*'s:
        for (char[] row: moleGrid) {
            Arrays.fill(row, '*');
        }
    }

    public boolean place(int x, int y) {
        /*
         * Given a location (x,y), place a mole at that location.
         * Return true if you can. (Also update number of moles left.)
         */

        // Place mole (if there isn't a mole there already):

        if (moleGrid[x][y] != 'M') {
            moleGrid[x][y] = 'M';
            // Update molesLeft (count number of moles currently in the grid, then deduct from 10):
            int numberOfMoles = 0;
            for (int row = 0; row < moleGrid.length; row++) {
                for (int col = 0; col < moleGrid.length; col++) {
                    if (moleGrid[row][col] == 'M') {
                        numberOfMoles++;
                    }
                }
            }
//			molesLeft = 10 - numberOfMoles;  // fails their tests??
            molesLeft = numberOfMoles;
            return true;
        } else {
            return false;
        }
    }

    public void whack(int x, int y) {
        /*
         * Given a location, take a whack at that location. If that location
         * contains a mole, the score, number of attemptsLeft, and molesLeft
         * are updated. If that location does not contain a mole, only
         * attemptsLeft is updated.
         */

        // Initialise score at 0: for every whack user makes that hits a mole, points go up by 1:
        score = 0;
        // count number of moles (that the user hasn't hit yet):
        int numberOfMoles = 0;
        for (int row = 0; row < moleGrid.length; row++) {
            for (int col = 0; col < moleGrid.length; col++) {
                if (moleGrid[row][col] == 'M') {
                    numberOfMoles++;
                }
            }
        }

        // if (x,y) contains mole: update score, molesLeft & attemptsLeft
        if (moleGrid[x][y] == 'M') {
            moleGrid[x][y] = 'W';  // change Mole to Whacked
            score++;
            molesLeft = numberOfMoles - 1;
            attemptsLeft--;
            if (molesLeft > 0) {
                System.out.println(String.format("Whoohoo! You whacked a mole! %s to go!", molesLeft));
            } else {
                // System.out.println("CONGRATULATIONS! You whacked the last mole! WELL DONE!");
                // System.out.println("Let's see where all the moles were lurking...");
                System.out.println("""
                        CONGRATULATIONS! You whacked the last mole! WELL DONE! \n
                        Let's see where all the moles were lurking...
                        """);
                printGrid();
            }
        }
        // else if (x,y) contains NO mole: update attemptsLeft
        else if (moleGrid[x][y] == 'W') {
            attemptsLeft--;
            System.out.println("Ah you've already whacked a mole there. You just lost an attempt, sorry!");
        } else {
            attemptsLeft--;
            molesLeft = numberOfMoles;  // if not specified here, molesLeft will remain (initialised at) 0, and code will break in main
            System.out.println("No mole hit, try again.");
        }
    }

    public void printGridToUser() {
        /*
         * Print the grid without showing where the moles are.
         * For every spot that has recorded a “whacked mole,”
         * print out a W, or * otherwise.
         */

        // Hide the 'M's (duplicate grid and convert them to '*'s)
        // (Deep copy):
        char[][] moleGridToUser = new char[moleGrid.length][];
        for ( int i = 0; i < moleGridToUser.length; i++ ) {
            moleGridToUser[i] = new char[moleGrid[i].length];
            System.arraycopy(moleGrid[i], 0, moleGridToUser[i], 0, moleGrid[i].length);
        }
        // change 'M's to '*'s:
        for (int row = 0; row < moleGridToUser.length; row++) {
            for (int col = 0; col < moleGridToUser.length; col++) {
                if (moleGridToUser[row][col] == 'M') {
                    moleGridToUser[row][col] = '*';
                }
            }
        }

        // Print out duplicate moleGrid:
        for (char[] row : moleGridToUser) {
            System.out.println(Arrays.toString(row));
        }
    }

    public void printGrid() {
        /*
         * Print grid completely. This dumps the 2d array on the screen.
         * The places where the moles are are printed as an 'M'. The places
         * where the moles have been whacked are printed as a 'W'. The places
         * that don’t have a mole are left as *.
         */

        // Print out moleGrid:
        for (char[] row : moleGrid) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static void main(String[] args) { // "a main method"
        // 1) Instantiate game with number of attempts and grid dimensions
        int maxAttempts = 50;
        int gridDims = 10;
        WhackAMole game = new WhackAMole(maxAttempts, gridDims);

        // 2) Randomly place 10 moles:
        Random randomInst = new Random();
        int coords = 0;
        for (coords = 0; coords < gridDims; coords++) {
            int randomX = randomInst.nextInt(gridDims);  // range: [0,9]
            int randomY = randomInst.nextInt(gridDims);
            game.place(randomX, randomY);
        }

        // 3) Allow user to enter x and y coords where they want to whack
        //    Tell them they have a max of 50 attempts to get all moles.
        // System.out.println("Welcome to a new game! You have " + maxAttempts + " attempts to "
        // 		+ "get all " + gridDims + " of the moles in our 10x10 grid. \n At any point, you can input the "
        // 		+ "coordinates (-5, -5) to display the mole grid, \n or input the coordinates (-1,-1) "
        // 		+ "to surrender, which will terminate the game and \n display the WhackAMole grid. "
        // 		+ "\n Have fun!");
        String welcomeMessage = String.format(
                """
                  Welcome to a new game! You have %1$s attempts to get all %2$s of the moles in our 10x10 grid. 
                  \n At any point, you can input the coordinates (-5, -5) to display the mole grid, \n or input 
                  the coordinates (-1,-1) to surrender, which will terminate the game and \n display the 
                  WhackAMole grid. \n Have fun!
                """, maxAttempts, gridDims
        );
        System.out.println(welcomeMessage);

        int i = 0;
        for (i = 0; i <= maxAttempts; i++) {
            if (i == maxAttempts) {
                // System.out.println("GAME OVER. You have used up all " + maxAttempts +
                // 		" attempts to whack all the moles. Displaying your game:");
                String gameOverMessage = String.format(
                        """
                          GAME OVER. You have used up all %s attempts to whack all the moles. 
                          Displaying your game:
                        """, maxAttempts
                );
                System.out.println(gameOverMessage);
                game.printGrid();
                break;
            } else {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter an X coordinate: ");
                int userInputX = scanner.nextInt();
                System.out.println("Please enter a Y coordinate: ");
                int userInputY = scanner.nextInt();
                if (userInputX == -1 && userInputY == -1) {
                    System.out.println("User surrendered. Displaying entire grid:");
                    game.printGrid();
                    break;
                } else if (userInputX == -5 && userInputY == -5) {
                    System.out.println("Displaying your game progress. 'W' is where you've successfully whacked a mole!");
                    game.printGridToUser();
                } else {
                    // System.out.println("Taking a whack at location (" + userInputX + ", " + userInputY + ")...");
                    System.out.println(String.format("Taking a whack at location (%1$s, %2$s)...", userInputX, userInputY));
                    game.whack(userInputX, userInputY);
                    if (game.molesLeft == 0) {
                        break;
                    }
                }
            }
        }
    }
}
