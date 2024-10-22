package com.we.suck.at.java.budgettingiguess.services;

import com.we.suck.at.java.budgettingiguess.BaseTest;
import com.we.suck.at.java.budgettingiguess.test.TestHarness;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DirectoryFileProviderTest extends BaseTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void TestDataDirectoriesDifferentBetweenTestAndRun(){
        Path directoryUnderTest = DirectoryFileProvider.GetDataDirectory();
        TestHarness.resetUnderTest();
        Path directoryUnderRun = DirectoryFileProvider.GetDataDirectory();
        assertNotEquals(directoryUnderTest.toUri().toString(), directoryUnderRun.toUri().toString());
    }

}