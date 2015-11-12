/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

import bookstore.exceptions.NonexistentEntityException;
import bookstore.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cpp5126
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) throws PreexistingEntityException, Exception {
        if (user.getWishListCollection() == null) {
            user.setWishListCollection(new ArrayList<WishList>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<WishList> attachedWishListCollection = new ArrayList<WishList>();
            for (WishList wishListCollectionWishListToAttach : user.getWishListCollection()) {
                wishListCollectionWishListToAttach = em.getReference(wishListCollectionWishListToAttach.getClass(), wishListCollectionWishListToAttach.getWishListID());
                attachedWishListCollection.add(wishListCollectionWishListToAttach);
            }
            user.setWishListCollection(attachedWishListCollection);
            em.persist(user);
            for (WishList wishListCollectionWishList : user.getWishListCollection()) {
                User oldUserIDOfWishListCollectionWishList = wishListCollectionWishList.getUserID();
                wishListCollectionWishList.setUserID(user);
                wishListCollectionWishList = em.merge(wishListCollectionWishList);
                if (oldUserIDOfWishListCollectionWishList != null) {
                    oldUserIDOfWishListCollectionWishList.getWishListCollection().remove(wishListCollectionWishList);
                    oldUserIDOfWishListCollectionWishList = em.merge(oldUserIDOfWishListCollectionWishList);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUser(user.getUserID()) != null) {
                throw new PreexistingEntityException("User " + user + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getUserID());
            Collection<WishList> wishListCollectionOld = persistentUser.getWishListCollection();
            Collection<WishList> wishListCollectionNew = user.getWishListCollection();
            Collection<WishList> attachedWishListCollectionNew = new ArrayList<WishList>();
            for (WishList wishListCollectionNewWishListToAttach : wishListCollectionNew) {
                wishListCollectionNewWishListToAttach = em.getReference(wishListCollectionNewWishListToAttach.getClass(), wishListCollectionNewWishListToAttach.getWishListID());
                attachedWishListCollectionNew.add(wishListCollectionNewWishListToAttach);
            }
            wishListCollectionNew = attachedWishListCollectionNew;
            user.setWishListCollection(wishListCollectionNew);
            user = em.merge(user);
            for (WishList wishListCollectionOldWishList : wishListCollectionOld) {
                if (!wishListCollectionNew.contains(wishListCollectionOldWishList)) {
                    wishListCollectionOldWishList.setUserID(null);
                    wishListCollectionOldWishList = em.merge(wishListCollectionOldWishList);
                }
            }
            for (WishList wishListCollectionNewWishList : wishListCollectionNew) {
                if (!wishListCollectionOld.contains(wishListCollectionNewWishList)) {
                    User oldUserIDOfWishListCollectionNewWishList = wishListCollectionNewWishList.getUserID();
                    wishListCollectionNewWishList.setUserID(user);
                    wishListCollectionNewWishList = em.merge(wishListCollectionNewWishList);
                    if (oldUserIDOfWishListCollectionNewWishList != null && !oldUserIDOfWishListCollectionNewWishList.equals(user)) {
                        oldUserIDOfWishListCollectionNewWishList.getWishListCollection().remove(wishListCollectionNewWishList);
                        oldUserIDOfWishListCollectionNewWishList = em.merge(oldUserIDOfWishListCollectionNewWishList);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getUserID();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getUserID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            Collection<WishList> wishListCollection = user.getWishListCollection();
            for (WishList wishListCollectionWishList : wishListCollection) {
                wishListCollectionWishList.setUserID(null);
                wishListCollectionWishList = em.merge(wishListCollectionWishList);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
