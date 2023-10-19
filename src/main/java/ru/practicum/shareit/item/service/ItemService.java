package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addUser(ItemDto itemDto, Long userId);

    ItemDto updateUser(ItemDto itemDto, Long itemId, Long userId);

    ItemDto getItem(Long itemId);

    List<ItemDto> getItems(Long itemId);

    List<ItemDto> getItemsSearch(String text);
}
