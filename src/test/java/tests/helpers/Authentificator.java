package tests.helpers;

import io.restassured.specification.RequestSpecification;
import pojos.AuthDto;
import pojos.TokenDto;

public class Authentificator {

    public static TokenDto getToken(RequestSpecification specification, AuthDto authDto) {
        return specification
                .body(authDto)
                .post(Constants
                        .AUTH_CONTROLLER_PATH)
                .then()
                .statusCode(200)
                .log()
                .body()
                .extract()
                .as(TokenDto.class);
    }
}
