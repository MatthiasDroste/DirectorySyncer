package com.droste.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * 1. put the target files into a map, key is the subpath under target, value the file.
 * 2. for every file in source: see if key exists in targetmap.
 * 2.1 if exists then compare filesize
 * 2.1.1 if equal nothing to do
 * 2.1.2 if different copy file from source to target, but add (n) to the name. 
 *   collisiondetection for different n's needed
 * 2.2 if not exists then copy from source to target, keep the subpath.   
 */
public class DirectorySyncer
{
	private static final LinkOption NFL = LinkOption.NOFOLLOW_LINKS;
	private final Path target;
	private final Path source;

	public DirectorySyncer(String source, String target)
	{
		this.source = new File(source).toPath();
		this.target = new File(target).toPath();
		assert (Files.exists(this.source, NFL) && Files.exists(this.target, NFL));
	}

	public Map<String, Path> buildTargetFileMap() throws IOException
	{
		final Map<String, Path> targetMap = new HashMap<String, Path>();
		Files.walkFileTree(target, new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
			{
				targetMap.put(target.relativize(file).toString(), file);
				return super.visitFile(file, attrs);
			}
		});
		return targetMap;
	}
	
}
