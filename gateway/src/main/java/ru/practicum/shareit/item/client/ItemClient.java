package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> getItemsByOwner(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getInfoAboutItemById(Long userId, Long itemId) {
        return get("/" + itemId.toString(), userId);
    }

    public ResponseEntity<Object> searchForItemPotentialTenant(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> createItem(Long userId, ItemSaveDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, ItemDto itemDto) {
        return patch("/" + itemId.toString(), userId, itemDto);
    }

    public void deleteItem(Long itemId) {
        delete("/" + itemId.toString());
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentDto commentDto) {
        Map<String, Object> var = Map.of("itemId", itemId);
        return post("/{itemId}/comment", userId, var, commentDto);
    }
}
