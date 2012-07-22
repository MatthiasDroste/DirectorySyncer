/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.droste.file.report;

import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.*;

/**
 * Contains the results of a synchronization
 */
public class Report {
    private int noOfChangedFiles = 0;
    private final Map<Path, Path> changedFiles = new HashMap<Path, Path>();
    private int noOfNewFiles = 0;
    private final Map<Path, Path> newFiles = new HashMap<Path, Path>();
    private final List<Path> newDirectories = new ArrayList<Path>();
    private int noOfNewDirectories = 0;
    private double syncTimeInSeconds = 0.0;
    private int noOfSourceFiles = 0;
    private int noOfSourceDirectories = 0;
    private int noOfTargetFiles = 0;
    private int noOfRelocatedFiles = 0;
	private final Map<Path, Path> relocatedFiles = new HashMap<Path, Path>();
	private final Map<Path, List<Path>> additionalFiles = new HashMap<Path, List<Path>>();
    
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
    
	public void addRelocatedFile(Path relocatedFile, Path fileInTarget)
    {
        noOfRelocatedFiles++;
		relocatedFiles.put(relocatedFile, fileInTarget);
    }

	/** additional file: exists already at a different location in the target. Still copied */
	public void addAdditionalFile(Path additionalFile, List<Path> filesInTarget)
	{
		additionalFiles.put(additionalFile, filesInTarget);
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
    
    /**
     * List of files that have a new location in the target => nothing copied for them.
     */
	public Map<Path, Path> getRelocatedFiles()
	{
		return Collections.unmodifiableMap(relocatedFiles);
    }

	/** List of files that are copied even though they already exist at a different location in the target */
	public Map<Path, List<Path>> getAdditionalFiles()
	{
		return additionalFiles;
	}

    public int getNoOfChanges() {
        return getNoOfChangedFiles() + getNoOfNewDirectories() + getNoOfNewFiles();
    }

    public double getSyncTime() {
        return syncTimeInSeconds;
    }

    public void setSyncTime(long timeDiffInMillis) {
        try {
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            Duration duration = datatypeFactory.newDuration(timeDiffInMillis);
            this.syncTimeInSeconds = duration.getSeconds();
            if (syncTimeInSeconds == 0) syncTimeInSeconds = timeDiffInMillis/1000.0;
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void countSourceFiles() {
        noOfSourceFiles++;
    }
    
    public int getNoOfSourceFiles()
    {
        return noOfSourceFiles;
    }

    public void countDirectories() {
        noOfSourceDirectories++;
    }
    
    public int getNoOfSourceDirectories(){
        return noOfSourceDirectories;
    }

    public void countTargetFiles() {
        noOfTargetFiles++;
    }
    
    public int getNoOfTargetFiles()
    {
        return noOfTargetFiles;
    }
    
    /** files that had a new location in the target and were not copied */
    public int getNoOfRelocatedFiles()
    {
        return noOfRelocatedFiles;
    }

	public void removeDirectory(Path directory)
	{
		noOfNewDirectories--;
		newDirectories.remove(directory);
	}
}
