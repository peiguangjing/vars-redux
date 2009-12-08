/*
 * Copyright 2005 MBARI
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/*
Created on Dec 4, 2003 @author achase
 */
package vars.annotation.ui.dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mbari.vcr.IVCR;
import org.mbari.vcr.ui.VCRSelectionPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vars.UserAccount;
import vars.annotation.ui.ToolBelt;
import vars.old.annotation.ui.VideoService;
import vars.annotation.ui.Lookup;

/**
 * <p><!--Insert summary here--></p>
 *
 * @author  <a href="http://www.mbari.org">MBARI</a>
 * @version  $Id: ConnectionDialog.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class ConnectionDialog extends OkayCancelDialog {

    private final Logger log = LoggerFactory.getLogger(getClass());


    private javax.swing.JPanel jContentPane = null;


    private javax.swing.JPanel jPanel = null;


    private UserConnectPanel userConnectPanel = null;


    private VCRSelectionPanel vcrConnectionPanel = null;


    public ConnectionDialog(ToolBelt toolBelt) {
        super((Frame) Lookup.getApplicationFrameDispatcher().getValueObject(), true);
        userConnectPanel = new UserConnectPanel(toolBelt);
        initialize();
    }

    /**
     * shortcut method for this class.... use this!
     *
     * @return  The UserAccount that we've connected with.
     */
    public UserAccount doLogin() {
        this.setSize(new Dimension(400, 300));

        // Center the popup window
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setVisible(true);
        final UserAccount activeUser = getUserConnectPanel().getActiveUser();

        return activeUser;
    }


    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getJPanel(), java.awt.BorderLayout.CENTER);
        }

        return jContentPane;
    }


    private javax.swing.JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new javax.swing.JPanel();
            final java.awt.GridLayout layGridLayout = new java.awt.GridLayout();
            layGridLayout.setRows(2);
            jPanel.setLayout(layGridLayout);
            jPanel.add(getUserConnectPanel(), null);
            jPanel.add(getVcrConnectionPanel(), null);
        }

        return jPanel;
    }

    /**
     * @return  Initialized UserConnectPanel
     */
    public UserConnectPanel getUserConnectPanel() {


        return userConnectPanel;
    }

    /**
     * @return  Initialized VCRConnectionPanel
     */
    public VCRSelectionPanel getVcrConnectionPanel() {
        if (vcrConnectionPanel == null) {
            try {
                vcrConnectionPanel = new VCRSelectionPanel();
            }
            catch (final Exception e) {
                log.error("Failed to create a VCRConnectionPanel", e);
            }
        }

        return vcrConnectionPanel;
    }

    /**
     * This method initializes this
     *
     *
     */
    private void initialize() {

        // this.setSize(398, 323);
        getContentPane().add(getJContentPane(), BorderLayout.CENTER);
        this.setTitle("VARS - Connect");
        this.pack();
        this.setSize(400, this.getHeight());
        this.setLocationRelativeTo((Frame) Lookup.getApplicationFrameDispatcher().getValueObject());
        initializeOkayButton();
    }

    /**
     * This method initializes jButton
     *
     *
     */
    private void initializeOkayButton() {
        setCloseDialogOnOkay(false);
        getOkayButton().addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent ae) {
                final boolean success = getUserConnectPanel().attemptLogin();

                // login complete, close dialog.
                if (success) {
                    final IVCR vcr = vcrConnectionPanel.getVcr();
                    VideoService videoService = (VideoService) Lookup.getVideoArchiveDispatcher().getValueObject();
                    // TODO VideoService might be null. May have to instantiate it.
                    videoService.setVCR(vcr);
                    ConnectionDialog.this.dispose();
                }
            }

        });
    }

    /**
     * Method description
     *
     *
     * @param b
     */
    public void setVisible(final boolean b) {
        if (b) {
            VideoService videoService = (VideoService) Lookup.getVideoArchiveDispatcher().getValueObject();
            getVcrConnectionPanel().setVcr(videoService.getVCR());
        }

        super.setVisible(b);
    }
}