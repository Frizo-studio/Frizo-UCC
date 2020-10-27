package com.frizo.ucc.server.dao.event;

import com.frizo.ucc.server.model.EventApplyer;
import com.frizo.ucc.server.model.EventApplyerPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventApplyerRepository extends JpaRepository<EventApplyer, EventApplyerPrimaryKey> {
}
