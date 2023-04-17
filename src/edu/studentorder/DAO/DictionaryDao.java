package edu.studentorder.DAO;

import edu.studentorder.domain.Street;
import edu.studentorder.exeption.DaoException;

import java.util.List;

public interface DictionaryDao {

    List<Street> findStreet(String pattern) throws DaoException;
}
