package ru.practicum.shareit.booking.mappers;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDTO;

public class BookingMapper {

    public static Booking toBooking(BookingRequestDTO bookingRequestDTO, User user, Item item) {
        if (bookingRequestDTO == null) return new Booking();

        return new Booking(
                bookingRequestDTO.getId(),
                bookingRequestDTO.getStartTime(),
                bookingRequestDTO.getEndTime(),
                item,
                user,
                bookingRequestDTO.getStatus()
        );
    }


    public static BookingDTO toBookingDTO(Booking booking, ItemDTO itemDTO, UserDTO userDTO) {
        if (booking == null) return new BookingDTO();

        return new BookingDTO(
                booking.getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                itemDTO,
                userDTO,
                booking.getStatus()
        );
    }
}
