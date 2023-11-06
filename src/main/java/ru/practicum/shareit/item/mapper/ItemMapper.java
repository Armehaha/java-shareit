package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBook;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public static Item toItem(ItemDto itemDto, User user) {
        return new Item(itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), user);
    }

    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static ItemDto itemDtoExtended(Item item, List<CommentDto> commentsByItemId) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getRequestId(), commentsByItemId);
    }

    public static ItemBook toItemBook(Item item) {
        return new ItemBook(
                item.getId(),
                item.getName());
    }

    public static ItemDto toItemWithBooking(Item item, List<Booking> itemBookings, List<Comment> itemComments, Long userId) {
        List<BookingDtoOut> bookingWithItemList = itemBookings.stream()
                .filter(booking -> BookingStatus.APPROVED.equals(booking.getStatus()))
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
        Optional<BookingDtoOut> lastBooking = bookingWithItemList.stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(BookingDtoOut::getStart));
        Optional<BookingDtoOut> nextBooking = bookingWithItemList.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(BookingDtoOut::getStart));
        List<CommentDto> itemCommentsDto = itemComments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            lastBooking = Optional.empty();
            nextBooking = Optional.empty();
        }
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), lastBooking.orElse(null), nextBooking.orElse(null), itemCommentsDto);
    }
}
