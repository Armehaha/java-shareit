package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    @Autowired
    private final BookingService service;

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBooking(@RequestHeader("X-Sharer-User-id") Long userId, @PathVariable("bookingId") Long bookingId) {
        return service.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> findAll(@RequestHeader("X-Sharer-User-id") Long userId, @RequestParam(value = "state", defaultValue = "ALL") String bookingState) {
        return service.getAllBooking(userId, bookingState);
    }

    @PostMapping
    public BookingDtoOut postBooking(@RequestHeader("X-Sharer-User-id") Long userId, @Valid @RequestBody BookingDto bookingDto) {
        return service.postBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateBook(@RequestHeader("X-Sharer-User-id") Long userId, @PathVariable("bookingId") Long bookingId, @RequestParam(name = "approved") Boolean approved) {
        return service.updateBook(userId, bookingId, approved);
    }


    @GetMapping("/owner")
    public List<BookingDtoOut> getAllOwner(@RequestHeader("X-Sharer-User-id") long ownerId, @RequestParam(value = "state", defaultValue = "ALL") String bookingState) {
        return service.findAllOwner(ownerId, bookingState);
    }
}
