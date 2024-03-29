package com.eziosoft.floatzel.Objects;

import com.google.gson.annotations.Expose;
import net.dv8tion.jda.api.entities.Emoji;

public class SimpleEmoji {
    @Expose
    private String name;
    @Expose
    private Long id;
    @Expose
    private boolean animated;

    public SimpleEmoji(String oof) {
        this.name = oof;
        this.id = 2L;
        this.animated = false;
    }

    public SimpleEmoji(){}

    public String getName() {
        return name;
    }

    public boolean isAnimated() {
        return animated;
    }

    public Long getId() {
        return id;
    }

    public String getEmoji(){
        return Emoji.fromEmote(this.name, this.id, this.animated).getAsMention();
    }
}
