package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemsByOwner(Long userId);

    ItemDto getInfoAboutItemById(Long itemId, Long userId);

    List<ItemDto> searchForItemPotentialTenant(String text);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId);

    void deleteItem(Long itemId);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto);

//    Long getOwnerId(Long itemId);
}
