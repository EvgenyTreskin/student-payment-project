package edu.studentorder.validator;

import edu.studentorder.domain.Child;
import edu.studentorder.domain.Person;
import edu.studentorder.domain.register.AnswerCityRegister;
import edu.studentorder.domain.register.AnswerCityRegisterItem;
import edu.studentorder.domain.register.CityRegisterResponse;
import edu.studentorder.domain.StudentOrder;
import edu.studentorder.exeption.CityRegisterException;
import edu.studentorder.validator.register.CityRegisterChecker;
import edu.studentorder.validator.register.FakeCityRegisterChecker;

public class CityRegisterValidator {

    public String hostName;
    int port;
    String login;
    String password;
    public CityRegisterChecker personChecker;

    public CityRegisterValidator() {
        personChecker = new FakeCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        AnswerCityRegister answer = new AnswerCityRegister();

        answer.addItem(checkPerson(studentOrder.getHusband()));
        answer.addItem(checkPerson(studentOrder.getWife()));

        for (Child child : studentOrder.getChildren()) {
            answer.addItem(checkPerson(child));
        }
        return answer;
    }

    private AnswerCityRegisterItem checkPerson(Person person) {

        try {
            CityRegisterResponse childAnswer = personChecker.checkPerson(person);

        } catch (CityRegisterException ex) {
            ex.printStackTrace(System.out);
        }
        return null;
    }
}
