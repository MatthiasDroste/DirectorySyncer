package com.droste.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

import org.junit.*;

public class TestDirectorySyncer
{
	private final String tempSrcDir = "temp";
	private final String tempTargetDir = "temp2";
	private Path sourceFile;
	private Path targetFile;

	@Before
	public void setup() throws IOException
	{
		sourceFile = createTempFile(tempSrcDir, "src/test/resources/source/einsteiger.php.html");
		targetFile = createTempFile(tempTargetDir, "src/test/resources/target/einsteiger.php.html");
	}

	@After
	public void cleanup() throws IOException
	{
		cleanup(tempSrcDir);
		cleanup(tempTargetDir);
	}

	@Test
	public void testBuildTargetFileMap() throws IOException
	{
		DirectorySyncer syncer = new DirectorySyncer("src/test/resources/source", "src/test/resources/target");
		assertNotNull(syncer);
		Map<String, Path> targetMap = syncer.buildTargetFileMap();
		assertEquals(8, targetMap.size());
	}

	@Test
	public void testFindSourcesSameFileSameSizeDontCopy() throws IOException
	{
		Files.delete(targetFile);
		Files.copy(sourceFile, targetFile);

		DirectorySyncer syncer = new DirectorySyncer("src/test/resources/source/same", "src/test/resources/target/same");
		Map<String, Path> targetMap = syncer.buildTargetFileMap();
		assertEquals(1, targetMap.size());
		syncer.findAndHandleSourcesInTargetMap(targetMap);
		assertTrue(Files.size(sourceFile) == Files.size(targetFile));
		assertEquals(1, syncer.buildTargetFileMap().size());
	}

	@Test
	public void testRenameDuplicateFile()
	{
		DirectorySyncer syncer = new DirectorySyncer(tempSrcDir, tempTargetDir);
		assertEquals("einsteiger.php (1).html", syncer.renameDuplicateFile(sourceFile, 1));
		assertEquals("einsteiger.php (2).html", syncer.renameDuplicateFile(sourceFile, 2));
	}

	@Test
	public void testRenameWithExistingDuplicates() throws IOException
	{
		Files.copy(targetFile, new File(tempTargetDir + "/" + "einsteiger.php (1).html").toPath());
		DirectorySyncer syncer = new DirectorySyncer(tempSrcDir, tempTargetDir);
		Map<String, Path> targetMap = syncer.buildTargetFileMap();
		assertEquals(2, targetMap.size());

		syncer.findAndHandleSourcesInTargetMap(targetMap);

		assertTrue(Files.exists(new File("temp2/einsteiger.php (2).html").toPath()));
		assertEquals(3, syncer.buildTargetFileMap().size());
	}

	@Test
	public void testDifferentFileSizeCopy() throws IOException
	{
		DirectorySyncer syncer = new DirectorySyncer(tempSrcDir, tempTargetDir);
		Map<String, Path> targetMap = syncer.buildTargetFileMap();
		assertEquals(1, targetMap.size());

		assertFalse(Files.size(sourceFile) == Files.size(targetFile));
		syncer.findAndHandleSourcesInTargetMap(targetMap);
		Path copyOfSource = targetFile.getParent().resolve("einsteiger.php (1).html");
		assertTrue(Files.exists(copyOfSource));
		assertTrue(Files.size(sourceFile) == Files.size(copyOfSource));
	}

	@Test
	public void testNewFileCopy() throws IOException
	{
		Files.delete(targetFile);

		DirectorySyncer syncer = new DirectorySyncer(tempSrcDir, tempTargetDir);
		Map<String, Path> targetMap = syncer.buildTargetFileMap();
		assertEquals(0, targetMap.size());

		syncer.findAndHandleSourcesInTargetMap(targetMap);
		assertTrue(Files.exists(targetFile));
		assertTrue(Files.size(sourceFile) == Files.size(targetFile));
	}

	@Test
	public void testAll() throws IOException
	{
		final Path rootSource = new File("src/test/resources/source").toPath();
		final Path rootTarget = new File("src/test/resources/target").toPath();
		copyDirectory(rootSource, tempSrcDir);
		copyDirectory(rootTarget, tempTargetDir);

		DirectorySyncer syncer = new DirectorySyncer(tempSrcDir, tempTargetDir);
		Map<String, Path> targetMap = syncer.buildTargetFileMap();
		assertEquals(8, targetMap.size());

		syncer.findAndHandleSourcesInTargetMap(targetMap);
		assertEquals(19, syncer.buildTargetFileMap().size());
	}

	private void copyDirectory(final Path rootSource, final String copytodir) throws IOException
	{
		Files.walkFileTree(rootSource, new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
			{
				Files.copy(file, new File(copytodir + "/" + rootSource.relativize(file)).toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				return super.visitFile(file, attrs);
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
			{
				Path newdir = new File(copytodir + "/" + rootSource.relativize(dir)).toPath();
				if (!Files.exists(newdir))
					Files.createDirectory(newdir);
				return super.preVisitDirectory(dir, attrs);
			}
		});
	}

	private void cleanup(String dir) throws IOException
	{
		Files.walkFileTree(new File(dir).toPath(), new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
			{
				Files.delete(file);
				return super.visitFile(file, attrs);
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
			{
				Files.delete(dir);
				return super.postVisitDirectory(dir, exc);
			}
		});

	}

	private Path createTempFile(String tempDir, String filePath) throws IOException
	{
		Path path = new File(tempDir).toPath();
		if (!Files.exists(path))
			path = Files.createDirectory(path);
		final Path source = new File(filePath).toPath();
		Path target = new File(tempDir + "/" + source.getFileName()).toPath();
		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		return target;
	}
}
