/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vars.query;

import java.util.Collection;
import java.util.Map;
import vars.ILink;

/**
 *
 * @author brian
 */
public interface IQueryDAO extends IQueryable {

    Integer getCountOfUniqueValuesByColumn(String columnName);

    Map<String, String> getMetaData();

    Collection<?> getUniqueValuesByColumn(String columnName);

    Collection<ILink> findByConceptNames(Collection<String> conceptNames);

    Collection<ILink> findAllLinkTemplates();

    /**
     *
     * @return A URL (or other identififier) for the database that's being queried.
     *      Originally this was the JDBC URL.
     */
    String getURL();

    Collection<String> findAllNamesUsedInAnnotations();

}
