package ru.practicum.shareit.request.repository;
import ru.practicum.shareit.request.model.Request;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
