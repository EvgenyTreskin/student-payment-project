package edu.studentorder.validator;

import edu.studentorder.domain.Child;
import edu.studentorder.domain.Person;
import edu.studentorder.domain.register.AnswerCityRegister;
import edu.studentorder.domain.register.AnswerCityRegisterItem;
import edu.studentorder.domain.register.CityRegisterResponse;
import edu.studentorder.domain.StudentOrder;
import edu.studentorder.exeption.CityRegisterException;
import edu.studentorder.exeption.TransportException;
import edu.studentorder.validator.register.CityRegisterChecker;
import edu.studentorder.validator.register.FakeCityRegisterChecker;

public class CityRegisterValidator {

    public static final String INTERNAL_CODE = "NO_GRN";

    //    public String hostName;
//    int port;
//    String login;
//    String password;
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
        AnswerCityRegisterItem.CityStatus status = null;
        AnswerCityRegisterItem.CityError error = null;

        try {
            CityRegisterResponse cityRegisterResponse = personChecker.checkPerson(person);
            status = cityRegisterResponse.isExisting() ?
                    AnswerCityRegisterItem.CityStatus.YES :
                    AnswerCityRegisterItem.CityStatus.NO;
        } catch (CityRegisterException ex) {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(ex.getCode(), ex.getMessage());
        } catch (TransportException ex) {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(INTERNAL_CODE, ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(INTERNAL_CODE, ex.getMessage());
        }
        AnswerCityRegisterItem answerCityRegisterItem = new AnswerCityRegisterItem(status, person, error);

        return answerCityRegisterItem;
    }
}
