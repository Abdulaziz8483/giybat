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
public class PostEntity {
     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     String id;

     @Column(nullable = false)
     String title;

     @Column(nullable = false, columnDefinition = "TEXT")
     String content;

     @Column(nullable = false)
     Integer profileId;

     @Column(nullable = false)
     LocalDateTime createdAt;

     @Column
     LocalDateTime publishedAt;

     @Column
     LocalDateTime updatedAt;

     @Column(nullable = false)
     @Enumerated(EnumType.STRING)
     PostStatus status;

     @Column(name = "attach_id", nullable = false)
     String attachId;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "attach_id", insertable = false, updatable = false)
     AttachEntity photo;

     @Column(nullable = false)
     Boolean visible;
}
