package eu.builderscoffee.commons.common.utils;

import lombok.Data;

/**
 * A Quadlet consist of 4 anonymous values that can be stored in an instance
 * @param <W>
 * @param <X>
 * @param <Y>
 * @param <Z>
 */
@Data
public class Quadlet<W, X, Y, Z> {
    private final W first;
    private final X second;
    private final Y third;
    private final Z fourth;

    public Quadlet(W first, X second, Y third,Z fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }
}