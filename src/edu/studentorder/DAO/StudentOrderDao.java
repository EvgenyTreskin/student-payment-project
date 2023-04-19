package edu.studentorder.DAO;

import edu.studentorder.domain.StudentOrder;
import edu.studentorder.exeption.DaoException;

public interface StudentOrderDao {
    Long saveStudentOrder(StudentOrder studentOrder) throws DaoException;
}
