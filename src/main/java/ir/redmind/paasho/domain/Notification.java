package ir.redmind.paasho.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ir.redmind.paasho.domain.enumeration.NotificationType;

import ir.redmind.paasho.domain.enumeration.NotificationStatus;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotificationStatus status;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "notification_users",
               joinColumns = @JoinColumn(name = "notification_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("notifications")
    private User from;

    @ManyToOne
    @JsonIgnoreProperties("notifications")
    private Event relatedEvent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Notification description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NotificationType getType() {
        return type;
    }

    public Notification type(NotificationType type) {
        this.type = type;
        return this;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public Notification status(NotificationStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Notification users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Notification addUsers(User user) {
        this.users.add(user);
        return this;
    }

    public Notification removeUsers(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public User getFrom() {
        return from;
    }

    public Notification from(User user) {
        this.from = user;
        return this;
    }

    public void setFrom(User user) {
        this.from = user;
    }

    public Event getRelatedEvent() {
        return relatedEvent;
    }

    public Notification relatedEvent(Event event) {
        this.relatedEvent = event;
        return this;
    }

    public void setRelatedEvent(Event event) {
        this.relatedEvent = event;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification notification = (Notification) o;
        if (notification.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notification.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
