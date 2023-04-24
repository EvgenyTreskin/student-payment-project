package edu.studentorder;

import edu.studentorder.domain.*;
import edu.studentorder.domain.children.AnswerChildren;
import edu.studentorder.domain.register.AnswerCityRegister;
import edu.studentorder.domain.student.AnswerStudent;
import edu.studentorder.domain.wedding.AnswerWedding;
import edu.studentorder.mail.MailSender;
import edu.studentorder.validator.ChildrenValidator;
import edu.studentorder.validator.CityRegisterValidator;
import edu.studentorder.validator.StudentValidator;
import edu.studentorder.validator.WeddingValidator;

import java.util.LinkedList;
import java.util.List;

public class StudentOrderValidator {

    private CityRegisterValidator cityRegisterValidator;
    private WeddingValidator weddingValidator;
    private ChildrenValidator childrenValidator;
    private StudentValidator studentValidator;
    private MailSender mailSender;

    public StudentOrderValidator() {
        cityRegisterValidator = new CityRegisterValidator();
        weddingValidator = new WeddingValidator();
        childrenValidator = new ChildrenValidator();
        studentValidator = new StudentValidator();
        mailSender = new MailSender();
    }

    public static void main(String[] args) {
        StudentOrderValidator studentOrderValidator = new StudentOrderValidator();
        studentOrderValidator.checkAll();

    }

    public void checkAll() {
        List<StudentOrder> studentOrderList = readStudentOrders();
        for (StudentOrder studentOrder : studentOrderList) {

            checkOneOrder(studentOrder);
        }
    }

    public List<StudentOrder> readStudentOrders() {
        List<StudentOrder> studentOrderList = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            StudentOrder studentOrder = SaveStudentOrder.buildStudentOrder(i);
            studentOrderList.add(studentOrder);
        }

        return studentOrderList;
    }

    public void checkOneOrder(StudentOrder studentOrder) {

        AnswerCityRegister citiAnswer = checkCityRegister(studentOrder);

//        AnswerWedding weddingAnswer = checkWedding(studentOrder);
//        AnswerChildren childrenAnswer = checkChildren(studentOrder);
//        AnswerStudent studentAnswer = checkStudent(studentOrder);
//
//        sendMail(studentOrder);
    }


    public AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        return cityRegisterValidator.checkCityRegister(studentOrder);
    }

    public AnswerWedding checkWedding(StudentOrder studentOrder) {
        return weddingValidator.checkWedding(studentOrder);
    }

    public AnswerChildren checkChildren(StudentOrder studentOrder) {
        return childrenValidator.checkChildren(studentOrder);
    }

    public AnswerStudent checkStudent(StudentOrder studentOrder) {
        return studentValidator.checkStudent(studentOrder);
    }

    public void sendMail(StudentOrder studentOrder) {
        mailSender.sendMail(studentOrder);
    }
}
