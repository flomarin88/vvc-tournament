package org.fmarin.admintournoi.subscription;

public enum Level {

    NATIONAL(1, "National", "success"),
    REGIONAL(2, "Régional", "info"),
    DEPARTEMENTAL(3, "Départemental", "warning"),
    LOISIRS(4, "Loisirs", "danger");

    private Integer value;
    private String label;
    private String color;

    Level(Integer value, String label, String color) {
        this.value = value;
        this.label = label;
        this.color = color;
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

    public String getColor() {
        return color;
    }
}
