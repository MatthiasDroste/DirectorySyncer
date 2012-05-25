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
	private final String srcDir = "temp";
	private final String targetDir = "temp2";
	private Path source;
	private Path target;

	@Test
	public void testNull()
	{
		assertTrue(true);
	}
	
	@Test
	public void testBuildTargetFileMap() throws IOException
	{
		DirectorySyncer syncer = new DirectorySyncer("src/test/resources/source","src/test/resources/target");
		assertNotNull(syncer);
		Map<String, Path> targetMap = syncer.buildTargetFileMap();
		assertEquals(8, targetMap.size());
	}

	@Test
	public void testFindSourcesSameFileSameSizeDontCopy() throws IOException
	{
		DirectorySyncer syncer = new DirectorySyncer("src/test/resources/source/same", "src/test/resources/target/same");
		Map<String, Path> targetMap = syncer.buildTargetFileMap();

		syncer.findSourcesInTargetMap(targetMap);
	}

	@Test
	public void testDifferentFileSizeCopy() throws IOException
	{
		DirectorySyncer syncer = new DirectorySyncer(srcDir, targetDir);
		Map<String, Path> targetMap = syncer.buildTargetFileMap();
		assertEquals(1, targetMap.size());

		assertFalse(Files.size(source) == Files.size(target));
		syncer.findSourcesInTargetMap(targetMap);
		Path copyOfSource = target.getParent().resolve(target.getFileName() + " (1)");
		assertTrue(Files.exists(copyOfSource));
		assertTrue(Files.size(source) == Files.size(copyOfSource));
	}

	@Test
	public void testNewFileCopy()
	{

	}

	@Before
	public void setup() throws IOException
	{
		source = createTempFile(srcDir, "src/test/resources/source/einsteiger.php.html");
		target = createTempFile(targetDir, "src/test/resources/target/einsteiger.php.html");
	}

	@After
	public void cleanup() throws IOException
	{
		cleanup(srcDir);
		cleanup(targetDir);
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
