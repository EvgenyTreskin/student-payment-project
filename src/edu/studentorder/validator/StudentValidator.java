package edu.studentorder.validator;

import edu.studentorder.domain.AnswerStudent;
import edu.studentorder.domain.StudentOrder;

public class StudentValidator {



    public AnswerStudent checkStudent(StudentOrder studentOrder) {
        System.out.println("checkStudent is running");
        return new AnswerStudent();
    }
}
