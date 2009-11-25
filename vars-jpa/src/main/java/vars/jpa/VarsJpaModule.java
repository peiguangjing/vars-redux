/*
 * @(#)VarsJpaModule.java   2009.09.21 at 09:03:15 PDT
 *
 * Copyright 2009 MBARI
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package vars.jpa;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import vars.ExternalDataPersistenceService;
import vars.EXPDDataPersistenceService;
import vars.MiscDAOFactory;
import vars.MiscFactory;
import vars.annotation.AnnotationDAOFactory;
import vars.annotation.AnnotationFactory;
import vars.annotation.AnnotationPersistenceService;
import vars.annotation.AnnotationPersistenceServiceImpl;
import vars.annotation.jpa.AnnotationDAOFactoryImpl;
import vars.annotation.jpa.AnnotationFactoryImpl;
import vars.knowledgebase.KnowledgebasePersistenceService;
import vars.knowledgebase.KnowledgebaseDAOFactory;
import vars.knowledgebase.KnowledgebasePersistenceServiceImpl;
import vars.knowledgebase.KnowledgebaseFactory;
import vars.knowledgebase.jpa.KnowledgebaseDAOFactoryImpl;
import vars.knowledgebase.jpa.KnowledgebaseFactoryImpl;
import vars.query.QueryPersistenceService;
import vars.query.QueryPersistenceServiceImpl;

/**
 * Created by IntelliJ IDEA.
 * User: brian
 * Date: Aug 10, 2009
 * Time: 3:35:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class VarsJpaModule implements Module {

    private final String annotationPersistenceUnit;
    private final String knowledgebasePersistenceUnit;
    private final String miscPersistenceUnit;

    /**
     * Constructs ...
     *
     * @param annotationPersistenceUnit
     * @param knowledgebasePersistenceUnit
     * @param miscPersistenceUnit
     */
    public VarsJpaModule(String annotationPersistenceUnit, String knowledgebasePersistenceUnit,
                         String miscPersistenceUnit) {
        this.annotationPersistenceUnit = annotationPersistenceUnit;
        this.knowledgebasePersistenceUnit = knowledgebasePersistenceUnit;
        this.miscPersistenceUnit = miscPersistenceUnit;
    }

    public void configure(Binder binder) {

        /*
         * Binding an iso8601 data format
         */
        final DateFormat dateFormatISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") {{
            setTimeZone(TimeZone.getTimeZone("UTC"));
        }};
        binder.bind(DateFormat.class).toInstance(dateFormatISO);


        binder.bind(EntityManagerFactory.class).annotatedWith(Names.named("annotationPersistenceUnit")).toInstance(Persistence.createEntityManagerFactory(annotationPersistenceUnit));
        binder.bind(EntityManagerFactory.class).annotatedWith(Names.named("knowledgebasePersistenceUnit")).toInstance(Persistence.createEntityManagerFactory(knowledgebasePersistenceUnit));
        binder.bind(EntityManagerFactory.class).annotatedWith(Names.named("miscPersistenceUnit")).toInstance(Persistence.createEntityManagerFactory(miscPersistenceUnit));


        // Bind the names of the persistence units
        binder.bindConstant().annotatedWith(Names.named("annotationPersistenceUnit")).to(annotationPersistenceUnit);
        binder.bindConstant().annotatedWith(Names.named("knowledgebasePersistenceUnit")).to(
            knowledgebasePersistenceUnit);
        binder.bindConstant().annotatedWith(Names.named("miscPersistenceUnit")).to(miscPersistenceUnit);

        // Bind annotation object and DAO factories
        binder.bind(AnnotationDAOFactory.class).to(AnnotationDAOFactoryImpl.class).in(Scopes.SINGLETON);
        binder.bind(AnnotationFactory.class).to(AnnotationFactoryImpl.class);
        binder.bind(ExternalDataPersistenceService.class).to(EXPDDataPersistenceService.class);
        binder.bind(KnowledgebaseDAOFactory.class).to(KnowledgebaseDAOFactoryImpl.class).in(Scopes.SINGLETON);
        binder.bind(KnowledgebaseFactory.class).to(KnowledgebaseFactoryImpl.class);
        binder.bind(MiscDAOFactory.class).to(MiscDAOFactoryImpl.class).in(Scopes.SINGLETON);;
        binder.bind(MiscFactory.class).to(MiscFactoryImpl.class);
        binder.bind(AnnotationPersistenceService.class).to(AnnotationPersistenceServiceImpl.class).in(Scopes.SINGLETON);
        binder.bind(KnowledgebasePersistenceService.class).to(KnowledgebasePersistenceServiceImpl.class);
        binder.bind(QueryPersistenceService.class).to(QueryPersistenceServiceImpl.class);
        binder.bind(VarsUserPreferencesFactory.class).to(VarsUserPreferencesFactoryImpl.class).in(Scopes.SINGLETON);

    }
}
