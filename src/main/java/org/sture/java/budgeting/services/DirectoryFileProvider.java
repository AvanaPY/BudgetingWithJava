package org.sture.java.budgeting.services;

import org.sture.java.budgeting.utils.TestHarness;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryFileProvider {
    private static final Path dataDirectory = Paths.get("data");
    private static final Path dataDirectoryDuringTestHarness = Paths.get("data", "test");

    public static Path GetDataDirectory(){
        if(TestHarness.isRunningFromTestHarness()){
            return dataDirectoryDuringTestHarness;
        }
        return dataDirectory;
    }
}
