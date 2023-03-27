package edu.studentorder.validator;

import edu.studentorder.domain.CityRegisterCheckerResponse;
import edu.studentorder.domain.Person;

public class FakeCityRegisterChecker implements CityRegisterChecker{
    public CityRegisterCheckerResponse checkPerson(Person person){
        return null;
    }
}
