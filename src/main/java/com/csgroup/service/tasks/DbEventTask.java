package com.csgroup.service.tasks;

import com.csgroup.model.DbEvent;
import com.csgroup.repositories.DbEventRepository;
import com.csgroup.service.EventsProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DbEventTask implements Runnable {

    private EventsProcessorService eventAggregatorServiceImpl;

    private DbEventRepository dbrepo;

    private boolean isRunning = true;

    public DbEventTask(EventsProcessorService eventService, DbEventRepository repo) {
        eventAggregatorServiceImpl = eventService;
        dbrepo = repo;
    }

    @Override
    public void run() {

        while(isRunning) {
            DbEvent event = eventAggregatorServiceImpl.pullEvent();
            if (event.getId()!=null) {
                dbrepo.save(event);
            } else {
               isRunning = false;
            }
        }

    }
}
