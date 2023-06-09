package edu.studentorder.DAO;

import edu.studentorder.domain.CountryArea;
import edu.studentorder.domain.PassportOffice;
import edu.studentorder.domain.RegisterOffice;
import edu.studentorder.domain.Street;
import edu.studentorder.exeption.DaoException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;


import java.time.LocalDateTime;
import java.util.List;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


class DictionaryDaoImplTest {
    // private static final Logger logger = Logger.getLogger(DictionaryDaoImplTest.class.getName());
    @BeforeAll
    public static void startUp() throws Exception {
        DBInit.startUp();
    }

    @Test
    public void testStreet() throws DaoException {
        List<Street> streets = new DictionaryDaoImpl().findStreet("про");
        assertEquals(2, streets.size());
    }

    @Test
    public void testPassportOffice() throws DaoException {
        List<PassportOffice> passportOffices = new DictionaryDaoImpl().findPassportOffices("010020000000");
        assertEquals(2, passportOffices.size());
    }

    @Test
    public void testRegisterOffice() throws DaoException {
        List<RegisterOffice> registerOffices = new DictionaryDaoImpl().findRegisterOffices("010010000000");
        assertEquals(2, registerOffices.size());
    }

    @Test
    public void testArea() throws DaoException {
        List<CountryArea> countryAreas1 = new DictionaryDaoImpl().findAreas("");
        assertEquals(2, countryAreas1.size());
        List<CountryArea> countryAreas2 = new DictionaryDaoImpl().findAreas("020000000000");
        assertEquals(2, countryAreas2.size());
        List<CountryArea> countryAreas3 = new DictionaryDaoImpl().findAreas("020010000000");
        assertEquals(2, countryAreas3.size());
        List<CountryArea> countryAreas4 = new DictionaryDaoImpl().findAreas("020010010000");
        assertEquals(2, countryAreas4.size());
    }

}
