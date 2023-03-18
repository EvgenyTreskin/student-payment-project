public class SaveStudentOrder {

    public static void main(String[] args) {
        StudentOrder studentOrder = new StudentOrder();
        studentOrder.husbandFirstName = "Алексей";
        studentOrder.husbandLastName = "Петрова";
        studentOrder.wifeFirstName = "Галина";
        studentOrder.wifeLastName = "Петрова";

        long ans = saveStudentOrder(studentOrder);
        System.out.println(ans);
    }

    static long saveStudentOrder(StudentOrder studentOrder){
        long answer = 199;
        System.out.println("saveStudentOrder:" + studentOrder.husbandLastName);
        return answer;
    }
}
