package com.csgroup.tests.units;

import com.csgroup.enums.State;
import com.csgroup.model.LogEvent;
import com.csgroup.tests.integrations.JsonFileReaderTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class LargeJsonGenertor {

    @Test
    public void generate() throws IOException {
        LogEvent e1 = getEventStart1();
        LogEvent e2 = getEventStart2();

        ObjectMapper mapper = new ObjectMapper();
        File f = new File("C:\\dev\\LogMonitoringService\\src\\test\\resources\\inputs\\large1.json");
        f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (int i=0; i<10000000; i++) {
            e1.setId("a"+i);
            e2.setId("a"+i);

            if (i%10000==0) {
                System.out.print("Generating id=" + i + "/10000000\n");
                bw.flush();
            }
            bw.write(mapper.writeValueAsString(e1));
            bw.newLine();
            bw.write(mapper.writeValueAsString(e2));
            bw.newLine();
        }

        bw.close();

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
