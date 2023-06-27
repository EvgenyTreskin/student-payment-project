package edu.studentorder.validator.register;

import edu.studentorder.domain.register.CityRegisterResponse;
import edu.studentorder.domain.Person;
import edu.studentorder.exeption.CityRegisterException;
import edu.studentorder.exeption.TransportException;

public class RealCityRegisterChecker implements CityRegisterChecker {
    public CityRegisterResponse checkPerson(Person person)
            throws CityRegisterException, TransportException {
        return null;
    }
}
