package com.csgroup.tests.integrations;

import com.csgroup.config.JacksonConfig;
import com.csgroup.repositories.DbEventRepository;
import com.csgroup.service.LogProcessorService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
//@ActiveProfiles("test")
@ComponentScan(basePackages = {"com.csgroup"})
@Import({JacksonConfig.class})
@TestPropertySource(locations="classpath:application-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogProcessorServiceTest {
    @Autowired
    private LogProcessorService logProcessorService;

    @Autowired
    private DbEventRepository dbEventRepository;

    @Test
    public void testEventsInDbAllGood() throws FileNotFoundException {
        File f = ResourceUtils.getFile(JsonFileReaderTest.class.getResource(
                "/inputs/pdf-input-orginal-corrected.json"));
        logProcessorService.process(f.getAbsolutePath());
        long count = dbEventRepository.count();
        assertEquals(3, count);
    }


}
