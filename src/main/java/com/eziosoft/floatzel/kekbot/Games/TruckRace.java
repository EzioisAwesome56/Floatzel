package com.eziosoft.floatzel.kekbot.Games;

import com.eziosoft.floatzel.Util.Utils;
import com.eziosoft.floatzel.kekbot.KekGlue.CustomEmote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.math3.util.Precision;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TruckRace extends Game{
    private Message race;
    private int[] snails;
    private int[] movements = {-4,-5,6,0,1,3,-2,-2,3,1,1,1,1,1,1,0,0,0,5,-5,-4,2,-8,-5,2,3,9,-7,-5,9,0,0,0,0,-4,-7,-10,5,0,0,0,-6,4,5,1,-2,-3,-6,5,4,9,8,13,-3,-4,-4};
    private int[] move;
    private Timer timer = new Timer();
    private Random random = new Random();
    private long startTime;
    private boolean finished = false;
    private List<User> winners = new ArrayList<>();
    private final String snail = "\uD83D\uDEFB";
    private final String flag = "\uD83C\uDFF4";

    public TruckRace(TextChannel channel) {
        super(2,6, false, channel, "Truck Race", true);
    }

    @Override
    public void startGame() {
        startTime = System.currentTimeMillis();
        snails = new int[players.size()];
        move = new int[snails.length];
        prepareMessage();
    }

    @Override
    public String getRules() {
        return "It's the next level from snail race! Bigger rewards, higher stakes! Race your pickup truck to the finish!";
    }

    private void prepareMessage() {
        channel.sendMessage(drawRace()).queue(message -> race = message);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                race();
            }
        }, TimeUnit.SECONDS.toMillis(2), TimeUnit.SECONDS.toMillis(2));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                multiplier = Precision.round(multiplier + .1, 2);
            }
        }, TimeUnit.SECONDS.toMillis(30), TimeUnit.SECONDS.toMillis(30));
    }

    private String positionSnail(int pos, int placement) {
        String line = "------------------------------------------------------------";
        return line.substring(0, pos) + snail + line.substring(pos, line.length()) + (placement == 0 ? flag : CustomEmote.getTrophy(placement - 1));
    }

    private int getPlacement(User player) {
        if (winners.size() > 0) {
            for (int i = 0; i < winners.size(); i++) {
                if (winners.get(i).equals(player)) return i + 1;
            }
            return 0;
        } else return 0;
    }

    private String drawRace() {
        String space = "               ";
        String currentTime = Utils.convertMillisToHMmSs(System.currentTimeMillis() - startTime);
        StringBuilder builder = new StringBuilder();
        if (!finished) builder.append("***" + getString("game.snailrace.start") + "***");
        else builder.append("***" + getString("game.snailrace.end") + "***");
        builder.append(" (Time Spent: ").append(currentTime).append(")").append("\n\n");
        for (int i = 0; i < players.size(); i++) {
            User player = players.get(i);
            builder.append("`").append(
                            (player.getName().length() > 15 ?
                                    player.getName().substring(0, 11) + "..." :
                                    (player.getName().length() < 15 ? player.getName() + space.substring(0, 14-player.getName().length()) :
                                            player.getName())))
                    .append(":` ").append(positionSnail(snails[i], getPlacement(player)));
            if (!finished) {
                if (move[i] == -1) builder.append("***-1***");
                else if (move[i] == 0) builder.append("***0***");
                else builder.append("***+").append(move[i]).append("***");
            }
            builder.append("\n");
        }
        if (multiplier > 1) {
            builder.append("\n");
            builder.append("**" + getString("game.multiplier", multiplier) + "**");
        }
        return builder.toString();
    }

    private void race() {
        for (int i = 0; i < snails.length; i++) {
            if (snails[i] != 60) {
                int movement = movements[random.nextInt(movements.length)];
                if (movement == -1 && snails[i] != 0) {
                    snails[i] += movement;
                    move[i] = movement;
                } else if (movement >= 0) {
                    if (snails[i] + movement > 60) {
                        move[i] = 0;
                    } else {
                        snails[i] += movement;
                        move[i] = movement;
                    }
                } else move[i] = 0;
            } else move[i] = 0;
        }
        check();
    }

    private void check() {
        int finished = 0;
        for (int i = 0; i < snails.length; i++) {
            if (snails[i] == 60) {
                if (!winners.contains(players.get(i))) {
                    winners.add(players.get(i));
                    addWinner(players.get(i));
                }
                finished++;
            }
        }
        race.editMessage(drawRace()).queue(message -> race = message);
        if (players.size() == 2 && finished == 1) endGame(false);
        if (players.size() == 3 && finished == 2) endGame(false);
        if (players.size() > 3 && finished == 3) endGame(false);
        if (finished == players.size()) endGame(true);
    }

    @Override
    public void endTie() {
        timer.cancel();
        finished = true;
        race.editMessage(drawRace()).queue();
        super.endTie();
    }

    private void endGame(boolean tie) {
        if (!tie) endGame(winners, Precision.round(ThreadLocalRandom.current().nextInt(0, 150), 2), ThreadLocalRandom.current().nextInt(4, 8));
            //Ties are super rare to get, thus we up the rewards.
        else endTie(Precision.round(ThreadLocalRandom.current().nextInt(100, 200), 2), ThreadLocalRandom.current().nextInt(5, 8));
        timer.cancel();
        finished = true;
        race.editMessage(drawRace()).queue();
    }
}
