//package ru.practicum.shareit.item;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import ru.practicum.shareit.item.repository.ItemRepository;
//import ru.practicum.shareit.user.model.User;
//import ru.practicum.shareit.user.repository.UserRepository;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@Transactional
//@SpringBootTest
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//public class ItemRepositoryTests {
//
//    @Test
//    void findAllByOwnerId() {
//
//    }
//
//    @Test
//    void findById() {
//
//    }
//
//    @Test
//    void searchItems() {
//
//    }
//
//    @Test
//    void save() {
//        itemRepository.save(item);
//
//        TypedQuery<User> query =
//                entityManager.createQuery("FROM User WHERE id = :id", User.class);
//        User sut = query.setParameter("id", user1.getId()).getSingleResult();
//
//        assertEquals(user1, sut);
//    }
//
//    @Test
//    void deleteById() {
//
//    }
//}
