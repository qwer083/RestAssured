package pojos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pojos.request.Booking;

@Data
public class BookingResponseDto {
    @JsonProperty("bookingid")
    private Long bookingId;
    private Booking booking;
}