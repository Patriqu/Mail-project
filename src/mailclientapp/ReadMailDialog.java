/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailclientapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalBorders;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author ethenq
 */
public class ReadMailDialog extends javax.swing.JDialog {

	// A return status code - returned RET_CANCEL if Cancel button has been pressed
    public static final int RETURN_CANCEL = 0;
    public static final int RETURN_OK = 1;
    private int returnStatus = RETURN_CANCEL;
	
    private String sender = "";
    private String receiver = "";
    private String title = "";
    private String date = "";
    private String msgContent = "";
    private String user;
    private String type;
    
    private int index;
    
    private DefaultListModel defaultListModel = new DefaultListModel();
    private ListSelectionModel listSelectionModel;
    
    private java.awt.Frame parent;
    
    private int deletedMailIndex = -1;
    private String deletedMailName = "";
    
    private List attachments = new ArrayList<>();


    public ReadMailDialog(java.awt.Frame parent, boolean modal, int index, String title, String user, String type) throws FileNotFoundException {
        super(parent, modal);
        this.parent = parent;
        this.index = index;
        this.title = title;
        this.user = user;
        this.type = type;
        initComponents();
        
        readMailFromDatabase();
    }
    
    private void readMailFromDatabase() throws FileNotFoundException
    {
        setTitle(title);
        
        SAXBuilder saxBuilder = new SAXBuilder();
        
        String dirName = "";
        switch (type) {
            case "rec":
                dirName = "database/rec_mails/";
                break;
            case "sent":
                dirName = "database/sent_mails/";
                break;
            case "trash":
                dirName = "database/trash_mails/";
                break;
        }
        
        File file = new File(dirName + user + "/" + title + "/" + title + ".xml");
        
        try {  
            // converted file to document object  
            Document document = saxBuilder.build(file);  

            // get root node from xml  
            Element rootNode = document.getRootElement();

            sender = rootNode.getChild("sender").getValue();
            receiver = rootNode.getChild("receiver").getValue();
            title = rootNode.getChild("title").getValue();
            
            if (type.equals("sent")) {
            	date = rootNode.getChild("datetime").getValue();
            }
            else {
            	date = rootNode.getChild("received_date").getValue();
            } 
            
            msgContent = rootNode.getChild("mail_txt").getValue();
            
            //// add attachments to attachments list
            
            List<Element> tmpAttachments = rootNode.getChildren("attachment");          
            for (Element el : tmpAttachments)
            {
                String name = el.getChild("att_title").getValue();
                
                String path = dirName + user + "/" + title + "/" + name;
                attachments.add(path);
                
                File tmpFile = new File(path);
                double size = Math.round( ((double)tmpFile.length() / 1024.0 / 1024.0) * 10.0) / 10.0;
                
                String fullName = path + "  (rozmiar: " + size + " MB)"; 
                
                defaultListModel.add(0, fullName); 
            }

            jTextFieldSender.setText(sender);
            jTextFieldReceiver.setText(receiver);
            jTextFieldTitle.setText(title);
            jLabelDateValue.setText(date);
            jTextAreaText.setText(msgContent);
            
            System.gc();
        } catch (JDOMException | IOException e) {  
        }
    }
    
    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose(RETURN_CANCEL);
    }
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    
    public int getDeletedMailIndex()
    {
        int tmp =  deletedMailIndex;
        deletedMailName = "";
        deletedMailIndex = -1;
        
        return tmp;
    }
    
    public int getReturnStatus() {
        return returnStatus;
    }
    
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButtonCancel = new javax.swing.JButton();
        jPanelMsgContent = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaText = new javax.swing.JTextArea();
        jPanelTop = new javax.swing.JPanel();
        jPanelTitle = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextFieldTitle = new javax.swing.JTextField();
        jPanelAttachments = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListAttachments = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jButtonBrowse = new javax.swing.JButton();
        jLabelTitle = new javax.swing.JLabel();
        jLabelReceiver = new javax.swing.JLabel();
        jPanelReceiver = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextFieldReceiver = new javax.swing.JTextField();
        jLabelSender = new javax.swing.JLabel();
        jTextFieldSender = new javax.swing.JTextField();
        jButtonDelete = new javax.swing.JButton();
        jLabelDate = new javax.swing.JLabel();
        jLabelDateValue = new javax.swing.JLabel();
        jLabelMsgText = new javax.swing.JLabel();
        jButtonSendFurther = new javax.swing.JButton();
        jButtonAnswer = new javax.swing.JButton();

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
        jScrollPane2.setViewportView(jTextAreaText);
        jTextAreaText.setDisabledTextColor(Color.BLACK);

        javax.swing.GroupLayout gl_jPanelMsgContent = new javax.swing.GroupLayout(jPanelMsgContent);
        jPanelMsgContent.setLayout(gl_jPanelMsgContent);
        gl_jPanelMsgContent.setHorizontalGroup(
            gl_jPanelMsgContent.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
        );
        gl_jPanelMsgContent.setVerticalGroup(
            gl_jPanelMsgContent.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanelMsgContent.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout gl_jPanelTop = new javax.swing.GroupLayout(jPanelTop);
        jPanelTop.setLayout(gl_jPanelTop);
        gl_jPanelTop.setHorizontalGroup(
            gl_jPanelTop.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        gl_jPanelTop.setVerticalGroup(
            gl_jPanelTop.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(jTextFieldTitle);
        jTextFieldTitle.setDisabledTextColor(Color.BLACK);

        javax.swing.GroupLayout gl_jPanelTitle = new javax.swing.GroupLayout(jPanelTitle);
        jPanelTitle.setLayout(gl_jPanelTitle);
        gl_jPanelTitle.setHorizontalGroup(
            gl_jPanelTitle.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        gl_jPanelTitle.setVerticalGroup(
            gl_jPanelTitle.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanelTitle.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jListAttachments.setModel(defaultListModel);
        jScrollPane5.setViewportView(jListAttachments);
        jScrollPane5.setWheelScrollingEnabled(true);
        jScrollPane5.getVerticalScrollBar().setUnitIncrement(64);

        // Opening "File Saver" to copy attachment file in the other place (on disc)
        listSelectionModel = jListAttachments.getSelectionModel();
        listSelectionModel.addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();

                    int firstIndex = e.getFirstIndex();
                    int lastIndex = e.getLastIndex();
                    boolean isAdjusting = e.getValueIsAdjusting();
                    System.out.println("Event for indexes "
                        + firstIndex + " - " + lastIndex
                        + "; isAdjusting is " + isAdjusting
                        + "; selected indexes:");

                    if (lsm.isSelectionEmpty()) {
                        System.out.println(" <none>");
                    } else {
                        // Find out which indexes are selected.
                        int minIndex = lsm.getMinSelectionIndex();
                        int maxIndex = lsm.getMaxSelectionIndex();
                        for (int i = minIndex; i <= maxIndex; i++) {
                            if (lsm.isSelectedIndex(i)) {
                                System.out.println(" " + i);

                                String x = attachments.get(i).toString();

                                System.out.println("Component name: " + x);

                                jListAttachments.clearSelection();

                                JDialog jdialog = (JDialog) SwingUtilities.getRoot(jListAttachments);

                                final FileSaver fileSaver = new FileSaver(jdialog, true, x);
                            }
                        }
                    }
                    System.out.println("");
                }
            });

            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
            jPanel5.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );

            jLabel1.setText("Załączniki:");

            jButtonBrowse.setText("Przeglądaj");
            jButtonBrowse.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonBrowseActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout gl_jPanelAttachments = new javax.swing.GroupLayout(jPanelAttachments);
            jPanelAttachments.setLayout(gl_jPanelAttachments);
            gl_jPanelAttachments.setHorizontalGroup(
                gl_jPanelAttachments.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gl_jPanelAttachments.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addComponent(jLabel1)
                    .addGap(53, 53, 53)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonBrowse)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            gl_jPanelAttachments.setVerticalGroup(
                gl_jPanelAttachments.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(gl_jPanelAttachments.createSequentialGroup()
                    .addGroup(gl_jPanelAttachments.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(gl_jPanelAttachments.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_jPanelAttachments.createSequentialGroup()
                            .addGap(39, 39, 39)
                            .addComponent(jLabel1))
                        .addGroup(gl_jPanelAttachments.createSequentialGroup()
                            .addGap(34, 34, 34)
                            .addComponent(jButtonBrowse)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jLabelTitle.setText("Tytuł wiadomości:");
            jLabelReceiver.setText("Odbiorca wiadomości:");

            jScrollPane3.setViewportView(jTextFieldReceiver);
            jTextFieldReceiver.setDisabledTextColor(Color.BLACK);

            javax.swing.GroupLayout gl_jPanelReceiver = new javax.swing.GroupLayout(jPanelReceiver);
            jPanelReceiver.setLayout(gl_jPanelReceiver);
            gl_jPanelReceiver.setHorizontalGroup(
                gl_jPanelReceiver.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
            );
            gl_jPanelReceiver.setVerticalGroup(
                gl_jPanelReceiver.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
            );

            jLabelSender.setText("Nadawca wiadomości:");

            jButtonDelete.setText("Usuń wiadomość");
            jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonDeleteActionPerformed(evt);
                }
            });

            jLabelDate.setText("Data odebrania:");
            jLabelMsgText.setText("Treść wiadomości:");
            jButtonSendFurther.setText("Prześlij dalej");
            jButtonAnswer.setText("Odpowiedz");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jPanelAttachments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelTitle)
                                .addComponent(jLabelReceiver)
                                .addComponent(jLabelSender))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 35, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanelReceiver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextFieldSender))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jPanelTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(20, 20, 20))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(64, 64, 64)
                                    .addComponent(jLabelDate)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabelDateValue, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabelMsgText)
                        .addComponent(jPanelMsgContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(22, 22, 22))
                .addGroup(layout.createSequentialGroup()
                    .addGap(79, 79, 79)
                    .addComponent(jButtonAnswer)
                    .addGap(67, 67, 67)
                    .addComponent(jButtonSendFurther)
                    .addGap(66, 66, 66)
                    .addComponent(jButtonDelete)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(72, 72, 72))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanelTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldSender, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelSender))
                            .addGap(27, 27, 27)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanelReceiver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(9, 9, 9)
                                    .addComponent(jLabelReceiver)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(32, 32, 32)
                                    .addComponent(jPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelTitle)
                                    .addGap(29, 29, 29)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabelDateValue, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelDate))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanelAttachments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabelMsgText)
                    .addGap(3, 3, 3)
                    .addComponent(jPanelMsgContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonCancel)
                        .addComponent(jButtonDelete)
                        .addComponent(jButtonSendFurther)
                        .addComponent(jButtonAnswer))
                    .addContainerGap())
            );

            jTextFieldSender.setText(user);
            jTextFieldSender.setEnabled(false);
            jTextFieldSender.setDisabledTextColor(Color.BLACK);
            jTextFieldSender.setBorder(new MetalBorders.TextFieldBorder());
            
            if (type.equals("sent"))
            {
            	jLabelDate.setText("Data wysłania:");
            }
            
            pack();
            
            // Close the dialog when Esc key is pressed
            String cancelName = "cancel";
            InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
            ActionMap actionMap = getRootPane().getActionMap();
            actionMap.put(cancelName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doClose(RETURN_CANCEL);
                }
            });
        }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        doClose(RETURN_CANCEL);
    }

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        System.gc();
        
        String path = "database/rec_mails/" + user + "/" + title;
        System.out.println("Mail to remove: " + path);
        
        // usuwanie plików i folderu z mailem
        File dir = new File(path);
        String files[] = dir.list();
        
        for (String one : files)
        {
            File file = new File("database/rec_mails/" + user + "/" + title + "/" + one);
            file.delete();
            
            System.gc();
        }
        dir.delete();
        System.gc();
        
        deletedMailName = title;
        deletedMailIndex = index;
        
        /////////////////////////////////////////////
        // CODE OF ACKNOWLEDGE - DELETED MAIL
        /////////////////////////////////////////////
        
        final JDialog jDialogDeleteAck = new JDialog();
        jDialogDeleteAck.setTitle("Potwierdzenie");
        jDialogDeleteAck.getContentPane().setLayout(null);
        jDialogDeleteAck.setSize(240, 150);
        
        JLabel jLabelDeleteAck = new JLabel("Wiadomość została usunięta!");
        JButton jButtonOk = new JButton("OK");
        
        jLabelDeleteAck.setLocation(40, 20);
        jLabelDeleteAck.setSize(300, 20);
        
        jButtonOk.setLocation(90, 75);
        jButtonOk.setSize(60, 30);
        
        jDialogDeleteAck.getContentPane().add(jLabelDeleteAck);
        jDialogDeleteAck.getContentPane().add(jButtonOk);
        
        jButtonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jDialogDeleteAck.dispose();
                jDialogDeleteAck.setVisible(false);
                
                doClose(RETURN_CANCEL);
            }
        });
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int)screenSize.getWidth();
        int h = (int)screenSize.getHeight();
        
        Dimension dialogAckSize = jDialogDeleteAck.getSize();
        int x = (int)dialogAckSize.getWidth();
        int y = (int)dialogAckSize.getHeight();
        
        jDialogDeleteAck.setLocation((w/2)-(x/2), (h/2)-(y/2));
        
        jDialogDeleteAck.setResizable(false);
        jDialogDeleteAck.setModal(true);
        jDialogDeleteAck.setVisible(true);
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
    

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReadMailDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReadMailDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReadMailDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReadMailDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                UIManager.put("TextField.inactiveBackground", new ColorUIResource(new Color(255, 255, 255)));
                
                ReadMailDialog dialog;
                try {
                    dialog = new ReadMailDialog(new javax.swing.JFrame(), true, -1, "", "", "");
                    
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
	                    @Override
	                    public void windowClosing(java.awt.event.WindowEvent e) {
	                        System.exit(0);
	                    }
                });
                dialog.setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ReadMailDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }


    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonAnswer;
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonSendFurther;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelMsgText;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelDateValue;
    private javax.swing.JLabel jLabelReceiver;
    private javax.swing.JLabel jLabelSender;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JList jListAttachments;
    private javax.swing.JPanel jPanelMsgContent;
    private javax.swing.JPanel jPanelTop;
    private javax.swing.JPanel jPanelTitle;
    private javax.swing.JPanel jPanelAttachments;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelReceiver;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextAreaText;
    private javax.swing.JTextField jTextFieldReceiver;
    private javax.swing.JTextField jTextFieldSender;
    private javax.swing.JTextField jTextFieldTitle;
}
