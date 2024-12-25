package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentSaveDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ItemServiceTests {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private RequestService requestService;

    @Mock
    private RequestRepository requestRepository;

    private final User user = new User(1L, "User", "user@email.com");
    private final UserDto userDto = new UserDto(1L, "User", "user@email.com");

    private final Request request = Request.builder()
            .id(1L)
            .description("description")
            .requestor(user)
            .items(new ArrayList<>())
            .build();
    private final RequestDto requestDto = RequestDto.builder()
            .id(1L)
            .description("description")
            .requestor(userDto)
            .items(new ArrayList<>())
            .build();
    private final Item item = Item.builder()
            .id(1L)
            .name("ItemName")
            .description("description")
            .available(true)
            .owner(user)
            .request(request)
            .build();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("ItemName")
            .description("description")
            .available(true)
            .requestId(1L)
            .build();
    private final List<Booking> bookingList = List.of(Booking.builder()
                    .id(1L).item(item).booker(user)
                    .start(LocalDateTime.now().minusHours(2L))
                    .end(LocalDateTime.now().minusHours(1L))
                    .status(BookingStatus.WAITING).build(),
            Booking.builder()
                    .id(2L).item(item).booker(user)
                    .start(LocalDateTime.now().plusHours(1L))
                    .end(LocalDateTime.now().plusHours(2L))
                    .status(BookingStatus.WAITING).build());
    private final Comment comment = Comment.builder()
            .id(1L)
            .text("Text")
            .item(item)
            .author(user)
            .build();
    private final CommentDto commentDto = CommentDto
            .builder()
            .id(1L).text("Text")
            .authorName("User")
            .build();
    private final CommentSaveDto commentSaveDto = CommentSaveDto
            .builder()
            .text("Text")
            .build();
    private final ItemSaveDto itemSaveDto = ItemSaveDto.builder()
            .name("ItemName")
            .description("description")
            .available(true)
            .build();

    @Test
    void getItemsByOwnerTest() {

        Mockito.when(itemRepository.findAllByOwnerId(anyLong()))
                        .thenReturn(List.of(item));

        List<ItemDto> userItemsList = itemService.getItemsByOwner(1L);
        verify(itemRepository).findAllByOwnerId(anyLong());
    }

    @Test
    void getInfoAboutItemByIdTest() {

        Mockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito.when(commentRepository.findAllByItemId(anyLong()))
                .thenReturn(List.of(comment));
        Mockito.when(bookingRepository.getBookingItem(anyLong()))
                .thenReturn(bookingList);

        ItemDto requestedItemDto = itemService.getInfoAboutItemById(1L, 1L);

        assertEquals(requestedItemDto.getName(), item.getName());
        assertEquals(requestedItemDto.getDescription(), item.getDescription());
        assertEquals(requestedItemDto.getAvailable(), item.getAvailable());
        assertEquals(requestedItemDto.getLastBooking().getId(), 1L);
        assertEquals(requestedItemDto.getLastBooking().getBookerId(), 1L);
        assertEquals(requestedItemDto.getNextBooking().getId(), 2L);
        assertEquals(requestedItemDto.getNextBooking().getBookerId(), 1L);
    }

    @Test
    void searchForItemPotentialTenantTest() {

        assertThat(itemService.searchForItemPotentialTenant(""), hasSize(0));
        assertThat(itemService.searchForItemPotentialTenant(null), hasSize(0));

        Mockito.when(itemRepository.searchItems(anyString()))
                .thenReturn(List.of(item));

        assertEquals(itemService.searchForItemPotentialTenant("item"), List.of(itemDto));
    }

    @Test
    void createItemTest() {

        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(any()))
                .thenReturn(item);

        assertEquals(itemService.createItem(1L, itemSaveDto), itemDto);
    }

    @Test
    void updateItemTest() {

        ItemDto itemDtoUpdate = ItemDto.builder()
                .id(1L)
                .name("ItemUpdate")
                .description("DescriptionUpdate")
                .available(true)
                .requestId(1L)
                .build();

        Mockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(request));
        Mockito.when(itemRepository.save(any()))
                .thenReturn(item);

        assertEquals(itemService.updateItem(itemDtoUpdate, 1L, 1L), itemDtoUpdate);
    }

    @Test
    void deleteItemTest() {

        itemService.deleteItem(1L);

        verify(itemRepository).deleteById(1L);
    }

    @Test
    void createCommentTest() {

        Mockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(bookingRepository.findByItemIdAndBookerIdAndStatusAndEndIsBefore(anyLong(), anyLong(), any(), any()))
                .thenReturn(bookingList);
        Mockito.when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDto testComment = itemService.createComment(1L, 1L, commentSaveDto);

        assertEquals(testComment.getId(), commentDto.getId());
        assertEquals(testComment.getText(), commentDto.getText());
        assertEquals(testComment.getAuthorName(), commentDto.getAuthorName());
    }
}