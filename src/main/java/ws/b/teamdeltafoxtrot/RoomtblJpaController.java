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
import ws.b.teamdeltafoxtrot.entities.Typetbl;
import ws.b.teamdeltafoxtrot.entities.Bookingtbl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import ws.b.teamdeltafoxtrot.entities.Roomtbl;
import ws.b.teamdeltafoxtrot.exceptions.IllegalOrphanException;
import ws.b.teamdeltafoxtrot.exceptions.NonexistentEntityException;

/**
 *
 * @author priza
 */
public class RoomtblJpaController implements Serializable {

    public RoomtblJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Roomtbl roomtbl) {
        if (roomtbl.getBookingtblCollection() == null) {
            roomtbl.setBookingtblCollection(new ArrayList<Bookingtbl>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Typetbl typeId = roomtbl.getTypeId();
            if (typeId != null) {
                typeId = em.getReference(typeId.getClass(), typeId.getTypeId());
                roomtbl.setTypeId(typeId);
            }
            Collection<Bookingtbl> attachedBookingtblCollection = new ArrayList<Bookingtbl>();
            for (Bookingtbl bookingtblCollectionBookingtblToAttach : roomtbl.getBookingtblCollection()) {
                bookingtblCollectionBookingtblToAttach = em.getReference(bookingtblCollectionBookingtblToAttach.getClass(), bookingtblCollectionBookingtblToAttach.getBookId());
                attachedBookingtblCollection.add(bookingtblCollectionBookingtblToAttach);
            }
            roomtbl.setBookingtblCollection(attachedBookingtblCollection);
            em.persist(roomtbl);
            if (typeId != null) {
                typeId.getRoomtblCollection().add(roomtbl);
                typeId = em.merge(typeId);
            }
            for (Bookingtbl bookingtblCollectionBookingtbl : roomtbl.getBookingtblCollection()) {
                Roomtbl oldRIdOfBookingtblCollectionBookingtbl = bookingtblCollectionBookingtbl.getRId();
                bookingtblCollectionBookingtbl.setRId(roomtbl);
                bookingtblCollectionBookingtbl = em.merge(bookingtblCollectionBookingtbl);
                if (oldRIdOfBookingtblCollectionBookingtbl != null) {
                    oldRIdOfBookingtblCollectionBookingtbl.getBookingtblCollection().remove(bookingtblCollectionBookingtbl);
                    oldRIdOfBookingtblCollectionBookingtbl = em.merge(oldRIdOfBookingtblCollectionBookingtbl);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Roomtbl roomtbl) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Roomtbl persistentRoomtbl = em.find(Roomtbl.class, roomtbl.getRId());
            Typetbl typeIdOld = persistentRoomtbl.getTypeId();
            Typetbl typeIdNew = roomtbl.getTypeId();
            Collection<Bookingtbl> bookingtblCollectionOld = persistentRoomtbl.getBookingtblCollection();
            Collection<Bookingtbl> bookingtblCollectionNew = roomtbl.getBookingtblCollection();
            List<String> illegalOrphanMessages = null;
            for (Bookingtbl bookingtblCollectionOldBookingtbl : bookingtblCollectionOld) {
                if (!bookingtblCollectionNew.contains(bookingtblCollectionOldBookingtbl)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Bookingtbl " + bookingtblCollectionOldBookingtbl + " since its RId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (typeIdNew != null) {
                typeIdNew = em.getReference(typeIdNew.getClass(), typeIdNew.getTypeId());
                roomtbl.setTypeId(typeIdNew);
            }
            Collection<Bookingtbl> attachedBookingtblCollectionNew = new ArrayList<Bookingtbl>();
            for (Bookingtbl bookingtblCollectionNewBookingtblToAttach : bookingtblCollectionNew) {
                bookingtblCollectionNewBookingtblToAttach = em.getReference(bookingtblCollectionNewBookingtblToAttach.getClass(), bookingtblCollectionNewBookingtblToAttach.getBookId());
                attachedBookingtblCollectionNew.add(bookingtblCollectionNewBookingtblToAttach);
            }
            bookingtblCollectionNew = attachedBookingtblCollectionNew;
            roomtbl.setBookingtblCollection(bookingtblCollectionNew);
            roomtbl = em.merge(roomtbl);
            if (typeIdOld != null && !typeIdOld.equals(typeIdNew)) {
                typeIdOld.getRoomtblCollection().remove(roomtbl);
                typeIdOld = em.merge(typeIdOld);
            }
            if (typeIdNew != null && !typeIdNew.equals(typeIdOld)) {
                typeIdNew.getRoomtblCollection().add(roomtbl);
                typeIdNew = em.merge(typeIdNew);
            }
            for (Bookingtbl bookingtblCollectionNewBookingtbl : bookingtblCollectionNew) {
                if (!bookingtblCollectionOld.contains(bookingtblCollectionNewBookingtbl)) {
                    Roomtbl oldRIdOfBookingtblCollectionNewBookingtbl = bookingtblCollectionNewBookingtbl.getRId();
                    bookingtblCollectionNewBookingtbl.setRId(roomtbl);
                    bookingtblCollectionNewBookingtbl = em.merge(bookingtblCollectionNewBookingtbl);
                    if (oldRIdOfBookingtblCollectionNewBookingtbl != null && !oldRIdOfBookingtblCollectionNewBookingtbl.equals(roomtbl)) {
                        oldRIdOfBookingtblCollectionNewBookingtbl.getBookingtblCollection().remove(bookingtblCollectionNewBookingtbl);
                        oldRIdOfBookingtblCollectionNewBookingtbl = em.merge(oldRIdOfBookingtblCollectionNewBookingtbl);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = roomtbl.getRId();
                if (findRoomtbl(id) == null) {
                    throw new NonexistentEntityException("The roomtbl with id " + id + " no longer exists.");
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
            Roomtbl roomtbl;
            try {
                roomtbl = em.getReference(Roomtbl.class, id);
                roomtbl.getRId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roomtbl with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Bookingtbl> bookingtblCollectionOrphanCheck = roomtbl.getBookingtblCollection();
            for (Bookingtbl bookingtblCollectionOrphanCheckBookingtbl : bookingtblCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Roomtbl (" + roomtbl + ") cannot be destroyed since the Bookingtbl " + bookingtblCollectionOrphanCheckBookingtbl + " in its bookingtblCollection field has a non-nullable RId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Typetbl typeId = roomtbl.getTypeId();
            if (typeId != null) {
                typeId.getRoomtblCollection().remove(roomtbl);
                typeId = em.merge(typeId);
            }
            em.remove(roomtbl);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Roomtbl> findRoomtblEntities() {
        return findRoomtblEntities(true, -1, -1);
    }

    public List<Roomtbl> findRoomtblEntities(int maxResults, int firstResult) {
        return findRoomtblEntities(false, maxResults, firstResult);
    }

    private List<Roomtbl> findRoomtblEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Roomtbl.class));
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

    public Roomtbl findRoomtbl(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Roomtbl.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoomtblCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Roomtbl> rt = cq.from(Roomtbl.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
