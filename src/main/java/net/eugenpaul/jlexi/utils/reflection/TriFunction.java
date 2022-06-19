package net.eugenpaul.jlexi.utils.reflection;

public interface TriFunction<T, U, V, W> {
    public W apply(T t, U u, V v);
}
