/*
 * @(#)ImagePreferencesPanel.java   2010.09.21 at 11:36:55 PDT
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



package vars.annotation.ui.preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vars.UserAccount;
import vars.annotation.ui.Lookup;
import vars.shared.preferences.PreferenceUpdater;
import vars.shared.ui.video.ImageCaptureService;

import javax.swing.GroupLayout.Alignment;
import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.PreferencesFactory;

/**
 *
 *
 * @version        Enter version here..., 2010.03.17 at 11:17:31 PDT
 * @author         Brian Schlining [brian@mbari.org]
 */
public class ImagePreferencesPanel extends JPanel implements PreferenceUpdater {

    private final Logger log = LoggerFactory.getLogger(ImagePreferencesPanel.class);
    private JButton browseButton;
    private JButton btnOpenVideoSettings;
    private final ImagePreferencesPanelController controller;
    private JComboBox imageFormatComboBox;
    private JLabel imageFormatLabel;
    private JTextField imageTargetMappingTextField;
    private JTextField imageTargetTextField;
    private JLabel imageWebAccessLabel;
    private JLabel saveImagesToLabel;
    private JButton defaultsButton;
    private UserAccount userAccount;

    /**
     * Create the panel
     *
     * @param preferencesFactory
     */
    public ImagePreferencesPanel(PreferencesFactory preferencesFactory) {
        super();
        this.controller = new ImagePreferencesPanelController(this, preferencesFactory);

        try {
            initialize();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    protected JButton getBrowseButton() {
        if (browseButton == null) {
            browseButton = new JButton();
            browseButton.setText("Browse");
            browseButton.addActionListener(new BrowseAction());
        }

        return browseButton;
    }

    private JButton getBtnOpenVideoSettings() {
        if (btnOpenVideoSettings == null) {
            btnOpenVideoSettings = new JButton("Open Video Settings");
            btnOpenVideoSettings.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ImageCaptureService imageCaptureService = (ImageCaptureService) Lookup
                        .getImageCaptureServiceDispatcher().getValueObject();

                    if (imageCaptureService != null) {
                        imageCaptureService.showSettingsDialog();
                    }
                }
            });
        }

        return btnOpenVideoSettings;
    }

    protected ImagePreferencesPanelController getController() {
        return controller;
    }

    /**
     * @return
     */
    protected JComboBox getImageFormatComboBox() {
        if (imageFormatComboBox == null) {
            imageFormatComboBox = new JComboBox();
            imageFormatComboBox.setEnabled(false);
        }

        return imageFormatComboBox;
    }

    /**
     * @return
     */
    protected JLabel getImageFormatLabel() {
        if (imageFormatLabel == null) {
            imageFormatLabel = new JLabel();
            imageFormatLabel.setText("Image Format:");
        }

        return imageFormatLabel;
    }

    /**
     * @return
     */
    protected JTextField getImageTargetMappingTextField() {
        if (imageTargetMappingTextField == null) {
            imageTargetMappingTextField = new JTextField();
        }

        return imageTargetMappingTextField;
    }

    /**
     * @return
     */
    protected JTextField getImageTargetTextField() {
        if (imageTargetTextField == null) {
            imageTargetTextField = new JTextField();
        }

        return imageTargetTextField;
    }

    /**
     * @return
     */
    protected JLabel getImageWebAccessLabel() {
        if (imageWebAccessLabel == null) {
            imageWebAccessLabel = new JLabel();
            imageWebAccessLabel.setText("Image Web Access:");
        }

        return imageWebAccessLabel;
    }

    /**
     * @return
     */
    protected JLabel getSaveImagesToLabel() {
        if (saveImagesToLabel == null) {
            saveImagesToLabel = new JLabel();
            saveImagesToLabel.setText("Save Images to:");
        }

        return saveImagesToLabel;
    }

    private JButton getDefaultsButton() {
        if (defaultsButton == null) {
            defaultsButton = new JButton("Use as Defaults");
            defaultsButton.setToolTipText(
                "Click this button to use these video settings as the defaults for all users on this computer");
            defaultsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    controller.persistDefaults();
                }
            });
        }

        return defaultsButton;
    }

    /**
     * @return
     */
    public UserAccount getUserAccount() {
        return userAccount;
    }

    private void initialize() throws Exception {
            final javax.swing.GroupLayout groupLayout = new javax.swing.GroupLayout((JComponent) this);
            groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(groupLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                            .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                    .addComponent(getImageWebAccessLabel())
                                    .addComponent(getSaveImagesToLabel())
                                    .addComponent(getImageFormatLabel()))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                    .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(getImageTargetTextField(), javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(getBrowseButton()))
                                    .addComponent(getImageTargetMappingTextField(), javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                                    .addComponent(getImageFormatComboBox(), 0, 312, Short.MAX_VALUE)))
                            .addComponent(getBtnOpenVideoSettings())
                            .addComponent(getDefaultsButton()))
                        .addContainerGap())
            );
            groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(groupLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                            .addComponent(getSaveImagesToLabel())
                            .addComponent(getBrowseButton())
                            .addComponent(getImageTargetTextField(), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                            .addComponent(getImageWebAccessLabel())
                            .addComponent(getImageTargetMappingTextField(), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                            .addComponent(getImageFormatLabel())
                            .addComponent(getImageFormatComboBox(), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(getDefaultsButton())
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(getBtnOpenVideoSettings())
                        .addContainerGap(133, Short.MAX_VALUE))
            );
            setLayout(groupLayout);
    }

    /**
     */
    public void persistPreferences() {
        controller.persistPreferences();
    }

    /**
     *
     * @param userAccount
     */
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;

        // Pass it on to the controller to do the updating of UI fields
        controller.setUserAccount(userAccount);
    }

    /**
     */
    public void updatePreferences() {
        controller.persistPreferences();
    }

    /**
     * Action for browsing for a directory to save images into.
     */
    private class BrowseAction implements ActionListener {

        final JFileChooser chooser;

        /**
         * Constructs ...
         */
        public BrowseAction() {
            chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }

        /**
         *
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            int response = chooser.showOpenDialog(ImagePreferencesPanel.this);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                getImageTargetTextField().setText(file.getAbsolutePath());
            }
        }
    }
}
