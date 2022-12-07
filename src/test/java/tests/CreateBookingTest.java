package tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pojos.request.Booking;
import pojos.response.BookingResponseDto;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;

import tests.helpers.BookingGenerator;
import tests.helpers.BookingGenerator;
import static tests.helpers.Constants.BOOKING_CONTROLLER_PATH;

@Slf4j
public class CreateBookingTest extends BaseTest {


    @Test
    public void createBooking() {
        Booking.BookingDates dates = new Booking.BookingDates(
                LocalDate.of(2022, 12, 12),
                LocalDate.of(2022, 12, 18));

        Booking request = new Booking();
        request.setAdditionalneeds("Breakfast");
        request.setFirstname("Vasya");
        request.setLastname("Ivanov");
        request.setTotalPrice(333);
        request.setDepositPaid(true);

        request.setBookingDates(dates);

        BookingResponseDto response = given(requestSpecification)
                .when().body(request)
                .when()
                .post(BOOKING_CONTROLLER_PATH)
                .then().statusCode(200)
                .log()
                .body()
                .extract()
                .as(BookingResponseDto.class);

        Assertions.assertTrue(response.getBookingId() != null, "При ответе не получено id бронирования");
        Assertions.assertEquals(response.getBooking(), request, "Параметры бронирования, полученные при создании отличаются от переданных параметров");
        Assertions.assertEquals(BookingGenerator.getBooking(requestSpecification, response.getBookingId()), response.getBooking(), "Полученное бронирование при get-запросе отличается от тела ответа, полученного при создании бронирования");
    }

    @Test
    public void createBookingWithInvalidBody() {
        Booking request = new Booking();
        request.setAdditionalneeds("Breakfast");
        request.setFirstname("Vasya");
        request.setLastname("Ivanov");
        request.setTotalPrice(333);
        request.setDepositPaid(true);

        requestSpecification
                .when().body(request)
                .when()
                .post(BOOKING_CONTROLLER_PATH)
                .then().statusCode(500)
                .log()
                .body()
                .extract()
                .asString()
                .equals("Internal Server Error");
    }
}





