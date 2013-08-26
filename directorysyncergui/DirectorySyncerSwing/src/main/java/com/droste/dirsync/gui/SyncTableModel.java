/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.droste.dirsync.gui;

import java.nio.file.Path;
import java.util.*;

import javax.swing.table.AbstractTableModel;

class SyncTableModel extends AbstractTableModel {
 
    private final List<String> tableHeader;
    private final List<List<String>> tableContent;

    public SyncTableModel(Map<Path, String> files)
    {
        this.tableContent = getChangedFiles(files);
        this.tableHeader = getTableHeader();
    }
    
    
    public SyncTableModel(List<Path> directories)
    {
        this.tableContent = toTableContent(directories);
        this.tableHeader = toTableHeader("New Directories");
    }

	@Override
    public int getRowCount() {
        return tableContent.size();
    }

    @Override
    public int getColumnCount() {
        return tableHeader.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return tableContent.get(rowIndex).get(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        return tableHeader.get(column);
    }
    
    private List<String> getTableHeader() {
        final List<String> tableHeader = new ArrayList<String>();
        tableHeader.add("Location in source folder");
        tableHeader.add("Location in target folder");
        return tableHeader;
    }

    private List<List<String>> getChangedFiles(Map<Path, String> files) {
        List<List<String>> tableContent = new ArrayList<List<String>>();
        for (Map.Entry<Path, String> entry : files.entrySet()) {
            List<String> contentRow = new ArrayList<String>();
            contentRow.add(entry.getKey().toString());
            contentRow.add(entry.getValue());
            tableContent.add(contentRow);
        }
        return tableContent;
    }
    
    private List<List<String>> getConcatenatedFileNames(Map<Path, List<Path>> files)
    {
        List<List<String>> tableContent = new ArrayList<List<String>>();
        for (Map.Entry<Path, List<Path>> entry : files.entrySet()) {
            List<String> contentRow = new ArrayList<String>();
            contentRow.add(entry.getKey().toString());
            StringBuilder targetPaths = new StringBuilder();
            final List<Path> values = entry.getValue();
            for (Path value : values) targetPaths.append(value.toString()).append(" ");
            contentRow.add(targetPaths.toString());
            tableContent.add(contentRow);
        }
        return tableContent;
    }

    private List<List<String>> toTableContent(List<Path> directories) {
        List<List<String>> tableContent = new ArrayList<List<String>>();
        for (Path directory : directories)
        {
            List<String> contentRow = new ArrayList<String>();
            contentRow.add(directory.toString());
            tableContent.add(contentRow);
        }
        return tableContent;
    }

    private List<String> toTableHeader(String newDirectories) {
        List<String> tableHeader = new ArrayList<String>();
        tableHeader.add(newDirectories);
        return tableHeader;
    }
}
