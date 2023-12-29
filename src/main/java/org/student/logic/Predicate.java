package org.student.logic;

@FunctionalInterface
public interface Predicate<T> {
    boolean isValidMark(T mark);
}
