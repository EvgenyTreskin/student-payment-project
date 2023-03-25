package edu.studentorder;

import edu.studentorder.domain.Adult;
import edu.studentorder.domain.StudentOrder;

public class SaveStudentOrder {

    public static void main(String[] args) {
        buildStudentOrder();

//        StudentOrder studentOrder = new StudentOrder();
//        long ans = saveStudentOrder(studentOrder);
//        System.out.println(ans);
    }

    static long saveStudentOrder(StudentOrder studentOrder) {
        long answer = 199;
        System.out.println("saveStudentOrder:");
        return answer;
    }
    static StudentOrder buildStudentOrder(){
        StudentOrder studentOrder = new StudentOrder();
        Adult husband = new Adult();
        husband.setGivenName("Андрей");
        husband.setSurName("Петров");
        husband.setPassportNumber("1234");
        husband.setPassportSerial("004321");
        studentOrder.setHusband(husband);

        String ans = husband.getPersonString();
        System.out.println(ans);

        return studentOrder;
    }
}
