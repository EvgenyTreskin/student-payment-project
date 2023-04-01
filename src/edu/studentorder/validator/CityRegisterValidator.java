package edu.studentorder.validator;

import edu.studentorder.domain.register.AnswerCityRegister;
import edu.studentorder.domain.register.CityRegisterCheckerResponse;
import edu.studentorder.domain.StudentOrder;
import edu.studentorder.exeption.CityRegisterException;
import edu.studentorder.validator.register.CityRegisterChecker;
import edu.studentorder.validator.register.RealCityRegisterChecker;

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
        try {
            CityRegisterCheckerResponse husbandAnswer = personChecker.checkPerson(studentOrder.getHusband());
            CityRegisterCheckerResponse wifeAnswer = personChecker.checkPerson(studentOrder.getWife());
            CityRegisterCheckerResponse childAnswer = personChecker.checkPerson(studentOrder.getChild());
        } catch (CityRegisterException ex) {
            ex.printStackTrace();
        }

        AnswerCityRegister answer = new AnswerCityRegister();

        return answer;
    }
}
