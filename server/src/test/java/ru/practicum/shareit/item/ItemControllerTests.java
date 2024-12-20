package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTests {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private final String headerUserId = "X-Sharer-User-Id";

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .requestId(1L)
            .build();

    private final List<ItemDto> itemsDtoList = List.of(
            new ItemDto(1L, "Name1", "Description1", true, null,
                    null, null, null),
            new ItemDto(2L, "Name2", "Description2", true, null,
                    null, null, null));

    private final CommentDto commentDto = CommentDto.builder().id(1L).text("Text").authorName("Name").build();

    @Test
    void getItemsByOwnerTest() throws Exception {
        when(itemService.getItemsByOwner(anyLong()))
                .thenReturn(itemsDtoList);

        String sut = mvc.perform(get("/items")
                        .header(headerUserId, 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(sut, mapper.writeValueAsString(itemsDtoList));
    }

    @Test
    void getItemsByOwnerTest1() throws Exception {
        doThrow(new ValidationException("")).when(itemService).getItemsByOwner(anyLong());
        mvc.perform(get("/items")
                        .header(headerUserId, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getInfoAboutItemByIdTest() throws Exception {

        mvc.perform(get("/items/{id}", 1L)
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());

        verify(itemService).getInfoAboutItemById(1L, 1L);
    }

    @Test
    void getInfoAboutItemByIdTest1() throws Exception {

        doThrow(new ValidationException("")).when(itemService).getInfoAboutItemById(1L, 1L);

        mvc.perform(get("/items/{id}", 1L)
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void searchForItemPotentialTenantTest() throws Exception {
        when(itemService.searchForItemPotentialTenant(anyString()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search?text=description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Item")))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder("Description")));
    }

    @Test
    void searchForItemPotentialTenantTest1() throws Exception {

        mvc.perform(get("/items/search?text="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void searchForItemPotentialTenantTest2() throws Exception {
        doThrow(new ValidationException("")).when(itemService).searchForItemPotentialTenant(anyString());

        mvc.perform(get("/items/search?text=description"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItemTest() throws Exception {
        mvc.perform(post("/items")
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());

        verify(itemService).createItem(1L, itemDto);
    }

    @Test
    void createItemTest1() throws Exception {
        doThrow(new ValidationException("")).when(itemService).createItem(1L, itemDto);

        mvc.perform(post("/items")
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateItemTest() throws Exception {
        ItemDto updateItemDto = ItemDto.builder()
                .id(1L)
                .name("updateItem")
                .description("Description")
                .available(true)
                .requestId(1L)
                .build();

        mvc.perform(patch("/items/{id}", 1L)
                        .header(headerUserId, 1L)
                        .content(mapper.writeValueAsString(updateItemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService).updateItem(updateItemDto, 1L, 1L);
    }

    @Test
    void updateItemTest1() throws Exception {
        ItemDto updateItemDto = ItemDto.builder()
                .id(1L)
                .name("updateItem")
                .description("Description")
                .available(true)
                .requestId(1L)
                .build();

        doThrow(new ValidationException("")).when(itemService).updateItem(updateItemDto, 1L, 1L);

        mvc.perform(patch("/items/{id}", 1L)
                        .header(headerUserId, 1L)
                        .content(mapper.writeValueAsString(updateItemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteItemTest() throws Exception {
        mvc.perform(delete("/items/{itemId}", 1L))
                .andExpect(status().isOk());

        verify(itemService).deleteItem(1L);
    }

    @Test
    void deleteItemTest1() throws Exception {
        doThrow(new ValidationException("")).when(itemService).deleteItem(1L);

        mvc.perform(delete("/items/{itemId}", 1L))
                .andExpect(status().isBadRequest());

    }

    @Test
    void createCommentTest() throws Exception {
        when(itemService.createComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header(headerUserId, 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }

    @Test
    void createCommentTest1() throws Exception {

        doThrow(new ValidationException("")).when(itemService).createComment(anyLong(), anyLong(), any());

        mvc.perform(post("/items/1/comment")
                        .header(headerUserId, 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}