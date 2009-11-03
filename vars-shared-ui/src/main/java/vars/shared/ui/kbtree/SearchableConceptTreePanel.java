/*
 * @(#)SearchableConceptTreePanel.java   2009.10.26 at 09:00:09 PDT
 *
 * Copyright 2009 MBARI
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



/*
 * Created on Apr 16, 2004
 */
package vars.shared.ui.kbtree;

import com.google.inject.Inject;
import foxtrot.Job;
import foxtrot.Worker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.bushe.swing.event.EventBus;
import org.mbari.swing.LabeledSpinningDialWaitIndicator;
import org.mbari.swing.SearchableTreePanel;
import org.mbari.util.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vars.knowledgebase.Concept;
import vars.knowledgebase.ConceptDAO;
import vars.knowledgebase.ConceptName;
import vars.knowledgebase.ConceptNameDAO;
import vars.shared.ui.GlobalLookup;

/**
 * <p>A Panel that contains the JTree that represents the knowledgebase. Has
 * great search methods.</p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: SearchableConceptTreePanel.java 295 2006-07-06 23:47:31Z hohonuuli $
 */
public class SearchableConceptTreePanel extends SearchableTreePanel {

    /**
     * Hard-coded MBARI specific informaiton. The video labe requested that
     * the Knowledgebase tree be open to the marin-organism node on startup,
     * since that will be the most often used node.
     *
     * TODO 20050909 brian: THis needs to be pulled out into a configuration file.
     *
     */
    private static final String MARINE_ORGANISM = "marine organism";

    public static final Object DISPATCHER_KEY = SearchableConceptTreePanel.class;
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
         * Store previous searches so that we don't try to do database lookup on them again.
         */
    private final Collection<String> cachedGlobSearches;

    /**
         * Store previous searches so that we don't try to do database lookup on them again.
         * @uml.property  name="cachedWordSearches"
         * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
         */
    private final Collection<String> cachedWordSearches;
    private final ConceptNameDAO conceptNameDAO;
    private final ConceptDAO conceptDAO;

    /**
     * Constructor
     *
     * @param conceptDAO
     * @param conceptNameDAO
     * @param persistenceCache
     */
    @Inject
    public SearchableConceptTreePanel(ConceptDAO conceptDAO, ConceptNameDAO conceptNameDAO) {
        super();
        this.conceptDAO = conceptDAO;
        this.conceptNameDAO = conceptNameDAO;
        cachedWordSearches = new HashSet<String>();
        cachedGlobSearches = new HashSet<String>();
        Dispatcher.getDispatcher(DISPATCHER_KEY).setValueObject(this);
    }


    public void refreshTree() {
        refreshTreeAndOpenNode(conceptDAO.findRoot());
    }

    public void refreshTreeAndOpenNode(Concept concept) {
        if (log.isDebugEnabled()) {
            log.debug("Refreshing ConceptTree and opening it to '" +
                    concept.getPrimaryConceptName().getName() + "', " + concept);
        }
        cachedGlobSearches.clear();
        cachedWordSearches.clear();
        ConceptTree conceptTree = (ConceptTree) getJTree();
        conceptTree.refresh();
        conceptTree.setSelectedConcept(concept.getPrimaryConceptName().getName());
        validate();
    }

    /**
     * @param node
     *
     * @return
     */
    @Override
    public String getNodeTextToSearch(final DefaultMutableTreeNode node) {
        final Object userObject = node.getUserObject();
        final StringBuffer textToSearch = new StringBuffer();

        // Objects whos children have not been loaded yet will return a Boolean
        // as a user object. We should ignore these as much as we can.
        if (userObject instanceof TreeConcept) {
            final TreeConcept concept = (TreeConcept) node.getUserObject();
            final Concept c = concept.getConcept();

            /*
             * The text is actually a composite of all names,
             * including primary, secondary, and common
             */
            final Collection<ConceptName> conceptNames = new ArrayList<ConceptName>(c.getConceptNames());
            for (ConceptName conceptName1 : conceptNames) {
                textToSearch.append(conceptName1.getName());
                textToSearch.append(" ");
            }

        }

        return textToSearch.toString();
    }

    /**
     * This overridden method does a database lookup for searches. This is a
     * woorkaournd needed for lazy loading. This method will load the branches
     * of all matches from the database.
     *
     * @param text
     * @param useGlobSearch
     *
     * @return
     */
    @Override
    public boolean goToMatchingNode(final String text, final boolean useGlobSearch) {

        /*
         * Disable so that folks can't start multiple searches.
         */

        getSearchBtn().setEnabled(false);
        getSearchTextField().setEnabled(false);
        LabeledSpinningDialWaitIndicator waitIndicator = new LabeledSpinningDialWaitIndicator(this, "Searching for '" + text + "'");
        loadNodes(text, useGlobSearch, waitIndicator);
        boolean ok = super.goToMatchingNode(text, useGlobSearch);
        waitIndicator.dispose();
        getSearchBtn().setEnabled(true);
        getSearchTextField().setEnabled(true);
        getSearchTextField().requestFocus();

        return ok;
    }

    /**
     * Perfroms the database lookup of all matching Concepts.
     * @param text
     * @param useGlobSearch
     */
    private void loadNodes(final String text, final boolean useGlobSearch, final LabeledSpinningDialWaitIndicator waitIndicator) {
        Collection<ConceptName> matches = (Collection) Worker.post(new Job() {
            public Object run() {
                Collection<ConceptName> matches = null;
                try {

                    if (useGlobSearch) {
                        if (!cachedGlobSearches.contains(text)) {
                            matches = conceptNameDAO.findByNameContaining(text);
                            cachedGlobSearches.add(text);
                            cachedWordSearches.add(text);
                        }
                    }
                    else {
                        if (!cachedWordSearches.contains(text)) {
                            matches = conceptNameDAO.findByNameStartingWith(text);
                            cachedWordSearches.add(text);
                        }
                    }
                }
                catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Database lookup of " + text + " failed", e);
                        EventBus.publish(GlobalLookup.TOPIC_NONFATAL_ERROR, e);
                    }
                }
                return matches;
            }
        });
        

        /*
         * If we loaded the matched names from the database then we need
         * to open the Concept such that it gets cached under the root
         * concept.
         */
        if (matches != null) {
            int n = 0;
            for (Iterator i = matches.iterator(); i.hasNext(); ) {
                n++;
                final ConceptName cn = (ConceptName) i.next();
                waitIndicator.setLabel("Loading '" + cn.getName() + "'");

                /*
                 * Have to open the node in a seperate thread for the
                 * progress monitor to update. Here we're using foxtrot.
                 */
                Worker.post(new Job() {

                    public Object run() {
                        openNode(cn.getConcept());

                        return null;
                    }
                });
            }

        }
    }

    /**
     * Loads the branch of a particular concept. This method does the following
     * <ol>
     *      <li>Walks from the concept up the tree to the root concept, storing
     *      the concepts in a list. (This is very fast)</li>
     *  <li>Starts walking from the root down (using lazyExpand), searching each
     *      childnode for a matching primary name (which was stored in the first
     *      step</li>
     *  <li>If a matching primary name is found this stops otherwise
     *              it opens the next level and searches for the next mathc in the list.</li>
     *  <li></li>
     * </ol>
     * @param concept
     */
    public synchronized void openNode(final Concept concept) {
        if (log.isDebugEnabled()) {
            log.debug("Opening node containing '" + concept.getPrimaryConceptName().getName() +
                    "', " + concept);
        }

        if (concept == null) {
            return;
        }


        // Get the list of concepts up to root
        final LinkedList conceptList = new LinkedList();
        Concept c = concept;
        while (c != null) {
            conceptList.add(c);
            c = (Concept) c.getParentConcept();
        }

        // Walk the tree from root on down opening nodes as we go
        final ListIterator i = conceptList.listIterator(conceptList.size());

        // Skip the root
        i.previous();
        final JTree tree = getJTree();
        final DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        TreePath path = new TreePath(rootNode.getPath());
        tree.setSelectionPath(path);
        DefaultMutableTreeNode parentNode = rootNode;
        while (i.hasPrevious()) {
            c = (Concept) i.previous();
            final TreeConcept parentTreeConcept = (TreeConcept) parentNode.getUserObject();
            parentTreeConcept.lazyExpand(parentNode);

            // treeModel.reload(parentNode);
            final Enumeration enm = parentNode.children();
            while (enm.hasMoreElements()) {
                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) enm.nextElement();
                final TreeConcept tc = (TreeConcept) node.getUserObject();
                if (tc.getName().equals(c.getPrimaryConceptName().getName())) {
                    parentNode = node;

                    break;
                }
            }
        }

        final DefaultMutableTreeNode fParentNode = parentNode;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                treeModel.reload(fParentNode);
                tree.scrollPathToVisible(new TreePath(fParentNode));
            }
        });
    }

    /**
     * Override JTree to only accept instances of ConceptTree
     *
     * @param tree
     */
    @Override
    public void setJTree(JTree tree) {
        if (!(tree instanceof ConceptTree)) {
            throw new IllegalArgumentException("JTree must be an instanceof ConceptTree");
        }

        super.setJTree(tree);
    }

}