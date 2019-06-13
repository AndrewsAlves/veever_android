package com.veever.main.Events;

import java.util.Locale;

public class ChangeLanguageEvent {

    public Locale locale;

    public ChangeLanguageEvent(Locale locale) {
        this.locale = locale;
    }
}
