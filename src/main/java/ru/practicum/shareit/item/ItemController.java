package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final String headerUserId = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(headerUserId) Long ownerId) {
        try {
            log.info("Просмотр владельцем списка всех вещей.");
            return itemService.getItemsByOwner(ownerId);
        } catch (Exception e) {
            log.error("Ошибка просмотра владельцем списка всех вещей.");
            throw e;
        }
    }

    @GetMapping("/{itemId}")
    public ItemDto getInfoAboutItemById(@PathVariable Long itemId) {
        try {
            log.info("Просмотр информации о конкретной вещи по её идентификатору.");
            return itemService.getInfoAboutItemById(itemId);
        } catch (Exception e) {
            log.error("Ошибка просмотра информации о конкретной вещи по её идентификатору.");
            throw e;
        }
    }

    @GetMapping("/search")
    public List<ItemDto> searchForItemPotentialTenant(@RequestParam String text) {
        try {
            log.info("Поиск вещи потенциальным арендатором.");
            return itemService.searchForItemPotentialTenant(text);
        } catch (Exception e) {
            log.error("Ошибка поиска вещи потенциальным арендатором.");
            throw e;
        }
    }

    @ResponseBody
    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(headerUserId) Long ownerId) {
        try {
            if (ownerId == null) {
                throw new ValidationException("ownerId == null");
            }
            log.info("Добавление новой вещи владельцем.");
            return itemService.createItem(itemDto, ownerId);
        } catch (Exception e) {
            log.error("Ошибка добавления новой вещи владельцем.");
            throw e;
        }
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable Long itemId, @RequestHeader(headerUserId) Long ownerId) {
        try {
            log.info("Изменение новой вещи владельцем.");
            return itemService.updateItem(itemDto, ownerId, itemId);
        } catch (Exception e) {
            log.error("Ошибка изменения новой вещи владельцем.");
            throw e;
        }
    }
}
