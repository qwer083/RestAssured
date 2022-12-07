package tests;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pojos.request.Booking;
import pojos.response.BookingResponseDto;
import tests.helpers.BookingGenerator;
import tests.helpers.BookingGenerator;
import tests.helpers.Constants;

public class GetBookingTests extends BaseTest {
    @Test
    void getBooking() {
        Booking.BookingDates dates = new Booking.BookingDates(
                LocalDate.of(2022, 12, 12),
                LocalDate.of(2022, 12, 18));
        BookingResponseDto responseDto = BookingGenerator.createBooking(requestSpecification, dates);
        Booking booking =
                requestSpecification
                        .when()
                        .get(Constants.BOOKING_CONTROLLER_PATH + "/" + responseDto.getBookingId())
                        .then()
                        .statusCode(200)
                        .log()
                        .body()
                        .extract()
                        .as(Booking.class);
        Assertions.assertEquals(responseDto.getBooking(), booking, "Полученное бронирование отличается от созданного");
    }

    @Test
    void getBookingWithNotExistingId() {
        requestSpecification
                .when()
                .get(Constants.BOOKING_CONTROLLER_PATH + "/" + UUID.randomUUID().getMostSignificantBits())
                .then()
                .statusCode(404)
                .log()
                .body()
                .extract()
                .asString()
                .equals("Not found");
    }
}
