package com.droste.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import com.droste.file.report.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;

/**
 * 1. put the target files into a map, key is the subpath under target, value the file.<br/>
 * 2. for every file in source: see if key exists in targetmap. <br/>
 * 2.1 if exists then compare hash <br/>
 * 2.1.1 if equal nothing to do <br/>
 * 2.1.2 if different copy file from source to target, but add (n) to the name. collisiondetection for different n's
 * needed <br/>
 * 2.2.1 if not exists then copy from source to target, keep the subpath.
 * 2.2.2 BUT (perhaps optional): search for file(hash) in the whole target. if exists then we asume there was a move in the target and don't copy.
 * 2.2.2.1 Problem 1: Licence-files: ProductX is with licence1 in target, productY should be synced from the source. 
 *         the licence must be in the target!
 * 2.2.2.2 problem 2: mp3-folder was renamed/moved in target. The old location must not be resynced from the source.
 * => compare with siblings, offer (un)do in results-dialog
 */
public class DirectorySyncer
{
	private static final LinkOption NFL = LinkOption.NOFOLLOW_LINKS;
	private final Path target;
	private final Path source;
        private final long startTime = System.currentTimeMillis();
        private final Report report;
        private final boolean isSimulationMode;
        private final Map<Long, Path> hashedTargetMap = new HashMap<Long, Path>();
        private final Adler32 adler = new Adler32();
        
	public DirectorySyncer(String source, String target, boolean isSimulationMode)
	{
		this.source = new File(source).toPath();
		this.target = new File(target).toPath();
                this.isSimulationMode = isSimulationMode;
                        
		assert (Files.exists(this.source, NFL) && Files.exists(this.target, NFL));
            this.report = new Report();
	}

	public Map<String, Path> buildTargetFileMap() throws IOException
	{
		final Map<String, Path> targetMap = new HashMap<String, Path>();
		Files.walkFileTree(target, new SimpleFileVisitor<Path>()
		{
		    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        report.countTargetFiles();
                        targetMap.put(target.relativize(file).toString(), file);
                        Long hash = hash(file);
                        if (hash != null) {
                            hashedTargetMap.put(hash, file);
                        }
                        return super.visitFile(file, attrs);
                    }
		});
		return targetMap;
	}

	public Report findAndHandleSourcesInTargetMap(final Map<String, Path> targetMap) throws IOException
	{
		Files.walkFileTree(source, new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
			{
                            report.countSourceFiles();
				Path targetPath = targetMap.get(source.relativize(file).toString());
				if (targetPath != null) {
					if (Files.size(file) != Files.size(targetPath)) {
						handleChangedFile(file, targetPath);
					}
				} else {
                    if (!checkIfMoved(file))
                        handleNewFile(file);
				}
				return super.visitFile(file, attrs);
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
			{
                            report.countDirectories();
				Path newdir = target.resolve(source.relativize(dir));
				if (!Files.exists(newdir))
                                {
                                    if (!isSimulationMode) Files.createDirectory(newdir);
                                    report.addNewDirectory(newdir); 
                                }
                                return super.preVisitDirectory(dir, attrs);
			}

			private void handleNewFile(Path file) throws IOException
			{
                                final Path newTargetPath = target.resolve(source.relativize(file));
				if (!isSimulationMode) Files.copy(file, newTargetPath);
                                report.addNewFile(file, newTargetPath);
			}

			private void handleChangedFile(Path file, final Path targetPath) throws IOException
			{
				Path newTargetPath = targetPath;
				int counter = 1;
				while (Files.exists(newTargetPath)) {
                                        if (Files.size(newTargetPath) == Files.size(file)) return;
					String newName = renameDuplicateFile(targetPath, counter);
					counter++;
					newTargetPath = targetPath.getParent().resolve(newName);
				}
				if (!isSimulationMode) Files.copy(file, newTargetPath);
                                report.addChangedFile(file, newTargetPath);
			}

            private boolean checkIfMoved(Path file) {
                Long hash = hash(file);
                Path fileInTarget = hashedTargetMap.get(hash);
                if(fileInTarget != null && fileInTarget.getFileName().equals(file.getFileName()))
                {
                    report.addMovedFile(file);
                    return true;
                }
                return false;
            }
		});
                report.setSyncTime(System.currentTimeMillis() - startTime);
                return report;
	}

	String renameDuplicateFile(Path file, int counter)
	{
		String[] split = file.getFileName().toString().split("\\.");
		StringBuilder newName = new StringBuilder();
		for (int i = 0; i < split.length; i++) {
			newName.append(split[i]);
			if (i == split.length - 2)
				newName.append(" (").append(counter).append(")");
			newName.append(".");
		}
		newName.deleteCharAt(newName.length() - 1);
		return newName.toString();
	}

    private Long hash(Path file) 
    {
        try {
            adler.update(Files.readAllBytes(file));
            long value = adler.getValue();
            adler.reset();
            return value;
        } catch (IOException ex) {
            Logger.getLogger(DirectorySyncer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Map<Long, Path> getHashedTargetMap()
    {
        return Collections.unmodifiableMap(hashedTargetMap);
    }
}
