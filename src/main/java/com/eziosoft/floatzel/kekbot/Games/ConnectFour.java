package com.eziosoft.floatzel.kekbot.Games;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.Random;

public class ConnectFour extends Game{

    // game logic was hard to make
    // so i borrowed code from https://ssaurel.medium.com/creating-a-connect-four-game-in-java-f45356f1d6ba
    // for win checking

    private Random random = new Random();
    private int turn;

    private int[][] board = {
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0},
            // dummy board column to trip the for loop because oh my god
            {6,6,6,6,6,6,6}
    };

    private char[][] grid;

    private String[] tiles = {"⚪", "\uD83D\uDFE1", "\uD83D\uDD34"};

    private int lastCol = -1, lastTop = -1;

    private String horizontal() {
        return new String(grid[lastTop]);
    }

    private String slashDiagonal() {
        StringBuilder sb = new StringBuilder(6);

        for (int h = 0; h < 6; h++) {
            int w = lastCol + lastTop - h;

            if (0 <= w && w < 7) {
                sb.append(grid[h][w]);
            }
        }

        return sb.toString();
    }

    private String backslashDiagonal() {
        StringBuilder sb = new StringBuilder(6);

        for (int h = 0; h < 6; h++) {
            int w = lastCol - lastTop + h;

            if (0 <= w && w < 7) {
                sb.append(grid[h][w]);
            }
        }

        return sb.toString();
    }

    private boolean isWinningPlay() {
        if (lastCol == -1) {
            System.err.println("No move has been made yet");
            return false;
        }

        char sym = grid[lastTop][lastCol];
        // winning streak with the last play symbol
        String streak = String.format("%c%c%c%c", sym, sym, sym, sym);

        // check if streak is in row, col, diagonal or backslash diagonal
        return contains(horizontal(), streak) ||
                contains(vertical(), streak) ||
                contains(slashDiagonal(), streak) ||
                contains(backslashDiagonal(), streak);
    }

    private static boolean contains(String str, String substring) {
        return str.indexOf(substring) >= 0;
    }

    private String vertical() {
        StringBuilder sb = new StringBuilder(6);

        for (int h = 0; h < 6; h++) {
            sb.append(grid[h][lastCol]);
        }

        return sb.toString();
    }

    private int h = 6;
    private int w = 7;

    private static final char[] tileschar = {'R', 'Y'};

    public ConnectFour(TextChannel channel){
        super(2, true, channel, "Connect Four", false);
    }

    @Override
    public void startGame() {
        // i am going to steal this code and you cant stop me
        grid = new char[h][];
        for (int i = 0; i < h; i++) {
            Arrays.fill(grid[i] = new char[w], '0');
        }
        // decide who goes first
        turn = random.nextInt(2);
        if (players.size() < getMaxNumberOfPlayers()){
            multiplier = 1.1D;
            channel.sendMessage("You are " + tiles[1] + "\n" +
                    "Floatzel is " + tiles[2]).queue();
            if (turn == 1){
                channel.sendMessage("**Floatzel gets the first move!**").queue();
                doAi();
            } else {
                channel.sendMessage("**" + players.get(turn).getName() + " gets the first move!**").queue();
            }
        } else {
            // set a multiplyer of 1.5 because this isnt a bot xd
            multiplier = 1.5D;
            channel.sendMessage("**" + players.get(turn).getName() + " gets the first move!**").queue();
        }
        if (players.size() < getMaxNumberOfPlayers()){
            channel.sendMessage(makeBoard() + "\n" + players.get(0).getName() + ", your turn!").queue();
        } else {
            channel.sendMessage(makeBoard()).queue();
        }

    }

    private String makeBoard(){
        StringBuilder b = new StringBuilder();
        for(int y = 0; y < 6; y++){
            for (int x = 0; x < 7; x++){
                b.append(tiles[board[y][x]]);
            }
            b.append("\n");
        }
        return b.toString();
    }

    @Override
    public void acceptInputFromMessage(Message message) {
        if (turn + 1 == getPlayerNumber(message.getAuthor())){
            int move;
            try {
                move = Integer.parseInt(message.getContentRaw()) - 1;
            } catch (NumberFormatException e){
                if (message.getContentRaw().length() < 3) {
                    channel.sendMessage("That is not a valid move! please enter 1-7!").queue();
                }
                return;
            }
            if (move + 1 < 1 || move + 1 > 7){
                channel.sendMessage("That is not a valid move! please enter 1-7!").queue();
                return;
            }
            // put our chip in the slot
            int y = getY(move);
            if (board[0][move] > 0){
                channel.sendMessage("That row is full! Please select a different row!").queue();
                return;
            }
            putChip(move, y, turn + 1);
            if (isWinningPlay()){
                channel.sendMessage(makeBoard() + "\n**" + players.get(turn).getName() + " has won the game!**").queue();
                endGame(players.get(turn), Double.parseDouble(Integer.toString(random.nextInt(27))), 1);
                return;
            }
            turn++;
            // run the ai if needed
            if (players.size() < getMaxNumberOfPlayers()){
                doAi();
                // check if that was a winnning play
                if (isWinningPlay()){
                    channel.sendMessage("**Floatzel has won the game! Somehow!**").queue();
                    endGame();
                    return;
                }
            }
            if (turn > 1) turn = 0;
            channel.sendMessage(makeBoard() + "\n" + players.get(turn).getName() + ", your turn!").queue();
        } else {
            if (message.getContentRaw().length() < 3) channel.sendMessage("It is not your turn!").queue();
        }
    }

    private void doAi(){
        channel.sendMessage("**Floatzel is thinking...**").queue();
        channel.sendTyping().queue();
        // has the play even played any chips yet lmao?
        if (lastCol == -1){
            // randomly pick a place to put a chip
            int e = random.nextInt(7);
            putChip(e, getY(e), turn + 1);
            turn--;
            return;
        } else {
            // pick a random direction to place the chip (either left, above, or right of the player's chip)
            int act = random.nextInt(4);
            switch (act){
                case 0:
                    try {
                        putChip(lastCol + 1, getY(lastCol + 1), turn + 1);
                    } catch (ArrayIndexOutOfBoundsException e){
                        // randomly pick a fallback between putting it ontop or a random position
                        int dank = random.nextInt(8);
                        if (dank == 5){
                            putChip(lastCol - 1, getY(lastCol - 1), turn + 1);
                        } else {
                            dank = random.nextInt(7);
                            putChip(dank, getY(dank), turn + 1);
                        }
                    }
                    break;
                case 1:
                    try {
                        putChip(lastCol, getY(lastCol), turn + 1);
                    } catch (ArrayIndexOutOfBoundsException e){
                        // randomly pick a fallback between putting it ontop or a random position
                        int dank = random.nextInt(8);
                        if (dank == 5){
                            putChip(lastCol - 1, getY(lastCol - 1), turn + 1);
                        } else {
                            dank = random.nextInt(7);
                            putChip(dank, getY(dank), turn + 1);
                        }
                    }
                    break;
                case 2:
                    putChip(lastCol, getY(lastCol), turn + 1);
                    break;
                case 3:
                    int dank = random.nextInt(7);
                    putChip(dank, getY(dank), turn + 1);
                    break;
            }
        }
        // add 1 to turn
        turn++;
    }

    private int getY(int row){
        int y = 0;
        for (int e = 0; e < 5; e++){
            if (board[e + 1][row] > 0){
                break;
            }
            y++;
        }
        return y;
    }

    private void putChip(int x, int y, int turn){
        lastCol = x;
        lastTop = y;
        grid[lastTop][lastCol] = tileschar[turn - 1];
        board[y][x] = turn;
    }

    @Override
    public String getRules() {
        return "The classic game of four in a row! Drop your color chip into one of the 7 rows in order to try and line up 4 in a row!\n" +
                "you can drop your chip by sending a message with the number of row you want to put it in, from left to right!";
    }
}
