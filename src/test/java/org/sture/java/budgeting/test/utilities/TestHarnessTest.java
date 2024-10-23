package org.sture.java.budgeting.test.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sture.java.budgeting.utils.TestHarness;

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