package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBooking(User booker, Item item, BookingDto bookingDto) {
        return new Booking(bookingDto.getStart(), bookingDto.getEnd(), item, booker, BookingStatus.WAITING);

    }

    public static BookingDtoOut toDto(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker(),
                ItemMapper.toItemBook(booking.getItem()), booking.getBooker().getId());
    }


}
