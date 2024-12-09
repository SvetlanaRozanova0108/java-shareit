package ru.practicum.shareit.item.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
//                item.getNextBooking() != null ? BookingMapper.toBookingBookerDto(item.getNextBooking()) : null,
//                item.getLastBooking() != null ? BookingMapper.toBookingBookerDto(item.getLastBooking()) : null,
//                CommentMapper.toListDto(item.getComments()))
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequestId() != null ? itemDto.getRequestId() : null);
//                itemDto.getNextBooking() != null ? BookingMapper.toBooking(itemDto.getNextBooking(), booker) : null,
//                itemDto.getLastBooking() != null ? BookingMapper.toBooking(itemDto.getLastBooking(), booker) : null,
//                CommentMapper.toList(itemDto.getComments()))
    }
}
/*
package ru.practicum.shareit.item.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item map(ItemDto itemSaveDto);

    ItemDto map(Item item);

    Collection<ItemDto> map(Collection<Item> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(@MappingTarget Item item, ItemDto itemDto);
}
*/
