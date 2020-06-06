package com.frizo.ucc.server.service;

import com.frizo.ucc.server.UccServerApplication;
import com.frizo.ucc.server.payload.request.QueryEventRequest;
import com.frizo.ucc.server.payload.response.bean.EventBean;
import com.frizo.ucc.server.service.event.EventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UccServerApplication.class)
public class EventServiceTest {

    @Autowired
    EventService eventService;

    @Test
    public void testGetById(){
        Long id = 1L;
        EventBean bean = eventService.getById(id);
        System.out.println(bean.getTitle());
    }

    @Test
    public void testFindAllByKeywords(){
        QueryEventRequest request = new QueryEventRequest();
        //request.setKeywords("");
        request.setPageNumber(0);
        request.setSortBy("createdAt");
        request.setDirection("DESC");

//        LocalDateTime ldtA = LocalDateTime.of(2020, 5, 25, 0, 0);
//        Instant startTimeA = ldtA.toInstant(ZoneOffset.UTC);
//        System.out.println("startTimeA: " + startTimeA);
//        request.setStartTimeA(startTimeA);
//
//        LocalDateTime ldtB = LocalDateTime.of(2020, 5, 28, 0, 0);
//        Instant startTimeB = ldtB.toInstant(ZoneOffset.UTC);
//        System.out.println("startTimeB: " + startTimeB);
//        request.setStartTimeB(startTimeB);

        List<EventBean> beans = eventService.findAllByQuerySpec(request);
        beans.forEach(bean -> {
            System.out.println("標題: " + bean.getTitle());
            System.out.println("描述: " + bean.getDescription());
            System.out.println("人氣: " + bean.getLikes());
            if (bean.getLabelNameSet() != null){
                bean.getLabelNameSet().forEach(label -> {
                    System.out.println("標籤: " + label);
                });
            }
            System.out.println("活動開始時間: " + bean.getEventStartTime());
            System.out.println("------------------");
        });
    }

}