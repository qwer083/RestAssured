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
    public void getToken() {
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
        Assertions.assertEquals(15, token.getToken().length(),"Длина токена не соответствует требованиям");

    }
}