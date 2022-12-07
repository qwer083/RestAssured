package tests.helpers;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import io.restassured.specification.RequestSpecification;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import pojos.request.Booking;
import pojos.response.BookingResponseDto;

public class BookingGenerator {
    private static final Faker faker = new Faker();

    public static BookingResponseDto createBooking(RequestSpecification specification, Booking.BookingDates bookingDates) {
        Booking request = new Booking();
        request.setBookingDates(bookingDates);
        request.setFirstname(faker.name().firstName());
        request.setLastname(faker.name().lastName());
        request.setAdditionalneeds(faker.internet().emailAddress() + faker.internet().ipV4Address());
        request.setTotalPrice(faker.random().nextLong(1, 9999));
        request.setDepositPaid(true);

        BookingResponseDto responseDto =
                given(specification)
                        .body(request)
                        .when()
                        .post(Constants.BOOKING_CONTROLLER_PATH)
                        .then()
                        .statusCode(200)
                        .log()
                        .body()
                        .extract()
                        .as(BookingResponseDto.class);

        Assertions.assertEquals(responseDto.getBooking(), request,
                String.format("Параметры сгенерированной сущности отличаются от переданных." +
                        " Сгенерировано: %s \n Получено в ответе: %s", request.toString(), responseDto.getBooking().toString()));
        Assertions.assertNotNull(responseDto.getBookingId(), "При генерации бронирования id не был сгенерирован");
        return responseDto;
    }

    public static Booking getBooking(RequestSpecification specification, Long bookingId) {
        Booking responseDto = specification
                .when()
                .get(Constants.BOOKING_CONTROLLER_PATH + "/" + bookingId)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Booking.class);
        return responseDto;
    }
}
