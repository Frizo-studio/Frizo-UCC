package com.frizo.ucc.server.payload.response.bean;

import java.time.Instant;
import java.util.Set;

public class EventBean {

    private Long id;

    private String title;

    private String description;

    private int maxNumberOfPeople;

    private String dmUrl;

    private Instant registrationDeadline;

    private Instant eventStartTime;

    private Instant createdAt;

    private String place;

    private String posterName;

    private int fee;

    private int likes;

    private Set<String> labelNameSet;

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxNumberOfPeople() {
        return maxNumberOfPeople;
    }

    public void setMaxNumberOfPeople(int maxNumberOfPeople) {
        this.maxNumberOfPeople = maxNumberOfPeople;
    }

    public String getDmUrl() {
        return dmUrl;
    }

    public void setDmUrl(String dmUrl) {
        this.dmUrl = dmUrl;
    }

    public Instant getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Instant registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public Instant getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Instant eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public Set<String> getLabelNameSet() {
        return labelNameSet;
    }

    public void setLabelNameSet(Set<String> labelNameSet) {
        this.labelNameSet = labelNameSet;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
