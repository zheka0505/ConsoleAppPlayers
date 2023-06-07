package ru.inno.course.player.extensions;

import org.junit.jupiter.api.extension.*;

import java.nio.file.Files;
import java.nio.file.Path;


public class FileHelperExt implements AfterEachCallback, BeforeEachCallback, BeforeAllCallback, AfterAllCallback {
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Files.deleteIfExists(Path.of("data.json"));
        // n
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        //1
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        // 1
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // n
    }
}
