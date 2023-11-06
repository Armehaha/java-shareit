package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.UnknownStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public BookingDtoOut postBooking(Long userId, BookingDto dto) {
        User user = checkUser(userId);
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        allValid(dto, user, item);
        Booking booking = BookingMapper.toBooking(user, item, dto);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoOut updateBook(Long userId, Long bookingId, Boolean approved) {
        Booking booking = valid(userId, bookingId, "update");
        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        Objects.requireNonNull(booking)
                .setStatus(newStatus);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoOut getBooking(Long userId, Long bookingId) {
        Booking booking = valid(userId, bookingId, "findDetails");
        return BookingMapper.toDto(Objects.requireNonNull(booking));
    }


    @Override
    @Transactional
    public List<BookingDtoOut> findAllOwner(Long userId, String state) {
        checkUser(userId);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByItem_Owner_Id(userId, Sort.by(Sort.Direction.DESC, "start"));
                break;
            case "PAST":
                bookings = bookingRepository.findAllByItem_Owner_IdAndEndBefore(userId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByItem_Owner_IdAndEndAfter(userId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByItem_Owner_IdAndCurrentDate(userId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatus(userId, BookingStatus.WAITING, Sort.by(Sort.Direction.DESC, "start"));
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatus(userId, BookingStatus.REJECTED, Sort.by(Sort.Direction.DESC, "start"));
                break;
            default:
                throw new UnknownStateException("Unknown state: " + state);
        }
        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoOut> getAllBooking(Long userId, String state) {
        checkUser(userId);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByBookerId(userId, Sort.by(Sort.Direction.DESC, "start"));
                break;
            case "PAST":
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByBookerIdAndEndAfter(userId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByBookerIdAndCurrentDate(userId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING, Sort.by(Sort.Direction.DESC, "start"));
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED, Sort.by(Sort.Direction.DESC, "start"));
                break;
            default:
                throw new UnknownStateException("Unknown state: " + state);
        }
        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void allValid(BookingDto dto, User user, Item item) {
        if (!item.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (user.getId()
                .equals(item.getOwner()
                        .getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (dto.getEnd().isBefore(dto.getStart()) ||
                dto.getEnd().isEqual(dto.getStart()) || !item.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (dto.getStart()
                .isAfter(dto.getEnd()) || dto.getStart()
                .isEqual(dto.getEnd())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private Booking valid(long userId, long bookingId, String operation) {
        checkUser(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        switch (operation) {
            case "update":
                if (!booking.getItem()
                        .getOwner()
                        .getId()
                        .equals(userId)) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
                if (!booking.getStatus()
                        .equals(BookingStatus.WAITING)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                return booking;
            case "findDetails":
                if (!booking.getBooker().getId().equals(userId)
                        && !booking.getItem().getOwner().getId().equals(userId)) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
                return booking;
            default:
                return null;
        }
    }
}
