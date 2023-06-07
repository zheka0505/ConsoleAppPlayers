package ru.inno.course.player.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class MyTestWatcher implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        TestWatcher.super.testSuccessful(context);
        System.out.println("Успешное выполнение теста: " + context.getDisplayName());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        TestWatcher.super.testFailed(context, cause);
        if (context.getTags().contains("CRITICAL")){
            System.out.println("CRITICAL FAILURE");
            // telegram-channel post HTTP GET
            // email
        }

    }
}
