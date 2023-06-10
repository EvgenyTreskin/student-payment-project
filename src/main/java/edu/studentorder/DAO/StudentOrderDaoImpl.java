package edu.studentorder.DAO;

import edu.studentorder.config.Config;
import edu.studentorder.domain.*;
import edu.studentorder.exeption.DaoException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
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
            "SELECT so.*, ro.register_office_area_id, ro.register_office_name, " +
                    "po_h.passport_office_area_id as husband_passport_office_area_id, " +
                    "po_h.passport_office_name as husband_passport_office_name," +
                    "po_w.passport_office_area_id as wife_passport_office_area_id, " +
                    "po_w.passport_office_name as wife_passport_office_name " +
                    "FROM jc_student_order so " +
                    "INNER JOIN jc_register_office ro ON ro.register_office_id = so.register_office_id " +
                    "INNER JOIN jc_passport_office po_h ON po_h.passport_office_id = so.husband_passport_office_id " +
                    "INNER JOIN jc_passport_office po_w ON po_w.passport_office_id = so.wife_passport_office_id " +
                    "WHERE student_order_status = ? ORDER BY student_order_date LIMIT ?";


    private static final String SELECT_CHILD =
            "SELECT soc.*, ro.register_office_area_id, ro.register_office_name " +
                    "FROM jc_student_child soc " +
                    "INNER JOIN jc_register_office ro ON ro.register_office_id = " +
                    "soc.child_register_office_id " +
                    "WHERE soc.student_order_id IN ";

    private static final String SELECT_ORDERS_FULL =
            "SELECT so.*, ro.register_office_area_id, ro.register_office_name, " +
                    "po_h.passport_office_area_id as husband_passport_office_area_id, " +
                    "po_h.passport_office_name as husband_passport_office_name, " +
                    "po_w.passport_office_area_id as wife_passport_office_area_id, " +
                    "po_w.passport_office_name as wife_passport_office_name, " +
                    "soc.*, ro_c.register_office_area_id, ro_c.register_office_name " +
                    "FROM jc_student_order so " +
                    "INNER JOIN jc_register_office ro ON ro.register_office_id = so.register_office_id " +
                    "INNER JOIN jc_passport_office po_h ON po_h.passport_office_id = so.husband_passport_office_id " +
                    "INNER JOIN jc_passport_office po_w ON po_w.passport_office_id = so.wife_passport_office_id " +
                    "INNER JOIN jc_student_child soc ON soc.student_order_id = so.student_order_id " +
                    "INNER JOIN jc_register_office ro_c ON ro_c.register_office_id = soc.child_register_office_id " +
                    "WHERE student_order_status = ? ORDER BY so.student_order_id LIMIT ?";


    private Connection getConnection() throws SQLException {
        return ConnectionBuilder.getConnection();
    }


    @Override
    public Long saveStudentOrder(StudentOrder studentOrder) throws DaoException {
        Long result = -1L;

        log.debug("StudentOrder:{}", studentOrder);

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
            log.error(e.getMessage(), e);
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

    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException {
        return getStudentOrdersOneSelect();
//        return getStudentOrdersTwoSelect();
    }

    private List<StudentOrder> getStudentOrdersOneSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ORDERS_FULL)) {

            Map<Long, StudentOrder> maps = new HashMap<>();


            statement.setInt(1, StudentOrderStatus.START.ordinal());
            int limit = Integer.parseInt(Config.getProperty(Config.DB_LIMIT));
            statement.setInt(2, limit);

            ResultSet resultSet = statement.executeQuery();
            int counter = 0;


            while (resultSet.next()) {
                Long studentOrderId = resultSet.getLong("student_order_id");
                if (!maps.containsKey(studentOrderId)) {
                    StudentOrder studentOrder = getFullStudentOrder(resultSet);

                    result.add(studentOrder);
                    maps.put(studentOrderId, studentOrder);
                }
                StudentOrder studentOrder = maps.get(studentOrderId);
                studentOrder.addChild(fillChild(resultSet));
                counter++;
            }
            if (counter >= limit) {
                result.remove(result.size() - 1);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e);
        }
        return result;
    }


    private List<StudentOrder> getStudentOrdersTwoSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ORDERS, new String[]{"student_order_id"})) {

            statement.setInt(1, StudentOrderStatus.START.ordinal());
            statement.setInt(2, Integer.parseInt(Config.getProperty(Config.DB_LIMIT)));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                StudentOrder studentOrder = getFullStudentOrder(resultSet);

                result.add(studentOrder);
            }
            findChildren(connection, result);

            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    private StudentOrder getFullStudentOrder(ResultSet resultSet) throws SQLException {
        StudentOrder studentOrder = new StudentOrder();
        fillStudentOrder(resultSet, studentOrder);
        fillMarriage(resultSet, studentOrder);

        studentOrder.setHusband(fillAdult(resultSet, "husband_"));
        studentOrder.setWife(fillAdult(resultSet, "wife_"));
        return studentOrder;
    }

    private void findChildren(Connection connection, List<StudentOrder> result) throws SQLException {
        String cl = "(" + result.stream().map(studentOrder -> String.valueOf(studentOrder
                .getStudentOrderId())).collect(Collectors.joining(",")) + ")";

        Map<Long, StudentOrder> maps = result.stream().collect(Collectors.toMap
                (studentOrder -> studentOrder.getStudentOrderId(),
                        studentOrder -> studentOrder));

        try (PreparedStatement statement = connection.prepareStatement(SELECT_CHILD + cl)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Child child = fillChild(resultSet);
                StudentOrder studentOrder = maps.get(resultSet.getLong("student_order_id"));
                studentOrder.addChild(child);
            }
        }
    }


    private Adult fillAdult(ResultSet resultSet, String prefix) throws SQLException {
        Adult adult = new Adult();
        adult.setSurName(resultSet.getString(prefix + "surname"));
        adult.setGivenName(resultSet.getString(prefix + "given_name"));
        adult.setPatronymic(resultSet.getString(prefix + "patronymic"));
        adult.setDateOfBirth(resultSet.getDate(prefix + "date_of_birth").toLocalDate());
        adult.setPassportSerial(resultSet.getString(prefix + "passport_serial"));
        adult.setPassportNumber(resultSet.getString(prefix + "passport_number"));
        adult.setIssueDate(resultSet.getDate(prefix + "passport_date").toLocalDate());

        Long passportOfficeId = resultSet.getLong(prefix + "passport_office_id");
        String passportOfficeAdea = resultSet.getString(prefix + "passport_office_area_id");
        String passportOfficeName = resultSet.getString(prefix + "passport_office_name");

        PassportOffice passportOffice = new PassportOffice(passportOfficeId, passportOfficeAdea, passportOfficeName);
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

    private void fillStudentOrder(ResultSet resultSet, StudentOrder studentOrder) throws SQLException {
        studentOrder.setStudentOrderId(resultSet.getLong("student_order_id"));
        studentOrder.setStudentOrderDate(resultSet.getTimestamp("student_order_date").toLocalDateTime());
        studentOrder.setStudentOrderStatus(StudentOrderStatus.fromValue(resultSet.getInt("student_order_status")));
    }

    private void fillMarriage(ResultSet resultSet, StudentOrder studentOrder) throws SQLException {
        studentOrder.setMarriageCertificateId(resultSet.getString("certificate_id"));
        studentOrder.setMarriageDate(resultSet.getDate("marriage_date").toLocalDate());

        Long registerOfficeId = resultSet.getLong("register_office_id");

        String officeAreaId = resultSet.getString("register_office_area_id");
        String officeName = resultSet.getString("register_office_name");

        RegisterOffice registerOffice = new RegisterOffice(registerOfficeId, officeAreaId, officeName);
        studentOrder.setMarriageOffice(registerOffice);
    }

    private Child fillChild(ResultSet resultSet) throws SQLException {
        String surname = resultSet.getString("child_surname");
        String givenName = resultSet.getString("child_given_name");
        String patronymicName = resultSet.getString("child_patronymic");
        LocalDate dateOfBirth = resultSet.getDate("child_date_of_birth").toLocalDate();

        Child child = new Child(surname, givenName, patronymicName, dateOfBirth);
        child.setCertificateNumber(resultSet.getString("child_certificate_number"));
        child.setIssueDate(resultSet.getDate("child_certificate_date").toLocalDate());

        Long registerOfficeId = resultSet.getLong("child_register_office_id");
        String registerOfficeArea = resultSet.getString("register_office_area_id");
        String registerOfficeName = resultSet.getString("register_office_name");
        RegisterOffice registerOffice = new RegisterOffice(registerOfficeId, registerOfficeArea, registerOfficeName);
        child.setIssueDepartment(registerOffice);

        Address address = new Address();
        Street street = new Street(resultSet.getLong("child_street_code"), "");
        address.setStreet(street);
        address.setPostCode(resultSet.getString("child_post_index"));
        address.setBuilding(resultSet.getString("child_building"));
        address.setExtension(resultSet.getString("child_extension"));
        address.setApartment(resultSet.getString("child_apartment"));
        child.setAddress(address);

        return child;
    }
}
