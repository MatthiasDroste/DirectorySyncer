/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.droste.file.report;

import java.nio.file.Path;
import java.util.*;

/**
 * Contains the results of a synchronization
 */
public class Report {
    private int noOfChangedFiles = 0;
    private Map<Path, Path> changedFiles = new HashMap<Path, Path>();
    private int noOfNewFiles = 0;
    private Map<Path, Path> newFiles = new HashMap<Path, Path>();
    private List<Path> newDirectories = new ArrayList<Path>();
    private int noOfNewDirectories = 0;
    
    public void addChangedFile(Path file, Path newTargetPath) {
        noOfChangedFiles++;
        changedFiles.put(file, newTargetPath);
    }
    
    public void addNewFile(Path file, Path newTargetPath) {
        noOfNewFiles++;
        newFiles.put(file, newTargetPath);
    }

    public void addNewDirectory(Path newdir) {
        noOfNewDirectories++;
        newDirectories.add(newdir);
    }

    public int getNoOfChangedFiles()
    {
        return noOfChangedFiles;
    }
    
    public int getNoOfNewFiles()
    {
        return noOfNewFiles;
    }
    
    public int getNoOfNewDirectories()
    {
        return noOfNewDirectories;
    }

    public Map<Path, Path> getChangedFiles() {
        return Collections.unmodifiableMap(changedFiles);
    }
  
    
    public Map<Path, Path> getNewFiles() {
        return Collections.unmodifiableMap(newFiles);
    }
    
    public List<Path> getNewDirectories() {
        return Collections.unmodifiableList(newDirectories);
    }
}
