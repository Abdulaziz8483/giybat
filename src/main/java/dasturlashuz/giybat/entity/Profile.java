package dasturlashuz.giybat.entity;

import dasturlashuz.giybat.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Entity
@Table(name = "profile")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Column(nullable = false)
    String name;

    @Column
    String email;

    @Column
    String phone;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    boolean visible;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ProfileStatus status;

    @Column(nullable = false)
    LocalDateTime createdAt;

}
