package vars.knowledgebase.ui.actions;

import java.awt.Frame;
import javax.swing.JOptionPane;

import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vars.DAO;
import vars.knowledgebase.KnowledgebaseDAOFactory;
import vars.knowledgebase.LinkTemplate;
import vars.knowledgebase.ui.Lookup;

public class DeleteLinkTemplateTask {
	
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final KnowledgebaseDAOFactory knowledgebaseDAOFactory;
    
    public DeleteLinkTemplateTask(KnowledgebaseDAOFactory knowledgebaseDAOFactory) {
        this.knowledgebaseDAOFactory = knowledgebaseDAOFactory;
    }
    
    public boolean delete(LinkTemplate linkTemplate) {
        boolean okToProceed = (linkTemplate != null);

        // TODO get count of associations and linkRealizations that use this template and notify user before delete

        // TODO give option to drop existing associations and linkRealizations?
        
        /*
         * Let the user know just how much damage their about to do to the database
         */
        if (okToProceed) {
            final Frame frame = (Frame) Lookup.getApplicationFrameDispatcher().getValueObject();
            final int option = JOptionPane.showConfirmDialog(frame,
            		"Are you sure you want to delete '" + linkTemplate.stringValue() + 
            		"' ? Be aware that this will not effect existing annotations that use it.", 
                    "VARS - Delete LinkTemplate", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            okToProceed = (option == JOptionPane.YES_OPTION);
        }

        /*
         * Delete the linkTemplate
         */
        if (okToProceed) {
            try {
                DAO dao = knowledgebaseDAOFactory.newDAO();
                dao.startTransaction();
                linkTemplate = dao.merge(linkTemplate);
                linkTemplate.getConceptMetadata().removeLinkTemplate(linkTemplate);
                dao.remove(linkTemplate);
                dao.endTransaction();
            }
            catch (Exception e) {
                final String msg = "Failed to delete '" + linkTemplate.stringValue() + "'";
                log.error(msg, e);
                EventBus.publish(Lookup.TOPIC_NONFATAL_ERROR, msg);
                okToProceed = false;
            }
        }        
        
        return okToProceed;
    }

}
