package com.frizo.ucc.server.service.event.impl;

import com.frizo.ucc.server.config.AppProperties;
import com.frizo.ucc.server.dao.event.EventRepository;
import com.frizo.ucc.server.dao.event.EventSpec;
import com.frizo.ucc.server.dao.user.UserRepository;
import com.frizo.ucc.server.exception.RequestProcessException;
import com.frizo.ucc.server.model.Event;
import com.frizo.ucc.server.model.Label;
import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.payload.request.CreateEventRequest;
import com.frizo.ucc.server.payload.request.QueryEventRequest;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.service.event.EventService;
import com.frizo.ucc.server.utils.files.FrizoFileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        Event event = eventRepository.getOne(id);
        EventBean bean = new EventBean();
        BeanUtils.copyProperties(event, bean);
        return bean;
    }

    @Override
    public List<EventBean> findAllByQuerySpec(QueryEventRequest request) {
        Specification<Event> spec = EventSpec.eventBlurrySpec(request);
        PageRequest pageRequest = this.buildPageRequest(request);
        Page<Event> events = eventRepository.findAll(spec, pageRequest);
        List<EventBean> eventBeans = new ArrayList<>();
        events.forEach(event -> {
            EventBean bean = new EventBean();
            BeanUtils.copyProperties(event, bean);
            List<String> labelNames = new ArrayList<>();
            event.getLabelList().forEach(label -> {
                labelNames.add(label.getName());
            });
            bean.setLabelNameList(labelNames);
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
            List<Label> labels = new ArrayList<>();
            Event finalEvent = event;
            request.getLabelNameList().forEach(labelName -> {
                if (!labelName.equals("")) {
                    Label label = new Label();
                    label.setEvent(finalEvent);
                    label.setName(labelName);
                    labels.add(label);
                }
            });
            event.setLabelList(labels);
            event = eventRepository.save(event);
            EventBean bean = new EventBean();
            BeanUtils.copyProperties(event, bean);
            List<String> labelNameList = new ArrayList<>();
            event.getLabelList().forEach(label -> {
                labelNameList.add(label.getName());
            });
            bean.setLabelNameList(labelNameList);
            return bean;
        }catch (Exception ex){
            throw new RequestProcessException(ex.getMessage());
        }
    }


    private PageRequest buildPageRequest(QueryEventRequest request) {
        Sort.Direction direction = Sort.Direction.DESC;
        if (request.getDirection().equals("ASC")){
            direction = Sort.Direction.ASC;
        }
        if (request.getSortBy() == null){
            request.setSortBy("createdAt");
        }
        Sort sort = Sort.by(direction, request.getSortBy());
        return PageRequest.of(request.getPageNumber(), 20, sort);
    }

}
