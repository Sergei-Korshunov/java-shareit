package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findByItemId(Long itemId);

    @Query("SELECT c FROM Comment c WHERE c.item.id IN :itemIds")
    Collection<Comment> findByItemIdIn(@Param("itemIds") List<Long> itemIds);
}