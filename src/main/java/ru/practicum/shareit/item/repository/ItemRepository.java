package ru.practicum.shareit.item.repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    List<Item> getItemsByOwner(Long ownerId);

    Item getInfoAboutItemById(Long itemId);

    List<Item> searchForItemPotentialTenant(String text);

    Item createItem(Item item);

    Item updateItem(Item item);

    void deleteOwner(Long ownerId);
}
