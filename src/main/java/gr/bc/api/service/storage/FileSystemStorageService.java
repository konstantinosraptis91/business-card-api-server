package gr.bc.api.service.storage;

//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.stereotype.Service;
//import org.springframework.util.FileSystemUtils;
//import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Konstantinos Raptis
 */
//@Service
//public class FileSystemStorageService implements StorageService {
//
//    private final Path rootLocation;
//
//    @Autowired
//    public FileSystemStorageService(StorageProperties properties) {
//        this.rootLocation = Paths.get(properties.getLocation());
//    }
//
//    @Override
//    public void init() {
//        try {
//            Files.createDirectory(rootLocation);
//        } catch (IOException ex) {
//            throw new StorageException("Could not initialize storage", ex);
//        }
//    }
//
//    @Override
//    public void store(MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
//            }
//            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
//        } catch (IOException ex) {
//            throw new StorageException("Failed to store file " + file.getOriginalFilename(), ex);
//        }
//    }
//
//    @Override
//    public Path load(String fileName) {
//        return rootLocation.resolve(fileName);
//    }
//
//    @Override
//    public Resource loadAsResource(String fileName) {
//        try {
//            Path file = load(fileName);
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                throw new StorageFileNotFoundException("Could not read file: " + fileName);
//            }
//        } catch (MalformedURLException ex) {
//            throw new StorageFileNotFoundException("Could not read file: " + fileName, ex);
//        }
//    }
//
//    @Override
//    public boolean delete(String fileName) {
//        return FileSystemUtils.deleteRecursively(new File(fileName));
//    }
//
//}
