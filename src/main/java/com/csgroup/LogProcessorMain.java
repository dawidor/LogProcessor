package com.csgroup;

import com.csgroup.service.LogProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import static java.lang.System.exit;


@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class LogProcessorMain implements CommandLineRunner {

    @Autowired
    private LogProcessorService processorService;;

    public static void main(String[] args) throws Exception {

        SpringApplication app = new SpringApplication(LogProcessorMain.class);
        app.setBannerMode(Banner.Mode.OFF);

        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {

        if (args.length > 0) {
            String filename = args[0];
            log.info("File to process: " + filename);
            processorService.process(filename);

        } else {
            log.error("You need to specify input file as argument to this program!!!");
            exit(-1);
        }



        //exit(0);
    }
}
