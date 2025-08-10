package com.crm.file.controller.internal;

import com.crm.file.BaseIntegrationTest;
import com.crm.file.dto.request.internal.CreateDefaultFileRequest;
import com.crm.file.enums.FileType;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class InternalFileControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/internal/files";

    @Test
    @DisplayName("Create default directory expected success")
    public void createDefaultDirectoryExpectedSuccess() {

        CreateDefaultFileRequest request = new CreateDefaultFileRequest("Root");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(BASE_URI + "/default")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", is(request.getName()))
                .body("fileType", is(FileType.DIRECTORY.name()))
                .body("fileExtension", nullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());

    }

}