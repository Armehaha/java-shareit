package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ItemDto addUser(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateUser(ItemDto itemDto, Long itemId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        Item item = ItemMapper.toItem(itemDto, user);
        item.setId(itemId);
        if (item.getName() == null || item.getName().isBlank()) {
            item.setName(oldItem.getName());
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item.setDescription(oldItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(oldItem.getAvailable());
        }
        return ItemMapper.toDto(itemRepository.save(item));

    }

    @Override
    public ItemDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        ItemDto itemDtoOut = ItemMapper.toDto(item);
        itemDtoOut.setComments(getCommentsByItemId(itemId));
        if (!item.getOwner()
                .getId()
                .equals(userId)) {
            return itemDtoOut;
        }
        List<Booking> bookings = bookingRepository.findAllByItemAndStatusOrderByStartAsc(item, BookingStatus.APPROVED);
        List<BookingDtoOut> bookingDTOList = bookings.stream()
                .map(BookingMapper::toDto)
                .collect(toList());

        itemDtoOut.setLastBooking(getLastBooking(bookingDTOList, LocalDateTime.now()));
        itemDtoOut.setNextBooking(getNextBooking(bookingDTOList, LocalDateTime.now()));
        return itemDtoOut;
    }

    public List<CommentDto> getCommentsByItemId(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);


        return comments.stream()
                .map(CommentMapper::toDto)
                .collect(toList());
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        List<Item> itemsOwner = itemRepository.findItemsByOwnerId(userId);
        List<Comment> itemsComments = commentRepository.findCommentsByItemIn(itemsOwner);
        List<Booking> itemsBookings = bookingRepository.findBookingsByItems(itemsOwner);
        List<ItemDto> itemWithBooking = new ArrayList<>();
        for (Item item : itemsOwner) {
            List<Comment> itemComments = itemsComments.stream()
                    .filter(comment -> comment.getItem().equals(item))
                    .collect(Collectors.toList());
            List<Booking> itemBookings = itemsBookings.stream()
                    .filter(booking -> booking.getItem().equals(item))
                    .collect(Collectors.toList());
            ItemDto itemDto = ItemMapper.toItemWithBooking(item,
                    itemBookings, itemComments, userId);

            itemWithBooking.add(itemDto);
        }

        return itemWithBooking;

    }

    @Override
    @Transactional
    public List<ItemDto> getItemsSearch(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text)
                .stream()
                .map(ItemMapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public CommentDto createComment(long userId, CommentDto commentDto, long itemId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<Booking> bookings = bookingRepository.findAllByUserIdAndItemIdAndEndDateIsPassed(userId, itemId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Comment entityComment = commentRepository.save(CommentMapper.toEntity(commentDto, user, item));
        return CommentMapper.toDto(entityComment);
    }

    private BookingDtoOut getLastBooking(List<BookingDtoOut> bookings, LocalDateTime time) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings.stream()
                .filter(bookingDTO -> !bookingDTO.getStart()
                        .isAfter(time))
                .reduce((booking1, booking2) -> booking1.getStart()
                        .isAfter(booking2.getStart()) ? booking1 : booking2)
                .orElse(null);
    }

    private BookingDtoOut getNextBooking(List<BookingDtoOut> bookings, LocalDateTime time) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings.stream()
                .filter(bookingDTO -> bookingDTO.getStart()
                        .isAfter(time))
                .findFirst()
                .orElse(null);
    }

}
