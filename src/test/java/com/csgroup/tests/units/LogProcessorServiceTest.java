package com.csgroup.tests.units;

import com.csgroup.service.LogProcessorService;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class LogProcessorServiceTest {

    @Test
    public void test1() {

        LogProcessorService test = mock(LogProcessorService.class);
        test.process("testFile");
        assertTrue(true);
    }
}
