package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.codehaus.groovy.syntax.Token;
import org.junit.jupiter.api.BeforeEach;

import static tests.helpers.Constants.BASE_PATH;

public class BaseTest {
    public RequestSpecification requestSpecification = RestAssured.given();

    @BeforeEach
    public void createRequestSpecification() {
        requestSpecification.contentType(ContentType.JSON).baseUri(BASE_PATH).log().all();
    }

}
