package tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pojos.AuthDto;
import pojos.TokenDto;

import static io.restassured.RestAssured.given;
import static tests.helpers.Constants.*;

@Slf4j
public class AuthTest extends BaseTest {
    @Test
    public void authentificate() {
        TokenDto token = requestSpecification
                .body(new AuthDto(PASSWORD, USERNAME))
                .log().all()
                .when()
                .post(AUTH_CONTROLLER_PATH)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDto.class);
        log.info(token.getToken());
        Assertions.assertEquals(15, token.getToken().length(), "Длина токена не соответствует требованиям");
    }

    @Test
    public void authentificateInvalidPassword() {
        requestSpecification
                .body(new AuthDto("5454", USERNAME))
                .log().all()
                .when()
                .post(AUTH_CONTROLLER_PATH)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString()
                .equals("Bad credentials");

    }

    @Test
    public void authentificateInvalidUsername() {
        requestSpecification
                .body(new AuthDto(PASSWORD, "INVALID"))
                .log().all()
                .when()
                .post(AUTH_CONTROLLER_PATH)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString()
                .equals("Bad credentials");

    }
}