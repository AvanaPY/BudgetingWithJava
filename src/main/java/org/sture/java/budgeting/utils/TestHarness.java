package org.sture.java.budgeting.utils;

import org.sture.java.budgeting.developer.Developer;
import org.sture.java.budgeting.services.DirectoryFileProvider;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestHarness {
    private static boolean runningFromTestHarness = false;

    public static void setRunningFromTestHarnessTrue(){
        runningFromTestHarness = true;
    }

    public static boolean isRunningFromTestHarness(){
        return runningFromTestHarness;
    }

    public static void CheckIsRunningFromTestHarness(){
        if(!isRunningFromTestHarness())
            throw new RuntimeException("Running from test harness!");
    }

    public static void resetUnderTest() {
        if(isRunningFromTestHarness())
            runningFromTestHarness = false;
    }

    private static Path GetTestDirectoryOrThrowIfNotUnderTestWithDoubleCheckBecauseWhatTheFrick(){
        CheckIsRunningFromTestHarness();
        Path testDirectory = DirectoryFileProvider.GetTestDataDirectory();
        Path currentDirectory = DirectoryFileProvider.GetDataDirectory();
        if(testDirectory != currentDirectory)
            throw new RuntimeException("Test Directory and Current Directory do not match.");
        return testDirectory;
    }

    public static void CreateTestStoreDirectory(){
        CheckIsRunningFromTestHarness();
        Path directory = GetTestDirectoryOrThrowIfNotUnderTestWithDoubleCheckBecauseWhatTheFrick();
        File f = new File(String.valueOf(directory));
        boolean success = f.mkdirs();
        if(!success){
            Developer.DebugMessage("Tried to create test store directory but failed.");
        }
    }

    public static void DeleteTestStoreDirectory(){
        CheckIsRunningFromTestHarness();

        Path directory = GetTestDirectoryOrThrowIfNotUnderTestWithDoubleCheckBecauseWhatTheFrick();
        File f = new File(String.valueOf(directory));
        if(f.exists() && f.isDirectory()){
           deleteDirectory(f);
        }
    }

    private static void deleteDirectory(File file){
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null) {
                for(File f : files){
                    if(!Files.isSymbolicLink(f.toPath())){
                        deleteDirectory(f);
                    }
                }
            }
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }
}
