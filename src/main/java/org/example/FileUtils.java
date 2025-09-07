package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static final int FILE_APPEND = 1;

    /**
     * Mimics PHP's file_put_contents.
     *
     * @param path   The file path
     * @param data   The string to write
     * @return       Number of bytes written
     * @throws IOException If an I/O error occurs
     */
    public static int file_put_contents(String path, String data) throws IOException {
        return file_put_contents(path, data, 0);
    }

    /**
     * Mimics PHP's file_put_contents with flags.
     *
     * @param path   The file path
     * @param data   The string to write
     * @param flags  Use FILE_APPEND to append instead of overwrite
     * @return       Number of bytes written
     * @throws IOException If an I/O error occurs
     */
    public static int file_put_contents(String path, String data, int flags) throws IOException {
        File file = new File(path);

        if ((flags & FILE_APPEND) == FILE_APPEND) {
            try (FileWriter fw = new FileWriter(file, true)) {
                fw.write(data);
            }
        } else {
            Files.write(Paths.get(path), data.getBytes(StandardCharsets.UTF_8));
        }

        return data.getBytes(StandardCharsets.UTF_8).length;
    }

    /**
     * Mimics PHP's file_get_contents.
     *
     * @param path   The file path
     * @return       File contents as string
     * @throws IOException If an I/O error occurs
     */
    public static String file_get_contents(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
