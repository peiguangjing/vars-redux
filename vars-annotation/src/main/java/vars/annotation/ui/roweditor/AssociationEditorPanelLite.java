/*
 * @(#)AssociationEditorPanelLite.java   2009.12.15 at 02:11:16 PST
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



package vars.annotation.ui.roweditor;

import foxtrot.Task;
import foxtrot.Worker;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.bushe.swing.event.EventBus;
import org.mbari.swing.JFancyButton;
import org.mbari.swing.LabeledSpinningDialWaitIndicator;
import org.mbari.swing.SearchableComboBoxModel;
import org.mbari.swing.WaitIndicator;
import vars.ILink;
import vars.LinkBean;
import vars.LinkComparator;
import vars.annotation.Association;
import vars.annotation.Observation;
import vars.annotation.ui.Lookup;
import vars.annotation.ui.ToolBelt;
import vars.knowledgebase.Concept;
import vars.knowledgebase.ConceptName;
import vars.knowledgebase.ConceptNameTypes;
import vars.knowledgebase.SimpleConceptNameBean;
import vars.shared.ui.HierachicalConceptNameComboBox;
import vars.shared.ui.LinkListCellRenderer;

/**
 *
 *
 * @version        Enter version here..., 2009.12.15 at 02:11:16 PST
 * @author         Brian Schlining [brian@mbari.org]
 */
public class AssociationEditorPanelLite extends JPanel {

    private JButton cancelButton;
    private final AssociationEditorPanelLiteController controller;
    private JLabel lblLinkName;
    private JLabel lblSearchFor;
    private JLabel lblToConcept;
    private JLabel lblValue;
    private JTextField linkNameTextField;
    private JTextField linkValueTextField;
    private JButton okButton;
    private JComboBox linksComboBox;
    private JTextField searchTextField;
    private HierachicalConceptNameComboBox toConceptComboBox;
    private final ILink nil = new LinkBean(ILink.VALUE_NIL, ILink.VALUE_NIL, ILink.VALUE_NIL);

    /**
     * Constructs ...
     *
     * @param toolBelt
     */
    public AssociationEditorPanelLite(ToolBelt toolBelt) {
        controller = new AssociationEditorPanelLiteController(toolBelt, this);
        initialize();
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JFancyButton();
            cancelButton.setPreferredSize(new Dimension(30, 23));
            cancelButton.setToolTipText("Cancel");
            cancelButton.setIcon(new ImageIcon(getClass().getResource("/images/vars/annotation/stop.png")));
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resetDisplay();
                }
            });
        }

        return cancelButton;
    }

    private JLabel getLblLinkName() {
        if (lblLinkName == null) {
            lblLinkName = new JLabel("Link");
        }

        return lblLinkName;
    }

    private JLabel getLblSearchFor() {
        if (lblSearchFor == null) {
            lblSearchFor = new JLabel("Search for: ");
        }

        return lblSearchFor;
    }

    private JLabel getLblToConcept() {
        if (lblToConcept == null) {
            lblToConcept = new JLabel("To");
        }

        return lblToConcept;
    }

    private JLabel getLblValue() {
        if (lblValue == null) {
            lblValue = new JLabel("Value");
        }

        return lblValue;
    }

    protected JTextField getLinkNameTextField() {
        if (linkNameTextField == null) {
            linkNameTextField = new JTextField();
            linkNameTextField.setColumns(10);
        }

        return linkNameTextField;
    }

    protected JTextField getLinkValueTextField() {
        if (linkValueTextField == null) {
            linkValueTextField = new JTextField();
            linkValueTextField.setColumns(10);
        }

        return linkValueTextField;
    }

    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JFancyButton();
            okButton.setPreferredSize(new Dimension(30, 23));
            okButton.setIcon(new ImageIcon(getClass().getResource("/images/vars/annotation/add.png")));
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ILink link = new LinkBean(getLinkNameTextField().getText(),
                            (String) getToConceptComboBox().getSelectedItem(),
                            getLinkValueTextField().getText());
                    controller.doOkay(link);
                }
            });
        }

        return okButton;
    }

    protected JComboBox getLinksComboBox() {
        if (linksComboBox == null) {
            linksComboBox = new JComboBox();
            linksComboBox.setRenderer(new LinkListCellRenderer());
            linksComboBox.setModel(new SearchableComboBoxModel(new LinkComparator()));
            linksComboBox.setToolTipText("Links in Knowledgebase");
            linksComboBox.addItemListener(new ItemListener() {

                public void itemStateChanged(final ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        setLink((ILink) e.getItem());
                    }
                }

            });
        }

        return linksComboBox;
    }

    private JTextField getSearchTextField() {
        if (searchTextField == null) {
            searchTextField = new JTextField();
            searchTextField.setColumns(10);
        }

        return searchTextField;
    }

    private HierachicalConceptNameComboBox getToConceptComboBox() {
        if (toConceptComboBox == null) {
            toConceptComboBox = new HierachicalConceptNameComboBox(controller.getToolBelt().getAnnotationPersistenceService());
        }

        return toConceptComboBox;
    }


    private void initialize() {
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(getLinksComboBox(), 0, 438, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(getLblSearchFor())
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(getSearchTextField(), GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(getLblLinkName())
                                                        .addComponent(getLblValue()))
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(getLinkNameTextField(), GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addComponent(getLblToConcept())
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addComponent(getToConceptComboBox(), 0, 205, Short.MAX_VALUE))
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(getLinkValueTextField(), GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addComponent(getCancelButton())
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addComponent(getOkButton())))))
                                .addGap(4))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(getLblSearchFor())
                                        .addComponent(getSearchTextField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(getLinksComboBox(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(getLblLinkName())
                                                .addComponent(getLinkNameTextField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(getLblToConcept())
                                                .addComponent(getToConceptComboBox(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(getLblValue())
                                        .addComponent(getLinkValueTextField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(getCancelButton())
                                        .addComponent(getOkButton()))
                                .addContainerGap(165, Short.MAX_VALUE))
        );
        setLayout(groupLayout);
    }

    private void resetDisplay() {
        getSearchTextField().setText("");
        setLink(nil);
    }

    public void setTarget(Observation observation , Association association) {
        controller.setTarget(observation, association);
        ILink defaultLink = nil;

        /*
         * If an association is set we're editing it. If not we're adding it.
         *
         * Here we figure out what the intial link and conceptName should be.
         */
        if (observation != null) {

            if (association != null) {
                defaultLink = association;
                getOkButton().setToolTipText("Accept Edits");
            }
            else {
                getOkButton().setToolTipText("Add Association");
            }
        }

        /*
         * Set the selected link in the combobox. It should be the one that
         * matches the association or 'nil' if no association was provided.
         */
        JComboBox comboBox = getLinksComboBox();
        SearchableComboBoxModel model = (SearchableComboBoxModel) comboBox.getModel();
        if (!model.contains(defaultLink)) {
            model.addElement(association);
        }
        comboBox.setSelectedItem(model);
        setLink(defaultLink);


    }

    private void setLink(final ILink link) {
        WaitIndicator waitIndicator = new LabeledSpinningDialWaitIndicator(this, "Loading data ...");
        getLinkNameTextField().setText(link.getLinkName());
        getLinkValueTextField().setText(link.getLinkValue());

        if (link.getToConcept().equals(ILink.VALUE_NIL) || link.getToConcept().equals(ILink.VALUE_SELF)) {
            ConceptName conceptName = new SimpleConceptNameBean(link.getToConcept(), ConceptNameTypes.COMMON.getName());
            getToConceptComboBox().addItem(conceptName);
        }
        else {
            // Retrieve the child concepts and add to gui
            try {
                final Concept c = (Concept) Worker.post(new Task() {
                    @Override
                    public Object run() throws Exception {
                        return controller.getToolBelt().getAnnotationPersistenceService().findConceptByName(link.getToConcept());
                    }
                });
                getToConceptComboBox().setConcept(c);

            }
            catch (final Exception e) {
                EventBus.publish(Lookup.TOPIC_NONFATAL_ERROR, e);
                ConceptName conceptName = new SimpleConceptNameBean(link.getToConcept(), ConceptNameTypes.COMMON.getName());
                getToConceptComboBox().addItem(conceptName);
            }
        }
        waitIndicator.dispose();
    }
}
