package dao;

import java.lang.reflect.Field;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class JpaDAO implements InterfaceDAO {
    private EntityManager entityManager;

    public JpaDAO() {}

    public JpaDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private void ensureEntityManager() throws Exception {
        if (entityManager == null) {
            throw new Exception("EntityManager is null");
        }
    }

    private interface TxWork {
        void run() throws Exception;
    }

    private void inTransaction(TxWork work) throws Exception {
        ensureEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        boolean newTx = false;
        if (!tx.isActive()) {
            tx.begin();
            newTx = true;
        }
        try {
            work.run();
            if (newTx) {
                tx.commit();
            }
        } catch (Exception e) {
            if (newTx && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void save(Object o) throws Exception {
        inTransaction(() -> entityManager.persist(o));
    }

    @Override
    public void delete(Object o) throws Exception {
        inTransaction(() -> {
            Object managed = entityManager.contains(o) ? o : entityManager.merge(o);
            entityManager.remove(managed);
        });
    }

    @Override
    public void update(Object o) throws Exception {
        inTransaction(() -> entityManager.merge(o));
    }

    @Override
    public <T> T findById(Object id, Class<T> clazz) throws Exception {
        ensureEntityManager();
        return entityManager.find(clazz, id);
    }

    @Override
    public <T> java.util.List<T> findAll(Class<T> clazz) throws Exception {
        ensureEntityManager();
        return entityManager.createQuery("FROM " + clazz.getSimpleName(), clazz).getResultList();
    }

    @Override
    public <T> List<T> findByCriteria(Object criteria, Class<T> clazz) throws Exception {
        ensureEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(clazz);
            Root<T> root = cq.from(clazz);
            
            Field[] fields = criteria.getClass().getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                Object value = f.get(criteria);
                if (value != null) {
                    cq.where(cb.equal(root.get(f.getName()), value));
                }
            }
            return entityManager.createQuery(cq).getResultList();
    }

}
