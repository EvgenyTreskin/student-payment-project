package edu.studentorder;

import edu.studentorder.domain.Adult;
import edu.studentorder.domain.StudentOrder;

public class SaveStudentOrder {

    public static void main(String[] args) {
       // buildStudentOrder();

//        StudentOrder studentOrder = new StudentOrder();
//        long ans = saveStudentOrder(studentOrder);
//        System.out.println(ans);
    }

    static long saveStudentOrder(StudentOrder studentOrder) {
        long answer = 199;
        System.out.println("saveStudentOrder:");
        return answer;
    }
    static StudentOrder buildStudentOrder(long id){
        StudentOrder studentOrder = new StudentOrder();
        studentOrder.setStudentOrderId(id);

        Adult husband = new Adult("Иванов", "Андрей", "Петрович", null);

        return studentOrder;
    }
}
