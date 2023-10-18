package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto addUser(ItemDto itemDto, Long userId) {
        checkUser(userId);
        Item item = ItemMapper.toItem(itemDto, userId);
        return ItemMapper.toDto(itemRepository.addUser(item));
    }

    @Override
    public ItemDto updateUser(ItemDto itemDto, Long itemId, Long userId) {
        User user = checkUser(userId);
        return ItemMapper.toDto(itemRepository.updateUser(itemDto, itemId, user));

    }

    @Override
    public ItemDto getItem(Long itemId) {
        return ItemMapper.toDto(itemRepository.getItem(itemId));
    }

    @Override
    public List<ItemDto> getItems(Long itemId) {
        List<Item> items = itemRepository.getItems(itemId);
        List<ItemDto> itemDtos = new ArrayList<>();
        items.forEach(el -> itemDtos.add(ItemMapper.toDto(el)));
        return itemDtos;
    }

    @Override
    public List<ItemDto> getItemsSearch(String text) {
        List<Item> items = itemRepository.getItemsSearch(text);
        List<ItemDto> itemDtos = new ArrayList<>();
        items.forEach(el -> itemDtos.add(ItemMapper.toDto(el)));
        return itemDtos;
    }

    private User checkUser(Long userId) {
        User user = userRepository.getUser(userId);
        if (user == null) {
            throw new RuntimeException();
        }
        return user;
    }
}
