package com.eziosoft.floatzel.Objects;

import net.dv8tion.jda.api.entities.Emoji;

public class SimpleEmoji {
    private String name;
    private Long id;
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
