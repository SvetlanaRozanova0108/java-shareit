package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentSaveDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RequestService requestService;
    private final RequestRepository requestRepository;

    @Override
    public List<ItemDto> getItemsByOwner(Long userId) {
        List<ItemDto> items = itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return items;
    }

    @Override
    public ItemDto getInfoAboutItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с Id " + itemId + " не найдена.")));
        ItemDto result = ItemMapper.toItemDto(item);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            updateBooking(result);
        }
        List<Comment> comments = commentRepository.findAllByItemId(result.getId());
        result.setComments(CommentMapper.toListDto(comments));
        return result;
    }

    @Override
    public List<ItemDto> searchForItemPotentialTenant(String text) {
        return itemRepository.searchItems(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDto createItem(Long userId, ItemSaveDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        item.setRequest(itemDto.getRequestId() != null ?
                RequestMapper.toRequest(requestService.getRequestById(userId, itemDto.getRequestId())) : null);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с Id " + itemId + " не найдена.")));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("Пользователь с Id " + userId + " не владелец."));
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequestId() != null) {
            var request = requestRepository.findById(itemDto.getRequestId()).orElseThrow(
                    () -> new NotFoundException("Запрос с ID " + itemDto.getRequestId() + " не найден"));
            item.setRequest(request);
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long itemId, CommentSaveDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID " + userId + "не найден.")));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с ID " + itemId + "не найдена.")));
        List<Booking> bookings = bookingRepository
                .findByItemIdAndBookerIdAndStatusAndEndIsBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());
        if (!bookings.isEmpty()) {
            Comment comment = new Comment();
            comment.setText(commentDto.getText());
            comment.setItem(item);
            comment.setAuthor(user);
            comment.setCreated(LocalDateTime.now());
            return CommentMapper.toCommentDto(commentRepository.save(comment));
        } else {
            throw new NotAvailableException(String.format("Резервирование не найдено."));
        }
    }

    private ItemDto updateBooking(ItemDto itemDto) {
        LocalDateTime time = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.getBookingItem(itemDto.getId());
        Booking lastBooking = bookings.stream()
                .filter(obj -> !(obj.getStatus().equals(BookingStatus.REJECTED)))
                .filter(obj -> obj.getStart().isBefore(time))
                .min((obj1, obj2) -> obj2.getStart().compareTo(obj1.getStart())).orElse(null);
        Booking nextBooking = bookings.stream()
                .filter(obj -> !(obj.getStatus().equals(BookingStatus.REJECTED)))
                .filter(obj -> obj.getStart().isAfter(time))
                .min(Comparator.comparing(Booking::getStart)).orElse(null);
        if (lastBooking != null) {
            itemDto.setLastBooking(BookingMapper.toBookingBookerDto(lastBooking));
        }
        if (nextBooking != null) {
            itemDto.setNextBooking(BookingMapper.toBookingBookerDto(nextBooking));
        }
        return itemDto;
    }
}

