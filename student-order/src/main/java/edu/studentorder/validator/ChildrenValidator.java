package edu.studentorder.validator;

import edu.studentorder.domain.children.AnswerChildren;
import edu.studentorder.domain.StudentOrder;

public class ChildrenValidator {
    public AnswerChildren checkChildren(StudentOrder studentOrder) {
        System.out.println("checkChildren is running");
        return new AnswerChildren();
    }
}
