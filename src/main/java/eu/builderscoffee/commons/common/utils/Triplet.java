package eu.builderscoffee.commons.common.utils;

import lombok.Data;

@Data
public class Triplet<X, Y, Z> {
    private final X left;
    private final Y center;
    private final Z right;

    public Triplet(X left, Y center,Z right) {
        this.left = left;
        this.center = center;
        this.right = right;
    }
}
