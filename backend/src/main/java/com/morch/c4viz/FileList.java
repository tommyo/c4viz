package com.morch.c4viz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileList {
    private static String path() throws RuntimeException {
        String path = System.getenv("C4VIZ_SOURCE_DIR");
        if (path == null) {
            throw new RuntimeException("C4VIZ_SOURCE_DIR environment must be set");
        }
        return path;
    }

    public static String[] list() {
        String path = path();
        List<String> local = Stream.of(new File(path).listFiles())
            .filter(file -> !file.isDirectory())
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
