package edu.studentorder.validator.register;

import edu.studentorder.domain.Adult;
import edu.studentorder.domain.Child;
import edu.studentorder.domain.register.CityRegisterResponse;
import edu.studentorder.domain.Person;
import edu.studentorder.exeption.CityRegisterException;

public class FakeCityRegisterChecker implements CityRegisterChecker {

    public static final String GOOD_1 = "1000";
    public static final String GOOD_2 = "2000";
    public static final String BAD_1 = "1001";
    public static final String BAD_2 = "2001";
    public static final String ERROR_1 = "1002";
    public static final String ERROR_2 = "2002";

    public CityRegisterResponse checkPerson(Person person)
            throws CityRegisterException {
        CityRegisterResponse response = new CityRegisterResponse();
        if (person instanceof Adult) {
            Adult t = (Adult) person;
            String ps = t.getPassportSerial();
            if (ps.equals(GOOD_1) || ps.equals(GOOD_2)) {
                response.setExisting(true);
                response.setTemporal(false);
            }
            if (ps.equals(BAD_1) || ps.equals(BAD_2)) {
                response.setExisting(false);
            }
            if (ps.equals(ERROR_1) || ps.equals(ERROR_2)) {
                CityRegisterException ex = new CityRegisterException("Fake ERROR " + ps);
                throw ex;
            }
        }
        if (person instanceof Child){
            response.setExisting(true);
            response.setTemporal(true);
        }
        System.out.println(response);
        return response;
    }
}
