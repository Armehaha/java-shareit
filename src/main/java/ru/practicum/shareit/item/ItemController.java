package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.addUser(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateUser(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsSearch(@RequestParam(defaultValue = "null") String text) {
        return itemService.getItemsSearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody CommentDto commentDto, @PathVariable long itemId) {
        return itemService.createComment(userId, commentDto, itemId);
    }
}
