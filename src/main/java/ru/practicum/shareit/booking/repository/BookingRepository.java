package ru.practicum.shareit.booking.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            " where " +
            "     b.item.id = :itemId " +
            "     and b.booker.id = :bookerId" +
            "     and b.status= :status" +
            "     and (b.end<:end)")
    List<Booking> findByItemIdAndBookerIdAndStatusAndStartIsBefore(Long itemId, Long bookerId, BookingStatus status, LocalDateTime end);

    @Modifying
    @Query("Update Booking b "
            + "set b.status = :status  "
            + "where b.id = :bookingId")
    void save(BookingStatus status, Long bookingId);

    //StateAllUser
    @Query("SELECT b FROM Booking b "
            + "WHERE b.booker.id = :bookerId "
            + "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    //StateCurrentUser
    @Query("SELECT b from Booking b " +
            "WHERE b.booker.id = :id " +
            "AND :time > b.start " +
            "AND :time < b.end ")
    List<Booking> findAllCurrentBookingsByUser(Long id, LocalDateTime time);

    //StatePastUser
    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime time);

    //StateFutureUser
    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime time);

    //StateWaitingAndRejectedUser
    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.owner.id = :ownerId "
            + "AND b.status = :status "
            + "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    //StateAllItem
    List<Booking> findAllByItemIdOrderByStartDesc(Long itemId);

    //StateCurrentItem
    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.owner.id = :ownerId "
            + "AND :time between b.start AND b.end "
            + "ORDER BY b.start DESC")
    List<Booking> findAllCurrentBookingsByItemOwner(Long ownerId, LocalDateTime time);

    //StatePastItem
    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.owner.id = :ownerId "
            + "AND b.end < :time "
            + "ORDER BY b.start DESC")
    List<Booking> findAllByItemIdInAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime time);

    //StateFutureItem
    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.owner.id = :ownerId "
            + "AND b.start > :time "
            + "ORDER BY b.start DESC")
    List<Booking> findAllByItemIdInAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime time);

    //StateWaitingAndRejectItem
    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.owner.id = :ownerId "
            + "AND b.start > :time AND b.status = :status "
            + "ORDER BY b.start DESC")
    List<Booking> findAllByItemIdInAndStatusOrderByStartDesc(Long ownerId, LocalDateTime time, BookingStatus status);

    @Query("SELECT b FROM Booking b "
            + "INNER JOIN Item i ON b.item.id = i.id "
            + "WHERE i.id = :itemId "
            + "ORDER BY b.start DESC")
    List<Booking> getBookingItem(Long itemId);
}
