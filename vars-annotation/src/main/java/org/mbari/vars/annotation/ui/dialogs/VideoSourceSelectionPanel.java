/*
 * VideoSourcePanel.java
 *
 * Created on March 19, 2007, 3:45 PM
 */

package org.mbari.vars.annotation.ui.dialogs;

import java.awt.BorderLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.mbari.vars.annotation.VideoSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author  brian
 */
public class VideoSourceSelectionPanel extends JPanel {
    
    private final VideoSourceSelectionPanelController controller = new VideoSourceSelectionPanelController(this);
    private IVideoSourcePanel tapePanel = new VideoSourcePanelTape();
    private IVideoSourcePanel filePanel = new VideoSourcePanelFile();
    private JPanel selectedPanel;
    
    private static final Logger log = LoggerFactory.getLogger(VideoSourceSelectionPanel.class);
    
    /** Creates new form VideoSourcePanel */
    public VideoSourceSelectionPanel() {
        initComponents();
        
        // Set up the default VideoSourcePanel
        String defaultVideoSource = controller.getProperty("video.source.default");
        VideoSource videoSource = null;
        if (defaultVideoSource == null || defaultVideoSource.equalsIgnoreCase("tape")) {
            getVideoSourceComboBox().setSelectedItem(VideoSource.VideoTape);
            ((VideoSourcePanelTape) tapePanel).getHdCheckBox().setSelected(false);
        }
        else if (defaultVideoSource.equalsIgnoreCase("hd-tape")) {
            getVideoSourceComboBox().setSelectedItem(VideoSource.VideoTape);
            ((VideoSourcePanelTape) tapePanel).getHdCheckBox().setSelected(true);
        }
        else {
            getVideoSourceComboBox().setSelectedItem(VideoSource.QuickTimeMovie);
        }
    }
    
    protected JComboBox getVideoSourceComboBox() {
        return videoSourceComboBox;
    }
    

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        videoSourceComboBox = new javax.swing.JComboBox();
        videoSourcePanel = new javax.swing.JPanel();

        jLabel1.setText("Video Source:");

        videoSourceComboBox.setModel(new DefaultComboBoxModel(VideoSource.values()));
        videoSourceComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                videoSourceComboBoxItemStateChanged(evt);
            }
        });

        videoSourcePanel.setLayout(new java.awt.BorderLayout());

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(videoSourcePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(videoSourceComboBox, 0, 290, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(videoSourceComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(videoSourcePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(168, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void videoSourceComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_videoSourceComboBoxItemStateChanged
        VideoSource videoSource = (VideoSource) videoSourceComboBox.getSelectedItem();
        setVideoSource(videoSource);
    }//GEN-LAST:event_videoSourceComboBoxItemStateChanged
    
    
    public void setVideoSource(VideoSource videoSource) {
        switch (videoSource) {
            case QuickTimeMovie:
                selectedPanel = (JPanel) filePanel;
                break;
            case VideoTape:
            default:
                selectedPanel = (JPanel) tapePanel;
        }
        videoSourcePanel.removeAll();
        videoSourcePanel.add(selectedPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    public void open() {
        IVideoSourcePanel p = (IVideoSourcePanel) selectedPanel;
        if (p.isValidVideoSource()) {
            p.open();
        }
        else {
            log.info("Tried calling open on an invalid IVideoSourcePanel. The request was ignored");
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JComboBox videoSourceComboBox;
    private javax.swing.JPanel videoSourcePanel;
    // End of variables declaration//GEN-END:variables
    
}