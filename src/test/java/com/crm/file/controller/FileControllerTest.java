package com.crm.file.controller;

import com.crm.file.BaseIntegrationTest;
import com.crm.file.enums.FileType;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_PERMISSIONS_HEADER_NAME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Sql(scripts = "classpath:sql/insertTestFile.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/deleteTestFile.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class FileControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/files";

    @Test
    @DisplayName("Get file expected success")
    public void getFileExpectedSuccess() {
        UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c1");

        given()
                .contentType(ContentType.JSON)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .get(BASE_URI + "/{fileId}", fileId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(fileId.toString()))
                .body("name", notNullValue())
                .body("fileType", is(FileType.DIRECTORY.name()))
                .body("fullName", notNullValue())
                .body("childrenFiles", hasSize(1))
                .body("fileExtension", nullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Get file content expected success")
    public void getFileContentExpectedSuccess() {
        UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c4");

        given()
                .contentType(ContentType.JSON)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .get(BASE_URI + "/{fileId}/content", fileId)
                .then()
                .log().all()
                .assertThat()
                .contentType("image/jpeg")
                .header("Content-Disposition", "attachment; filename=\"Employee`s_Photo.jpeg\"")
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Get file when not exists expected not found")
    public void getFileWhenNotExistsExpectedNotFound() {
        UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc023900");

        given()
                .contentType(ContentType.JSON)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .get(BASE_URI + "/{fileId}", fileId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is("File is not found"));
    }

    @Test
    @DisplayName("Create file when `FILE` type expected success")
    @SneakyThrows
    public void createFileWhenFileTypeExpectedSuccess() {
        UUID parentFileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c1");

        given()
                .contentType(ContentType.MULTIPART)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .multiPart("name", "Created file")
                .multiPart("fileType", FileType.FILE)
                .multiPart("parentFileId", parentFileId)
                .multiPart("content", new ClassPathResource("image/file.png").getFile())
                .post(BASE_URI)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", is("Created file"))
                .body("fullName", is("Created_file.png"))
                .body("fileType", is(FileType.FILE.name()))
                .body("fileExtension", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Create file when `FILE` type and content is not specified expected bad request")
    @SneakyThrows
    public void createFileWhenFileTypeAndContentNotSpecifiedExpectedBadRequest() {
        UUID parentFileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c1");

        given()
                .contentType(ContentType.MULTIPART)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .multiPart("name", "Created file")
                .multiPart("fileType", FileType.FILE)
                .multiPart("parentFileId", parentFileId)
                .post(BASE_URI)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("File must have a content"));
    }

    @Test
    @DisplayName("Create file when when parent has 'FILE' type expected conflict")
    @SneakyThrows
    public void createFileWhenParentHasFileTypeExpectedConflict() {
        UUID parentFileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c3");

        given()
                .contentType(ContentType.MULTIPART)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .multiPart("name", "Created file")
                .multiPart("fileType", FileType.FILE)
                .multiPart("parentFileId", parentFileId)
                .multiPart("content", new ClassPathResource("image/file.png").getFile())
                .post(BASE_URI)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is("Parent file has 'FILE' Type"));
    }

    @Test
    @DisplayName("Create file when `DIRECTORY` type expected success")
    @SneakyThrows
    public void createFileWhenDirectoryTypeExpectedSuccess() {
        UUID parentFileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c2");

        given()
                .contentType(ContentType.MULTIPART)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .multiPart("name", "Created directory")
                .multiPart("fileType", FileType.DIRECTORY)
                .multiPart("parentFileId", parentFileId)
                .post(BASE_URI)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", is("Created directory"))
                .body("fullName", is("Created_directory"))
                .body("fileType", is(FileType.DIRECTORY.name()))
                .body("fileExtension", nullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Create file when `DIRECTORY` type and parent not exists expected not found")
    @SneakyThrows
    public void createFileWhenDirectoryAndParentNotExistsTypeExpectedNotFound() {
        UUID parentFileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc023777");

        given()
                .contentType(ContentType.MULTIPART)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .multiPart("name", "Created directory")
                .multiPart("fileType", FileType.DIRECTORY)
                .multiPart("parentFileId", parentFileId)
                .post(BASE_URI)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is("Parent file is not found"));
    }

    @Test
    @DisplayName("Update file expected success")
    @SneakyThrows
    public void updateFileExpectedSuccess() {
        UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c2");

        given()
                .contentType(ContentType.MULTIPART)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .multiPart("name", "Update directory")
                .patch(BASE_URI + "/{fileId}", fileId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", is("Update directory"))
                .body("fileType", is(FileType.DIRECTORY.name()))
                .body("fullName", is("Update_directory"))
                .body("fileExtension", nullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Update file when 'File' type expected success")
    @SneakyThrows
    public void updateFileWhenFileTypeExpectedSuccess() {
        UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c3");

        given()
                .contentType(ContentType.MULTIPART)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .multiPart("name", "Update file")
                .multiPart("content", new ClassPathResource("image/file.png").getFile())
                .patch(BASE_URI + "/{fileId}", fileId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", is("Update file"))
                .body("fullName", is("Update_file.png"))
                .body("fileType", is(FileType.FILE.name()))
                .body("fileExtension", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Update file when content is not specified expected success")
    @SneakyThrows
    public void updateFileWhenContentIsNotSpecifiedExpectedSuccess() {
        UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c3");

        given()
                .contentType(ContentType.MULTIPART)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .multiPart("name", "Update file")
                .patch(BASE_URI + "/{fileId}", fileId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", is("Update file"))
                .body("fileType", is(FileType.FILE.name()))
                .body("fullName", is("Update_file.docx"))
                .body("fileExtension", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Delete file expected success")
    public void deleteFileExpectedSuccess() {
        UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c4");

        given()
                .contentType(ContentType.JSON)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .delete(BASE_URI + "/{fileId}", fileId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("Delete file with force delete expected success")
    public void deleteFileWithForceDeleteExpectedSuccess() {
        UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c1");

        given()
                .contentType(ContentType.JSON)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .queryParam("forceDelete", true)
                .delete(BASE_URI + "/{fileId}", fileId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("Delete file without force delete expected conflict")
    public void deleteFileWithoutForceDeleteExpectedConflict() {
        UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c1");

        given()
                .contentType(ContentType.JSON)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .delete(BASE_URI + "/{fileId}", fileId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is("Directory contains files"));
    }

}