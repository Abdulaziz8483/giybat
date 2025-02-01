package dasturlashuz.giybat.entity;

import dasturlashuz.giybat.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class Post {
     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     String id;

     @Column(nullable = false)
     String title;

     @Column(nullable = false)
     String content;

     @Column(nullable = false)
     Long profileId;

     @Column(nullable = false)
     LocalDateTime createdAt;

     @Column
     LocalDateTime updatedAt;

     @Column(nullable = false)
     PostStatus status;

     @Column(nullable = false)
     Boolean visible;
}
