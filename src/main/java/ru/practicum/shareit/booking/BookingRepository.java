package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.booker.id = :bookerId AND b.end <= :currentTime")
    List<Booking> findAllByUserIdAndItemIdAndEndDateIsPassed(Long bookerId, Long itemId, LocalDateTime currentTime);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.item IN ?1")
    List<Booking> findBookingsByItems(List<Item> items);

    List<Booking> findAllByBookerId(Long bookerId, Sort sort);

    List<Booking> findAllByItem_Owner_IdAndStatus(Long ownerId, BookingStatus status, Sort sort);

    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime currentTime, Sort sort);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start < :currentTime AND b.end > :currentTime")
    List<Booking> findAllByBookerIdAndCurrentDate(Long bookerId, LocalDateTime currentTime, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findAllByItem_Owner_Id(Long ownerId, Sort sort);


    List<Booking> findAllByItemAndStatusOrderByStartAsc(Item item, BookingStatus bookingStatus);

    List<Booking> findAllByBookerIdAndEndAfter(Long bookerId, LocalDateTime currentTime, Sort sort);

    List<Booking> findAllByItem_Owner_IdAndEndAfter(Long ownerId, LocalDateTime currentTime, Sort sort);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start < :currentTime AND b.end > :currentTime")
    List<Booking> findAllByItem_Owner_IdAndCurrentDate(Long ownerId, LocalDateTime currentTime, Sort sort);

    List<Booking> findAllByItem_Owner_IdAndEndBefore(Long ownerId, LocalDateTime currentTime, Sort sort);

}
