package com.frizo.ucc.server.payload.request;

import java.time.Instant;

public class QueryEventRequest {

    private String keywords;

    private int pageNumber;

    private Instant createTimeA;

    private Instant createTimeB;

    private Instant startTimeA;

    private Instant startTimeB;

    private Instant registrationDeadlineA;

    private Instant registrationDeadlineB;

    private String direction = "DESC"; // ASC 生序，DESC 降序。

    private String sortBy; // likes, createdAt

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Instant getCreateTimeA() {
        return this.createTimeA;
    }

    public void setCreateTimeA(Instant createTimeA) {
        this.createTimeA = createTimeA;
    }

    public Instant getCreateTimeB() {
        return createTimeB;
    }

    public void setCreateTimeB(Instant createTimeB) {
        this.createTimeB = createTimeB;
    }

    public Instant getStartTimeA() {
        return startTimeA;
    }

    public void setStartTimeA(Instant startTimeA) {
        this.startTimeA = startTimeA;
    }

    public Instant getStartTimeB() {
        return startTimeB;
    }

    public void setStartTimeB(Instant startTimeB) {
        this.startTimeB = startTimeB;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Instant getRegistrationDeadlineA() {
        return registrationDeadlineA;
    }

    public void setRegistrationDeadlineA(Instant registrationDeadlineA) {
        this.registrationDeadlineA = registrationDeadlineA;
    }

    public Instant getRegistrationDeadlineB() {
        return registrationDeadlineB;
    }

    public void setRegistrationDeadlineB(Instant registrationDeadlineB) {
        this.registrationDeadlineB = registrationDeadlineB;
    }
}
