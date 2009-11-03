package vars.annotation.jpa

import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Version
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn
import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import groovy.beans.Bindable
import javax.persistence.TableGenerator

import vars.LinkCategory
import vars.annotation.Association
import vars.annotation.Observation
import vars.jpa.JPAEntity
import javax.persistence.EntityListeners
import vars.jpa.TransactionLogger
import vars.jpa.KeyNullifier
import vars.EntitySupportCategory
import javax.persistence.Transient
import vars.annotation.Association
import vars.annotation.Observation

@Entity(name = "Association")
@Table(name = "Association")
@EntityListeners( value = [TransactionLogger.class, KeyNullifier.class] )
@NamedQueries( value = [
    @NamedQuery(name = "Association.findById",
                query = "SELECT v FROM Association v WHERE v.id = :id"),
    @NamedQuery(name = "Association.findByLinkName",
                query = "SELECT v FROM Association v WHERE v.linkName = :linkName"),
    @NamedQuery(name = "Association.findByToConcept",
                query = "SELECT a FROM Association a WHERE a.toConcept = :toConcept") ,
    @NamedQuery(name = "Association.findByLinkValue",
                query = "SELECT a FROM Association a WHERE a.linkValue = :linkValue")
])
class GAssociation implements Serializable, Association, JPAEntity {

    @Transient
    private static final PROPS = Collections.unmodifiableList([Association.PROP_LINKNAME, Association.PROP_TOCONCEPT, Association.PROP_TOCONCEPT])

    @Id
    @Column(name = "id", nullable = false, updatable=false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Association_Gen")
    @TableGenerator(name = "Association_Gen", table = "UniqueID",
            pkColumnName = "TableName", valueColumnName = "NextID",
            pkColumnValue = "Association", allocationSize = 1)
    Long id

    /** Optimistic lock to prevent concurrent overwrites */
    @Version
    @Column(name = "LAST_UPDATED_TIME")
    private Timestamp updatedTime
    
    @ManyToOne(optional = false, targetEntity = GObservation.class)
    @JoinColumn(name = "ObservationID_FK")
    @Bindable
    Observation observation

    @Column(name = "LinkName", nullable = false, length = 50)
    @Bindable
    String linkName

    @Column(name = "ToConcept", nullable = false, length = 50)
    @Bindable
    String toConcept

    @Column(name = "LinkValue", nullable = false, length = 100)
    @Bindable
    String linkValue

    GAssociation() {
        // Default constructor
    }

    GAssociation(String linkName, String toConcept, String linkValue) {
        this.linkName = linkName
        this.toConcept = toConcept
        this.linkValue = linkValue
    }

    String getFromConcept() {
        return observation?.getConceptName()
    }

    String stringValue() {
        use (LinkCategory) {
            return formatLinkAsString()
        }
    }

    public String toString() {
        return stringValue()
    }


    @Override
    boolean equals(that) {
        return EntitySupportCategory.equals(this, that, PROPS)
    }

    @Override
    int hashCode() {
        return EntitySupportCategory.hashCode(this, PROPS)
    }
}