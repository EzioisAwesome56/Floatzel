package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Config;
import com.eziosoft.floatzel.Floatzel;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.*;
import java.io.File;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TwitterManager extends ListenerAdapter {

    // stuff from kekbot we probably need
    private static Instant lastTweet;
    // floatzel stiuff resumes
    long[] admins = Config.groupa;
    long[] mods = Config.groupb;
    private Map<Long, Message> currentTweets = new HashMap<>();
    ConfigurationBuilder cb = new ConfigurationBuilder().setDebugEnabled(true)
            .setOAuthConsumerKey(Config.key)
            .setOAuthConsumerSecret(Config.secretkey)
            .setOAuthAccessToken(Config.access)
            .setOAuthAccessTokenSecret(Config.secretaccess);
    // move some of this shit down here i guess
    // also make a static copy of the cb
    private static ConfigurationBuilder oof = new ConfigurationBuilder().setDebugEnabled(true)
            .setOAuthConsumerKey(Config.key)
            .setOAuthConsumerSecret(Config.secretkey)
            .setOAuthAccessToken(Config.access)
            .setOAuthAccessTokenSecret(Config.secretaccess);
    private static final TwitterFactory tf = new TwitterFactory(oof.build());
    private static final Twitter tweeter = tf.getInstance();
            // hey the solution was to import twitter4j-stream in maven and problem solved!
            // i went ahead and added it to maven so you may need to reimport
            TwitterStream twitter = new TwitterStreamFactory(cb.build()).getInstance();
            StatusListener listener = new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    //We do need this tho
                    int rank = 0;
                    if (Arrays.stream(admins).anyMatch(l -> l == status.getUser().getId())) rank = 1;
                    else if (Arrays.stream(mods).anyMatch(l -> l == status.getUser().getId())) rank = 2;

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setThumbnail(status.getUser().getProfileImageURL());
                    builder.setColor(rank == 1 ? Color.RED : Color.BLUE);
                    builder.setTitle("New tweet by: " + status.getUser().getName(), "https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId());
                    builder.setAuthor("@" + status.getUser().getScreenName());
                    builder.setTimestamp(Instant.now());
                    if (status.getMediaEntities().length > 0) builder.setImage(status.getMediaEntities()[0].getMediaURL());
                    builder.setDescription(status.getText());

                    if (!Floatzel.isdev) {
                        Floatzel.jda.getTextChannelById(rank == 1 ? Config.achan : Config.bchan).sendMessage(builder.build()).queue(m -> currentTweets.put(status.getId(), m));
                    } else {
                        Floatzel.jda.getTextChannelById(Config.devchan).sendMessage(builder.build()).queue(m -> currentTweets.put(status.getId(), m));
                    }
                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                    //We may not need this
                    if (currentTweets.containsKey(statusDeletionNotice.getStatusId())) {
                        Message m = currentTweets.get(statusDeletionNotice.getStatusId());
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setColor(Color.GRAY);
                        builder.setTimestamp(m.getCreationTime());
                        builder.setTitle("This tweet was removed from Twitter.");
                        m.editMessage(builder.build()).queue();
                        currentTweets.remove(statusDeletionNotice.getStatusId());
                    }
                }

                @Override
                public void onTrackLimitationNotice(int i) {
                    //We may not need this
                }

                @Override
                public void onScrubGeo(long l, long l1) {
                    //We may not need this
                }

                @Override
                public void onStallWarning(StallWarning stallWarning) {
                    //We may not need this
                }

                @Override
                public void onException(Exception e) {
                    //We may not need this
                }
            };

    // borrowed from kekbot
    public static int tweet(String message) {
        try {
            tweeter.updateStatus(message);
            lastTweet = Instant.now();
        } catch (TwitterException e) {
            System.out.println("Twitter error occured, handling...");
            Error.CatchTweet(e);

            //If we get timed out, we try the same message again.
            if (e.getStatusCode() == -1) {
                tweet(message);
                return -1;
            } else if (e.getStatusCode() == 187){
                // duplicate
                // return is
                return 187;
            }

        }
        return 420;
    }

    // make a copy of the class, but have it take a file argument
    public static int tweet(String message, String path) {
        File image = new File(Floatzel.class.getResource(path).getFile());
        StatusUpdate update = new StatusUpdate(message);
        update.setMedia(image);
        try {
            tweeter.updateStatus(update);
            lastTweet = Instant.now();
        } catch (TwitterException e) {
            System.out.println("Twitter error occured!");
            System.out.println(e.getMessage());
            String endl = System.getProperty("line.separator");

            //If we get timed out, we try the same message again.
            if (e.getStatusCode() == -1) {
                tweet(message);
                return -1;
            } else if (e.getStatusCode() == 187){
                // okay so it was a dupelicate status
                // return it and try again
                return e.getStatusCode();
            }

        }
        return 420;
    }



    public TwitterManager() {}

    public void shutdown() {
        twitter.shutdown();
    }

    @Override
    public void onReady(ReadyEvent event) {
        if (event.getJDA().getShardInfo().getShardId() == event.getJDA().getShardInfo().getShardTotal() - 1) {
            twitter.addListener(listener);
            twitter.sample();

            long[] all = new long[admins.length + mods.length];
            int count = 0;
            for (long admin : admins) {
                all[count] = admin;
                count++;
            }
            for (long mod : mods) {
                all[count] = mod;
                count++;
            }
            FilterQuery filterQuery = new FilterQuery();
            filterQuery.follow(all);
            twitter.filter(filterQuery);
        }
    }
}
