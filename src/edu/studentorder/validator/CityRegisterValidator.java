package edu.studentorder.validator;

import edu.studentorder.domain.AnswerCityRegister;
import edu.studentorder.domain.StudentOrder;

public class CityRegisterValidator {

    public String hostName;
    int port;
    String login;
    String password;
    public CityRegisterChecker personChecker;

    public CityRegisterValidator() {
        personChecker = new RealCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        personChecker.checkPerson(studentOrder.getHusband());
        personChecker.checkPerson(studentOrder.getWife());
        personChecker.checkPerson(studentOrder.getChild());

        AnswerCityRegister answer = new AnswerCityRegister();

        return answer;
    }
}
