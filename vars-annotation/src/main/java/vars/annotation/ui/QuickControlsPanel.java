/*
 * @(#)QuickControlsPanel.java   2009.11.16 at 03:58:14 PST
 *
 * Copyright 2009 MBARI
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package vars.annotation.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;
import vars.annotation.CameraDirections;
import vars.annotation.FormatCodes;
import vars.annotation.Observation;
import vars.annotation.VideoArchive;
import vars.annotation.VideoFrame;
import vars.annotation.ui.actions.ChangeAnnotationModeAction;
import vars.annotation.ui.commandqueue.Command;
import vars.annotation.ui.commandqueue.CommandEvent;
import vars.annotation.ui.commandqueue.impl.ChangeCameraDirectionsCmd;

/**
 *
 * @author  <a href="http://www.mbari.org">MBARI</a>
 */
public class QuickControlsPanel extends JPanel {

    private final ChangeAnnotationModeAction action;
    private JLabel cameraLabel;
    private JComboBox cbCameraDirection;
    private javax.swing.JComboBox modeChoiceBox;
    private javax.swing.JLabel modeLabel;
    private ModeObject[] modeObjects;

    /**
     * Constructs ...
     */
    public QuickControlsPanel(ToolBelt toolBelt) {
        super();
        action = new ChangeAnnotationModeAction(toolBelt);
        initialize();
    }

    private JLabel getCameraLabel() {
        if (cameraLabel == null) {
            cameraLabel = new JLabel();
            cameraLabel.setText(" Camera Direction: ");
        }

        return cameraLabel;
    }

    private JComboBox getCbCameraDirection() {
        if (cbCameraDirection == null) {
            cbCameraDirection = new CameraDirectionComboBox();

            // Forward selections to CameraDirectionDispatcher
            cbCameraDirection.addItemListener(new ItemListener() {

                public void itemStateChanged(final ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        Collection<Observation> observations = (Collection<Observation>) Lookup.getSelectedObservationsDispatcher().getValueObject();
                        Collection<VideoFrame> videoFrames = new HashSet<VideoFrame>();
                        for (Observation observation : observations) {
                            videoFrames.add(observation.getVideoFrame());
                        }
                        if (videoFrames.size() > 0) {
                            CameraDirections cameraDirections = (CameraDirections) cbCameraDirection.getSelectedItem();
                            final String cameraDirection = cameraDirections.getDirection();
                            Command command = new ChangeCameraDirectionsCmd(cameraDirection, videoFrames);
                            CommandEvent commandEvent = new CommandEvent(command);
                            EventBus.publish(commandEvent);
                        }
                        Lookup.getCameraDirectionDispatcher().setValueObject(cbCameraDirection.getSelectedItem());
                    }
                }

            });
            cbCameraDirection.setSelectedItem(CameraDirections.DESCEND);
            Lookup.getCameraDirectionDispatcher().setValueObject(cbCameraDirection.getSelectedItem());
        }

        return cbCameraDirection;
    }

    private javax.swing.JComboBox getModeChoiceBox() {
        if (modeChoiceBox == null) {
            modeChoiceBox = new ModeComboBox();
        }

        return modeChoiceBox;
    }

    private javax.swing.JLabel getModeLabel() {
        if (modeLabel == null) {
            modeLabel = new javax.swing.JLabel();
            modeLabel.setText(" Annotation Mode: ");
        }

        return modeLabel;
    }

    /**
     * @return
     */
    public ModeObject[] getModeObjects() {
        if (modeObjects == null) {
            modeObjects = new ModeObject[] { new ModeObject(FormatCodes.OUTLINE.getCode()),
                                             new ModeObject(FormatCodes.DETAILED.getCode()) };
        }

        return modeObjects;
    }

    private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(getModeLabel(), null);
        this.add(getModeChoiceBox(), null);
        this.add(getCameraLabel(), null);
        this.add(getCbCameraDirection(), null);
        getModeChoiceBox();
    }

    class ModeComboBox extends JComboBox implements PropertyChangeListener {

        ModeComboBox() {
            super();
            initialize();
        }

        void initialize() {
            setEditable(false);
            setComponentOrientation(java.awt.ComponentOrientation.UNKNOWN);
            setMaximumRowCount(5);
            setName("Annotation Mode");
            final ModeObject[] mos = getModeObjects();
            for (int i = 0; i < mos.length; i++) {
                addItem(mos[i]);
            }

            setPreferredSize(new java.awt.Dimension(120, 25));
            addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    final Object selectedItem = getSelectedItem();
                    if (!(selectedItem instanceof ModeObject)) {
                        return;
                    }

                    final ModeObject mode = (ModeObject) modeChoiceBox.getSelectedItem();
                    action.setFormatCode(mode.code);
                    action.doAction();
                }

            });
            Lookup.getVideoArchiveDispatcher().addPropertyChangeListener(this);
        }

        /**
         *
         * @param evt
         */
        public void propertyChange(final PropertyChangeEvent evt) {
            final VideoArchive va = (VideoArchive) evt.getNewValue();
            if (va != null) {
                final char formatCode = va.getVideoArchiveSet().getFormatCode();
                final ModeObject[] modes = getModeObjects();
                for (int i = 0; i < modes.length; i++) {
                    if (modes[i].code == formatCode) {
                        final JComboBox cb = getModeChoiceBox();
                        final ModeObject mo = (ModeObject) cb.getSelectedItem();
                        if ((mo != null) && !mo.equals(modes[i])) {
                            cb.setSelectedItem(modes[i]);
                        }

                        return;
                    }
                }
            }
        }
    }


    class ModeObject {

        private final char code;
        private final String codeString;

        ModeObject(final char c) {
            code = c;
            codeString = FormatCodes.getDescriptiveName(c + "");
        }


        public boolean equals(final Object obj) {
            boolean ok = false;
            if ((obj == null) || !obj.getClass().equals(getClass())) {
                ok = false;
            }
            else if (obj == this) {
                ok = true;
            }
            else {
                final ModeObject that = (ModeObject) obj;
                if (this.code == that.code) {
                    ok = true;
                }
            }

            return ok;
        }

        public String toString() {
            return codeString;
        }
    }
}
