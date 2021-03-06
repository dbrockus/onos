/*
 * Copyright 2014 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onlab.util;

import com.google.common.base.Strings;
import com.google.common.primitives.UnsignedLongs;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import static java.nio.file.Files.delete;
import static java.nio.file.Files.walkFileTree;
import static org.onlab.util.GroupedThreadFactory.groupedThreadFactory;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Miscellaneous utility methods.
 */
public abstract class Tools {

    private Tools() {
    }

    private static final Logger log = getLogger(Tools.class);

    /**
     * Returns a thread factory that produces threads named according to the
     * supplied name pattern.
     *
     * @param pattern name pattern
     * @return thread factory
     */
    public static ThreadFactory namedThreads(String pattern) {
        return new ThreadFactoryBuilder()
                .setNameFormat(pattern)
                        // FIXME remove UncaughtExceptionHandler before release
                .setUncaughtExceptionHandler((t, e) -> log.error("Uncaught exception on {}", t.getName(), e)).build();
    }

    /**
     * Returns a thread factory that produces threads named according to the
     * supplied name pattern and from the specified thread-group. The thread
     * group name is expected to be specified in slash-delimited format, e.g.
     * {@code onos/intent}. The thread names will be produced by converting
     * the thread group name into dash-delimited format and pre-pended to the
     * specified pattern.
     *
     * @param groupName group name in slash-delimited format to indicate hierarchy
     * @param pattern   name pattern
     * @return thread factory
     */
    public static ThreadFactory groupedThreads(String groupName, String pattern) {
        return new ThreadFactoryBuilder()
                .setThreadFactory(groupedThreadFactory(groupName))
                .setNameFormat(groupName.replace(GroupedThreadFactory.DELIMITER, "-") + "-" + pattern)
                        // FIXME remove UncaughtExceptionHandler before release
                .setUncaughtExceptionHandler((t, e) -> log.error("Uncaught exception on {}", t.getName(), e)).build();
    }

    /**
     * Returns a thread factory that produces threads with MIN_PRIORITY.
     *
     * @param factory backing ThreadFactory
     * @return thread factory
     */
    public static ThreadFactory minPriority(ThreadFactory factory) {
        return new ThreadFactoryBuilder()
                .setThreadFactory(factory)
                .setPriority(Thread.MIN_PRIORITY)
                .build();
    }

    /**
     * Returns true if the collection is null or is empty.
     *
     * @param collection collection to test
     * @return true if null or empty; false otherwise
     */
    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Converts a string from hex to long.
     *
     * @param string hex number in string form; sans 0x
     * @return long value
     */
    public static long fromHex(String string) {
        return UnsignedLongs.parseUnsignedLong(string, 16);
    }

    /**
     * Converts a long value to hex string; 16 wide and sans 0x.
     *
     * @param value long value
     * @return hex string
     */
    public static String toHex(long value) {
        return Strings.padStart(UnsignedLongs.toString(value, 16), 16, '0');
    }

    /**
     * Converts a long value to hex string; 16 wide and sans 0x.
     *
     * @param value long value
     * @param width string width; zero padded
     * @return hex string
     */
    public static String toHex(long value, int width) {
        return Strings.padStart(UnsignedLongs.toString(value, 16), width, '0');
    }

    /**
     * Suspends the current thread for a specified number of millis.
     *
     * @param ms number of millis
     */
    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    /**
     * Slurps the contents of a file into a list of strings, one per line.
     *
     * @param path file path
     * @return file contents
     */
    public static List<String> slurp(File path) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;

        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Purges the specified directory path.&nbsp;Use with great caution since
     * no attempt is made to check for symbolic links, which could result in
     * deletion of unintended files.
     *
     * @param path directory to be removed
     * @throws java.io.IOException if unable to remove contents
     */
    public static void removeDirectory(String path) throws IOException {
        DirectoryDeleter visitor = new DirectoryDeleter();
        walkFileTree(Paths.get(path), visitor);
        if (visitor.exception != null) {
            throw visitor.exception;
        }
    }

    /**
     * Purges the specified directory path.&nbsp;Use with great caution since
     * no attempt is made to check for symbolic links, which could result in
     * deletion of unintended files.
     *
     * @param dir directory to be removed
     * @throws java.io.IOException if unable to remove contents
     */
    public static void removeDirectory(File dir) throws IOException {
        DirectoryDeleter visitor = new DirectoryDeleter();
        walkFileTree(Paths.get(dir.getAbsolutePath()), visitor);
        if (visitor.exception != null) {
            throw visitor.exception;
        }
    }

    // Auxiliary path visitor for recursive directory structure removal.
    private static class DirectoryDeleter extends SimpleFileVisitor<Path> {

        private IOException exception;

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attributes)
                throws IOException {
            if (attributes.isRegularFile()) {
                delete(file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path directory, IOException ioe)
                throws IOException {
            delete(directory);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException ioe)
                throws IOException {
            this.exception = ioe;
            return FileVisitResult.TERMINATE;
        }
    }


    /**
     * Copies the specified directory path.&nbsp;Use with great caution since
     * no attempt is made to check for symbolic links, which could result in
     * copy of unintended files.
     *
     * @param src directory to be copied
     * @param dst destination directory to be removed
     * @throws java.io.IOException if unable to remove contents
     */
    public static void copyDirectory(String src, String dst) throws IOException {
        walkFileTree(Paths.get(src), new DirectoryCopier(src, dst));
    }

    /**
     * Copies the specified directory path.&nbsp;Use with great caution since
     * no attempt is made to check for symbolic links, which could result in
     * copy of unintended files.
     *
     * @param src directory to be copied
     * @param dst destination directory to be removed
     * @throws java.io.IOException if unable to remove contents
     */
    public static void copyDirectory(File src, File dst) throws IOException {
        walkFileTree(Paths.get(src.getAbsolutePath()),
                     new DirectoryCopier(src.getAbsolutePath(),
                                         dst.getAbsolutePath()));
    }

    // Auxiliary path visitor for recursive directory structure copying.
    private static class DirectoryCopier extends SimpleFileVisitor<Path> {
        private Path src;
        private Path dst;
        private StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;

        DirectoryCopier(String src, String dst) {
            this.src = Paths.get(src);
            this.dst = Paths.get(dst);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Path targetPath = dst.resolve(src.relativize(dir));
            if (!Files.exists(targetPath)) {
                Files.createDirectory(targetPath);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.copy(file, dst.resolve(src.relativize(file)), copyOption);
            return FileVisitResult.CONTINUE;
        }
    }

}
