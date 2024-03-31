package com.encuentrame.project.encuentrame.services;

import com.encuentrame.project.encuentrame.entities.FileSystem;
import com.encuentrame.project.encuentrame.entities.Image;
import com.encuentrame.project.encuentrame.repositories.FileSystemRepository;
import com.encuentrame.project.encuentrame.repositories.StorageRepository;
import com.encuentrame.project.encuentrame.util.ImageUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class StorageService {
    @Autowired
    private StorageRepository repository;

    @Autowired
    private FileSystemRepository fileSystemRepository;

    private final Lock lock = new ReentrantLock();


    private String uploadFolderPath = System.getProperty("user.dir") + "/src/main/resources/static/img/pets/";

    public String uploadImage(MultipartFile file) throws IOException {
        try {
            String fileName = uploadImageToFileSystem(file);
            Image imageData = repository.save(Image.builder()
                    .name(fileName)
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                    .build());
            if (imageData != null) {
                return "file uploaded successfully : " + fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte[] downloadImage(String fileName) {
        Optional<Image> dbImageData = repository.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }

    public String uploadImageToFileSystem(MultipartFile file) throws Exception {
        String fileName = generateUniqueFileName(file);
        String filePath = uploadFolderPath + fileName;

        FileSystem fileData = fileSystemRepository.save(FileSystem.builder()
                .name(fileName)
                .type(file.getContentType())
                .filePath(filePath).build());

        file.transferTo(new File(filePath));

        if (fileData != null) {
            return fileName;
        }
        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileSystem> fileData = fileSystemRepository.findByName(fileName);
        String filePath = fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    private String generateUniqueFileName(MultipartFile file) {
        lock.lock(); // Bloquea el acceso a este bloque de código para otros hilos
        try {
            // Agregar una pausa de 1 milisegundo
            Thread.sleep(1);
            
            // Genera un nombre basado en la fecha y hora actual
            LocalDateTime now = LocalDateTime.now();
            String fileName = String.format("%04d%02d%02d%02d%02d%02d%03d",
                    now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                    now.getHour(), now.getMinute(), now.getSecond(), now.getNano() / 1000000);

                    String extension = "";
                    int dotIndex = file.getOriginalFilename().lastIndexOf(".");
                    if (dotIndex > 0) {
                        extension = file.getOriginalFilename().substring(dotIndex);
                    }
            

            fileName += extension; // por ejemplo, asumiendo que siempre es una imagen JPG

            return fileName;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restablece la interrupción del hilo
            return null; // Maneja la excepción de manera adecuada
        } finally {
            lock.unlock(); // Desbloquea el acceso para otros hilos
        }
    }
}
