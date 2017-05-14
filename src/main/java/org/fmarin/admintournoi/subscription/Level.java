package org.fmarin.admintournoi.subscription;

public enum Level {

    NATIONAL(1, "National"),
    REGIONAL(2, "Régional"),
    DEPARTEMENTAL(3, "Départemental"),
    LOISIRS(4, "Loisirs");

    private Integer value;
    private String label;

    Level(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Level valueOf(int value) {
        for (Level level : Level.values()) {
            if (value == level.getValue()) {
                return level;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
