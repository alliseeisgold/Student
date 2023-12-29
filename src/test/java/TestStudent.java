import org.junit.After;
import org.student.exception.InvalidMarkException;
import org.student.logic.Predicate;
import org.student.logic.Student;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestStudent {
    private static class IntegerValidator implements Predicate<Integer> {
        @Override
        public boolean isValidMark(Integer mark) {
            return mark >= 1 && mark <= 5;
        }
    }

    private Student<Integer> student;

    @Before
    public void setUp() {
        student = new Student<>("John", new IntegerValidator());
    }

    @Test
    public void testStudentConstructors() {
        Student<Integer> student1 = new Student<>("Cole");
        assertEquals("Cole", student1.getName());
        Student<Integer> student2 = new Student<>("Cole", List.of(2, 4));
        assertEquals(2, student2.getMarks().size());
    }

    @Test
    public void testGetName() {
        assertEquals("John", student.getName());
    }

    @Test
    public void testSetName() {
        student.setName("Alice");
        assertEquals("Alice", student.getName());
    }

    @Test
    public void testAddMark() {
        assertThrows(InvalidMarkException.class, () -> student.addMark(85));
        assertDoesNotThrow(() -> student.addMark(2));
        assertDoesNotThrow(() -> student.addMark(3));
    }

    @Test
    public void testDeleteMarkByIndex() {
        try {
            student.addMark(5);
        } catch (InvalidMarkException e) {
            throw new RuntimeException(e);
        }
        student.deleteMark(0);
        assertEquals(0, student.getMarks().size());
    }

    @Test
    public void testDeleteMarkByName() {
        try {
            student.addMark(5);
        } catch (InvalidMarkException e) {
            throw new RuntimeException(e);
        }
        assertThrows(InvalidMarkException.class, () -> student.deleteMark(Integer.valueOf(1)));
        assertDoesNotThrow(() -> student.deleteMark(Integer.valueOf(5)));
        assertEquals(0, student.getMarks().size());
    }

    @Test
    public void testUndoAction() {
        try {
            student.addMark(5);
            student.addMark(4);
        } catch (InvalidMarkException e) {
            throw new RuntimeException(e);
        }
        student.undoAction();
        assertEquals(1, student.getMarks().size());
        assertEquals(Integer.valueOf(5), student.getMarks().get(0));
        student.setName("Adam");
        assertEquals("Adam", student.getName());
        student.undoAction();
        assertEquals("John", student.getName());
    }

    @Test
    public void testStudentEquality() {
        Student<Integer> student1 = new Student<>("John", new IntegerValidator());
        Student<Integer> student2 = new Student<>("Alice", new IntegerValidator());
        Student<Integer> student3 = new Student<>("John", new IntegerValidator());
        assertNotEquals(student1, student2);
        assertEquals(student1, student3);
        assertEquals(student1.hashCode(), student3.hashCode());
    }

    @Test
    public void testStudentGetMarks() {
        try {
            student.addMark(4);
            student.addMark(2);
        } catch (InvalidMarkException e) {
            throw new RuntimeException(e);
        }
        assertEquals(student.getMarks(), List.of(4, 2));
    }

    @Test
    public void testStudentToString() {
        try {
            student.addMark(4);
            student.addMark(3);
            student.addMark(5);
        } catch (InvalidMarkException e) {
            throw new RuntimeException(e);
        }
        String answer = "John: [4, 3, 5]";
        assertEquals(answer, student.toString());
    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testStudentShowMarks() {
        List<Integer> marks = new ArrayList<>();
        marks.add(3);
        marks.add(5);
        marks.add(2);
        Student<Integer> student1 = new Student<>("Adam", new IntegerValidator(), marks);
        student1.showMarks();
        String expectedOutput = "[3, 5, 2]";
        String actualOutput = outContent.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }
}
