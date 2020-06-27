package com.frizo.ucc.server.dao.following;

import com.frizo.ucc.server.model.Following;
import com.frizo.ucc.server.model.FollowingPrimarykey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowingRepository extends JpaRepository<Following, FollowingPrimarykey> {

    // 提供 User 查詢自己的 following 列表資訊，accepted 為對方是否已經同意。
    List<Following> findAllByUserIdAndAcceptedEquals(Long id, boolean accepted);

    // 提供 User 查詢自己被誰追蹤(accepted = true)，或誰提出了追蹤要求(accepted = false)。
    List<Following> findAllByFollowingUserIdAndAcceptedEquals(Long id, boolean accepted);
}
