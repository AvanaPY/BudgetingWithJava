package org.sture.java.budgeting.store;

import org.sture.java.budgeting.services.job.Job;
import org.sture.java.budgeting.services.job.BackgroundJobExecutionService;
import org.sture.java.budgeting.providers.DirectoryFileProvider;
import org.sture.java.budgeting.utils.DTO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class StoreService<T, TDto extends DTO<T>> {
    protected final Path dataDirectory = DirectoryFileProvider.GetDataDirectory();
    protected final Path storeFilePath;

    private final DTOConverter<T, TDto> dtoFactory;
    private final BackgroundJobExecutionService jobExecutionerService;
    private UUID backgroundJobId;

    public StoreService(
            String storeFileName,
            DTOConverter<T, TDto> dtoFactory,
            BackgroundJobExecutionService jobExecutionerService) {
        storeFilePath = Paths.get(dataDirectory.toString(), storeFileName);
        this.dtoFactory = dtoFactory;
        this.jobExecutionerService = jobExecutionerService;

        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public final void Store(T object) {
        var dto = dtoFactory.ConvertToDTO(object);
        Store(dto);
    }

    public final void Store(T[] objects) {
        var dtoObjects = Arrays.stream(objects).map(dtoFactory::ConvertToDTO).toList();
        var dtoArray = dtoFactory.DoMagicToDTO(dtoObjects);
        Store(dtoArray);
    }

    public final void Store(List<T> objects) {
        var dtoObjects = objects.stream().map(dtoFactory::ConvertToDTO).toList();
        var dtoArray = dtoFactory.DoMagicToDTO(dtoObjects);
        Store(dtoArray);
    }

    private void Store(TDto obj){
        var lst = new ArrayList<TDto>();
        lst.add(obj);
        var dtoArray = dtoFactory.DoMagicToDTO(lst);
        Store(dtoArray);
    }

    protected void Store(TDto[] dtoObjects) {
        UUID uuid = UUID.randomUUID();
        Job job = new StoreJob<>(
                uuid,
                new DTOFileStream<>(storeFilePath),
                dtoObjects
        );
        job.setupOnDoneCallback(p -> backgroundJobId = null);
        backgroundJobId = jobExecutionerService.LaunchBackgroundJob(job);
    }

    public final boolean DeleteStoreIfExists(){
        File f = new File(storeFilePath.toUri());
        return f.delete();
    }

    public void WaitForBackgroundJobToComplete() {
        if(backgroundJobId != null)
            jobExecutionerService.WaitForJobToFinish(backgroundJobId);
    }

    private static class StoreJob<T> extends Job {
        private final DTOFileStream<T> fileStream;
        private final T[] objects;
        public StoreJob(UUID uuid, DTOFileStream<T> filestream, T[] objects) {
            super(uuid);
            this.fileStream = filestream;
            this.objects = objects;
        }

        @Override
        public void Execute() {
            try {
                UpdateProgression(0);
                UpdateMessage("Writing data...");
                fileStream.writeEntries(objects);
                UpdateProgression(0.5);

                UpdateMessage("Closing stream");
                fileStream.close();

            } catch (IOException e) {
                throw new RuntimeException("Stream error: " + e.getMessage());
            }
        }
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
            } catch (InvalidClassException | ClassNotFoundException e){
                throw new RuntimeException("Failed to read entries. Could not parse class: " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read entries: An IO Error occured.", e);
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
