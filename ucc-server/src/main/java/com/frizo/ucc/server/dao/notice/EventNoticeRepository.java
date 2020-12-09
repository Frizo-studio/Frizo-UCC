package com.frizo.ucc.server.dao.notice;

import com.frizo.ucc.server.model.EventNotice;
import com.frizo.ucc.server.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventNoticeRepository extends JpaRepository<EventNotice, Long> {

    Page<EventNotice> findAllByUser(User user, Pageable pageable);

    void deleteAllByUser(User user);

}
