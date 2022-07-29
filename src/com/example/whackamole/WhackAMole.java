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
         * Given a location (X, Y), place a mole at that location.
         * Also update number of moles left.
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
            // if this is not specified here, molesLeft will remain (initialised at) 0, and code will break in main
            molesLeft = numberOfMoles;
            System.out.println("No mole hit, try again.");
        }
    }

    public void printGridToUser() {
        /*
         * Print the grid without showing where the moles are.
         * For every spot that has recorded a “whacked mole,”
         * print out a W, and * otherwise.
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
         * Print entire grid to display the 2d array to the user.
         * Moles are printed as an 'M'. Whacked moles are printed as
         * a 'W'. Cells that don’t have a mole are left as *.
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

        // 3) Allow user to enter X and Y coordinates where they want to whack
        //    Tell them they have a max of 50 attempts to get all moles.
        String welcomeMessage = String.format(
                """
                  WELCOME to a new game! You have %1$s attempts to get all %2$s of the moles in our 10x10 grid. 
                  
                  At any point, you can input the coordinates (-5, -5) to display the mole grid,
                  or input the coordinates (-1,-1) to surrender, which will terminate the game and
                  display the WhackAMole grid.
                  
                  Have fun!
                """, maxAttempts, gridDims
        );
        System.out.println(welcomeMessage);

        int i = 0;
        for (i = 0; i <= maxAttempts; i++) {
            if (i == maxAttempts) {
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
                } else if (userInputX > 9 || userInputX < -1 || userInputY > 9 || userInputY < -1
                ) {
                    System.out.println(String.format("""
                            Coordinates go from (0,0) to (9,9). Or (-1,-1) to surrender and (-5,-5) to display your progress grid. 
                            Your input (%1$s, %2$s) was out of bounds. 
                            Please try again.
                            """, userInputX, userInputY));
                } else {
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
