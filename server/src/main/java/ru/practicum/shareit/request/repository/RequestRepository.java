package ru.practicum.shareit.request.repository;
import ru.practicum.shareit.request.model.Request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    
    List<Request> findByRequestorIdOrderByCreatedDesc(Long userId);

    List<Request> findByRequestorIdIsNot(Long userId);
}
