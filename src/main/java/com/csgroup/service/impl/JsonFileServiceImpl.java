package com.csgroup.service.impl;

import com.csgroup.model.DbEvent;
import com.csgroup.service.EventsProcessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.csgroup.model.LogEvent;
import com.csgroup.service.JsonFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
@Slf4j
public class JsonFileServiceImpl implements JsonFileService {

    @Autowired
    private EventsProcessorService eventAggregatorService;

    @Autowired
    private ObjectMapper mapper;

    @Value("${db.queue.read.threads:1}")
    private int dbThreads;

    private long successReadsCounter = 0;

    @Override
    public long readFile(String filename) {
        File file = new File(filename);
        try (Stream linesStream = Files.lines(file.toPath())) {
            linesStream.forEach(line -> {

                try {
                    LogEvent event = mapper
                            .readerFor(LogEvent.class)
                            .readValue(line.toString());

                    if (validateEvent(event)) {

                        successReadsCounter++;

                        if (log.isDebugEnabled()) {
                            log.debug(event.toString());
                        }

                        eventAggregatorService.publishEvent(event);

                    } else {
                        log.error("Event validation failed: " + event.toString());
                    }
                } catch (IOException e) {
                    log.error("Error when processing line: "+ line, e);
                }
            });

        } catch (IOException e) {
            log.error("Error when reading from stream ", e);
        }

        eventAggregatorService.getUnprocessedLogEvents()
                .stream().forEach(v-> {
                    log.error("Orphan event: " + v.toString());
        });

        sendPoisonEvents();

        long result = successReadsCounter;
        successReadsCounter = 0;

        return result;
    }

    private boolean validateEvent(LogEvent event) {
        boolean result = true;
        if (event==null || event.getId()==null || event.getState()==null
                ||event.getTimestamp()==null || event.getTimestamp()<Instant.EPOCH.toEpochMilli())
            result = false;
        return result;
    }

    private void sendPoisonEvents() {
        for (int i=0; i<dbThreads; i++) {
            eventAggregatorService.sendPoisonEvent();
        }
    }
}
