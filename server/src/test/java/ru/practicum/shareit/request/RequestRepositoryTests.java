package ru.practicum.shareit.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestRepositoryTests {

    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;
    private final RequestRepository requestRepository;
    private final User user2 = new User(20L, "user2", "user2@somemail.ru");
    private final Request request = new Request(1L, "description", user2, LocalDateTime.now());

    @Test
    void findByRequestorIdOrderByCreatedDesc() {

        var userId = 10L;
        var requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        TypedQuery<Request> query =
                entityManager.createQuery(" select i from Request i " +
                        "where i.requestor.id =:id " +
                        "order by created " , Request.class);
        var sut = query.setParameter("id", userId).getResultList();

        assertEquals(requests.size(), sut.size() );
    }

    @Test
    void findByRequestorIdIsNot() {
        var userId = 10L;
        var requests = requestRepository.findByRequestorIdIsNot(userId);
        TypedQuery<Request> query =
                entityManager.createQuery(" select i from Request i " +
                        "where i.requestor.id !=:id " +
                        "order by created " , Request.class);
        var sut = query.setParameter("id", userId).getResultList();

        assertEquals(requests.size(), sut.size() );
    }

    @Test
    void findById() {
        var sut = requestRepository.findById(1L);

        assertNotNull(sut);
    }

    @Test
    void save() throws JsonProcessingException {
        requestRepository.save(request);

        TypedQuery<Request> query =
                entityManager.createQuery("FROM Request WHERE id = :id", Request.class);
        Request sut = query.setParameter("id", request.getId()).getSingleResult();

        //assertEquals(request, sut);
        assertEquals(objectMapper.writeValueAsString(RequestMapper.toRequestDto(request)), objectMapper.writeValueAsString(RequestMapper.toRequestDto(sut)));
    }
}
