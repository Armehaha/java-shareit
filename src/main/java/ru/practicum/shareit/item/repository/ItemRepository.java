package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {
    Item addUser(Item item);

    Item updateUser(ItemDto item, Long itemId, User user);

    Item getItem(Long itemId);

    List<Item> getItems(Long itemId);

    List<Item> getItemsSearch(String text);
}
