package dasturlashuz.giybat.entity;

import dasturlashuz.giybat.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "profile")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
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
