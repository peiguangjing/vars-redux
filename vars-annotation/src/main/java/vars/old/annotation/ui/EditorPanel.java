/*
 * @(#)EditorPanel.java   2009.12.03 at 03:39:43 PST
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



package vars.old.annotation.ui;

import java.awt.BorderLayout;

/**
 * <p>A JPanel with an ObservationTable embedded in it.</p>
 *
 * @author  <a href="http://www.mbari.org">MBARI</a>
 */
public class EditorPanel extends javax.swing.JPanel {

    private ObservationTablePanel observationTablePanel;

    /**
     * Creates new form EditorPanel
     */
    public EditorPanel() {
        this.setLayout(new BorderLayout());
        initialize();
    }

    /**
     * This method is called from within the constructor to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initialize() {

        observationTablePanel = new ObservationTablePanel();
        add(observationTablePanel, BorderLayout.CENTER);
    }
}