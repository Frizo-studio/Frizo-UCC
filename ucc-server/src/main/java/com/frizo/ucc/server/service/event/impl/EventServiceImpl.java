package com.frizo.ucc.server.service.event.impl;

import com.frizo.ucc.server.config.AppProperties;
import com.frizo.ucc.server.dao.event.EventRepository;
import com.frizo.ucc.server.dao.event.EventSpec;
import com.frizo.ucc.server.dao.user.UserRepository;
import com.frizo.ucc.server.exception.BadRequestException;
import com.frizo.ucc.server.exception.RequestProcessException;
import com.frizo.ucc.server.exception.ResourceNotFoundException;
import com.frizo.ucc.server.model.Event;
import com.frizo.ucc.server.model.Label;
import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.payload.request.CreateEventRequest;
import com.frizo.ucc.server.payload.request.QueryEventRequest;
import com.frizo.ucc.server.payload.request.UpdateEventRequest;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.service.event.EventService;
import com.frizo.ucc.server.utils.common.PageRequestBuilder;
import com.frizo.ucc.server.utils.files.FrizoFileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AppProperties appProperties;

    @Override
    @Transactional
    public EventBean getById(Long id) {
        EventBean bean = new EventBean();
        Optional<Event> eventOptional = eventRepository.findById(id);
        eventOptional.ifPresent(event -> {
            BeanUtils.copyProperties(event, bean);
            bean.setPosterName(event.getUser().getName());
            Set<String> labelNames = new HashSet<>();
            event.getLabelSet().forEach(label -> {
                labelNames.add(label.getName());
            });
            bean.setLabelNameSet(labelNames);
        });
        return bean;
    }

    @Override
    public List<EventBean> findAllByQuerySpec(QueryEventRequest request) {
        Specification<Event> spec = EventSpec.eventBlurrySpec(request);
        Pageable pageRequest = PageRequestBuilder.create()
                .pageNumber(request.getPageNumber())
                .pageSize(10)
                .direction(request.getDirection())
                .sortBy(request.getSortBy())
                .build();
        Page<Event> events = eventRepository.findAll(spec, pageRequest);
        List<EventBean> eventBeans = new ArrayList<>();
        events.forEach(event -> {
            EventBean bean = new EventBean();
            BeanUtils.copyProperties(event, bean);
            bean.setPosterName(event.getUser().getName());
            Set<String> labelNames = new HashSet<>();
            event.getLabelSet().forEach(label -> {
                labelNames.add(label.getName());
            });
            bean.setLabelNameSet(labelNames);
            eventBeans.add(bean);
        });
        return eventBeans;
    }

    @Override
    public EventBean createEvent(Long userId, CreateEventRequest request) {
        try {
            User user = userRepository.getOne(userId);
            Event event = new Event();
            event.setUser(user);
            event.setTitle(request.getTitle());
            event.setDescription(request.getDescription());
            event.setFee(request.getFee());
            String dmName = FrizoFileUtils.storePhoto(request.getDmPicture(), appProperties.getFileDir().getDmDir());
            if (dmName != null){
                String dmUrl = appProperties.getFileDir().getDmBaseUrl() + dmName;
                event.setDmUrl(dmUrl);
            }
            event.setPlace(request.getPlace());
            event.setMaxNumberOfPeople(request.getMaxNumberOfPeople());
            event.setEventStartTime(request.getEventStartTime());
            event.setRegistrationDeadline(request.getRegistrationDeadline());
            Set<Label> labels = new HashSet<>();
            Event finalEvent = event;
            request.getLabelNameList().forEach(labelName -> {
                if (!labelName.equals("")) {
                    Label label = new Label();
                    label.setEvent(finalEvent);
                    label.setName(labelName);
                    labels.add(label);
                }
            });
            event.setLabelSet(labels);
            event = eventRepository.save(event);
            EventBean bean = new EventBean();
            BeanUtils.copyProperties(event, bean);
            Set<String> labelNameList = new HashSet<>();
            event.getLabelSet().forEach(label -> {
                labelNameList.add(label.getName());
            });
            bean.setLabelNameSet(labelNameList);
            bean.setPosterName(user.getName());
            return bean;
        }catch (Exception ex){
            throw new RequestProcessException(ex.getMessage());
        }
    }

    @Override
    public List<EventBean> findPostedEventByUserId(Long userId, int pageNumber, String sortBy, String direction) {
        User user = userRepository.getOne(userId);
        Pageable pageRequest = PageRequestBuilder.create()
                .pageNumber(pageNumber)
                .pageSize(5)
                .direction(direction)
                .sortBy(sortBy)
                .build();
        Page<Event> events = eventRepository.findAllByUser(user, pageRequest);
        List<EventBean> beans = new ArrayList<>();
        events.forEach(event -> {
            EventBean bean = new EventBean();
            BeanUtils.copyProperties(event, bean);
            Set<String> labelNames = new HashSet<>();
            event.getLabelSet().forEach(label -> {
                labelNames.add(label.getName());
            });
            bean.setLabelNameSet(labelNames);
            beans.add(bean);
        });
        return beans;
    }

    @Override
    @Transactional
    public void deleteEvent(Long userId, Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()){
            throw new ResourceNotFoundException("找不到活動: " + eventId);
        }
        Event event = optionalEvent.get();
        if (!event.getUser().getId().equals(userId)){
            throw new BadRequestException("您沒有權限刪除此活動");
        }
        eventRepository.delete(event);
    }

    @Override
    @Transactional
    public EventBean updateEvent(Long userId, UpdateEventRequest request) {
        Optional<Event> eventOptional = eventRepository.findById(request.getEventId());
        if (eventOptional.isEmpty()){
            throw new ResourceNotFoundException("找不到活動: " + request.getEventId());
        }
        Event event = eventOptional.get();
        if (!event.getUser().getId().equals(userId)){
            throw new BadRequestException("您沒有權限修改此活動");
        }
        event.setTitle(request.getTitle());
        event.setMaxNumberOfPeople(request.getMaxNumberOfPeople());
        event.setDescription(request.getDescription());
        event.setPlace(request.getPlace());
        event.setRegistrationDeadline(request.getRegistrationDeadline());
        event.setEventStartTime(request.getEventStartTime());
        event.setFee(request.getFee());
        try {
            String dmName = FrizoFileUtils.storePhoto(request.getDmPicture(), appProperties.getFileDir().getDmDir());
            if (dmName != null) {
                String dmUrl = appProperties.getFileDir().getDmBaseUrl() + dmName;
                event.setDmUrl(dmUrl);
            }
        }catch (Exception ex){
            throw new RequestProcessException(ex.getMessage());
        }
        Set<Label> labels = new HashSet<>();
        Event finalEvent = event;
        request.getLabelNameList().forEach(labelName -> {
            if (!labelName.equals("")) {
                Label label = new Label();
                label.setEvent(finalEvent);
                label.setName(labelName);
                labels.add(label);
            }
        });
        event.getLabelSet().clear();
        event.getLabelSet().addAll(labels);
        event = eventRepository.save(event);
        EventBean bean = new EventBean();
        BeanUtils.copyProperties(event, bean);
        Set<String> labelNameSet = new HashSet<>();
        event.getLabelSet().forEach(label -> {
            labelNameSet.add(label.getName());
        });
        bean.setLabelNameSet(labelNameSet);
        return bean;
    }

}
