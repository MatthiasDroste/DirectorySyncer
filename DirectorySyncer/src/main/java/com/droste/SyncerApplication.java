package com.droste;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import javax.swing.*;

import com.droste.file.DirectorySyncer;

public class SyncerApplication
{
	static String sourceDir;
	static String targetDir;

	public static void main(String[] args)
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		final JFrame frame = new JFrame("Directory Syncer");
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 300);
		String SOURCE_LABEL = "Source directory";
		JLabel sourceLabel = new JLabel(SOURCE_LABEL);
		frame.add(sourceLabel);
		final JTextField sourceDirectoyField = new JTextField(40);
		frame.add(sourceDirectoyField);

		JButton sourceChooseButton = new JButton("..");
		frame.add(sourceChooseButton);

		String TARGET_LABEL = "Target directory";
		frame.add(new JLabel(TARGET_LABEL));
		final JTextField targetDirectoyField = new JTextField(40);
		frame.add(targetDirectoyField);
		JButton targetChooseButton = new JButton("..");
		frame.add(targetChooseButton);

		
		ActionListener directoryActionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setAcceptAllFileFilterUsed(false);

				switch (fc.showOpenDialog(null))
					{
					case JFileChooser.APPROVE_OPTION:
						File file = fc.getSelectedFile();
						sourceDir = file.getAbsolutePath();
						sourceDirectoyField.setText(sourceDir);
						break;
					default:
						System.out.println("Auswahl abgebrochen");
					}
			}
		};
		sourceChooseButton.addActionListener(directoryActionListener);

		ActionListener targetDirectoryActionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setAcceptAllFileFilterUsed(false);

				switch (fc.showOpenDialog(null))
					{
					case JFileChooser.APPROVE_OPTION:
						File file = fc.getSelectedFile();
						targetDir = file.getAbsolutePath();
						targetDirectoyField.setText(targetDir);
						break;
					default:
						System.out.println("Auswahl abgebrochen");
					}
			}
		};
		sourceChooseButton.addActionListener(targetDirectoryActionListener);

		JButton syncButton = new JButton("Start Syncing Now");
		frame.add(syncButton);

		syncButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (sourceDir != null && targetDir != null) {
					DirectorySyncer directorySyncer = new DirectorySyncer(sourceDir, targetDir);
					try {
						Map<String, Path> targetMap = directorySyncer.buildTargetFileMap();
						directorySyncer.findAndHandleSourcesInTargetMap(targetMap);
						JDialog jDialog = new JDialog(frame);
						jDialog.setTitle("Syncing worked");
					} catch (IOException e1) {
						JDialog jDialog = new JDialog(frame);
						jDialog.setTitle("Syncing failed");
						e1.printStackTrace();
					}

				}
			}
		});

		frame.setVisible(true);

	}

}
