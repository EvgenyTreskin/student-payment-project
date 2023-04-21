package edu.studentorder.DAO;

import edu.studentorder.config.Config;
import edu.studentorder.domain.*;
import edu.studentorder.exeption.DaoException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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
            " wife_street_code, wife_building, wife_extension, " +
            "wife_apartment, wife_university_id, wife_student_number," +
            "certificate_id, register_office_id, marriage_date)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
            " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String INSERT_CHILD = "INSERT INTO public.jc_student_child(\n" +
            "\t student_order_id, child_surname, child_given_name, " +
            "child_patronymic, child_date_of_birth, child_certificate_number, " +
            "child_certificate_date, child_register_office_id, child_post_index, " +
            "child_street_code, child_building, child_extension, child_apartment)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


    private static final String SELECT_ORDERS =
            "SELECT * FROM jc_student_order WHERE student_order_status = 0" +
                    " ORDER BY student_order_date";

    //TODO refactoring - make one method
    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection
                (Config.getProperty(Config.DB_URL),
                        Config.getProperty(Config.DB_LOGIN),
                        Config.getProperty(Config.DB_PASSWORD));
        return connection;
    }


    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ORDERS, new String[]{"student_order_id"})) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                StudentOrder studentOrder = new StudentOrder();
                fillStudentOrder(resultSet, studentOrder);
                fillMarriage(resultSet, studentOrder);
                Adult husband = fillAdult(resultSet, "husband_");
                Adult wife = fillAdult(resultSet, "wife_");
                studentOrder.setHusband(husband);
                studentOrder.setWife(wife);

                result.add(studentOrder);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    private Adult fillAdult(ResultSet resultSet, String prefix) throws SQLException {
        Adult adult = new Adult();
        adult.setSurName(resultSet.getString(prefix + "surname"));
        adult.setGivenName(resultSet.getString(prefix + "given_name"));
        adult.setPatronymic(resultSet.getString(prefix +"patronymic"));
        adult.setDateOfBirth(resultSet.getDate(prefix + "date_of_birth").toLocalDate());
        adult.setPassportSerial(resultSet.getString(prefix + "passport_serial"));
        adult.setPassportNumber(resultSet.getString(prefix + "passport_number"));
        adult.setIssueDate(resultSet.getDate(prefix + "passport_date").toLocalDate());

        PassportOffice passportOffice = new PassportOffice(resultSet.getLong(prefix + "passport_office_id"), "", "");
        adult.setIssueDepartment(passportOffice);
        Address address = new Address();
        Street street = new Street(resultSet.getLong(prefix + "street_code"), "");
        address.setStreet(street);
        address.setPostCode(resultSet.getString(prefix + "post_index"));
        address.setBuilding(resultSet.getString(prefix + "building"));
        address.setExtension(resultSet.getString(prefix + "extension"));
        address.setApartment(resultSet.getString(prefix + "apartment"));
        adult.setAddress(address);

        University university = new University(resultSet.getLong(prefix + "university_id"), "");
        adult.setUniversity(university);
        adult.setStudentId(resultSet.getString(prefix + "student_number"));

        return adult;
    }

    private void fillMarriage(ResultSet resultSet, StudentOrder studentOrder) throws SQLException {
        studentOrder.setMarriageCertificateId(resultSet.getString("certificate_id"));
        studentOrder.setMarriageDate(resultSet.getDate("marriage_date").toLocalDate());

    }

    private void fillStudentOrder(ResultSet resultSet, StudentOrder studentOrder) throws SQLException {
        studentOrder.setStudentOrderId(resultSet.getLong("student_order_id"));
        studentOrder.setStudentOrderDate(resultSet.getTimestamp("student_order_date").toLocalDateTime());
        studentOrder.setStudentOrderStatus(StudentOrderStatus.fromValue(resultSet.getInt("student_order_status")));

        Long registerOfficeId = resultSet.getLong("register_office_id");
        RegisterOffice registerOffice = new RegisterOffice(registerOfficeId, "", "");
        studentOrder.setMarriageOffice(registerOffice);
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
            } catch (SQLException e) {
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
