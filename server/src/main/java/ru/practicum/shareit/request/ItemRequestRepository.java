package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequesterIdOrderByTimeCreateDesc(Long userId);

    @Query("SELECT r FROM ItemRequest r WHERE r.requester.id <> :requesterId ORDER BY r.timeCreate DESC")
    List<ItemRequest> findAllByRequesterIdNotOrderByTimeCreateDesc(@Param("requesterId") Long requesterId, Pageable page);
}