package edu.studentorder.validator;

import edu.studentorder.domain.wedding.AnswerWedding;
import edu.studentorder.domain.StudentOrder;

public class WeddingValidator {
    public AnswerWedding checkWedding(StudentOrder studentOrder) {
        System.out.println("checkWedding is running");
        return new AnswerWedding();
    }
}
