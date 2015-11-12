/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cpp5126
 */
@Entity
@Table(name = "Shipment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Shipment.findAll", query = "SELECT s FROM Shipment s"),
    @NamedQuery(name = "Shipment.findByShipmentID", query = "SELECT s FROM Shipment s WHERE s.shipmentID = :shipmentID"),
    @NamedQuery(name = "Shipment.findByOrderedItems", query = "SELECT s FROM Shipment s WHERE s.orderedItems = :orderedItems"),
    @NamedQuery(name = "Shipment.findByPackingList", query = "SELECT s FROM Shipment s WHERE s.packingList = :packingList"),
    @NamedQuery(name = "Shipment.findByContainers", query = "SELECT s FROM Shipment s WHERE s.containers = :containers"),
    @NamedQuery(name = "Shipment.findByInvoice", query = "SELECT s FROM Shipment s WHERE s.invoice = :invoice"),
    @NamedQuery(name = "Shipment.findByDelivery", query = "SELECT s FROM Shipment s WHERE s.delivery = :delivery")})
public class Shipment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ShipmentID")
    private Integer shipmentID;
    @Column(name = "OrderedItems")
    private Integer orderedItems;
    @Column(name = "PackingList")
    private String packingList;
    @Column(name = "Containers")
    private String containers;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Invoice")
    private BigDecimal invoice;
    @Column(name = "Delivery")
    private String delivery;

    public Shipment() {
    }

    public Shipment(Integer shipmentID) {
        this.shipmentID = shipmentID;
    }

    public Integer getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(Integer shipmentID) {
        this.shipmentID = shipmentID;
    }

    public Integer getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(Integer orderedItems) {
        this.orderedItems = orderedItems;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public String getContainers() {
        return containers;
    }

    public void setContainers(String containers) {
        this.containers = containers;
    }

    public BigDecimal getInvoice() {
        return invoice;
    }

    public void setInvoice(BigDecimal invoice) {
        this.invoice = invoice;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (shipmentID != null ? shipmentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Shipment)) {
            return false;
        }
        Shipment other = (Shipment) object;
        if ((this.shipmentID == null && other.shipmentID != null) || (this.shipmentID != null && !this.shipmentID.equals(other.shipmentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bookstore.Shipment[ shipmentID=" + shipmentID + " ]";
    }
    
}
