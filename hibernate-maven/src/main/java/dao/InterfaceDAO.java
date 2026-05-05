package dao;
import java.util.List;

public interface InterfaceDAO {
    void save(Object o) throws Exception;
    void delete(Object o) throws Exception;
    void update(Object o) throws Exception;
    <T> T findById(Object id, Class<T> clazz) throws Exception;
    <T> List<T> findAll(Class<T> clazz) throws Exception;
    <T> List<T> findByCriteria(Object criteria, Class<T> clazz) throws Exception;
}
