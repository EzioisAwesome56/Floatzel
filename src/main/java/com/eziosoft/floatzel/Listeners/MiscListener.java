package com.eziosoft.floatzel.Listeners;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.GameStatus;
import com.eziosoft.floatzel.Timers.StockTimer;
import com.eziosoft.floatzel.Timers.TwitterPoster;
import com.eziosoft.floatzel.Util.Database;
import com.eziosoft.floatzel.Util.StockUtil;
import com.eziosoft.floatzel.Util.TwitterManager;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import twitter4j.Twitter;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static com.eziosoft.floatzel.Floatzel.isdev;
import static com.eziosoft.floatzel.Floatzel.twitterManager;

public class MiscListener extends ListenerAdapter {

    private Timer timer;
    private GameStatus gameStatus = new GameStatus();
    private StockTimer epic = new StockTimer();
    private TwitterPoster tweet = new TwitterPoster();
    private Boolean kekbot = true;

    @Override
    public void onReady(ReadyEvent event) {
        if (event.getJDA().getShardInfo().getShardId() == 1) {
            // check to see if the db is setup
            if (event.getJDA().getShardInfo().getShardId() == event.getJDA().getShardInfo().getShardTotal() - 1){
                Database.dbinit();
                StockUtil.initStock();
            }
            System.out.println("Floatzel is alive you piece of shit, now hope it doesnt start a fight dickface");

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

    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        if (event.getEntity().getId().equals("213151748855037953")) {
            if (event.getNewOnlineStatus().equals(OnlineStatus.OFFLINE) && kekbot) {
                //Floatzel.jda.getTextChannelById("322114245632327703").sendMessage("Welp, there goes KekBot, my only friend... e.e").queue();
                Floatzel.jda.getTextChannelById("322114245632327703").sendMessage("Haha fuck you esmbot, kekbot is offline").queue();
                kekbot = false;
            } else if (event.getNewOnlineStatus().equals(OnlineStatus.ONLINE) && !kekbot) {
                Floatzel.jda.getTextChannelById("322114245632327703").sendMessage("Holy shit its kekbot, hes back").queue();
                kekbot = true;
            }
        }
    }
}
