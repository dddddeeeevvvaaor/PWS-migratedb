/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.teamdeltafoxtrot;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author priza
 */
@Entity
@Table(name = "typetbl")
@NamedQueries({
    @NamedQuery(name = "Typetbl.findAll", query = "SELECT t FROM Typetbl t"),
    @NamedQuery(name = "Typetbl.findByTypeId", query = "SELECT t FROM Typetbl t WHERE t.typeId = :typeId"),
    @NamedQuery(name = "Typetbl.findByTypeName", query = "SELECT t FROM Typetbl t WHERE t.typeName = :typeName"),
    @NamedQuery(name = "Typetbl.findByTypeCost", query = "SELECT t FROM Typetbl t WHERE t.typeCost = :typeCost")})
public class Typetbl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TypeId")
    private Integer typeId;
    @Basic(optional = false)
    @Column(name = "TypeName")
    private String typeName;
    @Basic(optional = false)
    @Column(name = "TypeCost")
    private int typeCost;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeId")
    private Collection<Roomtbl> roomtblCollection;

    public Typetbl() {
    }

    public Typetbl(Integer typeId) {
        this.typeId = typeId;
    }

    public Typetbl(Integer typeId, String typeName, int typeCost) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.typeCost = typeCost;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeCost() {
        return typeCost;
    }

    public void setTypeCost(int typeCost) {
        this.typeCost = typeCost;
    }

    public Collection<Roomtbl> getRoomtblCollection() {
        return roomtblCollection;
    }

    public void setRoomtblCollection(Collection<Roomtbl> roomtblCollection) {
        this.roomtblCollection = roomtblCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeId != null ? typeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Typetbl)) {
            return false;
        }
        Typetbl other = (Typetbl) object;
        if ((this.typeId == null && other.typeId != null) || (this.typeId != null && !this.typeId.equals(other.typeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ws.b.teamdeltafoxtrot.Typetbl[ typeId=" + typeId + " ]";
    }
    
}
