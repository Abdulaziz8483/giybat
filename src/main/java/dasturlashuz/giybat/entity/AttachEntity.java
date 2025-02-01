package dasturlashuz.giybat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "attach")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachEntity {
    @Id
    String id;

    @Column(nullable = false, name = "origin_name")
    String originName;

    @Column(nullable = false)
    Long size;

    @Column(nullable = false)
    String type;

    @Column(nullable = false)
    String path;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    String extension;

    @Column(nullable = false)
    Boolean visible;
}