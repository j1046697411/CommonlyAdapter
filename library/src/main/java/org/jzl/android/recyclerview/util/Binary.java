package org.jzl.android.recyclerview.util;

public class Binary<T, R>{
    public final T one;
    public final R two;

    private Binary(T one, R two) {
        this.one = one;
        this.two = two;
    }

    public static <T, R> Binary<T, R> of(T one, R two){
        return new Binary<>(one, two);
    }
}
