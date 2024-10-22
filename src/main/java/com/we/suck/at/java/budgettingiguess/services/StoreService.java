package com.we.suck.at.java.budgettingiguess.services;

import com.we.suck.at.java.budgettingiguess.dto.DTOFactory;
import com.we.suck.at.java.budgettingiguess.utils.DTO;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public abstract class StoreService<T, TDto extends DTO<T>> {
    protected final Path dataDirectory = DirectoryFileProvider.GetDataDirectory();
    protected final Path storeFilePath;

    private final DTOFactory<T, TDto> dtoFactory;
    public StoreService(
            String storeFileName,
            DTOFactory<T, TDto> dtoFactory) {
        storeFilePath = Paths.get(dataDirectory.toString(), storeFileName);
        this.dtoFactory = dtoFactory;
    }

    public final T[] Read() {
        try {
            var fileStream = new DTOFileStream<TDto>(storeFilePath);
            var object = fileStream.readEntries();
            fileStream.close();

            // Must use a toList() cast here otherwise Java gets upset :(
            var lst = Arrays.stream(object).map(dtoFactory::ConvertFromDTO).toList();
            return dtoFactory.DoMagicFromDTO(lst);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public final void Store(List<T> objects) {
        var dtoObjects = objects.stream().map(dtoFactory::ConvertToDTO).toList();
        var dtoArray = dtoFactory.DoMagicToDTO(dtoObjects);
        Store(dtoArray);
    }

    protected void Store(TDto[] dtoObjects) {
        try {
            var fileStream = new DTOFileStream<TDto>(storeFilePath);
            fileStream.writeEntries(dtoObjects);
            fileStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Stream error: " + e.getMessage());
        }
    }

    public final boolean DeleteStoreIfExists(){
        File f = new File(storeFilePath.toUri());
        return f.delete();
    }

    private static class DTOFileStream<T> {
        private final Path path;
        private FileInputStream fileInputStream;
        private ObjectInputStream objectInputStream;
        private FileOutputStream fileOutputStream;
        private ObjectOutputStream objectOutputStream;
        public DTOFileStream(Path path) {
            this.path = path;
        }

        public void writeEntries(T[] objects) throws FileNotFoundException {
            if(fileOutputStream == null) {
                fileOutputStream = new FileOutputStream(path.toUri().getPath());
                try {
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                objectOutputStream.writeObject(objects);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked")
        public T[] readEntries() throws FileNotFoundException {
            if(fileInputStream == null){
                fileInputStream = new FileInputStream(path.toUri().getPath());
                try {
                    objectInputStream = new ObjectInputStream(fileInputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                return (T[])objectInputStream.readObject();
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
