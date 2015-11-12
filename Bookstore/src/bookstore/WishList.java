/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cpp5126
 */
@Entity
@Table(name = "WishList")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WishList.findAll", query = "SELECT w FROM WishList w"),
    @NamedQuery(name = "WishList.findByWishListID", query = "SELECT w FROM WishList w WHERE w.wishListID = :wishListID"),
    @NamedQuery(name = "WishList.findByDateCreated", query = "SELECT w FROM WishList w WHERE w.dateCreated = :dateCreated"),
    @NamedQuery(name = "WishList.findByOrderNum", query = "SELECT w FROM WishList w WHERE w.orderNum = :orderNum"),
    @NamedQuery(name = "WishList.findByStatus", query = "SELECT w FROM WishList w WHERE w.status = :status")})
public class WishList implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "WishListID")
    private Integer wishListID;
    @Column(name = "DateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "OrderNum")
    private Integer orderNum;
    @Column(name = "Status")
    private Integer status;
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    @ManyToOne
    private User userID;

    public WishList() {
    }

    public WishList(Integer wishListID) {
        this.wishListID = wishListID;
    }

    public Integer getWishListID() {
        return wishListID;
    }

    public void setWishListID(Integer wishListID) {
        this.wishListID = wishListID;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wishListID != null ? wishListID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WishList)) {
            return false;
        }
        WishList other = (WishList) object;
        if ((this.wishListID == null && other.wishListID != null) || (this.wishListID != null && !this.wishListID.equals(other.wishListID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bookstore.WishList[ wishListID=" + wishListID + " ]";
    }
    
}
