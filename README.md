# EOSC GitHub Markup Scanner

This repository contains the source code for the GitHub Markup Scanner
which searches a GitHub repository, looking for Badges.
The program handles errors gracefully and provides informative messages
for missing repositories or network issues.

Key Features:

1. Accepts a GitHub repository URL and a search string as command-line arguments
2. Uses the GitHub API to fetch repository contents from the root directory
3. Identifies markup files by common extensions (.md, .rst, .txt, .asciidoc, etc.)
4. Downloads and scans each markup file line by line
5. Prints any lines containing to search string with line numbers
6. Handles various GitHub URL formats

## Prerequisites

Java 21 or greater is required to build and run this application.

Jackson JSON library for parsing GitHub API responses:
jackson-databind
jackson-core
jackson-annotations

## Quick Start

1. Check prerequisites and install any required software.
2. Clone the repository to your local workspace.
3. Build the application using `mvn clean verify`.
4. Run the application using the following command: `mvn exec:java`.

## Getting started as developer

### Execute tests and run the application

```shell
# Run tests with coverage
mvn clean test

# Run all tests including integration
mvn clean verify

# Generate coverage reports
mvn clean test jacoco:report

# Run SonarQube analysis
mvn clean verify sonar:sonar

# Build Jar
mvn clean package

# Skip coverage checks (development)
mvn clean test -Pdev

# Run the application
java -cp ".:jackson-databind.jar:jackson-core.jar:jackson-annotations.jar"
 eoscbeyond.eu.GitHubMarkupScanner https://github.com/user/repo
```

### Example output

```shell
Scanning: CHANGELOG.md
  No matches found.

Scanning: CODE_OF_CONDUCT.md
  No matches found.

Scanning: CONTRIBUTING.md
  No matches found.

Scanning: CONTRIBUTORS.md
  No matches found.

Scanning: LICENSE.txt
  No matches found.

Scanning: README.md
  Line 3: [![SQAaaS badge]
  (https://github.com/EOSC-synergy/SQAaaS/raw/master/badges/badges_150x116/
  badge_software_silver.png)](https://api.eu.badgr.io/public/assertions/
  WwFLpKJ0SqqREmM0OZtzWw "SQAaaS silver badge achieved")
```

## Project Structure

This project uses the standard Maven project structure.

``` text

<ROOT>
├── .mvn                # Maven wrapper.
├── src                 # Contains all source code and assets for the application.
|   ├── main
|   |   ├── java        # Contains release source code of the application.
|   └── test
|       ├── java        # Contains test source code.
|       └── resources   # Contains test resource assets.
└── target              # The output directory for the build.
```

## Technology Stack

Several frameworks are used in this application.

| Framework/Technology                               | Description                                               |
| -------------------------------------------------- | --------------------------------------------------------- |
| [Jackson JSON Library](https://github.com/FasterXML/jackson.git) | Jackson is a suite of data-processing tools for Java, including the flagship streaming JSON parser |
| [junit-jupiter-api](https://github.com/junit-team/junit5 ) | Java unit testing framework |

## Resources

[Issue Tracker](https://github.com/john-shepherdson/eosc.github.markup-scanner?status=new&status=open)
