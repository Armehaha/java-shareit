package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "booking")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @FutureOrPresent
    @Column(name = "start_date")
    private LocalDateTime start;
    @FutureOrPresent
    @Column(name = "end_date")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    public Booking(LocalDateTime start, LocalDateTime end, Item item, User booker, BookingStatus bookingStatus) {
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = bookingStatus;
    }
}
