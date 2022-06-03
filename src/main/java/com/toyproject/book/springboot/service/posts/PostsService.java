package com.toyproject.book.springboot.service.posts;

import com.toyproject.book.springboot.domian.posts.Posts;
import com.toyproject.book.springboot.domian.posts.PostsCustomRepository;
import com.toyproject.book.springboot.domian.posts.PostsRepository;
import com.toyproject.book.springboot.web.dto.PostsListResponseDto;
import com.toyproject.book.springboot.web.dto.PostsResponseDto;
import com.toyproject.book.springboot.web.dto.PostsSaveRequestDto;
import com.toyproject.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository repository;
    private final PostsCustomRepository customRepository;
    public Long save(PostsSaveRequestDto requestDto) {
        return repository.save(requestDto.toEntity()).getId();
    }

    @Transactional      // 굳이?
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts post = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        post.update(requestDto.getTitle(), requestDto.getContent());    // update 쿼리를 날리지 않음!!!!
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
}
