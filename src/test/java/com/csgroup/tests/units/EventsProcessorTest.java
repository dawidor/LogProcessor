package com.csgroup.tests.units;

import com.csgroup.enums.State;
import com.csgroup.model.DbEvent;
import com.csgroup.model.LogEvent;
import com.csgroup.service.EventsProcessorService;
import com.csgroup.service.impl.EventsProcessorServiceImpl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class EventsProcessorTest {

    @Test
    public void publishEventMock1() {
        EventsProcessorService test = mock(EventsProcessorService.class);
        test.publishEvent(getEventStart1());
        assertTrue(true);
    }

    @Test
    public void publishEvent1() {

        EventsProcessorServiceImpl service = getEventsProcessorService();
        BlockingQueue<DbEvent> queue = new ArrayBlockingQueue<>(5);
        service.setDbQueue(queue);
        LogEvent event1 = getEventStart1();

        service.publishEvent(getEventStart1());
        DbEvent result = queue.poll();

        assertNull(result);
    }

    @Test
    public void publishPullEvent2() {

        EventsProcessorServiceImpl service = getEventsProcessorService();
        BlockingQueue<DbEvent> queue = new ArrayBlockingQueue<>(5);
        service.setDbQueue(queue);
        LogEvent event1 = getEventStart2();

        service.publishEvent(getEventStart2());
        DbEvent result = queue.poll();
        assertNull(result);
    }

    @Test
    public void publishPullEvent1and2() {

        EventsProcessorServiceImpl service = getEventsProcessorService();
        BlockingQueue<DbEvent> queue = new ArrayBlockingQueue<>(5);
        service.setDbQueue(queue);
        LogEvent event1 = getEventStart1();
        LogEvent event2 = getEventStart2();

        service.publishEvent(getEventStart1());
        service.publishEvent(getEventStart2());
        DbEvent result = service.pullEvent();
        assertEquals(getDbEvent1(), result);
    }

    private EventsProcessorServiceImpl getEventsProcessorService() {
        EventsProcessorServiceImpl service = new EventsProcessorServiceImpl();
        Map<String, LogEvent> map = new HashMap<>();
        service.setAggregationLogMap(map);

        service.setQueueCapacity(1);
        service.setDurationThreshold(4L);
        return service;
    }

    @Test
    public void pullEventMock1() {

        BlockingQueue<DbEvent> queue = new ArrayBlockingQueue<>(5);
        queue.add(getDbEvent1());



        EventsProcessorServiceImpl service = new EventsProcessorServiceImpl();
        service.setDbQueue(queue);
        DbEvent event = service.pullEvent();
        assertNotNull(true);
    }

    private DbEvent getDbEvent1() {
        DbEvent event = new DbEvent();
        event.setId("abcd");
        event.setType("App");
        event.setDuration(1L);
        event.setAlert(false);
        event.setHost("host1");
        return event;
    }

    private LogEvent getEventStart1() {
        LogEvent event = new LogEvent();
        event.setId("abcd");
        event.setState(State.STARTED);
        event.setTimestamp(1491377495216L);
        event.setHost("host1");
        event.setType("App");
        return event;
    }

    private LogEvent getEventStart2() {
        LogEvent event = new LogEvent();
        event.setId("abcd");
        event.setState(State.FINISHED);
        event.setTimestamp(1491377495217L);
        event.setHost("host1");
        event.setType("App");
        return event;
    }

}
