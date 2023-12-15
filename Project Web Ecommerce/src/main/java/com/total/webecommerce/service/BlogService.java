package com.total.webecommerce.service;
import com.total.webecommerce.entity.dto.BlogDTO;
import com.total.webecommerce.entity.*;
import com.total.webecommerce.entity.projection.Public.BlogInfo;
import com.total.webecommerce.entity.projection.Public.CommentBlog;
import com.total.webecommerce.entity.support.NotificationStatus;
import com.total.webecommerce.exception.BadResquestException;
import com.total.webecommerce.exception.NotFoundException;
import com.total.webecommerce.mapper.BlogMapper;
import com.total.webecommerce.respository.OfAdmin.NotificationRepository;
import com.total.webecommerce.respository.OfAdmin.BrandRepository;
import com.total.webecommerce.respository.OfAdmin.RoleRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import com.total.webecommerce.respository.OrBlog.BlogRepository;
import com.total.webecommerce.respository.OrBlog.CommentOfBlogRepository;
import com.total.webecommerce.respository.OrBlog.ImageBlogRepository;
import com.total.webecommerce.resquest.OfBlog.CommentBlogResquest;
import com.total.webecommerce.resquest.OfBlog.CreateBlogResquest;
import com.total.webecommerce.resquest.OfBlog.UpdateBlogResquest;
import com.total.webecommerce.security.iCurrent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BlogService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ImageBlogRepository imageBlogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CommentOfBlogRepository commentOfBlog;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private iCurrent iCurrent;
    @Autowired
    private NotificationRepository notificationRepository;


    // Public về Blog Page
    public Page<BlogInfo> getBlogs(Integer page, Integer pageSize){
        return blogRepository.findBlogs(PageRequest.of(page, pageSize));
    }
    // lấy danh sách blog theo ID
    public BlogInfo getBlogById(Integer blogId) {
        Optional<BlogInfo> blogInfo = blogRepository.findBlogbyId(blogId);
        if (blogInfo.isEmpty()) {
            throw new NotFoundException("Không tìm thấy Blog nào với ID này !! ");
        }
        Blog blog = blogRepository.findById(blogId).orElse(null);
        blog.setViewBlog(blogInfo.get().getViewBlog() + 1);
        blogRepository.save(blog);
        return blogInfo.get();
    }
    // lấy top danh sách bài viết có lượt xem nhiều nhất
    public List<BlogDTO> getBlogByView() {
        return blogRepository.getBlogsHaveViewTop().stream().map(e -> BlogMapper.toBlogDto(e)).toList();
    }
    // lấy danh sách comment của Blog
    public Page<CommentBlog> getBlogWithComment(Integer page , Integer pageSize ,Integer blogId) {
        return commentOfBlog.findByBlog_IdOrderByIdDesc(blogId,PageRequest.of(page,pageSize));
    }

    public ResponseEntity<?> sendCommentOfBlog(Integer blogId, CommentBlogResquest comment) {
        Blog blog = blogRepository.findById(blogId).orElse(null);
        Optional<User> user = userRepository.findByEmail(comment.getEmail());
        if (user.isEmpty()) {
            throw new NotFoundException("Khong tìm thấy User này !! ");
        }
        CommentOfBlog comment1 = CommentOfBlog.builder()
                .blog(blog)
                .content(comment.getCommentOfUser())
                .user(user.get())
                .build();
        commentOfBlog.save(comment1);
        Notification notification = Notification.builder()
                .username(user.get().getName())
                .avatar(user.get().getAvatar())
                .typeOf(0)
                .content(user.get().getName()+" : "+comment.getCommentOfUser())
                .title("Post comment for Blog ID "+blogId)
                .notificationStatus(NotificationStatus.COMMENT)
                .build();
        notificationRepository.save(notification);

        return ResponseEntity.ok("Đăng bình luận thành công !! ");
    }
    public List<BlogDTO> findBrand(Integer blogId, Integer brandId) {
        return blogRepository.findBrand(brandId, blogId).stream().map(blog -> BlogMapper.toBlogDto(blog)).toList();
    }
    // to do : for Admin
    public Page<BlogInfo> getBlogsOfAdmin(Integer page, Integer pageSize) {
        return blogRepository.findBlogsAdmin(PageRequest.of(page, pageSize));
    }
    public Page<BlogInfo> getBlogsOfAuthor(Integer page, Integer pageSize) {
        User user = iCurrent.getUser();
        return blogRepository.findBlogsAuthor(user.getId(), PageRequest.of(page, pageSize));
    }
    public ResponseEntity<?> updateBlog(UpdateBlogResquest resquest){
        Optional<Blog> blog = blogRepository.findById(resquest.getBlogId());
        if(blog.isEmpty()){
            throw new NotFoundException("Not Found Blog !! ");
        }
        Brand item = brandRepository.findById(resquest.getBrandId()).get();
        blog.get().setStatusBlog(resquest.getPublicOf());
        blog.get().setBrand(item);
        blog.get().setTitle(resquest.getTitle());
        blog.get().setContent(resquest.getContent());
        blog.get().setDescription(resquest.getDescription());
        blogRepository.save(blog.get());
        User user = iCurrent.getUser();
        Notification notification = Notification.builder()
                .username(user.getName())
                .typeOf(1)
                .avatar(user.getAvatar())
                .content(user.getUsername()+"Update Blog with ID : "+resquest.getBlogId())
                .title("Update Blog ")
                .notificationStatus(NotificationStatus.UPDATE)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Update for Blog success !! ");
    }

    public Integer getLastId(){
        Blog blog = blogRepository.findAll().get(blogRepository.findAll().size()-1);
        return blog.getId();
    }

    public Integer createBlog(CreateBlogResquest resquest){
        User user = iCurrent.getUser();
        Brand brand = brandRepository.findById(resquest.getBrandId()).get();
        Blog blog = Blog.builder()
                .description(resquest.getDescription())
                .title(resquest.getTitle())
                .content(resquest.getContent())
                .brand(brand)
                .user(user)
                .statusBlog(resquest.getPublicOf())
                .build();
        blogRepository.save(blog);
        Notification notification = Notification.builder()
                .username(user.getName())
                .typeOf(1)
                .avatar(user.getAvatar())
                .content(user.getName()+"Create Blog have Title " +blog.getTitle())
                .title("Create Blog ")
                .notificationStatus(NotificationStatus.UPDATE)
                .build();
        notificationRepository.save(notification);
        return blog.getId();
    }

    public ResponseEntity<?> deleteBlog(Integer blogId) {
        Blog blog = blogRepository.findById(blogId).get();
        List<ImageBlog> imageOfBlogs = imageBlogRepository.findImageOfBlog(blogId);
        for (int i = 0; i <imageOfBlogs.size() ; i++) {
            imageBlogRepository.delete(imageOfBlogs.get(i));
        }
        User user = iCurrent.getUser();

        blogRepository.delete(blog);
        Notification notification = Notification.builder()
                .username(user.getName())
                .typeOf(1)
                .avatar(user.getAvatar())
                .content(user.getName()+"Delete Blog Has Title " +blog.getTitle())
                .title("Delete Blog ")
                .notificationStatus(NotificationStatus.UPDATE)
                .build();
        notificationRepository.save(notification);
        return ResponseEntity.ok("Delete Blog Seccess !! ");
    }

    public void deleteCommentOfBlog(Integer commentId) {
        User user = iCurrent.getUser();
        CommentOfBlog cmt = commentOfBlog.findById(commentId).orElseThrow(
                ()-> {
                    throw new NotFoundException("Not found this comment with ID "+commentId);
                }
        );
        Role role = roleRepository.findById(1).orElseThrow(
                ()-> {
                    throw new NotFoundException("Not found Role ");
                }
        );

        if(user.getId() != cmt.getUser().getId() && !user.getRoles().equals(role)){
            throw new BadResquestException("Bạn không có quyền !! ");
        }
        commentOfBlog.delete(cmt);
    }
}
