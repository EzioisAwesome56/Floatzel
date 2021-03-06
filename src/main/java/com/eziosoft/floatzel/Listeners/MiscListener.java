package com.eziosoft.floatzel.Listeners;

import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.GameStatus;
import com.eziosoft.floatzel.Timers.StockTimer;
import com.eziosoft.floatzel.Timers.TwitterPoster;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.StockUtil;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class MiscListener extends ListenerAdapter {

    private Timer timer;
    private GameStatus gameStatus = new GameStatus();
    private StockTimer epic = new StockTimer();
    private TwitterPoster tweet = new TwitterPoster();
    private Boolean kekbot = true;

    public static String kekoff = "lol kek died :(";

    @Override
    public void onReady(ReadyEvent event) {
        if (event.getJDA().getShardInfo().getShardId() == 1) {
            // check to see if the db is setup
            if (event.getJDA().getShardInfo().getShardId() == event.getJDA().getShardInfo().getShardTotal() - 1){
                Database.dbinit();
                try {
                    StockUtil.initStock();
                } catch (DatabaseException e){
                    Error.CatchOld(e);
                }
            }
            System.out.println("Floatzel is at your service, sir!");

            //Set timer to change "game" every 10 minutes.
            if (timer == null) {
                timer = new Timer();
                timer.schedule(gameStatus, 0, TimeUnit.MINUTES.toMillis(10));
                // also set the stocks to update every 15 minutes
                timer.schedule(epic, 0, TimeUnit.MINUTES.toMillis(15));
                // make the bot tweet every 30 minutes
                timer.schedule(tweet, 0, TimeUnit.MINUTES.toMillis(30));
            }
        }
    }

    /*@Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        if (event.getEntity().getId().equals("213151748855037953")) {
            if (event.getNewOnlineStatus().equals(OnlineStatus.OFFLINE) && kekbot) {
                Floatzel.jda.getTextChannelById("322114245632327703").sendMessage(kekoff).queue();
                kekbot = false;
            } else if (event.getNewOnlineStatus().equals(OnlineStatus.ONLINE) && !kekbot) {
                Floatzel.jda.getTextChannelById("322114245632327703").sendMessage("Holy shit its kekbot, hes back").queue();
                kekbot = true;
            }
        }
    }*/
}
