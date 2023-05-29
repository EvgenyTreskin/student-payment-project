package edu.studentorder.DAO;

import edu.studentorder.domain.CountryArea;
import edu.studentorder.domain.PassportOffice;
import edu.studentorder.domain.RegisterOffice;
import edu.studentorder.domain.Street;
import edu.studentorder.exeption.DaoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

class DictionaryDaoImplTest {

    @BeforeAll
    public static void startUp() throws Exception {
        URL url1 = DictionaryDaoImplTest.class.getClassLoader().
                getResource("student_project.sql");

        URL url2 = DictionaryDaoImplTest.class.getClassLoader().
                getResource("student_data.sql");

        List<String> str1 = Files.readAllLines(Paths.get(url1.toURI()));
        String sql1 = str1.stream().collect(Collectors.joining());

        List<String> str2 = Files.readAllLines(Paths.get(url2.toURI()));
        String sql2 = str2.stream().collect(Collectors.joining());

        try (Connection connection = ConnectionBuilder.getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(sql1);
            statement.executeUpdate(sql2);
        }
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
