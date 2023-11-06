package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addUser(ItemDto itemDto, Long userId);

    ItemDto updateUser(ItemDto itemDto, Long itemId, Long userId);

    ItemDto getItem(Long itemId, Long userId);

    List<ItemDto> getItems(Long userId);

    List<ItemDto> getItemsSearch(String text);


    CommentDto createComment(long userId, CommentDto commentDto, long itemId);

}
