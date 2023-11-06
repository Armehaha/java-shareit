package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class CommentMapper {
    public static Comment toEntity(CommentDto commentDto, User author, Item item) {
        return new Comment(commentDto.getText(), item, author, commentDto.getCreated());

    }

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getAuthor()
                .getName(), ItemMapper.toDto(comment.getItem()), comment.getText(), comment.getCreated());

    }
}
