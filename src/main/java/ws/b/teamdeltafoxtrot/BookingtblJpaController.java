/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.teamdeltafoxtrot;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ws.b.teamdeltafoxtrot.entities.Bookingtbl;
import ws.b.teamdeltafoxtrot.entities.Customertbl;
import ws.b.teamdeltafoxtrot.entities.Roomtbl;
import ws.b.teamdeltafoxtrot.exceptions.NonexistentEntityException;

/**
 *
 * @author priza
 */
public class BookingtblJpaController implements Serializable {

    public BookingtblJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ws.b_teamdeltafoxtrot_jar_0.0.1-SNAPSHOTPU");

    public BookingtblJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Bookingtbl bookingtbl) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customertbl custId = bookingtbl.getCustId();
            if (custId != null) {
                custId = em.getReference(custId.getClass(), custId.getCustId());
                bookingtbl.setCustId(custId);
            }
            Roomtbl RId = bookingtbl.getRId();
            if (RId != null) {
                RId = em.getReference(RId.getClass(), RId.getRId());
                bookingtbl.setRId(RId);
            }
            em.persist(bookingtbl);
            if (custId != null) {
                custId.getBookingtblCollection().add(bookingtbl);
                custId = em.merge(custId);
            }
            if (RId != null) {
                RId.getBookingtblCollection().add(bookingtbl);
                RId = em.merge(RId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Bookingtbl bookingtbl) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bookingtbl persistentBookingtbl = em.find(Bookingtbl.class, bookingtbl.getBookId());
            Customertbl custIdOld = persistentBookingtbl.getCustId();
            Customertbl custIdNew = bookingtbl.getCustId();
            Roomtbl RIdOld = persistentBookingtbl.getRId();
            Roomtbl RIdNew = bookingtbl.getRId();
            if (custIdNew != null) {
                custIdNew = em.getReference(custIdNew.getClass(), custIdNew.getCustId());
                bookingtbl.setCustId(custIdNew);
            }
            if (RIdNew != null) {
                RIdNew = em.getReference(RIdNew.getClass(), RIdNew.getRId());
                bookingtbl.setRId(RIdNew);
            }
            bookingtbl = em.merge(bookingtbl);
            if (custIdOld != null && !custIdOld.equals(custIdNew)) {
                custIdOld.getBookingtblCollection().remove(bookingtbl);
                custIdOld = em.merge(custIdOld);
            }
            if (custIdNew != null && !custIdNew.equals(custIdOld)) {
                custIdNew.getBookingtblCollection().add(bookingtbl);
                custIdNew = em.merge(custIdNew);
            }
            if (RIdOld != null && !RIdOld.equals(RIdNew)) {
                RIdOld.getBookingtblCollection().remove(bookingtbl);
                RIdOld = em.merge(RIdOld);
            }
            if (RIdNew != null && !RIdNew.equals(RIdOld)) {
                RIdNew.getBookingtblCollection().add(bookingtbl);
                RIdNew = em.merge(RIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bookingtbl.getBookId();
                if (findBookingtbl(id) == null) {
                    throw new NonexistentEntityException("The bookingtbl with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bookingtbl bookingtbl;
            try {
                bookingtbl = em.getReference(Bookingtbl.class, id);
                bookingtbl.getBookId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bookingtbl with id " + id + " no longer exists.", enfe);
            }
            Customertbl custId = bookingtbl.getCustId();
            if (custId != null) {
                custId.getBookingtblCollection().remove(bookingtbl);
                custId = em.merge(custId);
            }
            Roomtbl RId = bookingtbl.getRId();
            if (RId != null) {
                RId.getBookingtblCollection().remove(bookingtbl);
                RId = em.merge(RId);
            }
            em.remove(bookingtbl);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Bookingtbl> findBookingtblEntities() {
        return findBookingtblEntities(true, -1, -1);
    }

    public List<Bookingtbl> findBookingtblEntities(int maxResults, int firstResult) {
        return findBookingtblEntities(false, maxResults, firstResult);
    }

    private List<Bookingtbl> findBookingtblEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Bookingtbl.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Bookingtbl findBookingtbl(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bookingtbl.class, id);
        } finally {
            em.close();
        }
    }

    public int getBookingtblCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Bookingtbl> rt = cq.from(Bookingtbl.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
