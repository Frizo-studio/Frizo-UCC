package com.frizo.ucc.server.model;

import com.frizo.ucc.server.model.audit.UserDateAudit;

import javax.persistence.*;

/*
 * 當一個 User 申請追蹤目標 User 時，便會建立一筆 record，accepted 為 false。
 * 當目標 User 同意追蹤申請之後，或目標 User 設定為預設同意被追蹤，則 accepted 為 true。
 * 當 User 想知道自己目前正在追蹤的目標有誰時，get all where accepted = true and userId = userId。
 * 當 User 想知道自己目前有哪些未同意追蹤時，get all where accepted = flase and followingUserId = userId。
 * 當 User 拒絕追蹤要求時，則直接刪除 record。
 */

@Entity
@Table(name = "user_following")
public class Following extends UserDateAudit {

    @EmbeddedId
    private FollowingPrimarykey followingPrimarykey;


    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @MapsId("followingUserId")
    @ManyToOne
    @JoinColumn(name="followingUserId")
    private User followingUser;


    @Column(nullable = false)
    private boolean accepted = false;

    public FollowingPrimarykey getFollowingPrimarykey() {
        return followingPrimarykey;
    }

    public void setFollowingPrimarykey(FollowingPrimarykey followingPrimarykey) {
        this.followingPrimarykey = followingPrimarykey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFollowingUser() {
        return followingUser;
    }

    public void setFollowingUser(User followingUser) {
        this.followingUser = followingUser;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
