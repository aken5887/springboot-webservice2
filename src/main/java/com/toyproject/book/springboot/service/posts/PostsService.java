package com.toyproject.book.springboot.service.posts;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.toyproject.book.springboot.domian.posts.Posts;
import com.toyproject.book.springboot.domian.posts.PostsCustomRepository;
import com.toyproject.book.springboot.domian.posts.PostsRepository;
import com.toyproject.book.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository repository;
    private final PostsCustomRepository customRepository;

    private final AmazonS3Client amazonS3Client;

    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String CATEGORY_PREFIX = "/";
    private static final String TIME_SEPARATOR = "_";

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    public Long save(PostsSaveRequestDto requestDto) {
        return repository.save(requestDto.toEntity()).getId();
    }

    @Transactional      // 굳이?
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts post = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        post.update(requestDto);    // update 쿼리를 날리지 않음!!!!
        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts posts = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id="+id));
        return new PostsResponseDto(posts);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllPosts(){
        return customRepository.findAll().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePosts(Long id){
        Posts post = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id="+id));
        repository.delete(post);
    }


    @Transactional(readOnly = true)
    public Page<PostsListResponseDto> getPostPageList(Pageable pageable, PostsListRequestDto dto){
       return customRepository.getPostByPaging(pageable, dto);
    }

    public String uploadFileToS3(String category, MultipartFile multipartFile) {
        if(multipartFile.isEmpty()){
//            throw new IllegalArgumentException("File is Empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일이 비어있습니다.");
        }
        String fileName = makeFileName(category, multipartFile.getOriginalFilename());

        try(InputStream inputStream = multipartFile.getInputStream()){
            byte[] bytes = IOUtils.toByteArray(inputStream);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(bytes.length);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, fileName, byteArrayInputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }catch(IOException io){
//            throw new FileUploadException();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }catch(Exception e){
            log.error("파일 업로드에 실패했습니다.");
        }

//        return amazonS3Client.getUrl(bucketName,fileName).toString();
        return fileName;
    }

    public byte[] downloadFileS3(String resourcePath){
        S3Object s3Object = amazonS3Client.getObject(bucketName, resourcePath);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        try{
            return IOUtils.toByteArray(inputStream);
        }catch(IOException e){
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드에 실패하였습니다.");
        }
    }

    private static String makeFileName(String category, String originalFileName){
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());

        return category+CATEGORY_PREFIX+fileName+TIME_SEPARATOR+now+fileExtension;
    }
}
