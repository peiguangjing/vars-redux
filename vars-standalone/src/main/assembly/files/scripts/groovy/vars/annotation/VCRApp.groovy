package vars.annotation

import vars.annotation.ui.Lookup
import vars.annotation.ui.StatusLabelForVcr
import vars.annotation.ui.video.VideoControlPanel

import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JToolBar
import javax.swing.SwingUtilities
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 *
 * @author Brian Schlining
 * @since 2013-01-18
 */
class VCRApp {

    static buildFrame() {
        def frame = new JFrame()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        def pane = frame.contentPane
        pane.layout = new BorderLayout(5, 5)

        def toolBar = new JToolBar()
        toolBar.add(new StatusLabelForVcr())
        pane.add(toolBar, BorderLayout.NORTH)

        def videoControlPanel = new VideoControlPanel()
        pane.add(videoControlPanel, BorderLayout.CENTER)

        def lowerPanel = new JPanel()
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS))
        def button = new JButton("TIME")
        def videoService = Lookup.videoControlServiceDispatcher.getValueObject()

        lowerPanel.add(button)
        def textField = new JTextField()
        lowerPanel.add(textField)
        pane.add(lowerPanel, BorderLayout.SOUTH)

        button.addActionListener(new ActionListener() {
            void actionPerformed(ActionEvent e) {
                def videoTime = videoService.requestVideoTime()
                textField.text = videoTime.toString
            }
        })
        frame.pack()
        return frame

    }

    static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                def frame = buildFrame ()
                frame.visible = true
            }
        })

    }



}
