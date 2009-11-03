package vars.annotation.jpa;

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.CascadeType
import javax.persistence.Version
import java.sql.Timestamp
import javax.persistence.OrderBy
import javax.persistence.TableGenerator
import vars.annotation.VideoArchiveSet
import vars.annotation.CameraDeployment
import vars.annotation.VideoArchive
import vars.annotation.VideoFrame
import vars.jpa.JPAEntity
import vars.EntitySupportCategory
import javax.persistence.EntityListeners
import vars.jpa.TransactionLogger
import vars.jpa.KeyNullifier
import javax.persistence.Transient

@Entity(name = "VideoArchiveSet")
@Table(name = "VideoArchiveSet")
@EntityListeners( value = [TransactionLogger.class, KeyNullifier.class] )
@NamedQueries( value = [
    @NamedQuery(name = "VideoArchiveSet.findById", 
                query = "SELECT v FROM VideoArchiveSet v WHERE v.id = :id"),
    @NamedQuery(name = "VideoArchiveSet.findAll",
                query = "SELECT v FROM VideoArchiveSet v"),
    @NamedQuery(name = "VideoArchiveSet.findByTrackingNumber",
                query = "SELECT v FROM VideoArchiveSet v WHERE v.trackingNumber = :trackingNumber"),
    @NamedQuery(name = "VideoArchiveSet.findByPlatformName",
                query = "SELECT v FROM VideoArchiveSet v WHERE v.platformName = :platformName"),
    @NamedQuery(name = "VideoArchiveSet.findByFormatCode",
                query = "SELECT v FROM VideoArchiveSet v WHERE v.formatCode = :formatCode"),
    @NamedQuery(name = "VideoArchiveSet.findByStartDate",
                query = "SELECT v FROM VideoArchiveSet v WHERE v.startDate = :startDate"),
    @NamedQuery(name = "VideoArchiveSet.findByEndDate",
                query = "SELECT v FROM VideoArchiveSet v WHERE v.endDate = :endDate"),
    @NamedQuery(name = "VideoArchiveSet.findBetweenDates",
                query = "SELECT v FROM VideoArchiveSet v WHERE v.startDate BETWEEN :date0 AND :date1"),
    @NamedQuery(name = "VideoArchiveSet.findByPlatformAndTrackingNumber",
                query = "SELECT v FROM VideoArchiveSet v WHERE v.platformName = :platformName AND v.trackingNumber = :trackingNumber"),
    @NamedQuery(name = "VideoArchiveSet.findByPlatformAndSequenceNumber",
                query = "SELECT v FROM VideoArchiveSet v, IN (v.cameraDeployments) c WHERE v.platformName = :platformName AND c.sequenceNumber = :sequenceNumber")
])
class GVideoArchiveSet implements Serializable, VideoArchiveSet, JPAEntity {

    @Transient
    private static final PROPS = Collections.unmodifiableList([VideoArchiveSet.PROP_PLATFORM_NAME,
            VideoArchiveSet.PROP_TRACKING_NUMBER, VideoArchiveSet.PROP_START_DATE])


    @Id 
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "VideoArchiveSet_Gen")
    @TableGenerator(name = "VideoArchiveSet_Gen", table = "UniqueID",
            pkColumnName = "TableName", valueColumnName = "NextID",
            pkColumnValue = "VideoArchiveSet", allocationSize = 1)
    Long id

    /** Optimistic lock to prevent concurrent overwrites */
    @Version
    @Column(name = "LAST_UPDATED_TIME")
    private Timestamp updatedTime
    
    @Column(name = "TrackingNumber", length = 7)
    String trackingNumber

    @Column(name = "ShipName", length = 32)
    @Deprecated
    String shipName
    
    @Column(name = "PlatformName", nullable = false, length = 32)
    String platformName
    
    @Column(name = "FormatCode", length = 2)
    char formatCode
    
    @Column(name = "StartDTG")
    @Temporal(value = TemporalType.TIMESTAMP)
    Date startDate
    
    @Column(name = "EndDTG")
    @Temporal(value = TemporalType.TIMESTAMP)
    Date endDate
    
    @OneToMany(targetEntity = GVideoArchive.class,
            mappedBy = "videoArchiveSet",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @OrderBy(value = "name")
    Set<VideoArchive> videoArchives
    
    @OneToMany(targetEntity = GCameraDeployment.class,
            mappedBy = "videoArchiveSet",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    Set<CameraDeployment> cameraDeployments

    Set<CameraDeployment> getCameraDeployments() {
        if (cameraDeployments == null) {
            cameraDeployments = new HashSet()
        }
        return cameraDeployments
    }

    Set<VideoArchive> getVideoArchives() {
        if (videoArchives == null) {
            videoArchives = new HashSet<VideoArchive>()
        }
        return videoArchives
    }

    void addVideoArchive(VideoArchive videoArchive) {
        if (getVideoArchives().find { VideoArchive va -> va.name.equals(videoArchive.name) }) {
            throw new IllegalArgumentException("A VideoArchive named '${va.name} already exists in ${this}")
        }
        videoArchives << videoArchive
        videoArchive.videoArchiveSet = this
    }

    void removeVideoArchive(VideoArchive videoArchive) {
        videoArchives.remove(videoArchive)
        videoArchive.videoArchiveSet = null
    }

    void addCameraDeployment(CameraDeployment cameraDeployment) {
        getCameraDeployments().add(cameraDeployment)
        cameraDeployment.videoArchiveSet = this
    }

    void removeCameraDeployment(CameraDeployment cameraDeployment) {
        getCameraDeployments().remove(cameraDeployment)
        cameraDeployment.videoArchiveSet = null
    }

    VideoArchive getVideoArchiveByName(String videoArchiveName) {
        (VideoArchive) videoArchives.find { it.name.equals(videoArchiveName) }
    }

    List<VideoFrame> getVideoFrames() {
        def videoFrames = new ArrayList<? extends VideoFrame>()
        videoArchives.each { va ->
            videoFrames.addAll(va.videoFrames)
        }
        return videoFrames.sort { it.timecode }
    }

    boolean hasSequenceNumber(int seqNumber) {
        return (cameraDeployments.find {it.sequenceNumber == seqNumber} != null)
    }

    boolean hasVideoArchiveName(String videoArchiveName) {
        return (videoArchives.find { it.name.equals(videoArchiveName) } != null)
    }

    @Override
    String toString() {
        return EntitySupportCategory.basicToString(this, PROPS)
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