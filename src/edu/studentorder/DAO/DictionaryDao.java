package edu.studentorder.DAO;

import edu.studentorder.domain.PassportOffice;
import edu.studentorder.domain.RegisterOffice;
import edu.studentorder.domain.Street;
import edu.studentorder.exeption.DaoException;

import java.util.List;

public interface DictionaryDao {

    List<Street> findStreet(String pattern) throws DaoException;
    List<PassportOffice> findPassportOffices(String areaId) throws DaoException;
    List<RegisterOffice> findRegisterOffice(String areaId) throws DaoException;
}
