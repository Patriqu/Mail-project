package mailclientapp;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.WindowConstants;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author ethenq
 */
public class SettingsDialog extends javax.swing.JDialog {
    private static final long serialVersionUID = 1L;

    private String defaultServerPort, defaultServerIP, windowStyle,
            textSize, mailsDirectory;
    
    
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        System.out.println("SettingsDialog initialized");
        initComponents();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int)screenSize.getWidth();
        int h = (int)screenSize.getHeight();
        
        Dimension windowSize = getSize();
        int x = (int)windowSize.getWidth();
        int y = (int)windowSize.getHeight();
        
        setLocation((w/2)-(x/2), (h/2)-(y/2));
        toFront();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ustawienia programu");
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
            }
        });
        
        pack();
        setResizable(false);
        
        try {
            loadSettingsFromFile();
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(SettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadSettingsFromFile() throws JDOMException, IOException
    {
        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File("database/config.xml");
        
        Document document = saxBuilder.build(file);
        Element rootNode = document.getRootElement();
        Element childNode;

        childNode = rootNode.getChild("default_server_port");
        defaultServerPort = childNode.getValue();

        childNode = (Element) rootNode.getChild("default_server_ip");
        defaultServerIP = childNode.getValue();

        childNode = (Element) rootNode.getChild("window_style");
        windowStyle = childNode.getValue();

        childNode = (Element) rootNode.getChild("text_size");
        textSize = childNode.getValue();

        childNode = (Element) rootNode.getChild("mails_directory");
        mailsDirectory = childNode.getValue();
        
        jTextFieldDefaultServerPort.setText(defaultServerPort);
        jTextFieldDefaultServerIP.setText(defaultServerIP);
        jComboBoxWindowStyle.setSelectedItem(windowStyle);
        jTextFieldTextSize.setText(textSize);
        jTextFieldMailsDirectory.setText(mailsDirectory);
    }
    
    private void saveSettings()
    {
        SAXBuilder saxBuilder = new SAXBuilder();
        File file = new File("database/config.xml");
        
        Document document;
        try {
            document = saxBuilder.build(file);
            Element rootNode = document.getRootElement();
            
            String default_server_port = jTextFieldDefaultServerPort.getText();
            String default_server_ip = jTextFieldDefaultServerIP.getText();
            String window_style = jComboBoxWindowStyle.getSelectedItem().toString();
            String text_size = jTextFieldTextSize.getText();
            String mails_directory = jTextFieldMailsDirectory.getText();
            
            rootNode.getChild("default_server_port").setText(default_server_port);
            rootNode.getChild("default_server_ip").setText(default_server_ip);
            rootNode.getChild("window_style").setText(window_style);
            rootNode.getChild("text_size").setText(text_size);
            rootNode.getChild("mails_directory").setText(mails_directory);
            
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            
            try (FileOutputStream fos = new FileOutputStream("database/config.xml")) {
                xmlOutput.output(document, fos);
                fos.flush();
            }
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(SettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        setVisible(false);
        dispose();
    }
    
    private void initComponents() {
        jMenuItem = new javax.swing.JMenuItem();
        jPanelMain = new javax.swing.JPanel();
        jLabelDefaultServerPort = new javax.swing.JLabel();
        jLabelDefaultServerIP = new javax.swing.JLabel();
        jLabelWindowLayout = new javax.swing.JLabel();
        jLabelFontSize = new javax.swing.JLabel();
        jLabelMailsFolder = new javax.swing.JLabel();
        jSeparator = new javax.swing.JSeparator();
        jButtonSaveSettings = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jTextFieldMailsDirectory = new javax.swing.JTextField();
        jTextFieldDefaultServerPort = new javax.swing.JTextField();
        jTextFieldDefaultServerIP = new javax.swing.JTextField();
        jTextFieldTextSize = new javax.swing.JTextField();
        jComboBoxWindowStyle = new javax.swing.JComboBox();

        jMenuItem.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelDefaultServerPort.setText("Domyślny port serwera:");
        jLabelDefaultServerIP.setText("Domyślny adres IP serwera:");
        jLabelWindowLayout.setText("Wygląd okien:");
        jLabelFontSize.setText("Wielkość czcionki tekstowej:");
        jLabelMailsFolder.setText("Folder przechowywanych listów:");

        jButtonSaveSettings.setText("Zapisz ustawienia");
        jButtonSaveSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveSettingsActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Anuluj");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jComboBoxWindowStyle.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Domyślny", "Metal" }));

        javax.swing.GroupLayout gl_jPanelMain = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(gl_jPanelMain);
        gl_jPanelMain.setHorizontalGroup(
            gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanelMain.createSequentialGroup()
                .addGroup(gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gl_jPanelMain.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jButtonSaveSettings)
                        .addGap(42, 42, 42)
                        .addComponent(jButtonCancel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(gl_jPanelMain.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator)
                            .addGroup(gl_jPanelMain.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabelMailsFolder)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(gl_jPanelMain.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDefaultServerPort)
                            .addComponent(jLabelFontSize)
                            .addComponent(jLabelWindowLayout)
                            .addComponent(jLabelDefaultServerIP))
                        .addGroup(gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(gl_jPanelMain.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextFieldTextSize, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxWindowStyle, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldDefaultServerIP)))
                            .addGroup(gl_jPanelMain.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldDefaultServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addGroup(gl_jPanelMain.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextFieldMailsDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        gl_jPanelMain.setVerticalGroup(
            gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanelMain.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDefaultServerPort)
                    .addComponent(jTextFieldDefaultServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDefaultServerIP)
                    .addComponent(jTextFieldDefaultServerIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelWindowLayout)
                    .addComponent(jComboBoxWindowStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFontSize)
                    .addComponent(jTextFieldTextSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelMailsFolder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldMailsDirectory)
                .addGap(18, 18, 18)
                .addGroup(gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSaveSettings)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    private void jButtonSaveSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveSettingsActionPerformed
        saveSettings();
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        dispose();
    }


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSaveSettings;
    private javax.swing.JComboBox jComboBoxWindowStyle;
    private javax.swing.JLabel jLabelDefaultServerPort;
    private javax.swing.JLabel jLabelDefaultServerIP;
    private javax.swing.JLabel jLabelWindowLayout;
    private javax.swing.JLabel jLabelFontSize;
    private javax.swing.JLabel jLabelMailsFolder;
    private javax.swing.JMenuItem jMenuItem;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JSeparator jSeparator;
    private javax.swing.JTextField jTextFieldDefaultServerIP;
    private javax.swing.JTextField jTextFieldDefaultServerPort;
    private javax.swing.JTextField jTextFieldMailsDirectory;
    private javax.swing.JTextField jTextFieldTextSize;
}
