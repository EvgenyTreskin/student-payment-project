package edu.studentorder.DAO;

import edu.studentorder.domain.StudentOrder;
import edu.studentorder.exeption.DaoException;

import java.util.List;

public interface StudentOrderDao {
    Long saveStudentOrder(StudentOrder studentOrder) throws DaoException;

    List<StudentOrder> getStudentOrders() throws DaoException;
 }
