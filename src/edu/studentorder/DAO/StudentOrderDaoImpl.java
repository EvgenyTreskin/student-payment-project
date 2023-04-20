package edu.studentorder.DAO;

import edu.studentorder.config.Config;
import edu.studentorder.domain.*;
import edu.studentorder.exeption.DaoException;

import java.sql.*;
import java.time.LocalDateTime;

public class StudentOrderDaoImpl implements StudentOrderDao {

    private static final String INSERT_ORDER = "INSERT INTO jc_student_order (\n" +
            "student_order_status, student_order_date, " +
            "husband_surname, husband_given_name, husband_patronymic, " +
            "husband_date_of_birth, husband_passport_serial, " +
            "husband_passport_number, husband_passport_date," +
            " husband_passport_office_id, husband_post_index," +
            " husband_street_code, husband_building, husband_extension, " +
            "husband_apartment, husband_university_id, husband_student_number," +
            " wife_surname, wife_given_name, wife_patronymic, " +
            "wife_date_of_birth, wife_passport_serial, wife_passport_number," +
            " wife_passport_date, wife_passport_office_id, wife_post_index," +
            " wife_street_code, wife_building, wife_extension, "
            + "wife_apartment, wife_university_id, wife_student_number," +
            "certificate_id," +
            " register_office_id, marriage_date)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
            " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String INSERT_CHILD = "INSERT INTO public.jc_student_child(\n" +
            "\t student_order_id, child_surname, child_given_name, " +
            "child_patronymic, child_date_of_birth, child_certificate_number, " +
            "child_certificate_date, child_register_office_id, child_post_index, " +
            "child_street_code, child_building, child_extension, child_apartment)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


    //TODO refactoring - make one method
    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection
                (Config.getProperty(Config.DB_URL),
                        Config.getProperty(Config.DB_LOGIN),
                        Config.getProperty(Config.DB_PASSWORD));
        return connection;
    }

    @Override
    public Long saveStudentOrder(StudentOrder studentOrder) throws DaoException {
        Long result = -1L;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ORDER, new String[]{"student_order_id"})) {

            connection.setAutoCommit(false);

            try {
                //Header
                statement.setInt(1, StudentOrderStatus.START.ordinal());
                statement.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));

                //Husband and wife
                setParametersForAdult(statement, 3, studentOrder.getHusband());
                setParametersForAdult(statement, 18, studentOrder.getWife());

                // Marriage
                statement.setString(33, studentOrder.getMarriageCertificateId());
                statement.setLong(34, studentOrder.getMarriageOffice().getOfficeId());
                statement.setDate(35, Date.valueOf(studentOrder.getMarriageDate()));

                statement.executeUpdate();

                ResultSet generatedKeysResultSet = statement.getGeneratedKeys();
                if (generatedKeysResultSet.next()) {
                    result = generatedKeysResultSet.getLong(1);
                }

                saveChildren(connection, studentOrder, result);

                connection.commit();
            } catch (SQLException e){
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    private void saveChildren(Connection connection, StudentOrder studentOrder, Long studentOrderId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_CHILD)) {
            for (Child child : studentOrder.getChildren()) {
                statement.setLong(1, studentOrderId);
                setParametersForChild(statement, child);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void setParametersForChild(PreparedStatement statement, Child child) throws SQLException {
        int start = 1;
        setParametersForPerson(statement, 2, child);
        statement.setString(6, child.getCertificateNumber());
        statement.setDate(7, Date.valueOf(child.getIssueDate()));
        statement.setLong(8, child.getIssueDepartment().getOfficeId());
        setParametersForAddress(statement, start, child);
    }

    private void setParametersForPerson(PreparedStatement statement, int start, Person person) throws SQLException {
        statement.setString(start, person.getSurName());
        statement.setString(start + 1, person.getGivenName());
        statement.setString(start + 2, person.getPatronymic());
        statement.setDate(start + 3, Date.valueOf(person.getDateOfBirth()));
    }

    private void setParametersForAdult(PreparedStatement statement, int start, Adult adult) throws SQLException {
        setParametersForPerson(statement, start, adult);
        statement.setString(start + 4, adult.getPassportSerial());
        statement.setString(start + 5, adult.getPassportNumber());
        statement.setDate(start + 6, Date.valueOf(adult.getIssueDate()));
        statement.setLong(start + 7, adult.getIssueDepartment().getOfficeId());
        setParametersForAddress(statement, start, adult);
        statement.setLong(start + 13, adult.getUniversity().getUniversityId());
        statement.setString(start + 14, adult.getStudentId());
    }

    private void setParametersForAddress(PreparedStatement statement, int start, Person person) throws SQLException {
        Address address = person.getAddress();
        statement.setString(start + 8, address.getPostCode());
        statement.setLong(start + 9, address.getStreet().getStreetCode());
        statement.setString(start + 10, address.getBuilding());
        statement.setString(start + 11, address.getExtension());
        statement.setString(start + 12, address.getApartment());
    }


}
