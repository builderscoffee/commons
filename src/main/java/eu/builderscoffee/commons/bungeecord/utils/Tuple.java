package eu.builderscoffee.commons.bungeecord.utils;

import lombok.Data;

@Data
public class Tuple<X, Y> {
    private final X left;
    private final Y right;

    public Tuple(X left, Y right) {
        this.left = left;
        this.right = right;
    }
}
