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

    private String default_server_port, default_server_ip, window_style,
            text_size, mails_directory;
    
    /**
     * Creates new form SettingsDialog
     * @param parent
     * @param modal
     */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        System.out.println("Jestem w konstruktorze SettingsDialog");
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
                // dispose();
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
        default_server_port = childNode.getValue();

        childNode = (Element) rootNode.getChild("default_server_ip");
        default_server_ip = childNode.getValue();

        childNode = (Element) rootNode.getChild("window_style");
        window_style = childNode.getValue();

        childNode = (Element) rootNode.getChild("text_size");
        text_size = childNode.getValue();

        childNode = (Element) rootNode.getChild("mails_directory");
        mails_directory = childNode.getValue();
        
        jTextFieldDefaultServerPort.setText(default_server_port);
        jTextFieldDefaultServerIP.setText(default_server_ip);
        jComboBoxWindowStyle.setSelectedItem(window_style);
        jTextFieldTextSize.setText(text_size);
        jTextFieldMailsDirectory.setText(mails_directory);
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
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButtonSaveSettings = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jTextFieldMailsDirectory = new javax.swing.JTextField();
        jTextFieldDefaultServerPort = new javax.swing.JTextField();
        jTextFieldDefaultServerIP = new javax.swing.JTextField();
        jTextFieldTextSize = new javax.swing.JTextField();
        jComboBoxWindowStyle = new javax.swing.JComboBox();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Domyślny port serwera:");

        jLabel2.setText("Domyślny adres IP serwera:");

        jLabel3.setText("Wygląd okien:");

        jLabel4.setText("Wielkość czcionki tekstowej:");

        jLabel5.setText("Folder przechowywanych listów:");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jButtonSaveSettings)
                        .addGap(42, 42, 42)
                        .addComponent(jButtonCancel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextFieldTextSize, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxWindowStyle, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldDefaultServerIP)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldDefaultServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextFieldMailsDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldDefaultServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldDefaultServerIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxWindowStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldTextSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldMailsDirectory)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveSettingsActionPerformed
        saveSettings();
    }//GEN-LAST:event_jButtonSaveSettingsActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSaveSettings;
    private javax.swing.JComboBox jComboBoxWindowStyle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextFieldDefaultServerIP;
    private javax.swing.JTextField jTextFieldDefaultServerPort;
    private javax.swing.JTextField jTextFieldMailsDirectory;
    private javax.swing.JTextField jTextFieldTextSize;
    // End of variables declaration//GEN-END:variables
}
