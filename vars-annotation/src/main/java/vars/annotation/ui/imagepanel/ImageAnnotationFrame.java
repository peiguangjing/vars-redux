/*
 * @(#)ImageAnnotationFrame.java   2010.03.19 at 11:40:26 PDT
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



package vars.annotation.ui.imagepanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import org.jdesktop.jxlayer.JXLayer;
import org.mbari.swing.JImageCanvas2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vars.annotation.VideoFrame;
import vars.annotation.ui.ToolBelt;
import vars.knowledgebase.Concept;
import vars.shared.ui.AllConceptNamesComboBox;
import vars.shared.ui.ConceptNameComboBox;

/**
 *
 * @author brian
 */
public class ImageAnnotationFrame extends JFrame {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private JImageCanvas2 imageCanvas = new JImageCanvas2();
    private ConceptNameComboBox comboBox;
    private BufferedImage image;
    private URL imageUrl;
    private JXLayer<JImageCanvas2> layer;
    private final AnnotationLayerUI layerUI;
    private JToolBar toolBar;

    /**
     * Create the frame
     *
     * @param toolBelt
     */
    public ImageAnnotationFrame(final ToolBelt toolBelt) {
        super();
        layerUI = new AnnotationLayerUI<JImageCanvas2>(toolBelt);
        comboBox = new AllConceptNamesComboBox(toolBelt.getQueryPersistenceService());

        /*
         * When combo box changes, change the default concept used for point and
         * click annotations
         */
        comboBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Concept concept = toolBelt.getAnnotationPersistenceService().findConceptByName(
                        (String) comboBox.getSelectedItem());
                    layerUI.setConcept(concept);
                }
            }

        });
        initialize();
    }

    /**
     * The image is stored internally as a {@code BufferedImage}. This
     * call returns the underlying image
     * @return The image fetched in
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @return
     */
    public JXLayer<JImageCanvas2> getLayer() {
        if (layer == null) {
            layer = new JXLayer<JImageCanvas2>(imageCanvas);
            layer.setUI(layerUI);
        }

        return layer;
    }

    /**
     * @return
     */
    public JToolBar getToolBar() {
        if (toolBar == null) {
            toolBar = new JToolBar();
            toolBar.add(comboBox);
        }

        return toolBar;
    }

    /**
     * @return
     */
    public VideoFrame getVideoFrame() {
        return layerUI.getVideoFrame();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        add(getLayer(), BorderLayout.CENTER);
        add(getToolBar(), BorderLayout.SOUTH);
        setPreferredSize(new Dimension(640, 480));
        setImageUrl(null);
    }

    /**
     * Sets the URL of the image to display. The images is fetched from the
     * location specified. <b>IMPORTANT!! This method avoids the cacheing that
     * swing and/or {@code Toolkit} normally uses. So your image will not be
     * cached in memory</b>
     *
     * @param imageUrl the imageUrl to set
     */
    private void setImageUrl(final URL imageUrl) {
        URL oldUrl = this.imageUrl;
        this.imageUrl = imageUrl;
        log.debug("setImageUrl( " + imageUrl + " )");
        String title = (imageUrl == null) ? "" : imageUrl.toExternalForm();
        setTitle(title);
        if (imageUrl == null || !imageUrl.equals(oldUrl)) {
            imageCanvas.setUrl(imageUrl);
        }

    }

    /**
     *
     * @param videoFrame
     */
    public void setVideoFrame(VideoFrame videoFrame) {
        layerUI.setVideoFrame(videoFrame);
        URL url = null;
        try {
            url = new URL(videoFrame.getCameraData().getImageReference());

        }
        catch (Exception e) {

            // Do nothing
        }

        if (url == null || !url.equals(imageUrl)) {
            setImageUrl(url);
        }
        repaint();
    }

}
