package com.droste.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * 1. put the target files into a map, key is the subpath under target, value the file.<br/>
 * 2. for every file in source: see if key exists in targetmap. <br/>
 * 2.1 if exists then compare filesize <br/>
 * 2.1.1 if equal nothing to do <br/>
 * 2.1.2 if different copy file from source to target, but add (n) to the name. collisiondetection for different n's
 * needed <br/>
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

	public void findAndHandleSourcesInTargetMap(final Map<String, Path> targetMap) throws IOException
	{
		Files.walkFileTree(source, new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
			{
				Path targetPath = targetMap.get(source.relativize(file).toString());
				if (targetPath != null) {
					if (Files.size(file) != Files.size(targetPath)) {
						handleChangedFile(file, targetPath);
					}
				} else {
					handleNewFile(file);
				}
				return super.visitFile(file, attrs);
			}

			private void handleNewFile(Path file) throws IOException
			{
				Files.copy(file, target.resolve(source.relativize(file)));
			}

			private void handleChangedFile(Path file, Path targetPath) throws IOException
			{
				int counter = 1;
				while (Files.exists(targetPath)) {
					targetPath = new File(targetPath + " (" + (counter++) + ")").toPath();
				}
				Files.copy(file, targetPath);
			}
		});
	}

}
