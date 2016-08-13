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
public class MailReadDialog extends javax.swing.JDialog {

    private String sender = "";
    private String receiver = "";
    private String title = "";
    private String date = "";
    private String mail_txt = "";
    
    
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    
    private String user;
    private String type;
    
    private int index;
    
    private DefaultListModel model = new DefaultListModel();
    ListSelectionModel listSelectionModel;
    
    java.awt.Frame parent;
    
    private int deleted_mail_index = -1;
    private String deleted_mail_name = "";
    
    private List attachments = new ArrayList<>();

    /**
     * Creates new form WriteMailDialog
     */
    public MailReadDialog(java.awt.Frame parent, boolean modal, int index, String readMail, String user, String type) throws FileNotFoundException {
        super(parent, modal);
        
        this.parent = parent;
        this.user = user;
        this.index = index;
        this.type = type;

        initComponents();
        if (this.type.equals("sent"))
            jLabelDate.setText("Data wysłania:");
        
        // Close the dialog when Esc is pressed
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

        readMailFromBase(readMail, user);
    }
    
    
    private void readMailFromBase(String title, String user) throws FileNotFoundException
    {
        setTitle(title);
        
        SAXBuilder saxBuilder = new SAXBuilder();
        
        // FileReader file = new FileReader(new File ("database/rec_mails/" + user + "/" + title + "/" + title + ".xml") );
        
        String dirname = "";
        switch (type) {
            case "rec":
                dirname = "database/rec_mails/";
                break;
            case "sent":
                dirname = "database/sent_mails/";
                break;
            case "trash":
                dirname = "database/trash_mails/";
                break;
        }
        
        File file = new File(/*"database/rec_mails/"*/ dirname + user + "/" + title + "/" + title + ".xml");
        
        try {  
            // converted file to document object  
            Document document = saxBuilder.build(file);  

            // get root node from xml  
            Element rootNode = document.getRootElement();

            sender = rootNode.getChild("sender").getValue();
            receiver = rootNode.getChild("receiver").getValue();
            this.title = rootNode.getChild("title").getValue();
            
            if (type.equals("sent"))
                date = rootNode.getChild("datetime").getValue();
            else    
                date = rootNode.getChild("received_date").getValue();
            
            mail_txt = rootNode.getChild("mail_txt").getValue();
            
            //// dodaj załączniki do listy załączników
            
            List<Element> atts = rootNode.getChildren("attachment");
            
            for (Element el : atts)
            {
                String name = el.getChild("att_title").getValue();
                
                String path = /*"database/rec_mails/"*/ dirname + user + "/" + title + "/" + name;
                attachments.add(path);
                
                File plik = new File(path);
                double size = Math.round( ((double)plik.length() / 1024.0 / 1024.0) * 10.0) / 10.0;
                
                String full_name = path + "  (rozmiar: " + size + " MB)"; 
                
                model.add(0, full_name); 
            }

            jTextFieldSender.setText(sender);
            jTextFieldReceiver.setText(receiver);
            jTextFieldTitle.setText(this.title);
            jLabelDateV.setText(date);
            jTextAreaText.setText(mail_txt);
            
            // file.close();
            
            System.gc();
        } catch (JDOMException | IOException e) {  
        }
    }
    
   
    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        cancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaText = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextFieldTitle = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListAtts = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jButtonBrowse = new javax.swing.JButton();
        jLabelTitle = new javax.swing.JLabel();
        jLabelRec = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextFieldReceiver = new javax.swing.JTextField();
        jLabelSender = new javax.swing.JLabel();
        jTextFieldSender = new javax.swing.JTextField();
        jButtonDelete = new javax.swing.JButton();
        jLabelDate = new javax.swing.JLabel();
        jLabelDateV = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
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

        cancelButton.setText("Anuluj");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jTextAreaText.setColumns(20);
        jTextAreaText.setRows(5);
        jScrollPane2.setViewportView(jTextAreaText);
        jTextAreaText.setDisabledTextColor(Color.BLACK);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(jTextFieldTitle);
        jTextFieldTitle.setDisabledTextColor(Color.BLACK);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jListAtts.setModel(model);
        jScrollPane5.setViewportView(jListAtts);
        jScrollPane5.setWheelScrollingEnabled(true);
        jScrollPane5.getVerticalScrollBar().setUnitIncrement(64);

        // Otwieranie File Saver do skopiowania pliku załącznika w inne miejsce (na dysku)
        listSelectionModel = jListAtts.getSelectionModel();
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

                                // String x = jListAtts.getSelectedValue().toString();
                                String x = attachments.get(i).toString();

                                System.out.println("Component name: " + x);

                                jListAtts.clearSelection();

                                JDialog jdialog = (JDialog) SwingUtilities.getRoot(jListAtts);

                                final FileSaver fileSaver = new FileSaver(jdialog, true, x);

                                // fileSaver.setVisible(true);
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

            javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addComponent(jLabel1)
                    .addGap(53, 53, 53)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonBrowse)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(39, 39, 39)
                            .addComponent(jLabel1))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(34, 34, 34)
                            .addComponent(jButtonBrowse)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jLabelTitle.setText("Tytuł wiadomości:");

            jLabelRec.setText("Odbiorca wiadomości:");

            jScrollPane3.setViewportView(jTextFieldReceiver);
            jTextFieldReceiver.setDisabledTextColor(Color.BLACK);

            javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
            jPanel6.setLayout(jPanel6Layout);
            jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
            );
            jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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

            jLabel2.setText("Treść wiadomości:");

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
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelTitle)
                                .addComponent(jLabelRec)
                                .addComponent(jLabelSender))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 35, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextFieldSender))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(20, 20, 20))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(64, 64, 64)
                                    .addComponent(jLabelDate)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabelDateV, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(22, 22, 22))
                .addGroup(layout.createSequentialGroup()
                    .addGap(79, 79, 79)
                    .addComponent(jButtonAnswer)
                    .addGap(67, 67, 67)
                    .addComponent(jButtonSendFurther)
                    .addGap(66, 66, 66)
                    .addComponent(jButtonDelete)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(72, 72, 72))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldSender, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelSender))
                            .addGap(27, 27, 27)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(9, 9, 9)
                                    .addComponent(jLabelRec)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(32, 32, 32)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelTitle)
                                    .addGap(29, 29, 29)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabelDateV, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelDate))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel2)
                    .addGap(3, 3, 3)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelButton)
                        .addComponent(jButtonDelete)
                        .addComponent(jButtonSendFurther)
                        .addComponent(jButtonAnswer))
                    .addContainerGap())
            );

            jTextFieldSender.setText(user);
            jTextFieldSender.setEnabled(false);
            jTextFieldSender.setDisabledTextColor(Color.BLACK);
            jTextFieldSender.setBorder(new MetalBorders.TextFieldBorder());

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed

        System.gc();
        
        String path = "database/rec_mails/" + this.user + "/" + title;
        System.out.println("Mail to remove: " + path);
        
        // usuwanie plików i folderu z mailem
        File dir = new File(path);
        String files[] = dir.list();
        
        for (String one : files)
        {
            File file = new File("database/rec_mails/" + this.user + "/" + title + "/" + one);
            file.delete();
            
            System.gc();
        }
        dir.delete();
        System.gc();
        
        deleted_mail_name = title;
        deleted_mail_index = this.index;
        
        /////////////////////////////////////////////
        // CODE OF ACKNOWLEDGE - DELETED MAIL
        /////////////////////////////////////////////
        
        final JDialog del_ack = new JDialog();
        del_ack.setTitle("Potwierdzenie");
        del_ack.setLayout(null);
        del_ack.setSize(240, 150);
        
        JLabel label_ack = new JLabel("Wiadomość została usunięta!");
        JButton ok_button = new JButton("OK");
        
        label_ack.setLocation(40, 20);
        label_ack.setSize(300, 20);
        
        ok_button.setLocation(90, 75);
        ok_button.setSize(60, 30);
        
        del_ack.add(label_ack);
        del_ack.add(ok_button);
        
        ok_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                del_ack.dispose();
                del_ack.setVisible(false);
                
                doClose(RET_CANCEL);
            }
        });
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int)screenSize.getWidth();
        int h = (int)screenSize.getHeight();
        
        Dimension dialogAckSize = del_ack.getSize();
        int x = (int)dialogAckSize.getWidth();
        int y = (int)dialogAckSize.getHeight();
        
        del_ack.setLocation((w/2)-(x/2), (h/2)-(y/2));
        
        del_ack.setResizable(false);
        del_ack.setModal(true);
        del_ack.setVisible(true);
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseActionPerformed
        FileBrowser fileBrowser = new FileBrowser(new javax.swing.JDialog(), true);
        File file = fileBrowser.getAttachment();

        if (file != null)
        {
            String absPath = file.getAbsolutePath();
            double fileSize = Math.round( ((double)file.length() / 1024.0 / 1024.0) * 10.0) / 10.0;

            String all = absPath + "  (rozmiar: " + fileSize + " MB)";

            attachments.add(absPath);
            model.add(0, all);
        }
    }//GEN-LAST:event_jButtonBrowseActionPerformed
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    
    public /*String*/ int getDeletedMailName()
    {
        /*String*/ int tmp =  deleted_mail_index; // deleted_mail_name;
        deleted_mail_name = "";
        deleted_mail_index = -1;
        
        return tmp;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MailReadDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MailReadDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MailReadDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MailReadDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                UIManager.put("TextField.inactiveBackground", new ColorUIResource(new Color(255, 255, 255)));
                
                MailReadDialog dialog;
                try {
                    dialog = new MailReadDialog(new javax.swing.JFrame(), true, -1, "", "", "");
                    
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MailReadDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButtonAnswer;
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonSendFurther;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelDateV;
    private javax.swing.JLabel jLabelRec;
    private javax.swing.JLabel jLabelSender;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JList jListAtts;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
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
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}
