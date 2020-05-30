package com.frizo.ucc.server.dao;

import com.frizo.ucc.server.UccServerApplication;
import com.frizo.ucc.server.dao.event.EventRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UccServerApplication.class)
@WebAppConfiguration
public class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;

//    @Test
//    public void testFindAllByTitleLikeOrDescriptionLike(){
//        String keyword = "%吉%";
//        List<Event> events = eventRepository.findByTitleContainsOrDescriptionContains(keyword);
//        events.forEach(event -> {
//            System.out.println("getDescription " + event.getDescription());
//            System.out.println("event " + event.getTitle());
//            System.out.println("-------------------------");
//        });
//    }

}
