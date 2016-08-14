package mailclientapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author ethenq
 */
public class LoginDialog extends javax.swing.JDialog implements Runnable {

    private static final long serialVersionUID = 1L;
    
    // A return status code - returned RET_CANCEL if Cancel button has been pressed
    public static final String RETURN_CANCEL = "0";
    public static String RETURN_OK = "";
    
    private String returnStatus = "RET_CANCEL";
    
    
    public LoginDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int)screenSize.getWidth();
        int h = (int)screenSize.getHeight();
        
        Dimension windowSize = getSize();
        int x = (int)windowSize.getWidth();
        int y = (int)windowSize.getHeight();
        
        setLocation((w/2)-(x/2), (h/2)-(y/2));
        toFront();
        setTitle("Logowanie");

        // Close the dialog when Esc key is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RETURN_CANCEL);
            }
        });
    }

    public void setWarning(String warn) {
        jLabelWarning.setText(warn);
    }

    private void closeDialog(java.awt.event.WindowEvent evt) {
        returnStatus = "RET_CANCEL";
        doClose(RETURN_CANCEL);
    }
    
    private void setReturnValue(String retStatus) {
        returnStatus = retStatus;
        setVisible(false);
    }
    
    public void doClose(String retStatus)
    {
        setVisible(false);
    }
    
    public void showLoginDialog()
    {
        setVisible(true);
    }
    
    public void resetReturnStatus()
    {
        returnStatus = "";
    }
            
    public int getServerPort()
    {
        int p = Integer.parseInt(jTextFieldServerPort.getText());
        
        return p; 
    }
    
    public String getServerIP()
    {
        String ip = jTextFieldServerIP.getText();
        
        return ip;
    }
    
    public String getReturnStatus() {
        return returnStatus;
    }
    
    private void initComponents() {
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabelLogin = new javax.swing.JLabel();
        jLabelInfo = new javax.swing.JLabel();
        jLabelPassword = new javax.swing.JLabel();
        jTextFieldLogin = new javax.swing.JTextField();
        jLabelWarning = new javax.swing.JLabel();
        jLabelServerPort = new javax.swing.JLabel();
        jTextFieldServerPort = new javax.swing.JTextField();
        jLabelServerIP = new javax.swing.JLabel();
        jTextFieldServerIP = new javax.swing.JTextField();
        jTextFieldPassword = new javax.swing.JPasswordField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jButtonOk.setText("Zaloguj");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Anuluj");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jLabelLogin.setText("Login:");
        jLabelInfo.setText("Podaj login, hasło, IP serwera oraz port.");
        jLabelPassword.setText("Hasło:");
        jLabelServerPort.setText("Port serwera:");

        SAXBuilder saxBuilder = new SAXBuilder();
        File xml = new File("database/config.xml");

        String defaultPort = "";
        String defaultIP = "";

        try {
            Document document = saxBuilder.build(xml);
            Element rootNode = document.getRootElement();

            defaultPort = rootNode.getChild("default_server_port").getValue();
            defaultIP = rootNode.getChild("default_server_ip").getValue();

            System.out.println("Plik konfiguracyjny - Domyślny port serwera: " + defaultPort);
            System.out.println("Plik konfiguracyjny - Domyślne IP serwera: " + defaultIP);
        } catch (JDOMException | IOException e) {
        }
        jTextFieldServerPort.setText(defaultPort);
        jLabelServerIP.setText("IP serwera:");
        jTextFieldServerIP.setText(defaultIP);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelServerPort)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabelLogin)
                                .addComponent(jLabelPassword))
                            .addComponent(jLabelServerIP))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                            .addComponent(jTextFieldServerPort, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                            .addComponent(jTextFieldServerIP)
                            .addComponent(jTextFieldPassword)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jButtonOk, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCancel)
                        .addGap(45, 45, 45)))
                .addContainerGap(27, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelInfo)
                .addGap(74, 74, 74))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelInfo)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelLogin)
                    .addComponent(jTextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabelPassword)
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldServerIP, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelServerIP))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelServerPort))
                .addGap(18, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOk)
                    .addComponent(jButtonCancel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelWarning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );

        getRootPane().setDefaultButton(jButtonOk);
        dispose();

        pack();
    }

    
    
    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {
        String login = jTextFieldLogin.getText();
        String password = jTextFieldPassword.getText();
        
        RETURN_OK += login;
        RETURN_OK += "|";
        RETURN_OK += password;
              
        System.out.println("RETURN_OK " + RETURN_OK);
        
        setReturnValue(RETURN_OK);

        RETURN_OK = "";
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        returnStatus = "RET_CANCEL";        
        doClose(RETURN_CANCEL);
    }

    


    @Override
    public void run() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
                
                returnStatus = "EXIT";
            }
        });
        setVisible(true);
    }
            

    private javax.swing.JButton jButtonCancel;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JLabel jLabelInfo;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelServerPort;
    private javax.swing.JLabel jLabelServerIP;
    private javax.swing.JLabel jLabelWarning;
    private javax.swing.JTextField jTextFieldLogin;
    private javax.swing.JPasswordField jTextFieldPassword;
    private javax.swing.JTextField jTextFieldServerPort;
    private javax.swing.JTextField jTextFieldServerIP;
    private javax.swing.JButton jButtonOk;
}
