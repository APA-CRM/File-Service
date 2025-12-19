# APA-CRM — File-Service

File-Service is a Spring Boot microservice within the APA-CRM platform that provides file management capabilities: storing file metadata, file content, directory structure, and exposing REST endpoints for upload, download, update, and deletion. It also exposes internal endpoints used by other services to create default directories.

---

## Table of contents

- Project summary
- Technology stack
- Configuration & environment
- Authentication & integration
- API summary (public & internal)
- Testing
- Pull Requests
- Issues
- Code of Conduct
- License
- Contact & Support

---

## Project summary

File-Service implements a simple file metadata + content model. Files may be either FILE or DIRECTORY; file contents are stored in a FileContent entity (one-to-one with metadata) and directories can have children. The service validates file size, determines file extension/mime type, supports hierarchical directories, and provides an internal API for other services to create default directories.

This repository is a Java 21 Spring Boot application using Spring Data JPA for persistence, MapStruct for mapping, and standard Spring Web controllers for the REST API.

---

## Technology stack

- Java 21
- Spring Boot 3.x (parent POM)
- Spring Data JPA (persistence)
- Spring Web (REST controllers)
- MapStruct for DTO/entity mapping
- Lombok
- PostgreSQL driver (runtime)
- H2 for tests
- Build: Maven (mvnw wrapper included)
- Container: Docker (Dockerfile present)
- 
---

## Configuration & environment

File-Service reads configuration from standard Spring Boot sources (application.yml/properties, environment variables). Example environment properties typically required for deploying this service:

- DISCOVERY_HOST
- DISCOVERY_PORT
- FILE_DATASOURCE_HOST
- FILE_DATASOURCE_PORT
- FILE_DATASOURCE_DATABASE
- FILE_DATASOURCE_USERNAME
- FILE_DATASOURCE_PASSWORD
- RABBIT_HOST
- RABBIT_PORT
- RABBIT_USERNAME
- RABBIT_PASSWORD

See [application.properties](https://github.com/APA-CRM/File-Service/blob/develop/src/main/resources/application.properties)

---

## Authentication & integration

- File-Service expects to run in the platform where requests are authorized by the Gateway or an Auth-Service. There is no direct AuthClient in this service, but the service is designed to be used behind the platform gateway that forwards authenticated requests.
- The service exposes internal endpoints under `/api/internal/**` for other services to call (for example, creating a default directory).

---

## API summary

Public (example):
- GET /api/files/{fileId} — get metadata with children
- GET /api/files/{fileId}/content — download file content (returns attachment with correct content-type)
- POST /api/files — create file or directory (multipart/form-data for file content)
- PATCH /api/files/{fileId} — update name/parent/content
- DELETE /api/files/{fileId}?forceDelete=false — delete a file or directory (use forceDelete to remove non-empty directories)

Internal:
- POST /api/internal/files/default — create a default directory (used by other services)

See controller sources for exact request/response DTOs:
- CreateFileRequest — https://github.com/APA-CRM/File-Service/blob/develop/src/main/java/com/crm/file/dto/request/CreateFileRequest.java
- UpdateFileRequest — https://github.com/APA-CRM/File-Service/blob/develop/src/main/java/com/crm/file/dto/request/UpdateFileRequest.java
- FileResponse / FileWithChildrenResponse — https://github.com/APA-CRM/File-Service/blob/develop/src/main/java/com/crm/file/dto/response/FileResponse.java

Domain model highlights:
- FileMetadata (metadata, parent-child relationships) — https://github.com/APA-CRM/File-Service/blob/develop/src/main/java/com/crm/file/persistance/entity/FileMetadata.java
- FileContent (binary content stored as LOB) — https://github.com/APA-CRM/File-Service/blob/develop/src/main/java/com/crm/file/persistance/entity/FileContent.java
- FileService (business logic: create, update, delete, content handling) — https://github.com/APA-CRM/File-Service/blob/develop/src/main/java/com/crm/file/service/FileService.java

Validation:
- File size constraint annotation and validator — https://github.com/APA-CRM/File-Service/blob/develop/src/main/java/com/crm/file/constraint/FileSizeConstraint.java

---

## Testing

- Unit and integration tests are present and can be executed with:
  ./mvnw test
- The POM includes spring-boot-starter-test and H2 for test scope. Integration tests that touch DB or expect other services may require test containers or running dependent services locally.

---

## Pull Requests

- Open a Pull Request (PR) against the `develop` branch.
- Use a feature branch named like `feature/your-description`.
- Include tests for new behavior and ensure CI passes.
- Provide a clear and descriptive PR description and link to related issues.

---

## Issues

- Use GitHub Issues for bugs and feature requests: https://github.com/APA-CRM/File-Service/issues
- Include reproduction steps, expected vs actual behavior, logs, and environment details.

---

## Code of Conduct

- Be respectful and constructive in issues, PRs, and discussions.
- Follow the Contributor Covenant where applicable.

---

## License

This project is licensed under the terms found in the LICENSE file:
https://github.com/APA-CRM/File-Service/blob/develop/LICENSE

---

## Contact & Support

For help or questions, open an issue in this repository or contact the maintainers via the APA-CRM organization on GitHub.

---
