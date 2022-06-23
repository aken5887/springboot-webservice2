package com.toyproject.book.springboot.web;

import com.toyproject.book.springboot.config.auth.LoginUser;
import com.toyproject.book.springboot.config.auth.dto.SessionUser;
import com.toyproject.book.springboot.domian.user.User;
import com.toyproject.book.springboot.service.posts.PostsService;
import com.toyproject.book.springboot.service.user.UserService;
import com.toyproject.book.springboot.utils.CommonUtils;
import com.toyproject.book.springboot.web.dto.PostsResponseDto;
import com.toyproject.book.springboot.web.dto.PostsSaveRequestDto;
import com.toyproject.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.http2.Http2AsyncUpgradeHandler;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;
    private final UserService userService;

    private final HttpSession httpSession;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id){
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.deletePosts(id);
        return id;
    }

    @GetMapping("/api/v1/user/auth")
    public void userAuth(@LoginUser SessionUser user){
        userService.updateUserAuth(user);
    }

    /*
        @RequestPart 사용시 multipart/form-data 요청을 받음
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("category") String category,
                             @RequestPart(value="file")MultipartFile multipartFile) {
        return postsService.uploadFileToS3(category, multipartFile);
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("resourcePath") String resourcePath){
        byte[] data = postsService.downloadFileS3(resourcePath);
        ByteArrayResource resource = new ByteArrayResource(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(data.length);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(CommonUtils.createContentDisposition(resourcePath));
        return ResponseEntity.ok().headers(headers).body(resource);
    }

}
