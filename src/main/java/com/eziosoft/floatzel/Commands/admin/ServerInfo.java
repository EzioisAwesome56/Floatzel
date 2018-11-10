package com.eziosoft.floatzel.Commands.admin;

import com.eziosoft.floatzel.Commands.FCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;

import java.awt.*;

public class ServerInfo extends FCommand {
    public ServerInfo(){
        name = "servinf";
        description = "gets info about current server";
        ownerCommand = true;
        category = owner;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        // get server
        Guild serv = event.getGuild();
        // get information about server
        String name = serv.getName();
        String owner = serv.getOwner().getNickname();
        int members = serv.getMembers().size();
        String icon = serv.getIconUrl();
        int roles = serv.getRoles().size();
        int emotes = serv.getEmotes().size();
        int chans = serv.getTextChannels().size();
        int cats = serv.getCategories().size();
        String prole = serv.getPublicRole().getName();
        String defaultchan = serv.getDefaultChannel().getName();
        // start building the embed
        EmbedBuilder builder = new EmbedBuilder();
        builder.setThumbnail(icon);
        builder.setColor(Color.GREEN);
        builder.setTitle("Owner: " + owner);
        builder.setAuthor(name);
        builder.addField("# of roles", Double.toString(roles), true);
        builder.addField("# of members", Double.toString(members), true);
        builder.addField("# of custom emotes", Double.toString(emotes), true);
        builder.addField("# of channels", Double.toString(chans), true);
        builder.addField("# of categories", Double.toString(cats), true);
        builder.addField("Public role", prole, true);
        builder.addField("Default channel", defaultchan, true);
        // send the message
        event.getChannel().sendMessage(builder.build()).queue();
    }
}
