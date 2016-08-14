/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailclientapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ethenq
 */
public class FileSaver extends javax.swing.JDialog {

    private File attachment;
    public String filePath;
    
    public FileSaver(java.awt.Dialog parent, boolean modal, String filePath) {
        super(parent, modal); 
        this.filePath = filePath;
        initComponents();
    }

    public File getAttachment()
    {
        return attachment;
    }
    
    private void initComponents() {
    	addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });
    	
        jFileChooser = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jFileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooserActionPerformed(evt);
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
        
        jFileChooser.setApproveButtonText("Zapisz plik");
        jFileChooser.setDialogTitle("Zapisz plik");
        jFileChooser.setSelectedFile(new File(filePath));
        setVisible(true);
    }

    private void jFileChooserActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getActionCommand().equals("ApproveSelection"))
        {
            String copyPath = jFileChooser.getSelectedFile().getAbsolutePath();
            
            System.out.println("Ścieżka źródłowa pliku załącznika: " + filePath);
            System.out.println("Ścieżka kopiowania pliku załącznika: " + copyPath);
            
            try {
                FileInputStream fis = new FileInputStream(filePath);
                FileOutputStream fos = new FileOutputStream(copyPath);
                
                byte bytes[] = new byte[(int) new File(filePath).length()];
                
                try {
                    while (fis.read(bytes) > 0) {}

                    fos.write(bytes); 
                    fos.flush();
                    
                    fis.close();
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileSaver.class.getName()).log(Level.SEVERE, null, ex);
                }   
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
            
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
