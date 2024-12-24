package ru.practicum.shareit.booking.client;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.client.BaseClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }

    public ResponseEntity<Object> getListAllBookingsUser(Long userId, String state, @PositiveOrZero Integer page, @PositiveOrZero Integer pageSize) {
        Map<String, Object> var = Map.of("state", state, "page", page, "pageSize", pageSize);
        return get("?state={state}&page={page}&pageSize={pageSize}", userId, var);
    }

    public ResponseEntity<Object> getBookingInfo(Long userId, Long bookingId) {
        Map<String, Object> var = Map.of("bookingId", bookingId);
        return get("/{bookingId}", userId, var);
    }

    public ResponseEntity<Object> getListBookingsAllItems(Long ownerId, String state) {
        Map<String, Object> var = Map.of("state", state);
        return get("/owner/?state={state}", ownerId, var);
    }

    public ResponseEntity<Object> createBooking(Long userId, BookingItemDto bookingItemDto) {
        return post("", userId, bookingItemDto);
    }

    public ResponseEntity<Object> responseBooking(Long bookingId, Long userId, Boolean approved) {
        Map<String, Object> var = Map.of("bookingId",bookingId,"approved", approved);
        return patch("/{bookingId}?approved={approved}", userId, var);
    }
}
