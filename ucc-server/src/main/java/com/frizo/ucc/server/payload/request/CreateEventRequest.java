package com.frizo.ucc.server.payload.request;

import com.frizo.ucc.server.utils.common.DateUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

public class CreateEventRequest {

    private String title;

    private String description;

    private int maxNumberOfPeople;

    private MultipartFile dmPicture;

    private Instant registrationDeadline;

    private Instant eventStartTime;

    private String place;

    private int fee;

    private List<String> labelNameList;

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

    public MultipartFile getDmPicture() {
        return dmPicture;
    }

    public void setDmPicture(MultipartFile dmPicture) {
        this.dmPicture = dmPicture;
    }


    public Instant getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(String registrationDeadline) {
        this.registrationDeadline = DateUtils.stringToStartOfDay(registrationDeadline);
    }

    public Instant getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = DateUtils.stringToStartOfDay(eventStartTime);
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

    public List<String> getLabelNameList() {
        return labelNameList;
    }

    public void setLabelNameList(List<String> labelNameList) {
        this.labelNameList = labelNameList;
    }
}
