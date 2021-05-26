/*
 * Copyright (c) 2021. Increff
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.increff.diff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDiff {

	private static final Logger logger = Logger.getLogger(TestDiff.class);

	private File baseDir = null;

	@Before
	public void init() {
		baseDir = new File(System.getProperty("user.dir"));
		baseDir = new File(baseDir, "/src/test/data");
	}

	@Test
	public void testMatchingFiles() throws IOException {
		logger.info("*** testMatchingFiles() ***");
		File d1 = new File(baseDir, "d1");
		File d2 = new File(baseDir, "d2");
		boolean result = Diff.compareDirectories(d1, d2);
		assertTrue(result);
	}
	
	@Test
	public void testDifferentFileCount() throws IOException {
		logger.info("*** testDifferentFileCount() ***");
		File d1 = new File(baseDir, "d1");
		File d2 = new File(baseDir, "d3");
		boolean result = Diff.compareDirectories(d1, d2);
		assertTrue(result);
	}

	@Test
	public void testDifferentFileContents() throws IOException {
		logger.info("*** testDifferentFiles() ***");
		File d1 = new File(baseDir, "d1");
		File d2 = new File(baseDir, "d4");
		boolean result = Diff.compareDirectories(d1, d2);
		assertFalse(result);
	}

	@Test
	public void testInvalidDirectory() throws IOException {
		File d1 = new File(baseDir, "d1");
		File d2 = new File(baseDir, "Invalid directory");
		try {
			boolean result = Diff.compareDirectories(d1, d2);
			fail();
		} catch (IOException exception) {
			assertTrue(true);
		}
	}

	@Test
	public void testPrintDiff() {
		Set<String> diff = new HashSet<>(Arrays.asList("d1", "d2", "d3"));
		// Manually inspect log. Should print <<<d1 <<<d2 <<<d3
		Diff.printDiff(diff, "<<<");
		assertTrue(true);
	}

	@Test
	public void testMain() throws IOException {
		String[] args = {"src/test/data/d1","src/test/data/d2"};
		final InputStream original = System.in;
		Diff.main(args);
		System.setIn(original);
	}

}

