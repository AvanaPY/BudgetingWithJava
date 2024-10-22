package org.sture.java.budgeting.test;

public class TestHarness {
    private static boolean runningFromTestHarness = false;

    public static void setRunningFromTestHarnessTrue(){
        runningFromTestHarness = true;
    }

    public static boolean isRunningFromTestHarness(){
        return runningFromTestHarness;
    }

    public static void resetUnderTest() {
        if(isRunningFromTestHarness())
            runningFromTestHarness = false;
    }
}
