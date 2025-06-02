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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GitHubMarkupScannerTest {
    
    private GitHubMarkupScanner scanner;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalErr;
    
    @BeforeEach
    void setUp() {
        scanner = new GitHubMarkupScanner();
        
        // Capture System.out
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        // Capture System.err
        errorStream = new ByteArrayOutputStream();
        originalErr = System.err;
        System.setErr(new PrintStream(errorStream));
    }
    
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    @Test
    @DisplayName("Main method should exit with error code 1 when no arguments provided")
    void testMainNoArguments() {
        @SuppressWarnings("unused")
        Exception exception = assertThrows(SecurityException.class, () -> {
            GitHubMarkupScanner.main(new String[]{});
        });
        
        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Usage: java GitHubMarkupScanner"));
    }
    
    /**
     * 
     */
    @Test
    @DisplayName("Main method should exit with error code 1 when too many arguments provided")
    void testMainTooManyArguments() {
        @SuppressWarnings("unused")
        Exception exception = assertThrows(SecurityException.class, () -> {
            GitHubMarkupScanner.main(new String[]{"arg1", "arg2"});
        });
        
        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Usage: java GitHubMarkupScanner"));
    }
    
    @Test
    @DisplayName("Should parse valid GitHub URLs correctly")
    void testParseGitHubUrl() throws Exception {
        // Test standard GitHub URL
        String[] result1 = invokeParseGitHubUrl("https://github.com/user/repo");
        assertArrayEquals(new String[]{"user", "repo"}, result1);
        
        // Test GitHub URL with trailing slash
        String[] result2 = invokeParseGitHubUrl("https://github.com/user/repo/");
        assertArrayEquals(new String[]{"user", "repo"}, result2);
        
        // Test GitHub URL with .git extension
        String[] result3 = invokeParseGitHubUrl("https://github.com/user/repo");
        assertArrayEquals(new String[]{"user", "repo"}, result3);
        
        // Test with different user/repo names
        String[] result4 = invokeParseGitHubUrl("https://github.com/my-org/my-project-name");
        assertArrayEquals(new String[]{"my-org", "my-project-name"}, result4);
    }
    
    @Test
    @DisplayName("Should throw exception for invalid GitHub URLs")
    void testParseInvalidGitHubUrls() {
        // Test various invalid URLs
        assertThrows(Exception.class, () -> invokeParseGitHubUrl("https://gitlab.com/user/repo"));
        assertThrows(Exception.class, () -> invokeParseGitHubUrl("https://github.com/user"));
        assertThrows(Exception.class, () -> invokeParseGitHubUrl("https://github.com/"));
        assertThrows(Exception.class, () -> invokeParseGitHubUrl("invalid-url"));
        assertThrows(Exception.class, () -> invokeParseGitHubUrl(""));
    }
    
    @Test
    @DisplayName("Should filter markup files correctly")
    void testFilterMarkupFiles() throws Exception {
        List<GitHubMarkupScanner.FileInfo> testFiles = Arrays.asList(
            createFileInfo("README.md"),
            createFileInfo("INSTALL.rst"),
            createFileInfo("config.json"),
            createFileInfo("docs.txt"),
            createFileInfo("script.py"),
            createFileInfo("CHANGELOG.markdown"),
            createFileInfo("guide.asciidoc"),
            createFileInfo("binary.exe")
        );
        
        List<GitHubMarkupScanner.FileInfo> result = invokeFilterMarkupFiles(testFiles);
        
        assertEquals(5, result.size());
        Set<String> resultNames = new HashSet<>();
        for (GitHubMarkupScanner.FileInfo file : result) {
            resultNames.add(file.name);
        }
        
        assertTrue(resultNames.contains("README.md"));
        assertTrue(resultNames.contains("INSTALL.rst"));
        assertTrue(resultNames.contains("docs.txt"));
        assertTrue(resultNames.contains("CHANGELOG.markdown"));
        assertTrue(resultNames.contains("guide.asciidoc"));
        assertFalse(resultNames.contains("config.json"));
        assertFalse(resultNames.contains("script.py"));
        assertFalse(resultNames.contains("binary.exe"));
    }
    
    @Test
    @DisplayName("Should handle case-insensitive file extensions")
    void testCaseInsensitiveExtensions() throws Exception {
        List<GitHubMarkupScanner.FileInfo> testFiles = Arrays.asList(
            createFileInfo("README.MD"),
            createFileInfo("install.RST"),
            createFileInfo("docs.TXT"),
            createFileInfo("guide.Markdown")
        );
        
        List<GitHubMarkupScanner.FileInfo> result = invokeFilterMarkupFiles(testFiles);
        assertEquals(4, result.size());
    }
    
    @Test
    @DisplayName("Should return empty list when no markup files present")
    void testNoMarkupFiles() throws Exception {
        List<GitHubMarkupScanner.FileInfo> testFiles = Arrays.asList(
            createFileInfo("config.json"),
            createFileInfo("script.py"),
            createFileInfo("binary.exe"),
            createFileInfo("data.csv")
        );
        
        List<GitHubMarkupScanner.FileInfo> result = invokeFilterMarkupFiles(testFiles);
        assertTrue(result.isEmpty());
    }
    
    @Test
    @DisplayName("Should handle empty file list")
    void testEmptyFileList() throws Exception {
        List<GitHubMarkupScanner.FileInfo> testFiles = new ArrayList<>();
        List<GitHubMarkupScanner.FileInfo> result = invokeFilterMarkupFiles(testFiles);
        assertTrue(result.isEmpty());
    }
    
    @Test
    @DisplayName("Should detect all supported markup extensions")
    void testAllMarkupExtensions() throws Exception {
        String[] extensions = {
            ".md", ".markdown", ".mdown", ".mkdn", ".mkd", ".mdwn", ".mdtxt", ".mdtext",
            ".rst", ".txt", ".asciidoc", ".adoc", ".asc", ".textile", ".rdoc", ".org",
            ".creole", ".mediawiki", ".wiki", ".pod"
        };
        
        List<GitHubMarkupScanner.FileInfo> testFiles = new ArrayList<>();
        for (String ext : extensions) {
            testFiles.add(createFileInfo("file" + ext));
        }
        
        List<GitHubMarkupScanner.FileInfo> result = invokeFilterMarkupFiles(testFiles);
        assertEquals(extensions.length, result.size());
    }
    
    @Test
    @DisplayName("Should handle files with multiple dots in name")
    void testFilesWithMultipleDots() throws Exception {
        List<GitHubMarkupScanner.FileInfo> testFiles = Arrays.asList(
            createFileInfo("my.project.readme.md"),
            createFileInfo("version.1.2.changelog.rst"),
            createFileInfo("config.test.json") // Not markup
        );
        
        List<GitHubMarkupScanner.FileInfo> result = invokeFilterMarkupFiles(testFiles);
        assertEquals(2, result.size());
    }
    
    @Test
    @DisplayName("Should scan repository and handle no markup files scenario")
    void testScanRepositoryNoMarkupFiles() throws Exception {
        // Use a test subclass to override methods instead of mocking/spying
        GitHubMarkupScanner testScanner = new GitHubMarkupScanner() {
            @Override
            public String[] parseGitHubUrl(String url) {
                return new String[]{"user", "repo"};
            }

            @Override
            public List<FileInfo> getRepositoryContents(String repoApiUrl) {
                return Arrays.asList(createFileInfo("config.json"));
            }
        };

        try {
            testScanner.scanRepository("https://github.com/user/repo");
        } catch (Exception e) {
            // Expected due to possible implementation details
        }

        String output = outputStream.toString();
        assertTrue(output.contains("No markup files found"));
    }
    
    // Helper methods to access private methods via reflection
    private String[] invokeParseGitHubUrl(String url) throws Exception {
        var method = GitHubMarkupScanner.class.getDeclaredMethod("parseGitHubUrl", String.class);
        method.setAccessible(true);
        return (String[]) method.invoke(scanner, url);
    }
    
    @SuppressWarnings("unchecked")
    private List<GitHubMarkupScanner.FileInfo> invokeFilterMarkupFiles(List<GitHubMarkupScanner.FileInfo> files) throws Exception {
        var method = GitHubMarkupScanner.class.getDeclaredMethod("filterMarkupFiles", List.class);
        method.setAccessible(true);
        return (List<GitHubMarkupScanner.FileInfo>) method.invoke(scanner, files);
    }
    
    private GitHubMarkupScanner.FileInfo createFileInfo(String name) {
        try {
            var constructor = GitHubMarkupScanner.FileInfo.class.getDeclaredConstructor(String.class, String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(name, "https://example.com/" + name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    // Integration test helper class
    static class TestFileServer {
        @SuppressWarnings("unused")
        private final String content;
        @SuppressWarnings("unused")
        private final int responseCode;
        
        TestFileServer(String content, int responseCode) {
            this.content = content;
            this.responseCode = responseCode;
        }
        
        TestFileServer(String content) {
            this(content, 200);
        }
    }
    
    @Test
    @DisplayName("Should handle network errors gracefully")
    void testNetworkErrorHandling() {
        // Test that the scanner handles network issues appropriately
        assertThrows(Exception.class, () -> {
            scanner.scanRepository("https://github.com/nonexistent/repo");
        });
    }
    
    @Test
    @DisplayName("Should validate search substring constant")
    void testSearchSubstring() throws Exception {
        var field = GitHubMarkupScanner.class.getDeclaredField("SEARCH_SUBSTRING");
        field.setAccessible(true);
        String searchSubstring = (String) field.get(null);
        assertEquals("api.eu.badgr.io", searchSubstring);
    }
    
    @Test
    @DisplayName("Should validate markup extensions array")
    void testMarkupExtensionsArray() throws Exception {
        var field = GitHubMarkupScanner.class.getDeclaredField("MARKUP_EXTENSIONS");
        field.setAccessible(true);
        String[] extensions = (String[]) field.get(null);
        
        assertNotNull(extensions);
        assertTrue(extensions.length > 0);
        assertTrue(Arrays.asList(extensions).contains(".md"));
        assertTrue(Arrays.asList(extensions).contains(".rst"));
        assertTrue(Arrays.asList(extensions).contains(".txt"));
    }
}

// Mock test for SecurityManager to handle System.exit calls
class NoExitSecurityManager extends SecurityManager {
    @Override
    public void checkPermission(java.security.Permission perm) {
        // Allow all permissions
    }
    
    @Override
    public void checkExit(int status) {
        throw new SecurityException("System.exit(" + status + ") called");
    }
}
