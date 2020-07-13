package com.frizo.ucc.server.dao.notice;

import com.frizo.ucc.server.model.Following;
import com.frizo.ucc.server.model.FollowingPrimarykey;
import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.model.UserNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserNoticeRepository extends JpaRepository<UserNotice, Long> {

}
