package com.eziosoft.floatzel.kekbot;

import com.eziosoft.floatzel.kekbot.Games.*;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class GameRegistry {
    // more code imported from kekbot
    private Map<String, Integer> registeredGames = new HashMap<>();

    /**
     * Ghetto way of getting game objects.
     */
    public GameRegistry() {
        registerGame(0,"tictactoe", "ttt", "tic-tac-toe", "tic tac toe");
        registerGame(1, "snail race", "sr", "snailrace");
        registerGame(2, "hangman", "hm");
        registerGame(3, "russian roulette", "rr", "russianroulette");
        registerGame(4, "connect four", "c4", "cf");
    }

    /**
     * Registers a game.
     * @param id The game's ID.
     * @param aliases The name/aliases for the game.
     */
    private void registerGame(int id, String... aliases) {
        for (int i = 0; i < aliases.length; i++) {
            if (registeredGames.containsKey(aliases[i])) {
                throw new IllegalArgumentException("There is already a game registered with this name/alias.");
            }
            registeredGames.put(aliases[i], id);
        }
    }

    /**
     * Gets a game based on its ID.
     * @param name Name/Alias of the game.
     * @param channel The channel where the game will be played.
     * @return The game object that will be played.
     */
    public Game getGame(String name, TextChannel channel) {
        if (hasGame(name)) {
            int gameID = registeredGames.get(name);
            switch (gameID) {
                case 0: return new TicTacToe(channel);
                case 1: return new SnailRace(channel);
                case 2: return new Hangman(channel);
                case 3: return new RussianRoulette(channel);
                case 4: return new ConnectFour(channel);
                default: throw new NullPointerException("No game found with this ID. How'd you manage to get this error anyway?");
            }
        } else throw new NullPointerException("No game found with this alias.");
    }

    /**
     * Checks if the registry contains a game.
     * @param name Name/Alias of the game.
     * @return Whether or not if the game exists.
     */
    public boolean hasGame(String name) {
        return registeredGames.containsKey(name);
    }
}
