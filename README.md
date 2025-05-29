# EOSC GitHub Markup Scanner

This repository contains the source code for the GitHub Markup Scanner
which searches a GitHub repository, looking for Badges.
The program handles errors gracefully and provides informative messages
for missing repositories or network issues.

Key Features:

1. Accepts a GitHub repository URL as a command-line argument
2. Uses the GitHub API to fetch repository contents from the root directory
3. Identifies markup files by common extensions (.md, .rst, .txt, .asciidoc, etc.)
4. Downloads and scans each markup file line by line
5. Prints any lines containing 'api.eu.badgr.io' with line numbers
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

# Build with specific profile
mvn clean package -Pci

# Skip coverage checks (development)
mvn clean test -Pdev

# Compile the application
javac -cp ".:jackson-databind.jar:jackson-core.jar:jackson-annotations.jar" GitHubMarkupScanner.java

# Run the application
java -cp ".:jackson-databind.jar:jackson-core.jar:jackson-annotations.jar" GitHubMarkupScanner https://github.com/user/repo
```

### Example output

```shell
Scanning 3 markup files for 'api.eu.badgr.io':

Scanning: README.md
  Line 15: Visit our API at https://api.eu.badgr.io/docs for documentation
  Line 23: Configure endpoint: api.eu.badgr.io/v2/users

Scanning: INSTALL.md
  No matches found.
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

## Resources

[Issue Tracker](https://github.com/john-shepherdson/eosc.github.markup-scanner?status=new&status=open)
