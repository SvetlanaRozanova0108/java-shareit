package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemsByOwner(Long ownerId);

    ItemDto getInfoAboutItemById(Long itemId);

    List<ItemDto> searchForItemPotentialTenant(String text);

    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long ownerId, Long itemId);

    void deleteOwner(Long userId);
}
