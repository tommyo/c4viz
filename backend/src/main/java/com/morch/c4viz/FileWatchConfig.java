package com.morch.c4viz;

import java.io.File;
import java.time.Duration;

import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileWatchConfig {
    private FileSystemWatcher watch;

    @Autowired
    private FileList listener;

    @Bean
    public FileSystemWatcher fileSystemWatcher() {
        Duration poll = Duration.ofMillis(1000L);
        Duration quiet = Duration.ofMillis(500L);

        watch = new FileSystemWatcher(true, poll, quiet);
        watch.addSourceDirectory(new File(FileList.path()));
        watch.addListener(listener);
        watch.start();
        return watch;
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        if (watch != null) {
            watch.stop();
        }
    }
}
