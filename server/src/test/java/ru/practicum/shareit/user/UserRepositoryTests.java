package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTests {

    private final EntityManager entityManager;
    //private static Session session;
    private final UserRepository userRepository;
    private final User user1 = new User(1L, "User1", "user1@email.com");

//    @BeforeAll
//    static void init() {
//        session = HibernateUtil.getSessionFactory().openSession();
//        session.beginTransaction();
//    }
//
//    @AfterAll
//    static void clear() {
//        session.close();
//    }

    @Test
    void findAll() {

        var sut = userRepository.findAll();

        assertThat(sut).isNotEmpty();
    }

    @Test
    void findById() {
        var sut = userRepository.findById(10L);

        assertNotNull(sut);
    }

    @Test
    void save() {
        userRepository.save(user1);

        TypedQuery<User> query =
                entityManager.createQuery("FROM User WHERE id = :id", User.class);
        User sut = query.setParameter("id", user1.getId()).getSingleResult();

        assertEquals(user1, sut);
    }

    @Test
    void deleteById() {
        var userId = 10L;

        userRepository.deleteById(userId);

        TypedQuery<User> query =
                entityManager.createQuery("FROM User WHERE id = :id", User.class);
        var sut = query.setParameter("id", userId).getResultList();
        assertThat(sut).isEmpty();
    }
}
