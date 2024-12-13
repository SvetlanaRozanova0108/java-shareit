package ru.practicum.shareit.item;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final String headerUserId = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(headerUserId) Long userId) {
        try {
            log.info("Просмотр владельцем списка всех вещей.");
            return itemService.getItemsByOwner(userId);
        } catch (Exception e) {
            log.error("Ошибка просмотра владельцем списка всех вещей.");
            throw e;
        }
    }

    @GetMapping("/{itemId}")
    public ItemDto getInfoAboutItemById(@RequestHeader(headerUserId) Long userId,
                                        @PathVariable Long itemId) {
        try {
            log.info("Просмотр информации о конкретной вещи по её идентификатору.");
            return itemService.getInfoAboutItemById(itemId, userId);
        } catch (Exception e) {
            log.error("Ошибка просмотра информации о конкретной вещи по её идентификатору.");
            throw e;
        }
    }

    @GetMapping("/search")
    public List<ItemDto> searchForItemPotentialTenant(@RequestParam String text) {
        try {
            if (text == null || text.isBlank()) {
                return Collections.emptyList();
            }
            log.info("Поиск вещи потенциальным арендатором.");
            return itemService.searchForItemPotentialTenant(text);
        } catch (Exception e) {
            log.error("Ошибка поиска вещи потенциальным арендатором.");
            throw e;
        }
    }

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader(headerUserId) Long userId) {
        try {
            log.info("Добавление новой вещи владельцем.");
            return itemService.createItem(userId, itemDto);
        } catch (Exception e) {
            log.error("Ошибка добавления новой вещи владельцем.");
            throw e;
        }
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable Long itemId,
                              @RequestHeader(headerUserId) Long userId) {
        try {
            log.info("Изменение новой вещи владельцем.");
            return itemService.updateItem(itemDto, userId, itemId);
        } catch (Exception e) {
            log.error("Ошибка изменения новой вещи владельцем.");
            throw e;
        }
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        try {
            log.info("Удаление новой вещи владельцем.");
            itemService.deleteItem(itemId);
        } catch (Exception e) {
            log.error("Ошибка удаления новой вещи владельцем.");
            throw e;
        }
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(headerUserId) Long userId,
                                    @PathVariable Long itemId,
                                    @Valid @RequestBody CommentDto commentDto) {
        try {
            log.info("Добавление комментария.");
            return itemService.createComment(userId, itemId, commentDto);
        } catch (Exception e) {
            log.error("Ошибка добавления комментария.");
            throw e;
        }
    }
}
