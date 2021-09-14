package com.eziosoft.floatzel.kekbot.Games;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Random;

public class ConnectFour extends Game{

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

    private String[] tiles = {"⚪", "\uD83D\uDFE1", "\uD83D\uDD34"};

    public ConnectFour(TextChannel channel){
        super(2, true, channel, "Connect Four", false);
    }
    @Override
    public void startGame() {
        // decide who goes first
        turn = random.nextInt(2);
        if (players.size() < getMaxNumberOfPlayers()){
            channel.sendMessage("The ai doesnt work yet, therefore, you win!").queue();
            endGame(players.get(0), 1D, 1);
            return;
        } else {
            channel.sendMessage("**" + players.get(turn).getName() + " gets the first move!**").queue();
        }
        channel.sendMessage(makeBoard()).queue();
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
            // TODO: check to see if we won
            turn++;
            if (turn > 1) turn = 0;
            channel.sendMessage(makeBoard() + "\n" + players.get(turn).getName() + ", your turn!").queue();
        } else {
            if (message.getContentRaw().length() < 3) channel.sendMessage("It is not your turn!").queue();
        }
    }

    private boolean check(int y, int row){
        int next;
        return false;
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
        board[y][x] = turn;
    }

    @Override
    public String getRules() {
        return "The classic game of four in a row! Drop your color chip into one of the 7 rows in order to try and line up 4 in a row!\n" +
                "you can drop your chip by sending a message with the number of row you want to put it in, from left to right!";
    }
}
