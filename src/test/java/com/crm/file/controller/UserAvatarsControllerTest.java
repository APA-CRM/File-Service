package com.crm.file.controller;

import com.crm.file.BaseIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UserAvatarsControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/users";

    @Test
    @DisplayName("Create user avatar expected success response")
    @SneakyThrows
    public void createUserAvatar_thenSuccess() {

        given()
                .contentType(ContentType.MULTIPART)
                .when()
                .header(USER_ID_HEADER_NAME, 1L)
                .multiPart("content", new ClassPathResource("image/validFile.png").getFile())
                .post(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .contentType("image/png")
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Create user avatar when wrong file extension expected bad request response")
    @SneakyThrows
    public void createUserAvatar_whenWrongFileExtension_thenBadRequest() {

        given()
                .contentType(ContentType.MULTIPART)
                .when()
                .header(USER_ID_HEADER_NAME, 1L)
                .multiPart("content", new ClassPathResource("image/invalidFile.txt").getFile())
                .post(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("Wrong file type: allowed only JPG\\JPEG and PNG"));
    }

    @Test
    @DisplayName("Create user avatar when file is not present expected bad request response")
    @SneakyThrows
    public void createUserAvatar_whenFileIsNotPresent_thenBadRequest() {

        given()
                .contentType(ContentType.MULTIPART)
                .when()
                .header(USER_ID_HEADER_NAME, 1L)
                .multiPart("content", new ClassPathResource("image/validFile.png").getFile())
                .post(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("Content can't be null"));
    }

    @Test
    @DisplayName("Create user avatar when wrong user expected forbidden response")
    @SneakyThrows
    public void createUserAvatar_thenWrongUser_thenSuccess() {

        given()
                .contentType(ContentType.MULTIPART)
                .when()
                .header(USER_ID_HEADER_NAME, 2L)
                .multiPart("content", new ClassPathResource("image/validFile.png").getFile())
                .post(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", is("You are not allowed to update another user's avatar"));
    }

    @Test
    @DisplayName("Get user avatar expected success response")
    @SneakyThrows
    public void getUserAvatar_thenSuccess() {

        given()
                .header(USER_ID_HEADER_NAME, 1L)
                .contentType(ContentType.MULTIPART)
                .when()
                .get(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .contentType("image/png")
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Get user avatar when not exists expected not found response")
    @SneakyThrows
    public void getUserAvatar_whenNotExists_thenNotFound() {

        given()
                .header(USER_ID_HEADER_NAME, 1L)
                .contentType(ContentType.MULTIPART)
                .when()
                .get(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is("Avatar for user is not found"));
    }

    @Test
    @DisplayName("Update user avatar expected success response")
    @SneakyThrows
    public void updateUserAvatar_thenSuccess() {

        given()
                .contentType(ContentType.MULTIPART)
                .when()
                .header(USER_ID_HEADER_NAME, 1L)
                .multiPart("content", new ClassPathResource("image/validFile.png").getFile())
                .put(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .contentType("image/png")
                .statusCode(HttpStatus.OK.value())
                .defaultParser(Parser.TEXT)
                .body("", notNullValue());
    }

    @Test
    @DisplayName("Update user avatar when wrong user expected forbidden response")
    @SneakyThrows
    public void updateUserAvatar_thenWrongUser_thenSuccess() {

        given()
                .contentType(ContentType.MULTIPART)
                .when()
                .header(USER_ID_HEADER_NAME, 2L)
                .multiPart("content", new ClassPathResource("image/validFile.png").getFile())
                .put(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .contentType("image/png")
                .body("message", is("You are not allowed to update another user's avatar"));
    }

    @Test
    @DisplayName("Update user avatar when wrong file extension expected bad request response")
    @SneakyThrows
    public void updateUserAvatar_whenWrongFileExtension_thenBadRequest() {

        given()
                .contentType(ContentType.MULTIPART)
                .when()
                .header(USER_ID_HEADER_NAME, 1L)
                .multiPart("content", new ClassPathResource("image/invalidFile.txt").getFile())
                .post(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("Wrong file type: allowed only JPG\\JPEG and PNG"));
    }

    @Test
    @DisplayName("Delete user avatar expected success response")
    @SneakyThrows
    public void deleteUserAvatar_thenSuccess() {

        given()
                .contentType(ContentType.MULTIPART)
                .when()
                .header(USER_ID_HEADER_NAME, 1L)
                .delete(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("Delete user avatar when wrong user expected forbidden response")
    @SneakyThrows
    public void deleteUserAvatar_thenWrongUser_thenSuccess() {

        given()
                .contentType(ContentType.MULTIPART)
                .when()
                .header(USER_ID_HEADER_NAME, 2L)
                .delete(BASE_URI + "/%d/avatars".formatted(1L))
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", is("You are not allowed to update another user's avatar"));
    }

}
