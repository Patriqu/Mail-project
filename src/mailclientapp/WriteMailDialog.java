/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailclientapp;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author ethenq
 */
public class WriteMailDialog extends javax.swing.JDialog {

	// A return status code - returned RET_CANCEL if Cancel button has been pressed
    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    
    private String user;
    private String receiver = "";
    private String title = "";
    private String text = "";
    
    private Map<String, Object> mailData;
    
    private DefaultListModel defaultListModel = new DefaultListModel();
    private List attachments = new ArrayList<>();

    
    public WriteMailDialog(java.awt.Frame parent, boolean modal, String user) {
        super(parent, modal);
        
        this.user = user;
        mailData = new HashMap<>();
        initComponents();

        // Close the dialog when Esc key is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }
    
    public String getReceiver()
    {
        return this.receiver;
    }
   
   	public String getMsgTitle()
    {
        return this.title;
    }
   
   	public String getMsgText()
    {
        return this.text;
    }
    
    public int getReturnStatus() {
        return returnStatus;
    }

    public Map getMailData()
    {
        return mailData;
    }
    
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButtonCancel = new javax.swing.JButton();
        jPanelMsgContent = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        jTextAreaText = new javax.swing.JTextArea();
        jPanelMain = new javax.swing.JPanel();
        jPanelTitle = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextFieldTitle = new javax.swing.JTextField();
        jPanelAttachments = new javax.swing.JPanel();
        jButtonBrowse = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jListAtts = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabelTitle = new javax.swing.JLabel();
        jLabelReceiver = new javax.swing.JLabel();
        jButtonSend = new javax.swing.JButton();
        jPanelReceiver = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextFieldReceiver = new javax.swing.JTextField();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jButtonCancel.setText("Anuluj");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jTextAreaText.setColumns(20);
        jTextAreaText.setRows(5);
        jScrollPane.setViewportView(jTextAreaText);

        javax.swing.GroupLayout gl_jPanelMsgContent = new javax.swing.GroupLayout(jPanelMsgContent);
        jPanelMsgContent.setLayout(gl_jPanelMsgContent);
        gl_jPanelMsgContent.setHorizontalGroup(
            gl_jPanelMsgContent.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        gl_jPanelMsgContent.setVerticalGroup(
            gl_jPanelMsgContent.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout gl_jPanelMain = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(gl_jPanelMain);
        gl_jPanelMain.setHorizontalGroup(
            gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        gl_jPanelMain.setVerticalGroup(
            gl_jPanelMain.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(jTextFieldTitle);

        javax.swing.GroupLayout gl_jPanelTitle = new javax.swing.GroupLayout(jPanelTitle);
        jPanelTitle.setLayout(gl_jPanelTitle);
        gl_jPanelTitle.setHorizontalGroup(
            gl_jPanelTitle.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        gl_jPanelTitle.setVerticalGroup(
            gl_jPanelTitle.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanelTitle.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jButtonBrowse.setText("Przeglądaj");
        jButtonBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseActionPerformed(evt);
            }
        });

        jListAtts.setModel(defaultListModel);
        jScrollPane6.setViewportView(jListAtts);
        jScrollPane6.setWheelScrollingEnabled(true);
        jScrollPane6.getVerticalScrollBar().setUnitIncrement(64);

        jScrollPane5.setViewportView(jScrollPane6);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel1.setText("Załączniki:");

        javax.swing.GroupLayout gl_jPanelAttachments = new javax.swing.GroupLayout(jPanelAttachments);
        jPanelAttachments.setLayout(gl_jPanelAttachments);
        gl_jPanelAttachments.setHorizontalGroup(
            gl_jPanelAttachments.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gl_jPanelAttachments.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addGap(41, 41, 41)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jButtonBrowse)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        gl_jPanelAttachments.setVerticalGroup(
            gl_jPanelAttachments.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanelAttachments.createSequentialGroup()
                .addGroup(gl_jPanelAttachments.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gl_jPanelAttachments.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(gl_jPanelAttachments.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jButtonBrowse))
                    .addGroup(gl_jPanelAttachments.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelTitle.setText("Tytuł wiadomości:");

        jLabelReceiver.setText("Odbiorca wiadomości:");

        jButtonSend.setText("Wyślij");
        jButtonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jTextFieldReceiver);

        javax.swing.GroupLayout gl_jPanelReceiver = new javax.swing.GroupLayout(jPanelReceiver);
        jPanelReceiver.setLayout(gl_jPanelReceiver);
        gl_jPanelReceiver.setHorizontalGroup(
            gl_jPanelReceiver.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
        );
        gl_jPanelReceiver.setVerticalGroup(
            gl_jPanelReceiver.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelAttachments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jPanelMsgContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(186, 186, 186)
                                .addComponent(jButtonSend, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(105, 105, 105)
                                .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabelTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabelReceiver)
                                .addGap(21, 21, 21)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelReceiver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(20, 20, 20))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelReceiver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabelReceiver)))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelTitle))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelAttachments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelMsgContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSend)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        pack();
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        doClose(RET_CANCEL);
    }

    private void jButtonSendActionPerformed(java.awt.event.ActionEvent evt) {
        String receiver = jTextFieldReceiver.getText();
        String title = jTextFieldTitle.getText();
        String msgContent = jTextAreaText.getText();
        
        if ( !receiver.isEmpty() && !title.isEmpty() )
        {
            mailData.put("receiver", receiver);
            mailData.put("title", title);
            mailData.put("mail_txt", msgContent);

            System.out.println("Odbiorca wiadomości: " + receiver);
            System.out.println("Tytuł wiadomości: " + title);
            System.out.println("Tekst wiadomości: " + msgContent);

            Map<Integer, Map> tmpAttachments = new HashMap<>();

            for (int i = 0; i < defaultListModel.getSize(); i++)
            {          
                Map<String, Object> tmpMap = new HashMap<>();

                File file = new File(attachments.get(i).toString());

                double size = Math.round( ((double)file.length() / 1024.0 / 1024.0) * 10.0) / 10.0;

                tmpMap.put("att_title", file.getAbsolutePath());
                tmpMap.put("att_length", size);

                tmpAttachments.put(i, tmpMap);
            }

            mailData.put("attachments", tmpAttachments);

            doClose(RET_OK);
        }
    }

    private void jButtonBrowseActionPerformed(java.awt.event.ActionEvent evt) {
        FileBrowser fileBrowser = new FileBrowser(new javax.swing.JDialog(), true);
        File file = fileBrowser.getAttachment();
        
        if (file != null)
        {
            String absPath = file.getAbsolutePath();
            double fileSize = Math.round( ((double)file.length() / 1024.0 / 1024.0) * 10.0) / 10.0; 

            String all = absPath + "  (rozmiar: " + fileSize + " MB)";

            attachments.add(absPath);
            defaultListModel.add(0, all);
        }
    }
    
    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose(RET_CANCEL);
    }
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WriteMailDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WriteMailDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WriteMailDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WriteMailDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                WriteMailDialog dialog = new WriteMailDialog(new javax.swing.JFrame(), true, "");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }


    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JButton jButtonSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelReceiver;
    private javax.swing.JList jListAtts;
    private javax.swing.JPanel jPanelMsgContent;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelTitle;
    private javax.swing.JPanel jPanelAttachments;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelReceiver;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextAreaText;
    private javax.swing.JTextField jTextFieldReceiver;
    private javax.swing.JTextField jTextFieldTitle;

    private int returnStatus = RET_CANCEL;
}
