/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vars.annotation.jpa;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import vars.PropertyChange;
import vars.annotation.Association;
import vars.annotation.Observation;
import vars.annotation.VideoFrame;
import vars.jpa.JPAEntity;
import vars.jpa.KeyNullifier;
import vars.jpa.TransactionLogger;

/**
 *
 * @author brian
 */

@Entity(name = "Observation")
@Table(name = "Observation")
@EntityListeners( {TransactionLogger.class, KeyNullifier.class} )
@NamedQueries( {
    @NamedQuery(name = "Observation.findById",
                query = "SELECT v FROM Observation v WHERE v.id = :id"),
    @NamedQuery(name = "Observation.findByConceptName",
                query = "SELECT v FROM Observation v WHERE v.conceptName = :conceptName"),
    @NamedQuery(name = "Observation.findByNotes", query = "SELECT o FROM Observation o WHERE o.notes = :notes"),
    @NamedQuery(name = "Observation.findByObservationDate",
                query = "SELECT o FROM Observation o WHERE o.observationDate = :observationDate") ,
    @NamedQuery(name = "Observation.findByObserver",
                query = "SELECT o FROM Observation o WHERE o.observer = :observer")
})
public class ObservationImpl implements Serializable, Observation, JPAEntity, PropertyChange {

    @Transient
    final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Observation_Gen")
    @TableGenerator(name = "Observation_Gen", table = "UniqueID",
            pkColumnName = "TableName", valueColumnName = "NextID",
            pkColumnValue = "Observation", allocationSize = 1)
    Long id;

    /** Optimistic lock to prevent concurrent overwrites */
    @Version
    @Column(name = "LAST_UPDATED_TIME")
    private Timestamp updatedTime;

    @ManyToOne(optional = false, targetEntity = VideoFrameImpl.class)
    @JoinColumn(name = "VideoFrameID_FK")
    VideoFrame videoFrame;

    @Column(name = "ObservationDTG")
    @Temporal(value = TemporalType.TIMESTAMP)
    Date observationDate;

    @Column(name = "Observer", length = 50)
    String observer;

    @Column(name = "ConceptName", nullable = false, length = 50)
    String conceptName;

    @Column(name = "Notes", length = 200)
    String notes;

    @OneToMany(targetEntity = AssociationImpl.class,
            mappedBy = "observation",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    Set<Association> associations;

    public Set<Association> getAssociations() {
        if (associations == null) {
            associations = new HashSet<Association>();
        }
        return associations;
    }

    public void addAssociation(Association association) {
        if (getAssociations().add(association)) {
            ((AssociationImpl) association).setObservation(this);
        }
        propertyChangeSupport.firePropertyChange(PROP_ASSOCIATIONS, null, associations); // This method is added by @Bindable
    }

    public void removeAssociation(Association association) {
        if (getAssociations().remove(association)) {
            ((AssociationImpl) association).setObservation(null);
            propertyChangeSupport.firePropertyChange(PROP_ASSOCIATIONS, null, associations); // This method is added by @Bindable
        }
    }

    public boolean hasSample() {
        return false;  // TODO implement this.
    }


    public String getConceptName() {
        return conceptName;
    }

    public String getNotes() {
       return notes;
    }

    public Date getObservationDate() {
        return observationDate;
    }

    public String getObserver() {
        return observer;
    }

    public VideoFrame getVideoFrame() {
        return videoFrame;
    }

    public void setConceptName(String conceptName) {
       this.conceptName = conceptName;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }

    public void setNotes(String string) {
        this.notes = string;
    }

    public void setObservationDate(Date dtg) {
        this.observationDate = dtg;
    }

    public void setObserver(String observer) {
        this.observer = observer;
    }

    public Long getId() {
        return id;
    }

    public void addPropertyChangeListener(String string, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(string, listener);
    }

    public void removePropertyChangeListener(String string, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(string, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    void setVideoFrame(VideoFrame videoFrame) {
        this.videoFrame = videoFrame;
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return propertyChangeSupport.getPropertyChangeListeners();
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String string) {
        return propertyChangeSupport.getPropertyChangeListeners(string);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObservationImpl other = (ObservationImpl) obj;
        if (this.observationDate != other.observationDate && (this.observationDate == null || !this.observationDate.equals(other.observationDate))) {
            return false;
        }
        if ((this.observer == null) ? (other.observer != null) : !this.observer.equals(other.observer)) {
            return false;
        }
        if ((this.conceptName == null) ? (other.conceptName != null) : !this.conceptName.equals(other.conceptName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.observationDate != null ? this.observationDate.hashCode() : 0);
        hash = 59 * hash + (this.observer != null ? this.observer.hashCode() : 0);
        hash = 59 * hash + (this.conceptName != null ? this.conceptName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(" ([id=").append(getId()).append("] conceptName=");
        sb.append(conceptName).append(", observer=").append(observer);
        sb.append(", observationDate=").append(observationDate).append(")");
        return sb.toString();
    }




}