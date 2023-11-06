package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemBook;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingDtoOut {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private User booker;
    private ItemBook item;
    @NotNull
    private long bookerId;


}
