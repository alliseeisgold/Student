package org.student.logic;

import org.student.exception.InvalidMarkException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class Student<T> {
    private static final int COEFFICIENT = 31;
    private String name;
    private final List<T> marks;
    private final Predicate<T> markPredicate;
    private final Deque<Runnable> state;

    public Student(String name) {
        this(name, null, new ArrayList<>());
    }

    public Student(String name, List<T> marks) {
        this(name, null, marks);
    }

    public Student(String name, Predicate<T> markPredicate) {
        this(name, markPredicate, new ArrayList<>());
    }

    public Student(String name, Predicate<T> markPredicate, List<T> marks) {
        this.name = name;
        this.marks = marks;
        this.markPredicate = markPredicate;
        this.state = new ArrayDeque<>();
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            String currentName = this.name;
            this.name = name;
            state.push(() -> {
                this.name = currentName;
            });
        }
    }

    public String getName() {
        return name;
    }

    public void deleteMark(T mark) throws InvalidMarkException {
        if (!marks.contains(mark)) {
            throw new InvalidMarkException("У студента нет такой оценки");
        }
        state.push(() -> {
            marks.add(marks.indexOf(mark), mark);
        });
        marks.remove(mark);
    }

    public void deleteMark(int markIndex) {
        if (markIndex >= 0 && markIndex < marks.size()) {
            state.push(() -> {
                marks.add(markIndex, marks.get(markIndex));
            });
            marks.remove(markIndex);
        }
    }

    public void addMark(T mark) throws InvalidMarkException {
        if (markPredicate != null && !markPredicate.isValidMark(mark)) {
            throw new InvalidMarkException("Некорректная оценка!");
        }
        marks.add(mark);
        state.push(() -> {
            marks.remove(marks.lastIndexOf(mark));
        });
    }

    public List<T> getMarks() {
        return new ArrayList<>(marks);
    }

    public void showMarks() {
        System.out.println(marks);
    }

    public void undoAction() {
        if (!state.isEmpty()) {
            Runnable undoAction = state.pop();
            undoAction.run();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student<?> student = (Student<?>) o;
        return Objects.equals(name, student.name) && Objects.equals(marks, student.marks);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = COEFFICIENT * result + marks.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + ": " + marks.toString();
    }
}