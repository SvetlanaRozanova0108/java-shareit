package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"name", "description"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")

public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @Transient
    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY)
    @Transient
    private Booking lastBooking;

    @OneToMany(fetch = FetchType.LAZY)
    @Transient
    private Booking nextBooking;

    public Item(Long id, String name, String description, Boolean available, Long aLong) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setAvailable(available);
        this.setRequest(request);
    }
}
