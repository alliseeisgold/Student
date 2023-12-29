package org.student.main;

import org.student.logic.Predicate;

public class StringValidation implements Predicate<String> {

    @Override
    public boolean isValidMark(String mark) {
        return mark.equals("отл") || mark.equals("хор")
                || mark.equals("уд") || mark.equals("неуд");
    }
}
