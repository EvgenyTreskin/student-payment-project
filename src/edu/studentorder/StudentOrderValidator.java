package edu.studentorder;

import edu.studentorder.domain.*;
import edu.studentorder.mail.MailSender;
import edu.studentorder.validator.ChildrenValidator;
import edu.studentorder.validator.CityRegisterValidator;
import edu.studentorder.validator.StudentValidator;
import edu.studentorder.validator.WeddingValidator;

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
        StudentOrder[] studentOrderArray = readStudentOrders();
        for (StudentOrder studentOrder:studentOrderArray){
            System.out.println();
            checkOneOrder(studentOrder);
        }
    }

    public StudentOrder[] readStudentOrders() {
        StudentOrder[] studentOrdersArray = new StudentOrder[3];
        for (int i = 0; i < studentOrdersArray.length; i++) {
            studentOrdersArray[i] = SaveStudentOrder.buildStudentOrder(i);
        }

        return studentOrdersArray;
    }

    public void checkOneOrder(StudentOrder studentOrder){
        System.out.println("Finish");
        AnswerCityRegister citiAnswer = checkCityRegister(studentOrder);

        AnswerWedding weddingAnswer = checkWedding(studentOrder);
        AnswerChildren childrenAnswer = checkChildren(studentOrder);
        AnswerStudent studentAnswer = checkStudent(studentOrder);

        sendMail(studentOrder);
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
