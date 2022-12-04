package tests;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pojos.AuthDto;
import pojos.TokenDto;
import pojos.request.Booking;
import pojos.response.BookingResponseDto;
import tests.helpers.Authentificator;
import tests.helpers.BookingGenerator;
import tests.helpers.Constants;

import java.time.LocalDate;
import java.util.Map;

public class UpdateBookingTest extends BaseTest {

    @Test
    void PartialUpdate() {
        Booking.BookingDates bookingDates = new Booking.BookingDates(LocalDate.now().plusDays(2), LocalDate.now().plusDays(30));
        BookingResponseDto generateBooking = BookingGenerator.createBooking(requestSpecification, bookingDates);
        String bookingId = generateBooking.getBookingId().toString();
        Booking bookingBeforeChange = generateBooking.getBooking();

        String newLastName = "newLastName";
        Long newTotalPrice = bookingBeforeChange.getTotalPrice() + 10;
        Map<String, Object> jsonString = Map.of("lastname", newLastName, "totalprice", newTotalPrice);
        TokenDto tokenDto = Authentificator.getToken(requestSpecification, new AuthDto(Constants.PASSWORD, Constants.USERNAME));

        Booking bookingAfterChange = requestSpecification
                .cookie("token", tokenDto.getToken())
                .when()
                .body(jsonString)
                .patch(Constants.BOOKING_CONTROLLER_PATH + "/" + bookingId)
                .then()
                .statusCode(200)
                .log()
                .body()
                .extract()
                .as(Booking.class);

        Assertions.assertEquals(bookingBeforeChange.getBookingDates(), bookingAfterChange.getBookingDates(), "Даты бронирования не должны были измениться");
        Assertions.assertEquals(bookingBeforeChange.getFirstname(), bookingAfterChange.getFirstname(), "Имя пользователя не должно было измениться");
        Assertions.assertEquals(bookingBeforeChange.getAdditionalneeds(), bookingAfterChange.getAdditionalneeds(), "Комментарий не должен был изменитья");
        Assertions.assertEquals(bookingBeforeChange.isDepositPaid(), bookingAfterChange.isDepositPaid(), "Флаг оплаты депозита не должен был изменитья");
        Assertions.assertEquals(newTotalPrice, bookingAfterChange.getTotalPrice(), "Оплаченная стоимость не поменялась");
        Assertions.assertEquals(newLastName, bookingAfterChange.getLastname(), "Фамилия пользователя не поменялась");


    }

    @Test
    void fullUpdate() {
        Booking.BookingDates bookingDates = new Booking.BookingDates(LocalDate.now().plusDays(2), LocalDate.now().plusDays(30));
        BookingResponseDto generateBooking = BookingGenerator.createBooking(requestSpecification, bookingDates);
        String bookingId = generateBooking.getBookingId().toString();


        Booking bookingBeforeChange = generateBooking.getBooking();
        boolean newDepostPaid = false;
        LocalDate newCheckIn = bookingBeforeChange.getBookingDates().getCheckIn().plusDays(40);

        Booking bookingForRequest = ObjectUtils.cloneIfPossible(bookingBeforeChange);
        bookingForRequest.setDepositPaid(newDepostPaid);
        bookingForRequest.getBookingDates().setCheckIn(newCheckIn);

        TokenDto tokenDto = Authentificator.getToken(requestSpecification, new AuthDto(Constants.PASSWORD, Constants.USERNAME));

        Booking bookingAfterChange = requestSpecification
                .cookie("token", tokenDto.getToken())
                .when()
                .body(bookingForRequest)
                .patch(Constants.BOOKING_CONTROLLER_PATH + "/" + bookingId)
                .then()
                .statusCode(200)
                .log()
                .body()
                .extract()
                .as(Booking.class);

        Assertions.assertEquals(bookingBeforeChange.getFirstname(), bookingAfterChange.getFirstname(),"Имя пользователя не должно было измениться");
        Assertions.assertEquals(bookingBeforeChange.getLastname(), bookingAfterChange.getLastname(),"Фамилия пользователя не должна было измениться");
        Assertions.assertEquals(bookingBeforeChange.getAdditionalneeds(), bookingAfterChange.getAdditionalneeds(),"Комментарий не должен был измениться");
        Assertions.assertEquals(bookingBeforeChange.getBookingDates().getCheckOut(), bookingAfterChange.getBookingDates().getCheckOut(),"Дата выезда не дожна была измениться");
        Assertions.assertEquals(bookingBeforeChange.getTotalPrice(), bookingAfterChange.getTotalPrice(),"Флаг оплаты депозита не изменился");
        Assertions.assertEquals(bookingAfterChange.getBookingDates().getCheckIn(), newCheckIn,"Дата заезда не изменилась");

    }
}
