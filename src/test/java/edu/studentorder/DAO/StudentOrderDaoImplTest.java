package edu.studentorder.DAO;

import edu.studentorder.domain.*;
import edu.studentorder.exeption.DaoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentOrderDaoImplTest {

    StudentOrderDaoImpl studentOrderDaoImpl = new StudentOrderDaoImpl();

    @BeforeAll
    public static void startUp() throws Exception {
        DBInit.startUp();
    }

    @Test
    void saveStudentOrder() throws DaoException {
        StudentOrder studentOrder = buildStudentOrder(10);
        Long id = new StudentOrderDaoImpl().saveStudentOrder(studentOrder);
//        assertNotNull(id);
    }

    @Test
    void saveStudentOrderError(){
        StudentOrder studentOrder = buildStudentOrder(10);
        studentOrder.getHusband().setSurName(null);
        assertThrows(DaoException.class, () -> studentOrderDaoImpl.saveStudentOrder(studentOrder));
    }

    @Test
    void getStudentOrders() throws DaoException {
        List<StudentOrder> list = new StudentOrderDaoImpl().getStudentOrders();

    }

    public StudentOrder buildStudentOrder(long id) {
        StudentOrder studentOrder = new StudentOrder();
        RegisterOffice registerOffice1 = new RegisterOffice(1L, "", "");
        studentOrder.setStudentOrderId(id);
        studentOrder.setMarriageCertificateId("" + (123456000 + id));
        studentOrder.setMarriageDate(LocalDate.of(2016, 7, 4));
        studentOrder.setMarriageOffice(registerOffice1);

        Street street = new Street(1L, "First street");

        Address address = new Address("195000", street, "12", "", "142");

        // Муж
        Adult husband = new Adult("Петров", "Виктор", "Сергеевич", LocalDate.of(1997, 8, 24));
        PassportOffice passportOffice1 = new PassportOffice(1L, "", "");
        husband.setPassportSerial("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setIssueDate(LocalDate.of(2017, 9, 15));
        husband.setIssueDepartment(passportOffice1);
        husband.setStudentId("" + (100000 + id));
        husband.setAddress(address);
        husband.setUniversity(new University(2L, ""));
        husband.setStudentId("HH12345");
        // Жена
        Adult wife = new Adult("Петрова", "Вероника", "Алекссевна", LocalDate.of(1998, 3, 12));
        PassportOffice passportOffice2 = new PassportOffice(2L, "", "");
        wife.setPassportSerial("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setIssueDate(LocalDate.of(2018, 4, 5));
        wife.setIssueDepartment(passportOffice2);
        wife.setStudentId("" + (200000 + id));
        wife.setAddress(address);
        wife.setUniversity(new University(1L, ""));
        wife.setStudentId("WW12345");
        // Ребенок
        Child child1 = new Child("Петрова", "Ирина", "Викторовна", LocalDate.of(2018, 6, 29));
        RegisterOffice registerOffice3 = new RegisterOffice(3L, "", "");
        child1.setCertificateNumber("" + (300000 + id));
        child1.setIssueDate(LocalDate.of(2018, 7, 19));
        child1.setIssueDepartment(registerOffice3);
        child1.setAddress(address);
        // Ребенок
        Child child2 = new Child("Петров", "Евгений", "Викторович", LocalDate.of(2018, 6, 29));
        registerOffice3 = new RegisterOffice(3L, "", "");
        child2.setCertificateNumber("" + (400000 + id));
        child2.setIssueDate(LocalDate.of(2018, 7, 19));
        child2.setIssueDepartment(registerOffice3);
        child2.setAddress(address);

        studentOrder.setHusband(husband);
        studentOrder.setWife(wife);
        studentOrder.addChild(child1);
        studentOrder.addChild(child2);
        return studentOrder;
    }
}