package org.sture.java.budgeting.services;

import org.sture.java.budgeting.BaseTest;
import org.sture.java.budgeting.providers.DirectoryFileProvider;
import org.sture.java.budgeting.utils.TestHarness;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DirectoryFileProviderTest extends BaseTest {
    @Test
    void TestDataDirectoriesDifferentBetweenTestAndRun(){
        Path directoryUnderTest = DirectoryFileProvider.GetDataDirectory();
        TestHarness.resetUnderTest();
        Path directoryUnderRun = DirectoryFileProvider.GetDataDirectory();
        assertNotEquals(directoryUnderTest.toUri().toString(), directoryUnderRun.toUri().toString());
    }

}