package com.eziosoft.floatzel.SlashCommands.Local;

import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Objects.GuildSettings;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.Objects.SlashOption;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class prefix extends FSlashCommand {

    public prefix(){
        name = "prefix";
        help = "set the bot prefix for this server";
        needsServerAdmin = true;
        hasoptions = true;
        optlist.add(new SlashOption(OptionType.STRING, "Change prefix for bot in current-guild", "prefix"));
    }

    @Override
    public void execute(SlashCommandEvent e) {
        if (e.getOption("prefix") == null){
            e.getHook().editOriginal("Error: you did not specify what prefix to use in the \"prefix\" option!").queue();
        } else {
            GuildSettings s;
            String newprefix = e.getOption("prefix").getAsString();
            try {
                s = Floatzel.guildSettingsManager.loadGuildSettings(e.getGuild().getId());
            } catch (NullPointerException err){
                // just make a new guildsettings object
                GuildSettings temp = Floatzel.guildSettingsManager.makeNewGuildSettings(e.getGuild().getId());
                temp.setCommandsEnabled(true);
                temp.setPrefix(newprefix);
                Floatzel.guildSettingsManager.save(temp);
                e.getHook().editOriginal("Prefix has been set to "  + newprefix).queue();
                return;
            }
            s.setPrefix(newprefix);
            Floatzel.guildSettingsManager.save(s);
            e.getHook().editOriginal("Prefix has been set to "  + newprefix).queue();
        }
        return;
    }
}
