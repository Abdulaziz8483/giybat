package dasturlashuz.giybat.entity;

import dasturlashuz.giybat.enums.GeneralStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="profile")

public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "user_name")
    private String username;// email/phone
    @Column(name = "password")
    private String Password;
    @Column(name = "status")


    @Enumerated(EnumType.STRING)
    private GeneralStatus status;
    @Column(name="visible")
    private Boolean visible=true;

    @Column(name = "localDate_Time")
    private LocalDateTime localDateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public GeneralStatus getStatus() {
        return status;
    }

    public void setStatus(GeneralStatus status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}

