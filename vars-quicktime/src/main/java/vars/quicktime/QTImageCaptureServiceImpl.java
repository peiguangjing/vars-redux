/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vars.quicktime;

import java.awt.Frame;
import java.awt.Image;
import java.lang.reflect.Method;
import javax.swing.JOptionPane;
import org.mbari.framegrab.FakeGrabber;
import org.mbari.framegrab.GrabberException;
import org.mbari.framegrab.IGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vars.shared.ui.GlobalLookup;

import vars.shared.ui.video.ImageCaptureService;
import vars.shared.ui.video.ImageCaptureException;

/**
 * Adapter class to map QTX4J IGrabber to a VARS-REDUX ImageGrabber.
 * 
 * @author brian
 */
public class QTImageCaptureServiceImpl implements ImageCaptureService {

    private final IGrabber grabber;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public QTImageCaptureServiceImpl() {
        IGrabber myGrabber = null;
        try {
            myGrabber = (IGrabber) Class.forName("org.mbari.framegrab.VideoChannelGrabber").newInstance();
        }
        catch (Exception e) {
            log.warn("Failed to initialize QuickTime for Java components.", e);
            myGrabber = new FakeGrabber();
        }
        grabber = myGrabber;
    }


    public Image capture(String timecode) throws ImageCaptureException {
        Image image = null;
        try {
            image = grabber.grab();
        }
        catch (GrabberException e) {
            throw new ImageCaptureException(e);
        }
        return image;
    }

    public void dispose() {
        grabber.dispose();
    }

    public void showSettingsDialog() {
            Class clazz = grabber.getClass();
            try {
                // Use reflection since not all IGrabbers will have a
                // showSettingsDialog method.
                Method method = clazz.getMethod("showSettingsDialog", new Class[0]);
                method.invoke(grabber, new Object[0]);
            }
            catch (Throwable ex) {
                // Do nothing
                log.info("Unable to show dialog for " + clazz, ex);
                Frame frame = (Frame) GlobalLookup.getSelectedFrameDispatcher().getValueObject();
                JOptionPane.showMessageDialog(frame, "No settings are available", "VARS - Video Settings",
                        JOptionPane.INFORMATION_MESSAGE);
            }

    }



}