package com.morch.c4viz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class FileList implements FileChangeListener {

    @Autowired
    SimpMessagingTemplate broker;

    public static String path() throws RuntimeException {
        String path = System.getenv("C4VIZ_SOURCE_DIR");
        if (path == null) {
            throw new RuntimeException("C4VIZ_SOURCE_DIR environment must be set");
        }
        return path;
    }

    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        for (ChangedFiles files:changeSet) {
            for (ChangedFile file:files.getFiles()) {
                broker.convertAndSend("/topic/change", file.getFile().getName());
            }
        }
    }

    public String[] list() {
        String path = path();
        List<String> local = Stream.of(new File(path).listFiles())
            .map(File::getName)
            .collect(Collectors.toList());
        
        String[] out = new String[local.size()];
        return local.toArray(out);
    }

    public static Path resolve(String name) throws IOException {
        String path = path();
        Path resolved = Paths.get(path, name);
        if (!Files.exists(resolved)) {
            throw new FileNotFoundException("Couldn't find " + resolved);
        }
        return resolved;
    }
}
