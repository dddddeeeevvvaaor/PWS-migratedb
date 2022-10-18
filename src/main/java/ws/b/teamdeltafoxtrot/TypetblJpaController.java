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
import ws.b.teamdeltafoxtrot.entities.Roomtbl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import ws.b.teamdeltafoxtrot.entities.Typetbl;
import ws.b.teamdeltafoxtrot.exceptions.IllegalOrphanException;
import ws.b.teamdeltafoxtrot.exceptions.NonexistentEntityException;

/**
 *
 * @author priza
 */
public class TypetblJpaController implements Serializable {

    public TypetblJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Typetbl typetbl) {
        if (typetbl.getRoomtblCollection() == null) {
            typetbl.setRoomtblCollection(new ArrayList<Roomtbl>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Roomtbl> attachedRoomtblCollection = new ArrayList<Roomtbl>();
            for (Roomtbl roomtblCollectionRoomtblToAttach : typetbl.getRoomtblCollection()) {
                roomtblCollectionRoomtblToAttach = em.getReference(roomtblCollectionRoomtblToAttach.getClass(), roomtblCollectionRoomtblToAttach.getRId());
                attachedRoomtblCollection.add(roomtblCollectionRoomtblToAttach);
            }
            typetbl.setRoomtblCollection(attachedRoomtblCollection);
            em.persist(typetbl);
            for (Roomtbl roomtblCollectionRoomtbl : typetbl.getRoomtblCollection()) {
                Typetbl oldTypeIdOfRoomtblCollectionRoomtbl = roomtblCollectionRoomtbl.getTypeId();
                roomtblCollectionRoomtbl.setTypeId(typetbl);
                roomtblCollectionRoomtbl = em.merge(roomtblCollectionRoomtbl);
                if (oldTypeIdOfRoomtblCollectionRoomtbl != null) {
                    oldTypeIdOfRoomtblCollectionRoomtbl.getRoomtblCollection().remove(roomtblCollectionRoomtbl);
                    oldTypeIdOfRoomtblCollectionRoomtbl = em.merge(oldTypeIdOfRoomtblCollectionRoomtbl);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Typetbl typetbl) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Typetbl persistentTypetbl = em.find(Typetbl.class, typetbl.getTypeId());
            Collection<Roomtbl> roomtblCollectionOld = persistentTypetbl.getRoomtblCollection();
            Collection<Roomtbl> roomtblCollectionNew = typetbl.getRoomtblCollection();
            List<String> illegalOrphanMessages = null;
            for (Roomtbl roomtblCollectionOldRoomtbl : roomtblCollectionOld) {
                if (!roomtblCollectionNew.contains(roomtblCollectionOldRoomtbl)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Roomtbl " + roomtblCollectionOldRoomtbl + " since its typeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Roomtbl> attachedRoomtblCollectionNew = new ArrayList<Roomtbl>();
            for (Roomtbl roomtblCollectionNewRoomtblToAttach : roomtblCollectionNew) {
                roomtblCollectionNewRoomtblToAttach = em.getReference(roomtblCollectionNewRoomtblToAttach.getClass(), roomtblCollectionNewRoomtblToAttach.getRId());
                attachedRoomtblCollectionNew.add(roomtblCollectionNewRoomtblToAttach);
            }
            roomtblCollectionNew = attachedRoomtblCollectionNew;
            typetbl.setRoomtblCollection(roomtblCollectionNew);
            typetbl = em.merge(typetbl);
            for (Roomtbl roomtblCollectionNewRoomtbl : roomtblCollectionNew) {
                if (!roomtblCollectionOld.contains(roomtblCollectionNewRoomtbl)) {
                    Typetbl oldTypeIdOfRoomtblCollectionNewRoomtbl = roomtblCollectionNewRoomtbl.getTypeId();
                    roomtblCollectionNewRoomtbl.setTypeId(typetbl);
                    roomtblCollectionNewRoomtbl = em.merge(roomtblCollectionNewRoomtbl);
                    if (oldTypeIdOfRoomtblCollectionNewRoomtbl != null && !oldTypeIdOfRoomtblCollectionNewRoomtbl.equals(typetbl)) {
                        oldTypeIdOfRoomtblCollectionNewRoomtbl.getRoomtblCollection().remove(roomtblCollectionNewRoomtbl);
                        oldTypeIdOfRoomtblCollectionNewRoomtbl = em.merge(oldTypeIdOfRoomtblCollectionNewRoomtbl);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = typetbl.getTypeId();
                if (findTypetbl(id) == null) {
                    throw new NonexistentEntityException("The typetbl with id " + id + " no longer exists.");
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
            Typetbl typetbl;
            try {
                typetbl = em.getReference(Typetbl.class, id);
                typetbl.getTypeId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The typetbl with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Roomtbl> roomtblCollectionOrphanCheck = typetbl.getRoomtblCollection();
            for (Roomtbl roomtblCollectionOrphanCheckRoomtbl : roomtblCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Typetbl (" + typetbl + ") cannot be destroyed since the Roomtbl " + roomtblCollectionOrphanCheckRoomtbl + " in its roomtblCollection field has a non-nullable typeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(typetbl);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Typetbl> findTypetblEntities() {
        return findTypetblEntities(true, -1, -1);
    }

    public List<Typetbl> findTypetblEntities(int maxResults, int firstResult) {
        return findTypetblEntities(false, maxResults, firstResult);
    }

    private List<Typetbl> findTypetblEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Typetbl.class));
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

    public Typetbl findTypetbl(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Typetbl.class, id);
        } finally {
            em.close();
        }
    }

    public int getTypetblCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Typetbl> rt = cq.from(Typetbl.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
