package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut postBooking(Long userId, BookingDto dto);

    BookingDtoOut updateBook(Long userId, Long bookingId, Boolean approved);

    BookingDtoOut getBooking(Long userId, Long bookingId);

    List<BookingDtoOut> getAllBooking(Long userId, String state);

    List<BookingDtoOut> findAllOwner(Long userId, String state);
}
