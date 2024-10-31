package co.edu.uptc.utilities;

public enum Speed {
    LENTO(2),
    MEDIO(5),
    RAPIDO(10);

    private final int value;

    Speed(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Speed fromValue(int value) {
        for (Speed speed : Speed.values()) {
            if (speed.getValue() == value) {
                return speed;
            }
        }
        throw new IllegalArgumentException("No speed found with value: " + value);
    }
}

