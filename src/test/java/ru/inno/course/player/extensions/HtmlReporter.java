package ru.inno.course.player.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HtmlReporter implements TestWatcher, AfterAllCallback, BeforeAllCallback {
    // "Test 1" - OK | FAILED | SKIPPED | BROKEN
    private Map<String, String> statuses;

    private static final String htmlHead = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <style>
                    .ok{
                        background-color: #91c891
                    }
                    .fail{
                        background-color: #ff9991
                    }
                </style>
                <meta charset="UTF-8">
                <title>Report</title>
            </head>
            <body> 
                <table>
                    <thead>
                        <th>Name</th>
                        <th>Status</th>
                    </thead>
                    <tbody>
                            
            """;

    private static final String htmlTail = "</tbody></table></body></html>";

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        TestWatcher.super.testDisabled(context, reason);

        String testName = context.getDisplayName();
        statuses.put(testName, "SKIPPED");
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        TestWatcher.super.testSuccessful(context);
        statuses.put(context.getDisplayName(), "OK");
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        TestWatcher.super.testAborted(context, cause);
        statuses.put(context.getDisplayName(), "BROKEN");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        TestWatcher.super.testFailed(context, cause);
        statuses.put(context.getDisplayName(), "FAILED");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        Path path = getPath(context.getDisplayName());
        Files.writeString(path, htmlHead, StandardOpenOption.CREATE_NEW);

        for (String testName : statuses.keySet()) {
            //OK | FAILED | SKIPPED | BROKEN
            String status = statuses.get(testName);
            String line;
            if(status.equals("OK")){
                line = "<tr class=\"ok\">";
            } else
            {
                line = "<tr class=\"fail\">";
            }
            line += "<td>" + testName + "</td><td>" + status + "</td></tr>";
            Files.writeString(path, line, StandardOpenOption.APPEND);
        }

        Files.writeString(path, htmlTail, StandardOpenOption.APPEND);
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        statuses = new HashMap<>();
    }

    private Path getPath(String name) {
        return Path.of("report" +name+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM__hh_mm_ss")) + ".html");
    }
}


