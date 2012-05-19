package com.droste.file;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;

import org.junit.Test;

public class TestDirectorySyncer
{
	@Test
	public void testNull()
	{
		assertTrue(true);
	}
	
	@Test
	public void testBuildTargetFileMap()
	{
		DirectorySyncer syncer = new DirectorySyncer("src/test/resources/source","src/test/resources/target");
		assertNotNull(syncer);
		Map<String, File> targetMap = syncer.buildTargetFileMap();
		assertEquals(7, targetMap.size());
	}
}
