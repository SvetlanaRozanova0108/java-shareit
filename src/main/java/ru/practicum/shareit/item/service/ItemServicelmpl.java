package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemServicelmpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        return itemRepository.getItemsByOwner(ownerId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(toList());
    }

    @Override
    public ItemDto getInfoAboutItemById(Long itemId) {
        return itemMapper.toItemDto(itemRepository.getInfoAboutItemById(itemId));
    }

    @Override
    public List<ItemDto> searchForItemPotentialTenant(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        text = text.toLowerCase();
        return itemRepository.searchForItemPotentialTenant(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(toList());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        ItemDto input = null;
        if (userService.getUserById(ownerId) == null) {
            throw new NotFoundException("userService.getUserById(ownerId) == null");
        }
        input = itemMapper.toItemDto(itemRepository.createItem(itemMapper.toItem(itemDto, ownerId)));
        return input;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long ownerId, Long itemId) {
        if (itemDto.getName() == null) {
            itemDto.setName(itemRepository.getInfoAboutItemById(itemId).getName());
        }
        if (itemDto.getDescription() == null) {
            itemDto.setDescription(itemRepository.getInfoAboutItemById(itemId).getDescription());
        }
        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(itemRepository.getInfoAboutItemById(itemId).getAvailable());
        }
        if (userService.getUserById(ownerId) == null) {
            throw new NotFoundException("Пользователь с Id = " + ownerId + " не найден.");
        }
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        Item input = itemRepository.getInfoAboutItemById(itemId);
        if (!input.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Пользователь не владеет вещью.");
        }
        return itemMapper.toItemDto(itemRepository.updateItem(itemMapper.toItem(itemDto, ownerId)));
    }

    public void deleteOwner(Long ownerId) {
        itemRepository.deleteOwner(ownerId);
    }
}

