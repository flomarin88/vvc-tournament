package org.fmarin.admintournoi.subscription;

public enum Gender {
    MEN("Masculin"),
    WOMEN("Féminin");

    private final String translation;

    Gender(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return this.translation;
    }
}
