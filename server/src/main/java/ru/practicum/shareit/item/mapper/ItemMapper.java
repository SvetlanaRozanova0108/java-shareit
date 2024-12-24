package ru.practicum.shareit.item.mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {

        var result = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );

        if (item.getRequest() != null) {
            result.setRequestId(item.getRequest().getId());
        }

        return result;
    }
}
