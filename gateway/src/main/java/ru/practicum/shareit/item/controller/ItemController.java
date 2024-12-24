package ru.practicum.shareit.item.controller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentSaveDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;
    private final String headerUserId = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(headerUserId) Long userId) {
        try {
            log.info("Просмотр владельцем списка всех вещей.");
            return itemClient.getItemsByOwner(userId);
        } catch (Exception e) {
            log.error("Ошибка просмотра владельцем списка всех вещей.");
            throw e;
        }
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getInfoAboutItemById(@RequestHeader(headerUserId) Long userId,
                                        @PathVariable Long itemId) {
        try {
            log.info("Просмотр информации о конкретной вещи по её идентификатору.");
            return itemClient.getInfoAboutItemById(userId, itemId);
        } catch (Exception e) {
            log.error("Ошибка просмотра информации о конкретной вещи по её идентификатору.");
            throw e;
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchForItemPotentialTenant(@RequestParam @NotBlank String text) {
        try {
            log.info("Поиск вещи потенциальным арендатором.");
            return itemClient.searchForItemPotentialTenant(text);
        } catch (Exception e) {
            log.error("Ошибка поиска вещи потенциальным арендатором.");
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(headerUserId) Long userId,
                                             @Valid @RequestBody ItemSaveDto itemDto) {
        try {
            log.info("Добавление новой вещи владельцем.");
            return itemClient.createItem(userId, itemDto);
        } catch (Exception e) {
            log.error("Ошибка добавления новой вещи владельцем.");
            throw e;
        }
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(headerUserId) Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody ItemDto itemDto) {
        try {
            log.info("Изменение новой вещи владельцем.");
            return itemClient.updateItem(userId, itemId, itemDto);
        } catch (Exception e) {
            log.error("Ошибка изменения новой вещи владельцем.");
            throw e;
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long itemId) {
        try {
            log.info("Удаление новой вещи владельцем.");
            itemClient.deleteItem(itemId);
            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Ошибка удаления новой вещи владельцем.");
            throw e;
        }
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(headerUserId) Long userId,
                                                @PathVariable Long itemId,
                                                @Valid @RequestBody CommentSaveDto commentDto) {
        try {
            log.info("Добавление комментария.");
            return itemClient.createComment(userId, itemId, commentDto);
        } catch (Exception e) {
            log.error("Ошибка добавления комментария.");
            throw e;
        }
    }
}
