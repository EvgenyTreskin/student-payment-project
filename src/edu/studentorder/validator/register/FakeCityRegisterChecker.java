package edu.studentorder.validator.register;

import edu.studentorder.domain.register.CityRegisterCheckerResponse;
import edu.studentorder.domain.Person;

public class FakeCityRegisterChecker implements CityRegisterChecker{
    public CityRegisterCheckerResponse checkPerson(Person person){
        return null;
    }
}