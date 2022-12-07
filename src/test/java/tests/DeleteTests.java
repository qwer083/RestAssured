package tests;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import pojos.AuthDto;
import pojos.TokenDto;
import pojos.request.Booking;
import tests.helpers.Authentificator;
import tests.helpers.BookingGenerator;
import tests.helpers.BookingGenerator;
import tests.helpers.Constants;

public class DeleteTests extends BaseTest {
    @Test
    void deleteBooking() {
        Booking.BookingDates bookingDates = new Booking.BookingDates(
                LocalDate.now(),
                LocalDate.now().plusDays(222));
        Long bookingId = BookingGenerator.createBooking(requestSpecification, bookingDates).getBookingId();
        TokenDto tokenDto = Authentificator.getToken(requestSpecification, new AuthDto(Constants.PASSWORD, Constants.USERNAME));
        requestSpecification
                .cookie("token", tokenDto.getToken())
                .when()
                .delete(Constants.BOOKING_CONTROLLER_PATH + "/" + bookingId)
                .then()
                .statusCode(201)
                .log()
                .body()
                .toString()
                .equals("Created");

    }
    @Test
    void deleteBookingWithNotExistingId() {
        TokenDto tokenDto = Authentificator.getToken(requestSpecification, new AuthDto(Constants.PASSWORD, Constants.USERNAME));
        requestSpecification
                .cookie("token", tokenDto.getToken())
                .when()
                .delete(Constants.BOOKING_CONTROLLER_PATH + "/" + UUID.randomUUID().getMostSignificantBits())
                .then()
                .statusCode(404)
                .log()
                .body()
                .toString()
                .equals("Not Found");
    }
}
