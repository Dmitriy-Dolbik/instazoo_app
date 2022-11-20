package com.example.instazoo_app.services;

import com.example.instazoo_app.exceptions.NotFoundException;
import com.example.instazoo_app.models.Attachment;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.ImageRepository;
import com.example.instazoo_app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {
    @Value("${file.storage}")
    private String filePath;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    private String saveFileToServer(byte[] bytes) {
        String fileName = UUID.randomUUID().toString();
        File file = new File(filePath + fileName);
        try {
            Files.write(file.toPath(), compressBytes(bytes));
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getFileFromServer(String serverFileName) {
        File file = new File(filePath + serverFileName);
        try {
            return decompressBytes(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Attachment uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        log.info("Uploading image profile to User {}", user.getUsername());

        Attachment userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }
        Attachment attachment = new Attachment();
        attachment.setUserId(user.getId());
        String fileName = saveFileToServer(file.getBytes());
        attachment.setServerFileName(fileName);
        attachment.setImageBytes(compressBytes(file.getBytes()));
        attachment.setName(file.getOriginalFilename());
        return imageRepository.save(attachment);
    }

    public Attachment uploadImageToPost(MultipartFile file, Principal principal,
                                        Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post post = user.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(toSinglePostCollector());

        Attachment attachment = new Attachment();
        attachment.setPostId(post.getId());
        String fileName = saveFileToServer(file.getBytes());
        attachment.setServerFileName(fileName);
        attachment.setName(file.getOriginalFilename());
        log.info("Uploading image to Post {}", post.getId());

        return imageRepository.save(attachment);
    }

    public Attachment getImageToUser(Principal principal) {
        User user = getUserByPrincipal(principal);

        Attachment attachment = imageRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Cannot find image to User : " + user.getId()));
        if (!ObjectUtils.isEmpty(attachment) && attachment.getServerFileName() != null) {
            attachment.setImageBytes(getFileFromServer(attachment.getServerFileName()));
        }
        return attachment;
    }

    public Attachment getImageToPost(Long postId) {
        Attachment attachment = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new NotFoundException("Cannot find image to Post : " + postId));
        if (!ObjectUtils.isEmpty(attachment)) {
            attachment.setImageBytes(getFileFromServer(attachment.getServerFileName()));
        }
        return attachment;
    }

    //Перед сохранение будем уменьшать количество битов в файле
    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException e) {
            log.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    //Перед сохранение будем восстанавливать количество битов в файле
    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            log.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username : " + username));
    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
