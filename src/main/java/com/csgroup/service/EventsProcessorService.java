package com.csgroup.service;

import com.csgroup.model.DbEvent;
import com.csgroup.model.LogEvent;

import java.util.Collection;

public interface EventsProcessorService {

    void publishEvent(LogEvent event);

    DbEvent pullEvent();

    Collection<LogEvent> getUnprocessedLogEvents();

    void sendPoisonEvent();
}
