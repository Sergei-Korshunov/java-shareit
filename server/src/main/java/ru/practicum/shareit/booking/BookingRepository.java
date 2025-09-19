package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findByUserIdOrderByStartTimeDesc(Long bookerId);

    Collection<Booking> findByUserIdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findByUserIdAndEndTimeBeforeOrderByStartTimeDesc(Long bookerId, LocalDateTime end);

    Collection<Booking> findByUserIdAndStartTimeAfterOrderByStartTimeDesc(Long bookerId, LocalDateTime start);

    Collection<Booking> findByUserIdAndStatusOrderByStartTimeDesc(Long bookerId, BookingStatus status);

    boolean existsByUserIdAndItemIdAndEndTimeBefore(Long bookerId, Long itemId, LocalDateTime endTime);

    Optional<Booking> findFirstByItemIdAndEndTimeBeforeOrderByEndTimeDesc(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndStartTimeAfterOrderByStartTimeAsc(Long itemId, LocalDateTime now);

    Collection<Booking> findByItemOwnerIdOrderByStartTimeDesc(Long ownerId);

    Collection<Booking> findByItemOwnerIdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(Long ownerId, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findByItemOwnerIdAndEndTimeBeforeOrderByStartTimeDesc(Long ownerId, LocalDateTime end);

    Collection<Booking> findByItemOwnerIdAndStartTimeAfterOrderByStartTimeDesc(Long ownerId, LocalDateTime start);

    Collection<Booking> findByItemOwnerIdAndStatusOrderByStartTimeDesc(Long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND b.status = 'APPROVED' ORDER BY b.startTime ASC")
    Collection<Booking> findApprovedBookingsForItems(@Param("itemIds") List<Long> itemIds);
}
