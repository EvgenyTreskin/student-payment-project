public class StudetnOrderValidtor {
    public static void main(String[] args) {
        checkAll();

    }

    static void checkAll() {

        while (true) {
            StudentOrder studentOrder = readStudentOrder();

            if (studentOrder == null) {
                break;
            } else {
                System.out.println("Finish");
                AnswerCityRegister citiAnswer = checkCityRegister(studentOrder);
                if (!citiAnswer.success) {
                    //continue;
                    break;
                }
                AnswerWedding weddingAnswer = checkWedding(studentOrder);
                AnswerChildren childrenAnswer = checkChildren(studentOrder);
                AnswerStudent studentAnswer = checkStudent(studentOrder);

                sendMail(studentOrder);
            }

        }

    }



    static StudentOrder readStudentOrder() {
        StudentOrder studentOrder = new StudentOrder();
        return studentOrder;
    }

    static AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        CityRegisterValidator cityRegisterValidator1 = new CityRegisterValidator();
        cityRegisterValidator1.hostName = "Host1";
        cityRegisterValidator1.login = "Login1";
        cityRegisterValidator1.password = "password1";
        CityRegisterValidator cityRegisterValidator2 = new CityRegisterValidator();
        cityRegisterValidator2.hostName = "Host2";
        cityRegisterValidator2.login = "Login2";
        cityRegisterValidator2.password = "password2";
        AnswerCityRegister answerCityRegister1 = cityRegisterValidator1.checkCityRegister(studentOrder);
        AnswerCityRegister answerCityRegister2 = cityRegisterValidator2.checkCityRegister(studentOrder);
        return answerCityRegister1;
    }

    static AnswerWedding checkWedding(StudentOrder studentOrder) {
        WeddingValidator weddingValidator = new WeddingValidator();
        return weddingValidator.checkWedding(studentOrder);
    }

    static AnswerChildren checkChildren(StudentOrder studentOrder) {
        ChildrenValidator childrenValidator = new ChildrenValidator();
        return childrenValidator.checkChildren(studentOrder);
    }

    static AnswerStudent checkStudent(StudentOrder studentOrder) {
        return new StudentValidator().checkStudent(studentOrder);
    }
    static void sendMail(StudentOrder studentOrder) {
        new MailSender().sendMail(studentOrder);
    }
}
