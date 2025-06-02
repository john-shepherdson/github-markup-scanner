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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class GitHubMarkupScannerTest {

    
    /** 
     * @throws Exception
     */
    @Test
    void testParseGitHubUrl_ValidUrls() throws Exception {
        GitHubMarkupScanner scanner = new GitHubMarkupScanner();

        String[] result1 = scanner.parseGitHubUrl("https://github.com/owner/repo");
        assertArrayEquals(new String[]{"owner", "repo"}, result1);

        String[] result2 = scanner.parseGitHubUrl("https://github.com/owner/repo/");
        assertArrayEquals(new String[]{"owner", "repo"}, result2);

        String[] result3 = scanner.parseGitHubUrl("https://github.com/owner/repo.git");
        assertArrayEquals(new String[]{"owner", "repo.git"}, result3);
    }

    @Test
    void testParseGitHubUrl_InvalidUrl() {
        GitHubMarkupScanner scanner = new GitHubMarkupScanner();
        assertThrows(IllegalArgumentException.class, () -> {
            scanner.parseGitHubUrl("https://notgithub.com/owner/repo");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            scanner.parseGitHubUrl("https://github.com/owner");
        });
    }

    @Test
    void testFilterMarkupFiles() {
        GitHubMarkupScanner scanner = new GitHubMarkupScanner();
        List<GitHubMarkupScanner.FileInfo> files = List.of(
            new GitHubMarkupScanner.FileInfo("README.md", "url1"),
            new GitHubMarkupScanner.FileInfo("script.js", "url2"),
            new GitHubMarkupScanner.FileInfo("notes.txt", "url3"),
            new GitHubMarkupScanner.FileInfo("image.png", "url4")
        );
        List<GitHubMarkupScanner.FileInfo> markupFiles = scanner.filterMarkupFiles(files);
        assertEquals(2, markupFiles.size());
        assertTrue(markupFiles.stream().anyMatch(f -> f.name.equals("README.md")));
        assertTrue(markupFiles.stream().anyMatch(f -> f.name.equals("notes.txt")));
    }
}
