package edu.studentorder.DAO;

import edu.studentorder.domain.Street;
import edu.studentorder.exeption.DaoException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DictionaryDaoImpl implements DictionaryDao {

    private static final String GET_STREET = "SELECT street_code, street_name " +
            "FROM jc_street WHERE UPPER (street_name) LIKE UPPER(?)";

    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection
                ("jdbc:postgresql://localhost:5432/jc_student",
                        "postgres", "Postgres");
        return connection;
    }

    public List<Street> findStreet(String pattern) throws DaoException {
        List<Street> result = new LinkedList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(GET_STREET)) {
            statement.setString(1, "%" + pattern + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Street str = new Street(resultSet.getLong("street_code"), resultSet.getString("street_name"));
                result.add(str);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }
}
