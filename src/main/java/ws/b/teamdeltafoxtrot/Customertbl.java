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
@Table(name = "customertbl")
@NamedQueries({
    @NamedQuery(name = "Customertbl.findAll", query = "SELECT c FROM Customertbl c"),
    @NamedQuery(name = "Customertbl.findByCustId", query = "SELECT c FROM Customertbl c WHERE c.custId = :custId"),
    @NamedQuery(name = "Customertbl.findByCustName", query = "SELECT c FROM Customertbl c WHERE c.custName = :custName"),
    @NamedQuery(name = "Customertbl.findByCustPhone", query = "SELECT c FROM Customertbl c WHERE c.custPhone = :custPhone"),
    @NamedQuery(name = "Customertbl.findByCustGender", query = "SELECT c FROM Customertbl c WHERE c.custGender = :custGender")})
public class Customertbl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CustId")
    private Integer custId;
    @Basic(optional = false)
    @Column(name = "CustName")
    private String custName;
    @Basic(optional = false)
    @Column(name = "CustPhone")
    private String custPhone;
    @Basic(optional = false)
    @Column(name = "CustGender")
    private String custGender;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "custId")
    private Collection<Bookingtbl> bookingtblCollection;

    public Customertbl() {
    }

    public Customertbl(Integer custId) {
        this.custId = custId;
    }

    public Customertbl(Integer custId, String custName, String custPhone, String custGender) {
        this.custId = custId;
        this.custName = custName;
        this.custPhone = custPhone;
        this.custGender = custGender;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustGender() {
        return custGender;
    }

    public void setCustGender(String custGender) {
        this.custGender = custGender;
    }

    public Collection<Bookingtbl> getBookingtblCollection() {
        return bookingtblCollection;
    }

    public void setBookingtblCollection(Collection<Bookingtbl> bookingtblCollection) {
        this.bookingtblCollection = bookingtblCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (custId != null ? custId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customertbl)) {
            return false;
        }
        Customertbl other = (Customertbl) object;
        if ((this.custId == null && other.custId != null) || (this.custId != null && !this.custId.equals(other.custId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ws.b.teamdeltafoxtrot.Customertbl[ custId=" + custId + " ]";
    }
    
}
