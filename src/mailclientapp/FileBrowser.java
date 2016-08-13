/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailclientapp;

import java.io.File;

/**
 *
 * @author ethenq
 */
public class FileBrowser extends javax.swing.JDialog {

    private final int RET_CANCEL = 0;
    private final int RET_OK = 1;
    
    private File attachment = null;
    
    public FileBrowser(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });
        
        initComponents();
        setVisible(true);
    }

    public File getAttachment()
    {
        return attachment;
    }
    
    private void initComponents() {
        jFileChooser = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jFileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jFileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jFileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getActionCommand().equals("ApproveSelection"))
        {
            String path = jFileChooser.getSelectedFile().getAbsolutePath();
            
            System.out.println("Ścieżka pliku: " + path);
            this.attachment = jFileChooser.getSelectedFile();
            
            setVisible(false);
            dispose();
        }
        else
        {
            setVisible(false);
            dispose();
        }
    }


    private javax.swing.JFileChooser jFileChooser;
}
