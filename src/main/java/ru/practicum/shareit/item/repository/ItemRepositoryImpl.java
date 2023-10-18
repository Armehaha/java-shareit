package ru.practicum.shareit.item.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> itemMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item addUser(Item item) {
        item.setId(++id);
        itemMap.put(id, item);
        return item;
    }

    @Override
    public Item updateUser(ItemDto item, Long itemId, User user) {
        Item oldItem = itemMap.get(itemId);
        System.out.println(oldItem);
        System.out.println("---------------" + user);
        if (oldItem != null && Objects.equals(user.getId(), oldItem.getOwner())) {
            if (item.getName() != null && !item.getName().equals(oldItem.getName())) {
                oldItem.setName(item.getName());
            }
            if (item.getDescription() != null && !item.getDescription().equals(oldItem.getDescription())) {
                oldItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null && item.getAvailable() != oldItem.getAvailable()) {
                oldItem.setAvailable(item.getAvailable());
            }
            return oldItem;

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Item getItem(Long itemId) {
        Item item = itemMap.get(itemId);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return item;
    }

    @Override
    public List<Item> getItems(Long itemId) {
        return itemMap.values().stream().filter(el -> Objects.equals(el.getOwner(), itemId)).collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsSearch(String text) {
        if (text == null) {
            return new ArrayList<>();
        }
        List<Item> items = new ArrayList<>();
        for (Item i : itemMap.values()) {
            if (i.getAvailable() && (i.getName().toLowerCase().contains(text.toLowerCase()) ||
                    i.getDescription().toLowerCase().contains(text.toLowerCase()))) {
                items.add(i);
            }
        }
        return items;
    }
}
