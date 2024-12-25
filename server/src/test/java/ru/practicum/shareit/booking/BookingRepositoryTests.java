package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTests {

    private final EntityManager entityManager;
    private final BookingRepository bookingRepository;

    @Test
    void findByItemIdAndBookerIdAndStatusAndEndIsBefore() {

        var itemId = 10L;
        var bookerId = 20L;
        var status = BookingStatus.APPROVED;
        var end = LocalDateTime.now();

        var bookings = bookingRepository.findByItemIdAndBookerIdAndStatusAndEndIsBefore(itemId, bookerId, status, end);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b FROM Booking b " +
                        " where " +
                        "     b.item.id = :itemId " +
                        "     and b.booker.id = :bookerId" +
                        "     and b.status= :status" +
                        "     and b.end<:end", Booking.class);
        var sut = query
                .setParameter("itemId", itemId)
                .setParameter("bookerId", bookerId)
                .setParameter("status", status)
                .setParameter("end", end)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void updateBooking() {

        var bookingId = 20L;
        var status = BookingStatus.CANCELED;
        bookingRepository.update(status, bookingId);

        TypedQuery<Booking> query =
                entityManager.createQuery("FROM Booking WHERE id = :id", Booking.class);
        Booking sut = query.setParameter("id", bookingId).getSingleResult();

        assertEquals(status, sut.getStatus());
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        Integer offset = 0;
        Integer limit = 10;
        Pageable pageable = PageRequest.of(offset, limit);
        var bookerId = 10L;
        var bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId, pageable);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b FROM Booking b "
                        + "WHERE b.booker.id = :bookerId "
                                + "ORDER BY b.start DESC", Booking.class)
                        .setMaxResults(limit)
                        .setFirstResult(offset);

        var sut = query
                .setParameter("bookerId", bookerId)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void findAllCurrentBookingsByUser() {
        Integer offset = 0;
        Integer limit = 10;
        Pageable pageable = PageRequest.of(offset, limit);
        var id = 10L;
        var time = LocalDateTime.now();
        var bookings = bookingRepository.findAllCurrentBookingsByUser(id, time, pageable);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b from Booking b " +
                        "WHERE b.booker.id = :id " +
                        "AND :time > b.start " +
                        "AND :time < b.end ", Booking.class)
                        .setMaxResults(limit)
                        .setFirstResult(offset);

        var sut = query
                .setParameter("id", id)
                .setParameter("time", time)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDesc() {
        Integer offset = 0;
        Integer limit = 10;
        Pageable pageable = PageRequest.of(offset, limit);
        var id = 10L;
        var time = LocalDateTime.now();
        var bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(id, time, pageable);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b from Booking b " +
                        "WHERE b.booker.id = :id " +
                        "AND :time > b.start " +
                        "AND :time < b.end ", Booking.class)
                        .setMaxResults(limit)
                        .setFirstResult(offset);

        var sut = query
                .setParameter("id", id)
                .setParameter("time", time)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void findAllByBookerIdAndStartAfterOrderByStartDesc() {
        Integer offset = 0;
        Integer limit = 10;
        Pageable pageable = PageRequest.of(offset, limit);
        var id = 10L;
        var time = LocalDateTime.now();
        var bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(id, time, pageable);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b from Booking b " +
                        "WHERE b.booker.id = :id " +
                        "AND :time > b.start " +
                        "AND :time < b.end ", Booking.class)
                        .setMaxResults(limit)
                        .setFirstResult(offset);

        var sut = query
                .setParameter("id", id)
                .setParameter("time", time)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
        Integer offset = 0;
        Integer limit = 10;
        Pageable pageable = PageRequest.of(offset, limit);
        var ownerId = 10L;
        var status = BookingStatus.APPROVED;
        var bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(ownerId, status, pageable);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b FROM Booking b "
                        + "INNER JOIN Item i ON b.item.id = i.id "
                        + "WHERE i.owner.id = :ownerId "
                        + "AND b.status = :status "
                        + "ORDER BY b.start DESC", Booking.class)
                        .setMaxResults(limit)
                        .setFirstResult(offset);

        var sut = query
                .setParameter("ownerId", ownerId)
                .setParameter("status", status)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void findAllByItemIdOrderByStartDesc() {

        var itemId = 10L;
        var bookings = bookingRepository.findAllByItemIdOrderByStartDesc(itemId);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b FROM Booking b "
                        + "WHERE b.item.id = :itemId "
                        + "ORDER BY b.start DESC", Booking.class);

        var sut = query
                .setParameter("itemId", itemId)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void findAllCurrentBookingsByItemOwner() {

        var ownerId = 10L;
        var time = LocalDateTime.now();
        var bookings = bookingRepository.findAllCurrentBookingsByItemOwner(ownerId, time);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b FROM Booking b "
                        + "INNER JOIN Item i ON b.item.id = i.id "
                        + "WHERE i.owner.id = :ownerId "
                        + "AND :time between b.start AND b.end "
                        + "ORDER BY b.start DESC", Booking.class);

        var sut = query
                .setParameter("ownerId", ownerId)
                .setParameter("time", time)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void findAllByItemIdInAndEndBeforeOrderByStartDesc() {

        var ownerId = 10L;
        var time = LocalDateTime.now();
        var bookings = bookingRepository.findAllByItemIdInAndEndBeforeOrderByStartDesc(ownerId, time);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b FROM Booking b "
                        + "INNER JOIN Item i ON b.item.id = i.id "
                        + "WHERE i.owner.id = :ownerId "
                        + "AND b.end < :time "
                        + "ORDER BY b.start DESC", Booking.class);

        var sut = query
                .setParameter("ownerId", ownerId)
                .setParameter("time", time)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void findAllByItemIdInAndStartAfterOrderByStartDesc() {

        var ownerId = 10L;
        var time = LocalDateTime.now();
        var bookings = bookingRepository.findAllByItemIdInAndStartAfterOrderByStartDesc(ownerId, time);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b FROM Booking b "
                        + "INNER JOIN Item i ON b.item.id = i.id "
                        + "WHERE i.owner.id = :ownerId "
                        + "AND b.start > :time "
                        + "ORDER BY b.start DESC", Booking.class);

        var sut = query
                .setParameter("ownerId", ownerId)
                .setParameter("time", time)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void findAllByItemIdInAndStatusOrderByStartDesc() {

        var ownerId = 10L;
        var time = LocalDateTime.now();
        var status = BookingStatus.APPROVED;
        var bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(ownerId, time, status);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b FROM Booking b "
                        + "INNER JOIN Item i ON b.item.id = i.id "
                        + "WHERE i.owner.id = :ownerId "
                        + "AND b.start > :time AND b.status = :status "
                        + "ORDER BY b.start DESC", Booking.class);

        var sut = query
                .setParameter("ownerId", ownerId)
                .setParameter("time", time)
                .setParameter("status", status)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }

    @Test
    void getBookingItem() {

        var itemId = 10L;
        var bookings = bookingRepository.getBookingItem(itemId);

        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b FROM Booking b "
                        + "INNER JOIN Item i ON b.item.id = i.id "
                        + "WHERE i.id = :itemId "
                        + "ORDER BY b.start DESC", Booking.class);

        var sut = query
                .setParameter("itemId", itemId)
                .getResultList();

        assertEquals(bookings.size(), sut.size());
    }
}
