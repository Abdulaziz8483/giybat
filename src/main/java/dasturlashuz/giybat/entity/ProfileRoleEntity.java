package dasturlashuz.giybat.entity;

import dasturlashuz.giybat.enums.ProfileRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "profile_role")
@Entity
@Getter
@Setter
public class ProfileRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "profile_Id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_Id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private ProfileRole roles;

    @Column(name = "localDate_Time")
    private LocalDateTime localDateTime;
}
