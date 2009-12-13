package vars.annotation;

import vars.DAO;
import vars.ConceptNameValidator;
import vars.knowledgebase.Concept;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: brian
 * Date: Aug 7, 2009
 * Time: 2:33:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ObservationDAO extends DAO, ConceptNameValidator<Observation> {

    List<Observation> findAllByConceptName(String conceptName);

    /**
     * Finds all Observations in the database that use this Concept OR a child of this
     * Concept (if cascade = true)
     *
     * @param concept
     * @param cascade false = use only the concepts names to locate observations. true = Use the concepts names
     *      AND all its descendants names too.
     * @return A Set<IObservation> containing all matching observations. If none are found an empty collection 
     *      is returned
     */
    List<Observation> findAllByConcept(Concept concept, boolean cascade);

     /**
     * Retrieves all conceptnames actually used in annotations. This query
     * searches the Observation.conceptName and Association.toConcept fields
     * fields
     *
     * @return Set of Strings
     */
    List<String> findAllConceptNamesUsedInAnnotations();
    
    /**
     * Updates the fields of an observation in the database. This is used by the
     * annotation UI since we don't know when the observation was last modified
     * in the database so it's a workaround for concurrent modifications. NOTE:
     * it does not modify the parent {@link VideoFrame} or the child 
     * {@link Association}s; only the fields of the observation.
     * 
     * @param observation
     * @return
     */
    Observation updateFields(Observation observation);

}
