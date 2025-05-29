# Changelog

All notable changes to *EOSC Node Registry (demo)* will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

*For each release, use the following sub-sections:*

- *Added (for new features)*
- *Changed (for changes in existing functionality)*
- *Deprecated (for soon-to-be removed features)*
- *Removed (for now removed features)*
- *Fixed (for any bug fixes)*
- *Security (in case of vulnerabilities)*

## [1.3.2] 2025-03-05

### Changed

- Final clean ups, based on Checkstyle (Sun configuration) recommendations. Hopefully.

## [1.3.1] 2025-03-03

### Changed

- More clean ups, based on Checkstyle (Sun configuration) recommendations

### Fixed

- Used Singleton pattern to create sole instance of NodeRegistry
- Moved Jenkinsfile to top level of directory structure

## [1.3.0] 2025-02-28

### Fixed

- Issue swith incorrect version of Java in build file was resolved

## [1.2.0] 2025-02-26

### Changed

- Added Git workflow and associated files to lint the Markdown files
- More clean ups, based on SonarQube recommendations

### Fixed

- Issue with Docker container not finding CSV file containing node details has
been resolved

## [1.1.0] 2025-02-25

### Changed

- README file now contains SQAAaaS badge
- Docker file ensures jar file can be found and run
- General clean up, based on SonarQube recommendations
- Updated POM to support Jar packaging

## [1.0.0] 2025-02-24

First release.

Bug in Docker packaging means the CSV file containing the Node details is not found.
