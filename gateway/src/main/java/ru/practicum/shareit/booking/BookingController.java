package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.dto.BookingState;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
	private final BookingClient bookingClient;

	@Autowired
	public BookingController(BookingClient bookingClient) {
		this.bookingClient = bookingClient;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ResponseEntity<Object> createBooking(
			@Positive @RequestHeader(value = "X-Sharer-User-Id") Long userId,
			@RequestBody @Valid BookingRequestDTO booking) {

		return bookingClient.createBooking(userId, booking);
	}

	@ResponseStatus(HttpStatus.OK)
	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(
			@Positive @RequestHeader(value = "X-Sharer-User-Id") Long userId,
			@Positive @PathVariable Long bookingId,
			@RequestParam("approved") Boolean approved
	) {
		return bookingClient.approveBooking(userId, bookingId, approved);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(
			@Positive @RequestHeader(value = "X-Sharer-User-Id") Long userId,
			@Positive @PathVariable Long bookingId
	) {
		return bookingClient.getBookingById(userId, bookingId);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public ResponseEntity<Object> getUserBookings(
			@Positive @RequestHeader(value = "X-Sharer-User-Id") Long userId,
			@RequestParam(name = "state", defaultValue = "ALL") BookingState state
	) {
		return bookingClient.getUserBookings(userId, state);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerBookings(
			@Positive @RequestHeader(value = "X-Sharer-User-Id") Long userId,
			@RequestParam(name = "state", defaultValue = "ALL") BookingState state
	) {
		return bookingClient.getOwnerBookings(userId, state);
	}
}
