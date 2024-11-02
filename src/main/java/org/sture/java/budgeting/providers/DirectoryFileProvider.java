package org.sture.java.budgeting.providers;

import org.sture.java.budgeting.utils.TestHarness;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryFileProvider {
    private static final Path dataDirectory = Paths.get("data");
    private static final Path dataDirectoryDuringTestHarness = Paths.get("data", "test");

    /**
     * Gets the path to the active data directory.
     * This is equivalent to {@code GetTestDataDirectory} if running from TestHarness
     * @return Path
     */
    public static Path GetDataDirectory(){
        if(TestHarness.isRunningFromTestHarness()){
            CheckDataDirectoryAndTestDirectoryAreDifferent();
            return dataDirectoryDuringTestHarness;
        }
        return dataDirectory;
    }

    /**
     * Returns the path to the testing data directory.
     * @return Path
     */
    public static Path GetTestDataDirectory(){
        return dataDirectoryDuringTestHarness;
    }

    private static void CheckDataDirectoryAndTestDirectoryAreDifferent(){
        if(dataDirectory.toString().equals(dataDirectoryDuringTestHarness.toString()))
            throw new RuntimeException("Data directory and testing data directory must be different");
    }
}
