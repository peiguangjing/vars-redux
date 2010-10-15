package vars.annotation.jpa;

import vars.jpa.DAO;
import vars.annotation.CameraDataDAO;
import vars.annotation.CameraData;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.google.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by IntelliJ IDEA.
 * User: brian
 * Date: Aug 7, 2009
 * Time: 3:56:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class CameraDataDAOImpl extends DAO implements CameraDataDAO {

    @Inject
    public CameraDataDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public List<CameraData> findByImageReferenceContaining(String s) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("imageReference", "%" + s + "%");
        return findByNamedQuery("CameraData.findByImageReferenceLike", params);
    }

    public List<CameraData> findByImageReferencePostfix(String postfix) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("imageReference", "%" + postfix);
        return findByNamedQuery("CameraData.findByImageReferenceLike", params);
    }

    public List<CameraData> findByImageReferencePrefix(String prefix) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("imageReference", prefix + "%");
        return findByNamedQuery("CameraData.findByImageReferenceLike", params);
    }
}
