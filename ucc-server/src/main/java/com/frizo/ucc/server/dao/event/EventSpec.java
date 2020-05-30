package com.frizo.ucc.server.dao.event;

import com.frizo.ucc.server.exception.BadRequestException;
import com.frizo.ucc.server.model.Event;
import com.frizo.ucc.server.model.Label;
import com.frizo.ucc.server.payload.request.QueryEventRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.*;


public class EventSpec {

    private static void buildTimeRangeQueryPredicate(List<Predicate> predicates, CriteriaBuilder cb, Path<Instant> column, Instant timeA, Instant timeB) {
        if (timeA != null && timeB != null) {
            validTimeRange(timeA, timeB);
            predicates.add(cb.between(column, timeA, timeB));
        }
        if (timeA != null && timeB == null) {
            predicates.add(cb.greaterThan(column, timeA));
        }
        if (timeA == null && timeB != null) {
            predicates.add(cb.lessThan(column, timeB));
        }
    }

    private static void validTimeRange(Instant timeA, Instant timeB) {
        if (timeA.isAfter(timeB)) {
            throw new BadRequestException("查詢條件錯誤，請檢查日期區間。");
        }
    }


    public static Specification<Event> eventBlurrySpec(QueryEventRequest request) {
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Join<Event, Label> labelJoin = root.join("labelList", JoinType.LEFT);
                Path<String> title = root.get("title");
                Path<String> description = root.get("description");
                List<Predicate> orPredicates = new ArrayList<>();
                List<Predicate> andPredicates = new ArrayList<>();

                Optional<QueryEventRequest> optional = Optional.ofNullable(request);
                optional.map(QueryEventRequest::getKeywords).ifPresent(keywords -> {
                    List<String> keywordList = Arrays.asList(keywords.split(" "));
                    keywordList.forEach(keyword -> {
                        orPredicates.add(cb.like(labelJoin.get("name"), "%" + keyword + "%"));
                        orPredicates.add(cb.like(title, "%" + keyword + "%"));
                        orPredicates.add(cb.like(description, "%" + keyword + "%"));
                    });
                });

                Path<Instant> createdAt = root.get("createdAt");
                buildTimeRangeQueryPredicate(andPredicates, cb, createdAt, request.getCreateTimeA(), request.getCreateTimeB());

                Path<Instant> startTime = root.get("eventStartTime");
                buildTimeRangeQueryPredicate(andPredicates, cb, startTime, request.getStartTimeA(), request.getStartTimeB());

                Path<Instant> deadline = root.get("registrationDeadline");
                buildTimeRangeQueryPredicate(andPredicates, cb, deadline, request.getRegistrationDeadlineA(), request.getRegistrationDeadlineB());

                criteriaQuery.groupBy(root.get("id"));
                Predicate[] orArray = new Predicate[orPredicates.size()];
                Predicate orPredicate = cb.or(orPredicates.toArray(orArray));

                Predicate[] andArray = new Predicate[andPredicates.size()];
                Predicate andPredicate = cb.and(andPredicates.toArray(andArray));

                Predicate finalPredicate = cb.and(andPredicate, orPredicate);
                return finalPredicate;
            }
        };
    }

}
