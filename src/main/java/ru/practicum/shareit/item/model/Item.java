package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
public class Item {

    private Long id;
    private String name;
    private String description;
    @NotNull
    private Boolean available;
    private Long owner;
    private Long requestId;

}
