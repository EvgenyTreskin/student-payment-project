package edu.studentorder;

import edu.studentorder.DAO.DictionaryDaoImpl;
import edu.studentorder.DAO.StudentOrderDao;
import edu.studentorder.DAO.StudentOrderDaoImpl;
import edu.studentorder.domain.*;
import edu.studentorder.exeption.DaoException;

import java.time.LocalDate;
import java.util.List;

public class SaveStudentOrder {

    public static void main(String[] args) throws Exception {

//        List<Street> streets = new DictionaryDaoImpl().findStreet("нев");
//        for (Street s : streets) {
//            System.out.println(s.getStreetName());
//        }
//        List<PassportOffice> passportOffices = new DictionaryDaoImpl().findPassportOffices("010020000000");
//        for (PassportOffice p : passportOffices) {
//            System.out.println(p.getOfficeName());
//        }
//        List<RegisterOffice> registerOffices = new DictionaryDaoImpl().findRegisterOffices("010010000000");
//        for (RegisterOffice r : registerOffices) {
//            System.out.println(r.getOfficeName());
//        }
//        List<CountryArea> countryAreas1 = new DictionaryDaoImpl().findAreas("");
//        for (CountryArea c : countryAreas1) {
//            System.out.println(c.getAreaId() + ": " + c.getAreaName());
//        }
//        List<CountryArea> countryAreas2 = new DictionaryDaoImpl().findAreas("020000000000");
//        for (CountryArea c : countryAreas2) {
//            System.out.println(c.getAreaId() + ": " + c.getAreaName());
//        }
//        List<CountryArea> countryAreas3 = new DictionaryDaoImpl().findAreas("020010000000");
//        for (CountryArea c : countryAreas3) {
//            System.out.println(c.getAreaId() + ": " + c.getAreaName());
//        }
//        List<CountryArea> countryAreas4 = new DictionaryDaoImpl().findAreas("020010010000");
//        for (CountryArea c : countryAreas4) {
//            System.out.println(c.getAreaId() + ": " + c.getAreaName());
//        }

        StudentOrder s = buildStudentOrder(10);
        StudentOrderDao dao = new StudentOrderDaoImpl();
        Long id = dao.saveStudentOrder(s);
        System.out.println(id);
    }



    static long saveStudentOrder(StudentOrder studentOrder) {
        long answer = 199;
        System.out.println("saveStudentOrder:");
        return answer;
    }

    static StudentOrder buildStudentOrder(long id) {
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
        // Жена
        Adult wife = new Adult("Петрова", "Вероника", "Алекссевна", LocalDate.of(1998, 3, 12));
        PassportOffice passportOffice2 = new PassportOffice(2L, "", "");
        wife.setPassportSerial("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setIssueDate(LocalDate.of(2018, 4, 5));
        wife.setIssueDepartment(passportOffice2);
        wife.setStudentId("" + (200000 + id));
        wife.setAddress(address);
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
