package edu.studentorder.DAO;

import edu.studentorder.config.Config;
import edu.studentorder.domain.PassportOffice;
import edu.studentorder.domain.RegisterOffice;
import edu.studentorder.domain.Street;
import edu.studentorder.exeption.DaoException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DictionaryDaoImpl implements DictionaryDao {

    private static final String GET_STREET = "SELECT street_code, street_name " +
            "FROM jc_street WHERE UPPER (street_name) LIKE UPPER(?)";
    private static final String GET_PASSPORT = "SELECT * " +
            "FROM jc_passport_office WHERE passport_office_area_id = ? ";
    private static final String GET_REGISTER = "SELECT * " +
            "FROM jc_register_office WHERE register_office_area_id = ? ";

    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection
                (Config.getProperty(Config.DB_URL),
                        Config.getProperty(Config.DB_LOGIN),
                        Config.getProperty(Config.DB_PASSWORD));
        return connection;
    }

    @Override
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

    @Override
    public List<PassportOffice> findPassportOffices(String areaId) throws DaoException {
        List<PassportOffice> result = new LinkedList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(GET_PASSPORT)) {
            statement.setString(1, areaId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PassportOffice str = new PassportOffice(resultSet.getLong("passport_office_id"),
                        resultSet.getString("passport_office_area_id"),
                        resultSet.getString("passport_office_name"));
                result.add(str);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public List<RegisterOffice> findRegisterOffice(String areaId) throws DaoException {
        List<RegisterOffice> result = new LinkedList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(GET_REGISTER)) {
            statement.setString(1, areaId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RegisterOffice str = new RegisterOffice(resultSet.getLong("register_office_id"),
                        resultSet.getString("register_office_area_id"),
                        resultSet.getString("register_office_name"));
                result.add(str);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }
}
