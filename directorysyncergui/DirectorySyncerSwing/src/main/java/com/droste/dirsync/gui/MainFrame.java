/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.droste.dirsync.gui;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import javax.swing.*;

import com.droste.file.DirectorySyncer;
import com.droste.file.report.Report;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author matthias
 */
public class MainFrame extends javax.swing.JFrame {

	private Report report;
        private Font defaultFont;
        
        public Font getDefaultFont()
        {
            return defaultFont;
        }
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        getContentPane().setBackground(Color.white);
        setTitle("Directory Syncer");
        setIconImage(new ImageIcon("icons/folder_go.png").getImage());
        this.defaultFont = jLabel1.getFont();
    }

    private void chooseDirectory(JTextField textField) throws HeadlessException {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setBackground(Color.white);

        switch (fc.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                File file = fc.getSelectedFile();
                String sourceDir = file.getAbsolutePath();
                textField.setText(sourceDir);
                break;
            default:
                System.out.println("Auswahl abgebrochen");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sourceDirectoyField = new javax.swing.JTextField();
        targetDirectoryField = new javax.swing.JTextField();
        sourceButton = new javax.swing.JButton();
        targetButton = new javax.swing.JButton();
        synchronizeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Directory Syncer"));

        jLabel1.setText("Source Dir:");

        jLabel2.setText("Target Dir:");

        sourceDirectoyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceDirectoyFieldActionPerformed(evt);
            }
        });

        sourceButton.setText("Choose...");
        sourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceButtonActionPerformed(evt);
            }
        });

        targetButton.setText("Choose...");
        targetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                targetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sourceDirectoyField, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .addComponent(targetDirectoryField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sourceButton)
                    .addComponent(targetButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(sourceDirectoyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sourceButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(targetDirectoryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(targetButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        synchronizeButton.setText("Start Synchronization");
        synchronizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                synchronizeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(synchronizeButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(synchronizeButton)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sourceDirectoyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceDirectoyFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sourceDirectoyFieldActionPerformed

    private void sourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceButtonActionPerformed
        chooseDirectory(sourceDirectoyField );
    }//GEN-LAST:event_sourceButtonActionPerformed

    private void targetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetButtonActionPerformed
        chooseDirectory(targetDirectoryField);
    }//GEN-LAST:event_targetButtonActionPerformed

    private void synchronizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_synchronizeButtonActionPerformed
        final String sourceDir = sourceDirectoyField.getText();
        final String targetDir = targetDirectoryField.getText();
        if (sourceDir != null && targetDir != null) {
            DirectorySyncer directorySyncer = new DirectorySyncer(sourceDir, targetDir);
            try {
                Map<String, Path> targetMap = directorySyncer.buildTargetFileMap();
				this.report = directorySyncer.findAndHandleSourcesInTargetMap(targetMap);
				new ReportDialog(this, false).setVisible(true);
                //jDialog.setTitle("Syncing worked");
            } catch (IOException e1) {
                JDialog jDialog = new JDialog(this);
                jDialog.setTitle("Syncing failed");
                new ReportDialog(this, false).setVisible(true);
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }

        }
    }//GEN-LAST:event_synchronizeButtonActionPerformed

    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            String property = System.getProperty("os.name");
            String lookAndFeel = (property.contains("Windows")) ? "Windows" : "Nimbus";
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (lookAndFeel.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton sourceButton;
    private javax.swing.JTextField sourceDirectoyField;
    private javax.swing.JButton synchronizeButton;
    private javax.swing.JButton targetButton;
    private javax.swing.JTextField targetDirectoryField;
    // End of variables declaration//GEN-END:variables
	public Report getReport()
	{
		return this.report;
	}
}