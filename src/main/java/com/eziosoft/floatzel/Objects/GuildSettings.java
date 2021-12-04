package com.eziosoft.floatzel.Objects;

import com.eziosoft.floatzel.Floatzel;

import java.util.List;

public class GuildSettings {
        @PrimaryKey
        private String guildId;

        private boolean commandsEnabled;
        private String prefix;
        private List<String> notAllowedChans;

        public GuildSettings(){}

        public GuildSettings(String id){
            this.guildId = id;
        }

        public void setPrefix(String prefix){
            this.prefix = prefix;
        }

    public String getGuildId() {
        return guildId;
    }

    public String getPrefix(){
            return this.prefix;
        }

        public void addBannedChan(String id){
            this.notAllowedChans.add(id);
        }
        public void removeBannedChan(String id){
            this.notAllowedChans.remove(id);
        }

        public List<String> getNotAllowedChans() {
            return this.notAllowedChans;
        }
        public void setCommandsEnabled(boolean e){
            this.commandsEnabled = e;
        }

        public boolean isCommandsEnabled() {
            return commandsEnabled;
        }
}
