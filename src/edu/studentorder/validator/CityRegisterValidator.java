package edu.studentorder.validator;

import edu.studentorder.domain.AnswerCityRegister;
import edu.studentorder.domain.StudentOrder;

public class CityRegisterValidator {

    public String hostName;
    int port;
    String login;
    String password;

    public AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        System.out.println("CityRegister is running: " + hostName + ", " + login + ", " + password);
        AnswerCityRegister answer = new AnswerCityRegister();
        answer.success = false;
        return answer;
    }
}
