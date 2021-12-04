package com.eziosoft.floatzel.Objects;

public interface KekbotFunctionHooks {

    void saveKekXP(int kxp);

    int loadKxp(String id);

    boolean isRegisteredWord(String e);

    String getLocalizedString(String e, Object... fuck);
}
