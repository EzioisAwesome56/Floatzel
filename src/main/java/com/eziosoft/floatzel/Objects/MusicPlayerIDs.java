package com.eziosoft.floatzel.Objects;

import com.eziosoft.floatzel.Floatzel;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class MusicPlayerIDs {

    private long guildid;
    private String userid;
    private String textchannelid;

    public MusicPlayerIDs(CommandEvent e){
        this.guildid = e.getGuild().getIdLong();
        this.userid = e.getAuthor().getId();
        this.textchannelid = e.getTextChannel().getId();
    }

    public MusicPlayerIDs(SlashCommandEvent e){
        this.guildid = e.getGuild().getIdLong();
        this.userid = e.getMember().getId();
        this.textchannelid = e.getChannel().getId();
    }

    public Guild getGuild(){
        return Floatzel.jda.getGuildById(this.guildid);
    }

    public long getGuildid() {
        return this.guildid;
    }

    public String getMessagechannelid() {
        return this.textchannelid;
    }
    public MessageChannel getChannel(){
        return Floatzel.jda.getTextChannelById(this.textchannelid);
    }

    public String getTextchannelid() {
        return this.textchannelid;
    }

    public TextChannel getTextChannel(){
        return Floatzel.jda.getTextChannelById(this.textchannelid);
    }
    public Member getMember(){
        return Floatzel.jda.getGuildById(this.guildid).getMemberById(this.userid);
    }

    public String getUserid() {
        return this.userid;
    }
    public User getUser(){
        return Floatzel.jda.getUserById(this.userid);
    }
}
