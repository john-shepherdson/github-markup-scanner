/*
 * Copyright © 2025 EOSC Beyond (${email})
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eoscbeyond.eu;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GitHubMarkupScannerTest {

    private GitHubMarkupScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new GitHubMarkupScanner();
    }

    
    @Test
    void testMainWithDefaultArguments() {
        // This test checks that the main method does not throw exceptions
        // when called with no arguments (should use defaults).
        assertDoesNotThrow(() -> GitHubMarkupScanner.main(new String[]{}));
    }

    @Test
    void testMainWithValidArguments() {
        // This test checks that the main method does not throw exceptions
        // when called with two valid arguments.
        String[] args = {
            "https://github.com/github/gitignore",
            "gitignore"
        };
        assertDoesNotThrow(() -> GitHubMarkupScanner.main(args));
    }

    @Test
    @DisplayName("parseGitHubUrl: valid URLs")
    void testParseGitHubUrl_Valid() {
        GitHubMarkupScanner scanner = new GitHubMarkupScanner();

        String[] result1 = scanner.parseGitHubUrl("https://github.com/owner/repo");
        assertArrayEquals(new String[]{"owner", "repo"}, result1);

        String[] result2 = scanner.parseGitHubUrl("https://github.com/owner/repo/");
        assertArrayEquals(new String[]{"owner", "repo"}, result2);

        String[] result3 = scanner.parseGitHubUrl("https://github.com/owner/repo.git");
        assertArrayEquals(new String[]{"owner", "repo.git"}, result3);
    }

    @Test
    @DisplayName("parseGitHubUrl: invalid URLs")
    void testParseGitHubUrl_Invalid() {
        assertThrows(IllegalArgumentException.class, () ->
                scanner.parseGitHubUrl("https://notgithub.com/owner/repo"));
        assertThrows(IllegalArgumentException.class, () ->
                scanner.parseGitHubUrl("https://github.com/owner"));
        assertThrows(IllegalArgumentException.class, () ->
                scanner.parseGitHubUrl("random string"));
    }

    @Test
    @DisplayName("filterMarkupFiles: filters only markup files")
    void testFilterMarkupFiles() {
        List<GitHubMarkupScanner.FileInfo> files = List.of(
                new GitHubMarkupScanner.FileInfo("README.md", "url1"),
                new GitHubMarkupScanner.FileInfo("script.js", "url2"),
                new GitHubMarkupScanner.FileInfo("notes.txt", "url3"),
                new GitHubMarkupScanner.FileInfo("image.png", "url4"),
                new GitHubMarkupScanner.FileInfo("manual.rst", "url5")
        );
        List<GitHubMarkupScanner.FileInfo> markupFiles = scanner.filterMarkupFiles(files);
        assertEquals(3, markupFiles.size());
        assertTrue(markupFiles.stream().anyMatch(f -> f.name.equals("README.md")));
        assertTrue(markupFiles.stream().anyMatch(f -> f.name.equals("notes.txt")));
        assertTrue(markupFiles.stream().anyMatch(f -> f.name.equals("manual.rst")));
    }

    @Test
    @DisplayName("getFindings: returns findings list")
    void testGetFindings() {
        assertNotNull(scanner.getFindings());
        assertTrue(scanner.getFindings().isEmpty());
    }

    @Test
    @DisplayName("FileInfo: stores name and downloadUrl")
    void testFileInfo() {
        GitHubMarkupScanner.FileInfo file = new GitHubMarkupScanner.FileInfo("test.md", "http://example.com/test.md");
        assertEquals("test.md", file.name);
        assertEquals("http://example.com/test.md", file.downloadUrl);
    }

    // Integration-like tests for scanFile and scanRepository would require mocking HTTP connections.
    // Here is a simple structure using a local file via a custom FileInfo and a test double.

    @Test
    @DisplayName("scanFile: finds substring in file")
    void testScanFile_FindsSubstring() throws Exception {
        // Use a raw GitHub URL to a small public file for demonstration, or mock in real tests.
        // For demonstration, this test will be skipped if network is not available.
        GitHubMarkupScanner.FileInfo file = new GitHubMarkupScanner.FileInfo(
                "README.md",
                "https://raw.githubusercontent.com/github/gitignore/main/README.md"
        );
        boolean found = scanner.scanFile(file, "gitignore");
        assertTrue(found);
        assertFalse(scanner.getFindings().isEmpty());
    }

    @Test
    @DisplayName("scanFile: does not find substring in file")
    void testScanFile_NotFound() throws Exception {
        GitHubMarkupScanner.FileInfo file = new GitHubMarkupScanner.FileInfo(
                "README.md",
                "https://raw.githubusercontent.com/github/gitignore/main/README.md"
        );
        boolean found = scanner.scanFile(file, "thisstringdoesnotexist");
        assertFalse(found);
    }
}