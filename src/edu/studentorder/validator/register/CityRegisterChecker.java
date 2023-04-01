package edu.studentorder.validator.register;

import edu.studentorder.domain.register.CityRegisterCheckerResponse;
import edu.studentorder.domain.Person;
import edu.studentorder.exeption.CityRegisterException;

public interface CityRegisterChecker {
    CityRegisterCheckerResponse checkPerson(Person person) throws CityRegisterException;
}
