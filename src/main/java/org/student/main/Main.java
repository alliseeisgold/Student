package org.student.main;

import org.student.exception.InvalidMarkException;
import org.student.logic.Student;

import java.util.Scanner;


public class Main {
    private static void printInformation() {
        System.out.println("Выберите одно действие:");
        System.out.println("0. Завершение процесса");
        System.out.println("1. Создание объекта класса Студент с указанным методом.");
        System.out.println("2. Добавление одной оценки объекту ранее созданного Студента.");
        System.out.println("3. Удаление последней оценки с указанным значением у Студента.");
        System.out.println("4. Печать текстового описания Студента.");
    }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            Student<String> student = null;
            StringValidation validator = new StringValidation();
            printInformation();
            boolean isFinished = false;
            while (!isFinished) {
                System.out.print("Введите команду: ");
                int request = in.nextInt();
                in.nextLine();
                switch (request) {
                    case 0:
                        isFinished = true;
                        break;
                    case 1:
                        System.out.print("Введите имя студента: ");
                        String name = in.nextLine();
                        student = new Student<>(name, validator);
                        break;
                    case 2:
                        if (student != null) {
                            try {
                                System.out.print("Введите оценку(отл, хор, уд, неуд): ");
                                String mark = in.nextLine();
                                student.addMark(mark);
                            } catch (InvalidMarkException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        break;
                    case 3:
                        if (student != null) {
                            System.out.print("Введите оценку, которую хотите удалить: ");
                            String mark = in.nextLine();
                            int lastIndex = student.getMarks().lastIndexOf(mark);
                            student.deleteMark(lastIndex);
                        }
                        break;
                    default:
                        if (student != null) {
                            System.out.println(student);
                        }
                }
            }
        } catch (Exception e) {
            System.out.println("Something got wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
