package com.we.suck.at.java.budgettingiguess.services;

import com.we.suck.at.java.budgettingiguess.test.TestHarness;

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
