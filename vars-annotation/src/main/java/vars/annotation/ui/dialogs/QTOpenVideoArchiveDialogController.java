package vars.annotation.ui.dialogs;

import org.bushe.swing.event.EventBus;
import org.mbari.vcr.qt.TimeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vars.annotation.VideoArchive;
import vars.annotation.VideoArchiveDAO;
import vars.annotation.VideoFrame;
import vars.annotation.ui.Lookup;
import vars.quicktime.QTVideoControlServiceImpl;
import vars.shared.ui.video.FakeImageCaptureServiceImpl;

import java.util.Collection;

public class QTOpenVideoArchiveDialogController {

    private final QTOpenVideoArchiveDialog dialog;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public QTOpenVideoArchiveDialogController(QTOpenVideoArchiveDialog dialog) {
        this.dialog = dialog;
    }

    public VideoArchive openVideoArchive() {
        VideoArchive videoArchive = null;
        if (dialog.getOpenByLocationRB().isSelected()) {
            // Check to see if it exists
            videoArchive = findByLocation(dialog.getUrlTextField().getText());

            // I it doesn't exist create it using the parameters
            if (videoArchive == null) {
                videoArchive = createVideoArchive();
            }
        }
        else {
            videoArchive = findByLocation((String) dialog.getExistingNamesComboBox().getSelectedItem());
        }

        // Load the videoFrames in a transaction
        VideoArchiveDAO dao = dialog.getToolBelt().getAnnotationDAOFactory().newVideoArchiveDAO();
        dao.startTransaction();
        videoArchive = dao.find(videoArchive); // bring into transaction
        @SuppressWarnings("unused")
        Collection<VideoFrame> videoFrames = videoArchive.getVideoFrames();
        for (VideoFrame videoFrame : videoFrames) {
            videoFrame.getCameraData().getImageReference();
        }
        dao.endTransaction();

        // Configure the ImageCaptureService and VideoControlServices
        String name = videoArchive.getName(); // For movies the name should be the URL
        TimeSource timeSource = (TimeSource) dialog.getTimeSourceComboBox().getSelectedItem();
        try {
            QTVideoControlServiceImpl videoControlService = new QTVideoControlServiceImpl();
            videoControlService.connect(name, timeSource);
            Lookup.getImageCaptureServiceDispatcher().setValueObject(videoControlService);
            Lookup.getVideoControlServiceDispatcher().setValueObject(videoControlService);
        }
        catch (Exception e) {
            Lookup.getImageCaptureServiceDispatcher().setValueObject(new FakeImageCaptureServiceImpl());
            Lookup.getVideoControlServiceDispatcher().setValueObject(null);
            EventBus.publish(Lookup.TOPIC_WARNING, "Failed to open " + videoArchive.getName() +
                    " with QuickTime.\n Check to make sure the file exists.");
        }

        return videoArchive;

    }

    protected VideoArchive findByLocation(String location) {
        VideoArchiveDAO dao = dialog.getToolBelt().getAnnotationDAOFactory().newVideoArchiveDAO();
        dao.startTransaction();
        VideoArchive videoArchive = dao.findByName(location);
        dao.endTransaction();
        return videoArchive;
    }

    protected VideoArchive createVideoArchive() {
        String location = dialog.getUrlTextField().getText();
        int sequenceNumber = Integer.parseInt(dialog.getSequenceNumberTextField().getText());
        String platform = (String) dialog.getCameraPlatformComboBox().getSelectedItem();
        VideoArchiveDAO dao = dialog.getToolBelt().getAnnotationDAOFactory().newVideoArchiveDAO();
        dao.startTransaction();
        VideoArchive videoArchive = dao.findOrCreateByParameters(platform, sequenceNumber, location);
        dao.endTransaction();
        return videoArchive;
    }

}
