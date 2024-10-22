package com.we.suck.at.java.budgettingiguess.services;

import com.we.suck.at.java.budgettingiguess.dto.TrackingEntryDTO;
import com.we.suck.at.java.budgettingiguess.models.TrackingEntry;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class TrackingEntryStoreService {
    private final String storeFileName = "trackingEntry.store";
    private final Path dataDirectory = DirectoryFileProvider.GetDataDirectory();
    private final Path storeFilePath = Paths.get(dataDirectory.toString(), storeFileName);

    public TrackingEntryStoreService() {
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TrackingEntry[] Read() {
        try {
            var fileStream = new TrackingEntryFileStream(storeFilePath);
            var object = fileStream.readEntries();
            fileStream.close();

            // Must use a toList() cast here otherwise Java gets upset :(
            return Arrays.stream(object).map(TrackingEntryDTO::toTrackingEntry).toList().toArray(new TrackingEntry[0]);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Store(TrackingEntry entry) {
        var dto = new TrackingEntryDTO(entry);
        Store(dto);
    }

    public void Store(TrackingEntry[] entries) {
        TrackingEntryDTO[] dtoEntries = Arrays.stream(entries).map(TrackingEntryDTO::new).toList().toArray(new TrackingEntryDTO[0]);
        Store(dtoEntries);
    }

    public boolean DeleteStore(){
        File f = new File(storeFilePath.toUri());
        return f.delete();
    }

    private void Store(TrackingEntryDTO object){
        Store(new TrackingEntryDTO[] { object });
    }

    private void Store(TrackingEntryDTO[] objects){
        try {
            var fileStream = new TrackingEntryFileStream(storeFilePath);
            fileStream.writeEntries(objects);
            fileStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Stream error: " + e.getMessage());
        }
    }

    private static class TrackingEntryFileStream {
        private final Path path;
        private FileInputStream fileInputStream;
        private ObjectInputStream objectInputStream;
        private FileOutputStream fileOutputStream;
        private ObjectOutputStream objectOutputStream;
        public TrackingEntryFileStream(Path path) {
            this.path = path;
        }

        public void writeEntries(TrackingEntryDTO[] entries) throws FileNotFoundException {
            if(fileOutputStream == null) {
                fileOutputStream = new FileOutputStream(path.toUri().getPath());
                try {
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                objectOutputStream.writeObject(entries);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public TrackingEntryDTO[] readEntries() throws FileNotFoundException {
            if(fileInputStream == null){
                fileInputStream = new FileInputStream(path.toUri().getPath());
                try {
                    objectInputStream = new ObjectInputStream(fileInputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                return (TrackingEntryDTO[])objectInputStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void close() throws IOException {
            if(fileOutputStream != null) {
                objectOutputStream.close();
                fileOutputStream.close();
            }
            if(fileInputStream != null) {
                objectInputStream.close();
                fileInputStream.close();
            }
        }
    }
}
