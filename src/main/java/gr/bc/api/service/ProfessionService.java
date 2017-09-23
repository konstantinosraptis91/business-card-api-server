package gr.bc.api.service;

import gr.bc.api.model.Profession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.ProfessionDao;
import gr.bc.api.service.exception.ConflictException;
import gr.bc.api.service.exception.NotFoundException;
import gr.bc.api.service.exception.ServiceException;
import java.util.ArrayList;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author konstantinos
 */
@Service
public class ProfessionService {

    @Autowired
    @Qualifier("MySQLProfession")
    private ProfessionDao professionDao;

    /**
     * Save a profession
     *
     * @param profession The profession to be saved
     * @return The id of the new saved profession
     * @throws ServiceException
     */
    public long saveProfession(Profession profession) throws ServiceException {

        long id;

        try {
            id = professionDao.saveProfession(profession);
        } catch (DataAccessException ex) {
            throw new ConflictException(ex.getMessage());
        }

        return id;
    }

    /**
     * Update a profession
     *
     * @param id The id of the profession update will affect
     * @param profession The profession to be updated
     * @return
     * @throws ServiceException
     */
    public boolean updateProfession(long id, Profession profession) throws ServiceException {

        boolean result;

        try {
            result = professionDao.updateProfession(id, profession);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    /**
     * Delete a profession
     *
     * @param id The id of the profession delete will affect
     * @return
     * @throws ServiceException
     */
    public boolean deleteProfessionById(long id) throws ServiceException {

        boolean result;

        try {
            result = professionDao.deleteProfessionById(id);
        } catch (DataAccessException ex) {

            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }

            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    /**
     * Find a profession
     *
     * @param id The profession id
     * @return The profession
     * @throws ServiceException
     */
    public Profession findById(long id) throws ServiceException {

        Profession p;

        try {
            p = professionDao.findById(id);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return p;
    }

    /**
     * Find a profession
     *
     * @param name The profession name
     * @return The profession
     * @throws ServiceException
     */
    public Profession findByName(String name) throws ServiceException {

        Profession p;

        try {
            p = professionDao.findByName(name);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return p;
    }

    /**
     * Search for a profession. The difference between findByName and searchByName is that search looking for a
     * similar name and not for that name exacly
     *
     * @param name The name of the profession
     * @return The list of the professions
     * @throws ServiceException
     */
    public List<Profession> searchByName(String name) throws ServiceException {

        List<Profession> pList;

        try {
            pList = professionDao.searchByName(name);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return pList;
    }

    /**
     * Find all professions
     *
     * @return
     * @throws ServiceException
     */
    public List<Profession> findAllProfessions() throws ServiceException {

        List<Profession> pList;

        try {
            pList = professionDao.findAllProfessions();
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return pList;
    }

    /**
     * In case name is null find all professions, in other case find profession by name
     *
     * @param name The profession name
     * @return
     * @throws ServiceException
     */
    public List<Profession> find(String name) throws ServiceException {

        List<Profession> pList = new ArrayList<>();

        if (name == null) {
            pList = findAllProfessions();
        } else {
            Profession p = findByName(name);
            pList.add(p);
        }

        return pList;
    }

}
