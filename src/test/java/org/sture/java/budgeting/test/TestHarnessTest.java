package org.sture.java.budgeting.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestHarnessTest {

    @BeforeEach
    void beforeEach(){
        TestHarness.setRunningFromTestHarnessTrue();
        TestHarness.resetUnderTest();
    }

    @Test
    void setRunningFromTestHarnessTrue() {
        TestHarness.setRunningFromTestHarnessTrue();
        assertTrue(TestHarness.isRunningFromTestHarness());
    }
}