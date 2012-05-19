package com.droste.file;

/**
 * 1. put the target files into a map, key is the filename, value the file. 
 *   Careful: there might be multiple files with the same name
 * 2. for every file in source: see if key exists in targetmap.
 * 2.1 if exists then compare filesize
 * 2.1.1 if equal nothing to do
 * 2.1.2 if different copy file from source to target, but add (n) to the name. 
 *   collisiondetection for different n's needed
 * 2.2 if not exists then copy from source to target, keep the subpath.   
 */
public class DirectorySyncer
{
	
}
