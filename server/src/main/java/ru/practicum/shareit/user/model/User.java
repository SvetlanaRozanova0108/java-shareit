package ru.practicum.shareit.user.model;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"name", "email"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "email", length = 150)
    private String email;


}
