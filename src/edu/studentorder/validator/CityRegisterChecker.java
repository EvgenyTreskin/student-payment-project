package edu.studentorder.validator;

import edu.studentorder.domain.CityRegisterCheckerResponse;
import edu.studentorder.domain.Person;

public interface CityRegisterChecker {
    CityRegisterCheckerResponse checkPerson(Person person);
}
