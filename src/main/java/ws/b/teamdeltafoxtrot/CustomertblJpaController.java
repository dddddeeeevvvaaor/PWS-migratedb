/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.teamdeltafoxtrot;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ws.b.teamdeltafoxtrot.entities.Bookingtbl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import ws.b.teamdeltafoxtrot.entities.Customertbl;
import ws.b.teamdeltafoxtrot.exceptions.IllegalOrphanException;
import ws.b.teamdeltafoxtrot.exceptions.NonexistentEntityException;

/**
 *
 * @author priza
 */
public class CustomertblJpaController implements Serializable {

    public CustomertblJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customertbl customertbl) {
        if (customertbl.getBookingtblCollection() == null) {
            customertbl.setBookingtblCollection(new ArrayList<Bookingtbl>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Bookingtbl> attachedBookingtblCollection = new ArrayList<Bookingtbl>();
            for (Bookingtbl bookingtblCollectionBookingtblToAttach : customertbl.getBookingtblCollection()) {
                bookingtblCollectionBookingtblToAttach = em.getReference(bookingtblCollectionBookingtblToAttach.getClass(), bookingtblCollectionBookingtblToAttach.getBookId());
                attachedBookingtblCollection.add(bookingtblCollectionBookingtblToAttach);
            }
            customertbl.setBookingtblCollection(attachedBookingtblCollection);
            em.persist(customertbl);
            for (Bookingtbl bookingtblCollectionBookingtbl : customertbl.getBookingtblCollection()) {
                Customertbl oldCustIdOfBookingtblCollectionBookingtbl = bookingtblCollectionBookingtbl.getCustId();
                bookingtblCollectionBookingtbl.setCustId(customertbl);
                bookingtblCollectionBookingtbl = em.merge(bookingtblCollectionBookingtbl);
                if (oldCustIdOfBookingtblCollectionBookingtbl != null) {
                    oldCustIdOfBookingtblCollectionBookingtbl.getBookingtblCollection().remove(bookingtblCollectionBookingtbl);
                    oldCustIdOfBookingtblCollectionBookingtbl = em.merge(oldCustIdOfBookingtblCollectionBookingtbl);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Customertbl customertbl) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customertbl persistentCustomertbl = em.find(Customertbl.class, customertbl.getCustId());
            Collection<Bookingtbl> bookingtblCollectionOld = persistentCustomertbl.getBookingtblCollection();
            Collection<Bookingtbl> bookingtblCollectionNew = customertbl.getBookingtblCollection();
            List<String> illegalOrphanMessages = null;
            for (Bookingtbl bookingtblCollectionOldBookingtbl : bookingtblCollectionOld) {
                if (!bookingtblCollectionNew.contains(bookingtblCollectionOldBookingtbl)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Bookingtbl " + bookingtblCollectionOldBookingtbl + " since its custId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Bookingtbl> attachedBookingtblCollectionNew = new ArrayList<Bookingtbl>();
            for (Bookingtbl bookingtblCollectionNewBookingtblToAttach : bookingtblCollectionNew) {
                bookingtblCollectionNewBookingtblToAttach = em.getReference(bookingtblCollectionNewBookingtblToAttach.getClass(), bookingtblCollectionNewBookingtblToAttach.getBookId());
                attachedBookingtblCollectionNew.add(bookingtblCollectionNewBookingtblToAttach);
            }
            bookingtblCollectionNew = attachedBookingtblCollectionNew;
            customertbl.setBookingtblCollection(bookingtblCollectionNew);
            customertbl = em.merge(customertbl);
            for (Bookingtbl bookingtblCollectionNewBookingtbl : bookingtblCollectionNew) {
                if (!bookingtblCollectionOld.contains(bookingtblCollectionNewBookingtbl)) {
                    Customertbl oldCustIdOfBookingtblCollectionNewBookingtbl = bookingtblCollectionNewBookingtbl.getCustId();
                    bookingtblCollectionNewBookingtbl.setCustId(customertbl);
                    bookingtblCollectionNewBookingtbl = em.merge(bookingtblCollectionNewBookingtbl);
                    if (oldCustIdOfBookingtblCollectionNewBookingtbl != null && !oldCustIdOfBookingtblCollectionNewBookingtbl.equals(customertbl)) {
                        oldCustIdOfBookingtblCollectionNewBookingtbl.getBookingtblCollection().remove(bookingtblCollectionNewBookingtbl);
                        oldCustIdOfBookingtblCollectionNewBookingtbl = em.merge(oldCustIdOfBookingtblCollectionNewBookingtbl);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = customertbl.getCustId();
                if (findCustomertbl(id) == null) {
                    throw new NonexistentEntityException("The customertbl with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customertbl customertbl;
            try {
                customertbl = em.getReference(Customertbl.class, id);
                customertbl.getCustId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customertbl with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Bookingtbl> bookingtblCollectionOrphanCheck = customertbl.getBookingtblCollection();
            for (Bookingtbl bookingtblCollectionOrphanCheckBookingtbl : bookingtblCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Customertbl (" + customertbl + ") cannot be destroyed since the Bookingtbl " + bookingtblCollectionOrphanCheckBookingtbl + " in its bookingtblCollection field has a non-nullable custId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(customertbl);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customertbl> findCustomertblEntities() {
        return findCustomertblEntities(true, -1, -1);
    }

    public List<Customertbl> findCustomertblEntities(int maxResults, int firstResult) {
        return findCustomertblEntities(false, maxResults, firstResult);
    }

    private List<Customertbl> findCustomertblEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customertbl.class));
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

    public Customertbl findCustomertbl(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customertbl.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomertblCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customertbl> rt = cq.from(Customertbl.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
