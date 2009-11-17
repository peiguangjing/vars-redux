/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vars.annotation;

import vars.knowledgebase.Concept;
import vars.knowledgebase.ConceptDAO;

/**
 * DAO used by the Annotation application for special operations
 * 
 * @author brian
 */
public interface SpecialAnnotationDAO {

    boolean doesConceptNameExist(String conceptname);
    
    
    /**
     * Provides a quick lookup of a concept by name. This is an optimized
     * routine that only returns concepts you intend to read. Modifiying
     * concepts in the annotation ui could cause Persistence Exceptions since
     * no effort is being made to hang on to the reference of modified concepts
     * 
     * @param name
     * @return
     */
    Concept findConceptByName(String name);
    
    Concept findRootConcept();
    
    /**
     * Retrieve the underlying {@link ConceptDAO} used.
     * @return
     */
    ConceptDAO getConceptDAO();

}