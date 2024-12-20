package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTests {

    private final EntityManager entityManager;
    //private static Session session;
    private final ItemRepository itemRepository;
    private final User user1 = new User(20L, "User1", "user1@email.com");
    private final Item item = new Item(1L, "item", "description", true, user1, null, null, null, null);

    @Test
    void findAllByOwnerId() {
        var userId =10L;
        var items = itemRepository.findAllByOwnerId(userId);
        TypedQuery<Item> query =
                entityManager.createQuery("FROM Item WHERE owner.id = :id", Item.class);
        var sut = query.setParameter("id", userId).getResultList();

        assertEquals(items.size(), sut.size() );
    }

    @Test
    void findById() {
        var sut = itemRepository.findById(10L);

        assertNotNull(sut);
    }

    @Test
    void searchItems() {
        var text = "item";
        var items = itemRepository.searchItems(text);

        TypedQuery<Item> query =
                entityManager.createQuery(" select i from Item i " +
                        "where i.available is true " +
                        "and upper(i.name) like upper(concat('%', :text, '%')) " +
                        " or upper(i.description) like upper(concat('%', :text, '%'))", Item.class);
        var sut = query.setParameter("text", text).getResultList();

        assertEquals(items.size(), sut.size() );
    }

    @Test
    void save() {
        itemRepository.save(item);

        TypedQuery<Item> query =
                entityManager.createQuery("FROM Item WHERE id = :id", Item.class);
        Item sut = query.setParameter("id", item.getId()).getSingleResult();

        assertEquals(item, sut);
    }

    @Test
    void deleteById() {
        var itemId = 10L;
        itemRepository.deleteById(itemId);

        TypedQuery<Item> query =
                entityManager.createQuery("FROM Item WHERE id = :id", Item.class);
        var sut = query.setParameter("id", itemId).getResultList();
        assertThat(sut).isEmpty();
    }
}
