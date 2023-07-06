package com.total.webecommerce.service;

import com.total.webecommerce.entity.*;
import com.total.webecommerce.entity.support.NotificationStatus;
import com.total.webecommerce.respository.OfAdmin.NotificationRepository;
import com.total.webecommerce.respository.OrBlog.ImageBlogRepository;
import com.total.webecommerce.exception.BadResquestException;
import com.total.webecommerce.exception.NotFoundException;
import com.total.webecommerce.response.FileResponse;
import com.total.webecommerce.respository.OrBlog.BlogRepository;
import com.total.webecommerce.respository.OfUser.ImageRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import com.total.webecommerce.security.iCurrentImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final iCurrentImpl iCurrentUser;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final ImageBlogRepository imageBlogRepository;

    public FileResponse uploadFile(Integer userId, MultipartFile file) {
        validateFile(file);
        try {
            Optional<User> user = userRepository.findById(userId);
            Image image = Image.builder()
                    .type(file.getContentType())
                    .data(file.getBytes())
                    .user(user.get())
                    .build();
            imageRepository.save(image);
            user.get().setAvatar("http://localhost:8888/api/v1/files/getImage/" + user.get().getId() + "/" + image.getId());
            userRepository.save(user.get());
            return new FileResponse("http://localhost:8888/api/v1/files/getImage/" + user.get().getId() + "/" + image.getId());
        } catch (IOException e) {
            throw new RuntimeException("Có lỗi xảy ra trong quá trình upload ảnh ");
        }
    }

    public Image readImageUser(Integer userId, Integer imageId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new BadResquestException("Không tìm thấy hình ảnh User");
        }
        return imageRepository.findByUser_IdAndId(userId, imageId);
    }

    // image for Blog
    public FileResponse uploadImageBook(MultipartFile file, Integer blogId) {
        Optional<Blog> blog = blogRepository.findById(blogId);

        if (blog.isEmpty()) {
            throw new NotFoundException("Không tìm thầy Blog này ");
        }
        validateFile(file);
        try {
            ImageBlog imageBlog = ImageBlog.builder()
                    .type(file.getContentType())
                    .data(file.getBytes())
                    .user(iCurrentUser.getUser())
                    .blog(blog.get())
                    .build();
            imageBlogRepository.save(imageBlog);
            blog.get().setThumbail("http://localhost:8888/api/v1/files/blog/" + blog.get().getId() + "/" + imageBlog.getId());
            blogRepository.save(blog.get());
            User accOfAdmin = iCurrentUser.getUser();
            Notification notification = Notification.builder()
                    .username(accOfAdmin.getName())
                    .title("Update Image for Blog ")
                    .avatar(accOfAdmin.getAvatar())
                    .content(accOfAdmin.getName()+" Post Image for Blog with ID " +blog.get().getId())
                    .notificationStatus(NotificationStatus.UPDATE)
                    .typeOf(1)
                    .build();
            notificationRepository.save(notification);
            return new FileResponse("http://localhost:8888/api/v1/files/blog/" + blog.get().getId() + "/" + imageBlog.getId());
        } catch (IOException e) {
            throw new RuntimeException("Có lỗi xảy ra trong quá trình upload ảnh ");
        }
    }

    public ImageBlog readImageBlog(Integer blogId, Integer imageBlogId) {
        if (blogRepository.findById(blogId).isEmpty()) {
            throw new BadResquestException("Không tìm thấy hình ảnh Blog này ");
        }
        return imageBlogRepository.findByIdAndBlog_Id(imageBlogId, blogId);
    }

    private void validateFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // Tên file không được trống
        if (fileName == null || fileName.isEmpty()) {
            throw new BadResquestException("Tên file không hợp lệ");
        }

        // Type file có nằm trong ds cho phép hay không
        // avatar.png, image.jpg => png, jpg
        String fileExtension = getFileExtension(fileName);
        if (!checkFileExtension(fileExtension)) {
            throw new BadResquestException("Type file không hợp lệ");
        }

        // Kích thước size có trong phạm vi cho phép không
        double fileSize = (double) (file.getSize() / 1_048_576);
        if (fileSize > 2) {
            throw new BadResquestException("Size file không được vượt quá 2MB");
        }
    }

    public String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1) {
            return "";
        }
        return fileName.substring(lastIndex + 1);
    }

    public boolean checkFileExtension(String fileExtension) {
        List<String> fileExtensions = List.of("png", "jpg", "jpeg");
        return fileExtensions.contains(fileExtension);
    }

    public Image readFile(Integer id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found image");
                });
        return image;
    }

    public void deleteFile(Integer id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found image");
                });
        imageRepository.delete(image);
    }


}
