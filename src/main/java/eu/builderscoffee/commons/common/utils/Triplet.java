package eu.builderscoffee.commons.common.utils;

import lombok.Data;

/**
 * A triplet consist of 3 anonymous values that can be stored in an instance
 * @param <X>
 * @param <Y>
 * @param <Z>
 */
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
