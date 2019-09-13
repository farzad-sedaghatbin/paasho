package ir.redmind.paasho.service.dto;

import java.io.Serializable;

/**
 * A DTO for the Comment entity.
 */
public class ChatMinimizeDTO implements Serializable {

    private String avatar;
    private Long id;
    private String name;
    private boolean online;


    public ChatMinimizeDTO(String avatar, Long id, String name, boolean online) {
        this.avatar = avatar;
        this.id = id;
        this.name = name;
        this.online = online;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
