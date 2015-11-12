/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

import bookstore.exceptions.NonexistentEntityException;
import bookstore.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author cpp5126
 */
public class WishListJpaController implements Serializable {

    public WishListJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WishList wishList) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userID = wishList.getUserID();
            if (userID != null) {
                userID = em.getReference(userID.getClass(), userID.getUserID());
                wishList.setUserID(userID);
            }
            em.persist(wishList);
            if (userID != null) {
                userID.getWishListCollection().add(wishList);
                userID = em.merge(userID);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findWishList(wishList.getWishListID()) != null) {
                throw new PreexistingEntityException("WishList " + wishList + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WishList wishList) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WishList persistentWishList = em.find(WishList.class, wishList.getWishListID());
            User userIDOld = persistentWishList.getUserID();
            User userIDNew = wishList.getUserID();
            if (userIDNew != null) {
                userIDNew = em.getReference(userIDNew.getClass(), userIDNew.getUserID());
                wishList.setUserID(userIDNew);
            }
            wishList = em.merge(wishList);
            if (userIDOld != null && !userIDOld.equals(userIDNew)) {
                userIDOld.getWishListCollection().remove(wishList);
                userIDOld = em.merge(userIDOld);
            }
            if (userIDNew != null && !userIDNew.equals(userIDOld)) {
                userIDNew.getWishListCollection().add(wishList);
                userIDNew = em.merge(userIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = wishList.getWishListID();
                if (findWishList(id) == null) {
                    throw new NonexistentEntityException("The wishList with id " + id + " no longer exists.");
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
            WishList wishList;
            try {
                wishList = em.getReference(WishList.class, id);
                wishList.getWishListID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The wishList with id " + id + " no longer exists.", enfe);
            }
            User userID = wishList.getUserID();
            if (userID != null) {
                userID.getWishListCollection().remove(wishList);
                userID = em.merge(userID);
            }
            em.remove(wishList);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WishList> findWishListEntities() {
        return findWishListEntities(true, -1, -1);
    }

    public List<WishList> findWishListEntities(int maxResults, int firstResult) {
        return findWishListEntities(false, maxResults, firstResult);
    }

    private List<WishList> findWishListEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WishList.class));
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

    public WishList findWishList(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WishList.class, id);
        } finally {
            em.close();
        }
    }

    public int getWishListCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WishList> rt = cq.from(WishList.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
