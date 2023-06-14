package ru.inno.course.player.model.generics;

/** пример упаковки для пары значений, которые должны передаваться как один объект
 *
 * @param <L> - "левое" значение
 * @param <R> - "правое" значение
 *
 *           Pair<String, Integer> p = new Pair<>("test", 1);
 *
 */
public class Pair<L, R> {
    private L left;
    private R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
}
