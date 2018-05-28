package com.csgroup.service.impl;

import com.csgroup.enums.State;
import com.csgroup.model.DbEvent;
import com.csgroup.model.LogEvent;
import com.csgroup.service.EventsProcessorService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Setter
@Slf4j
public class EventsProcessorServiceImpl implements EventsProcessorService, InitializingBean {

    public static final String INCORRECT_EVENTS_STATE = "Incorrect events state";
    public static final String INTERRUPTED_EVENT_PUBLISHING = "Interrupted event publishing";
    public static final String INTERRUPTED_EVENT_CONSUMING = "Interrupted event consuming";

    private Map<String, LogEvent> aggregationLogMap;
    private BlockingQueue<DbEvent> dbQueue;

    @Value("${db.queue.capacity:1000}")
    private int queueCapacity;

    @Value("${logs.duration.threshold:4}")
    private Long durationThreshold;

    @Override
    public void afterPropertiesSet() throws Exception {
        aggregationLogMap = new ConcurrentHashMap<>();
        dbQueue = new ArrayBlockingQueue<>(queueCapacity);
    }

    @Override
    public void publishEvent(LogEvent event) {

        if (aggregationLogMap.putIfAbsent(event.getId(), event)!=null) {
            aggregationLogMap.computeIfPresent(event.getId(), (k, v) -> {
                LogEvent previous = aggregationLogMap.remove(k);

                if (previous.getState() != null && event.getState() != null
                        && previous.getState() != event.getState()) {
                    DbEvent ev = computeDbEvent(
                            previous.getState() == State.STARTED ? previous : event,
                            previous.getState() == State.STARTED ? event : previous);
                    try {
                        dbQueue.put(ev);
                        log.info("Event added to queue: " + ev.toString() );
                    } catch (InterruptedException e) {
                        log.error(INTERRUPTED_EVENT_PUBLISHING + "\n" + ev.toString(), e);
                    }
                } else {
                    log.error(INCORRECT_EVENTS_STATE + ": \n"
                            + previous.toString() + "\n" + event.toString());
                }
                return null;
            });
        }
    }

    @Override
    public DbEvent pullEvent() {
        try {
            DbEvent event = dbQueue.take();

            if (log.isDebugEnabled()) {
                log.debug("Dequeue event: " + event.toString());
            }

            return event;
        } catch (InterruptedException e) {
            log.error(INTERRUPTED_EVENT_CONSUMING, e);
            return null;
        }
    }

    private DbEvent computeDbEvent(LogEvent previous, LogEvent current) {
        DbEvent newDbEvent = new DbEvent();
        newDbEvent.setId(previous.getId());
        newDbEvent.setHost(previous.getHost() != null ? previous.getHost() : current.getHost());
        newDbEvent.setType(previous.getType() != null ? previous.getType() : current.getType());
        newDbEvent.setDuration(current.getTimestamp() - previous.getTimestamp());
        if (newDbEvent.getDuration() > durationThreshold) {
            newDbEvent.setAlert(true);
        }
        return newDbEvent;
    }
    @Override
    public Collection<LogEvent> getUnprocessedLogEvents() {
        return aggregationLogMap.entrySet().stream()
                .map(v->v.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public void sendPoisonEvent() {
        dbQueue.add(new DbEvent());
    }
}
