/*
 * @(#)OpenVideoArchiveDialog.java   2010.01.19 at 08:57:31 PST
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



package vars.annotation.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.mbari.awt.event.NonDigitConsumingKeyListener;
import org.mbari.swing.SpinningDialWaitIndicator;
import org.mbari.swing.WaitIndicator;
import org.mbari.text.IgnoreCaseToStringComparator;
import vars.annotation.VideoArchive;
import vars.annotation.VideoArchiveDAO;
import vars.annotation.ui.Lookup;
import vars.annotation.ui.PersistenceController;
import vars.annotation.ui.ToolBelt;
import vars.annotation.ui.VARSProperties;
import vars.shared.ui.dialogs.StandardDialog;

/**
 *
 *
 * @version        Enter version here..., 2010.01.19 at 03:23:56 PST
 * @author         Brian Schlining [brian@mbari.org]
 */
public class OpenVideoArchiveDialog extends StandardDialog {

    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final ItemListener rbItemListener = new SelectedRBItemListener();
    private JComboBox cameraPlatformComboBox;
    private JComboBox existingNamesConceptBox;
    private boolean loadExistingNames = true;
    private JCheckBox hdCheckBox;
    private JLabel lblCameraPlatform;
    private JLabel lblName;
    private JLabel lblSelectName;
    private JLabel lblSequenceNumber;
    private JLabel lblTapeNumber;
    private JTextField nameTextField;
    private JRadioButton openByNameRB;
    private JRadioButton openByPlatformRB;
    private JRadioButton openExistingRB;
    private JPanel panel;
    private JTextField sequenceNumberTextField;
    private JTextField tapeNumberTextField;

    private enum OpenType { BY_PARAMS, BY_NAME, EXISTING; }

    private final ToolBelt toolBelt;

    /**
     * Constructs ...
     */
    public OpenVideoArchiveDialog(final Window parent, final ToolBelt toolBelt) {
        super(parent);
        this.toolBelt = toolBelt;
        initialize();
        getRootPane().setDefaultButton(getOkayButton());
        pack();
    }

    private JComboBox getCameraPlatformComboBox() {
        if (cameraPlatformComboBox == null) {
            cameraPlatformComboBox = new JComboBox();
            cameraPlatformComboBox.setModel(new DefaultComboBoxModel(listCameraPlatforms()));
        }

        return cameraPlatformComboBox;
    }

    private JComboBox getExistingNamesConceptBox() {
        if (existingNamesConceptBox == null) {
            existingNamesConceptBox = new JComboBox();
        }

        return existingNamesConceptBox;
    }

    private JCheckBox getHdCheckBox() {
        if (hdCheckBox == null) {
            hdCheckBox = new JCheckBox("Check if High Definition");
            hdCheckBox.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        getOkayButton().doClick();
                    }
                    super.keyReleased(e);
                }
            });
        }

        return hdCheckBox;
    }

    private JLabel getLblCameraPlatform() {
        if (lblCameraPlatform == null) {
            lblCameraPlatform = new JLabel("Camera Platform:");
        }

        return lblCameraPlatform;
    }

    private JLabel getLblName() {
        if (lblName == null) {
            lblName = new JLabel("Name:");
        }

        return lblName;
    }

    private JLabel getLblSelectName() {
        if (lblSelectName == null) {
            lblSelectName = new JLabel("Select Name:");
        }

        return lblSelectName;
    }

    private JLabel getLblSequenceNumber() {
        if (lblSequenceNumber == null) {
            lblSequenceNumber = new JLabel("Sequence Number:");
        }

        return lblSequenceNumber;
    }

    private JLabel getLblTapeNumber() {
        if (lblTapeNumber == null) {
            lblTapeNumber = new JLabel("Tape Number:");
        }

        return lblTapeNumber;
    }

    private JTextField getNameTextField() {
        if (nameTextField == null) {
            nameTextField = new JTextField();
            nameTextField.setColumns(10);
        }

        return nameTextField;
    }

    private JRadioButton getOpenByNameRB() {
        if (openByNameRB == null) {
            openByNameRB = new JRadioButton("Open by Name");
            openByNameRB.setName(OpenType.BY_NAME.name());
            openByNameRB.addItemListener(rbItemListener);
            openByNameRB.setEnabled(false); // TODO have to look into implementing this. there are some gotchas
        }

        return openByNameRB;
    }

    private JRadioButton getOpenByPlatformRB() {
        if (openByPlatformRB == null) {
            openByPlatformRB = new JRadioButton("Open by Platform and Sequence Number");
            openByPlatformRB.setName(OpenType.BY_PARAMS.name());
            openByPlatformRB.addItemListener(rbItemListener);
        }

        return openByPlatformRB;
    }

    private JRadioButton getOpenExistingRB() {
        if (openExistingRB == null) {
            openExistingRB = new JRadioButton("Open Existing");
            openExistingRB.setName(OpenType.EXISTING.name());
            openExistingRB.addItemListener(rbItemListener);
            openExistingRB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (loadExistingNames) {
                        JComboBox comboBox = getExistingNamesConceptBox();
                        WaitIndicator waitIndicator = new SpinningDialWaitIndicator(comboBox);
                        List<String> names = toolBelt.getAnnotationPersistenceService().findAllVideoArchiveNames();
                        String[] van = new String[names.size()];
                        names.toArray(van);
                        comboBox.setModel(new DefaultComboBoxModel(van));
                        waitIndicator.dispose();
                        loadExistingNames = false;
                    }
                }
            });
        }

        return openExistingRB;
    }

    private JPanel getPanel() {
            if (panel == null) {
                    panel = new JPanel();
                    GroupLayout groupLayout = new GroupLayout(panel);
                    groupLayout.setHorizontalGroup(
                    	groupLayout.createParallelGroup(Alignment.LEADING)
                    		.addGroup(groupLayout.createSequentialGroup()
                    			.addContainerGap()
                    			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                    				.addComponent(getOpenByPlatformRB())
                    				.addGroup(groupLayout.createSequentialGroup()
                    					.addGap(29)
                    					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                    						.addComponent(getLblSequenceNumber())
                    						.addComponent(getLblCameraPlatform())
                    						.addComponent(getLblTapeNumber()))
                    					.addPreferredGap(ComponentPlacement.RELATED)
                    					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                    						.addComponent(getHdCheckBox())
                    						.addComponent(getCameraPlatformComboBox(), 0, 325, Short.MAX_VALUE)
                    						.addComponent(getSequenceNumberTextField(), GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    						.addComponent(getTapeNumberTextField(), GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)))
                    				.addGroup(groupLayout.createSequentialGroup()
                    					.addGap(29)
                    					.addComponent(getLblName())
                    					.addGap(83)
                    					.addComponent(getNameTextField(), GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                    				.addGroup(groupLayout.createSequentialGroup()
                    					.addComponent(getOpenByNameRB())
                    					.addPreferredGap(ComponentPlacement.RELATED, 314, Short.MAX_VALUE))
                    				.addGroup(groupLayout.createSequentialGroup()
                    					.addGap(29)
                    					.addComponent(getLblSelectName())
                    					.addGap(42)
                    					.addComponent(getExistingNamesConceptBox(), 0, 325, Short.MAX_VALUE))
                    				.addComponent(getOpenExistingRB()))
                    			.addContainerGap())
                    );
                    groupLayout.setVerticalGroup(
                    	groupLayout.createParallelGroup(Alignment.LEADING)
                    		.addGroup(groupLayout.createSequentialGroup()
                    			.addContainerGap()
                    			.addComponent(getOpenByPlatformRB())
                    			.addPreferredGap(ComponentPlacement.RELATED)
                    			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                    				.addComponent(getLblCameraPlatform())
                    				.addComponent(getCameraPlatformComboBox(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    			.addPreferredGap(ComponentPlacement.RELATED)
                    			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                    				.addComponent(getLblSequenceNumber())
                    				.addComponent(getSequenceNumberTextField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    			.addPreferredGap(ComponentPlacement.RELATED)
                    			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                    				.addComponent(getLblTapeNumber())
                    				.addComponent(getTapeNumberTextField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    			.addPreferredGap(ComponentPlacement.RELATED)
                    			.addComponent(getHdCheckBox())
                    			.addGap(4)
                    			.addComponent(getOpenByNameRB())
                    			.addPreferredGap(ComponentPlacement.RELATED)
                    			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                    				.addComponent(getLblName())
                    				.addComponent(getNameTextField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    			.addPreferredGap(ComponentPlacement.RELATED)
                    			.addComponent(getOpenExistingRB())
                    			.addPreferredGap(ComponentPlacement.RELATED)
                    			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                    				.addComponent(getLblSelectName())
                    				.addComponent(getExistingNamesConceptBox(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                    );
                    panel.setLayout(groupLayout);

                    buttonGroup.add(getOpenByNameRB());
                    buttonGroup.add(getOpenByPlatformRB());
                    buttonGroup.add(getOpenExistingRB());
                    buttonGroup.setSelected(getOpenByPlatformRB().getModel(), true);

                    getOkayButton().addActionListener(new OpenActionListener());

            }
            return panel;
    }

    private JTextField getSequenceNumberTextField() {
        if (sequenceNumberTextField == null) {
            sequenceNumberTextField = new JTextField();
            sequenceNumberTextField.setColumns(10);
            sequenceNumberTextField.addKeyListener(new NonDigitConsumingKeyListener());
        }

        return sequenceNumberTextField;
    }

    private JTextField getTapeNumberTextField() {
        if (tapeNumberTextField == null) {
            tapeNumberTextField = new JTextField();
            tapeNumberTextField.setColumns(10);
            tapeNumberTextField.addKeyListener(new NonDigitConsumingKeyListener());
            tapeNumberTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        getOkayButton().doClick();
                    }
                    super.keyReleased(e);
                }
            });
        }

        return tapeNumberTextField;
    }

    protected void initialize() {
        setPreferredSize(new Dimension(475, 400));
        getOkayButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        getOkayButton().addActionListener(new OpenActionListener());

        getCancelButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getContentPane().add(getPanel(), BorderLayout.CENTER);
    }

    private String[] listCameraPlatforms() {
        final Collection<String> cameraPlatforms = VARSProperties.getCameraPlatforms();
        String[] cp = new String[cameraPlatforms.size()];
        cameraPlatforms.toArray(cp);
        Arrays.sort(cp, new IgnoreCaseToStringComparator());
        return cp;
    }



    class OpenActionListener implements ActionListener {

        /**
         *
         * @param ev
         */
        public void actionPerformed(ActionEvent ev) {

            OpenVideoArchiveDialog.this.setVisible(false);

            Enumeration<AbstractButton> e = buttonGroup.getElements();
            OpenType openType = null;
            while (e.hasMoreElements()) {
                AbstractButton b = e.nextElement();
                if (b.isSelected()) {
                    openType = OpenType.valueOf(b.getName());
                    break;
                }
            }

            switch (openType) {
            case BY_NAME:
            {
                String name = getNameTextField().getText();
                VideoArchiveDAO dao = toolBelt.getAnnotationDAOFactory().newVideoArchiveDAO();
                dao.startTransaction();
                VideoArchive videoArchive = dao.findByName(name);
                Lookup.getVideoArchiveDispatcher().setValueObject(videoArchive);
            }
                break;
            case BY_PARAMS:
            {
                int seqNumber = Integer.parseInt(getSequenceNumberTextField().getText());
                String platform = (String) getCameraPlatformComboBox().getSelectedItem();
                int tapeNumber = Integer.parseInt(getTapeNumberTextField().getText());
                final String postfix = getHdCheckBox().isSelected() ? "HD" : null;
                String name = PersistenceController.makeVideoArchiveName(platform,
                    seqNumber, tapeNumber, postfix);
                toolBelt.getPersistenceController().openVideoArchive(platform, seqNumber, name);
            }
                break;
            case EXISTING:
            {
                String name = (String) getExistingNamesConceptBox().getSelectedItem();
                VideoArchiveDAO dao = toolBelt.getAnnotationDAOFactory().newVideoArchiveDAO();
                dao.startTransaction();
                VideoArchive videoArchive = dao.findByName(name);
                Lookup.getVideoArchiveDispatcher().setValueObject(videoArchive);
            }
                break;
            default:
            }

        }


    }

    /**
     *
     * @author brian
     *
     */
    class SelectedRBItemListener implements ItemListener {

        /**
         *
         * @param e
         */
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JRadioButton radioButton = (JRadioButton) e.getItemSelectable();
                OpenType openType = OpenType.valueOf(radioButton.getName());
                boolean cameraPlatformCB = false;
                boolean sequenceNumberTF = false;
                boolean hdChckB = false;
                boolean nameTF = false;
                boolean existingNamesCB = false;
                boolean tapeNumberTF = false;
                switch (openType) {
                case BY_NAME:
                    nameTF = true;
                    break;
                case BY_PARAMS:
                    cameraPlatformCB = true;
                    sequenceNumberTF = true;
                    tapeNumberTF = true;
                    hdChckB = true;
                    break;
                case EXISTING:
                    existingNamesCB = true;
                    break;
                default:

                // Falls through
                }

                getCameraPlatformComboBox().setEnabled(cameraPlatformCB);
                getSequenceNumberTextField().setEnabled(sequenceNumberTF);
                getHdCheckBox().setEnabled(hdChckB);
                getNameTextField().setEnabled(nameTF);
                getExistingNamesConceptBox().setEnabled(existingNamesCB);
                getTapeNumberTextField().setEnabled(tapeNumberTF);

            }

        }
    }
}