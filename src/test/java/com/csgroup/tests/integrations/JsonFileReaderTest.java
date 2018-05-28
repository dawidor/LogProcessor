package com.csgroup.tests.integrations;


import com.csgroup.config.JacksonConfig;
import com.csgroup.service.JsonFileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({JacksonConfig.class})
@ComponentScan(basePackages = {"com.csgroup"})
@TestPropertySource(locations="classpath:application-test.properties")
@Slf4j
public class JsonFileReaderTest {

    @Autowired
    private JsonFileService parser;

    @Test
    public void testJsonMppingAllBad() throws FileNotFoundException {
        File f = ResourceUtils.getFile(JsonFileReaderTest.class.getResource(
                "/inputs/input1.json"));

        long successCounter = parser.readFile(f.getAbsolutePath());
        assertEquals(0, successCounter);
    }

    @Test
    public void testJsonMppingFewBad() throws FileNotFoundException {
        File f = ResourceUtils.getFile(JsonFileReaderTest.class.getResource(
                "/inputs/pdf-input-orginal.json"));

        long successCounter = parser.readFile(f.getAbsolutePath());
        assertEquals(4, successCounter);
    }

    @Test
    public void testJsonMppingAllGood() throws FileNotFoundException {
        File f = ResourceUtils.getFile(JsonFileReaderTest.class.getResource(
                "/inputs/pdf-input-orginal-corrected.json"));

        long successCounter = parser.readFile(f.getAbsolutePath());
        assertEquals(6, successCounter);
    }

}
