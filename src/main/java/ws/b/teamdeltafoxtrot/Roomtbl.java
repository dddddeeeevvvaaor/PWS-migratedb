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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author priza
 */
@Entity
@Table(name = "roomtbl")
@NamedQueries({
    @NamedQuery(name = "Roomtbl.findAll", query = "SELECT r FROM Roomtbl r"),
    @NamedQuery(name = "Roomtbl.findByRId", query = "SELECT r FROM Roomtbl r WHERE r.rId = :rId"),
    @NamedQuery(name = "Roomtbl.findByRName", query = "SELECT r FROM Roomtbl r WHERE r.rName = :rName"),
    @NamedQuery(name = "Roomtbl.findByRStatus", query = "SELECT r FROM Roomtbl r WHERE r.rStatus = :rStatus")})
public class Roomtbl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "RId")
    private Integer rId;
    @Basic(optional = false)
    @Column(name = "RName")
    private String rName;
    @Basic(optional = false)
    @Column(name = "RStatus")
    private String rStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rId")
    private Collection<Bookingtbl> bookingtblCollection;
    @JoinColumn(name = "TypeId", referencedColumnName = "TypeId")
    @ManyToOne(optional = false)
    private Typetbl typeId;

    public Roomtbl() {
    }

    public Roomtbl(Integer rId) {
        this.rId = rId;
    }

    public Roomtbl(Integer rId, String rName, String rStatus) {
        this.rId = rId;
        this.rName = rName;
        this.rStatus = rStatus;
    }

    public Integer getRId() {
        return rId;
    }

    public void setRId(Integer rId) {
        this.rId = rId;
    }

    public String getRName() {
        return rName;
    }

    public void setRName(String rName) {
        this.rName = rName;
    }

    public String getRStatus() {
        return rStatus;
    }

    public void setRStatus(String rStatus) {
        this.rStatus = rStatus;
    }

    public Collection<Bookingtbl> getBookingtblCollection() {
        return bookingtblCollection;
    }

    public void setBookingtblCollection(Collection<Bookingtbl> bookingtblCollection) {
        this.bookingtblCollection = bookingtblCollection;
    }

    public Typetbl getTypeId() {
        return typeId;
    }

    public void setTypeId(Typetbl typeId) {
        this.typeId = typeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rId != null ? rId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Roomtbl)) {
            return false;
        }
        Roomtbl other = (Roomtbl) object;
        if ((this.rId == null && other.rId != null) || (this.rId != null && !this.rId.equals(other.rId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ws.b.teamdeltafoxtrot.Roomtbl[ rId=" + rId + " ]";
    }
    
}
