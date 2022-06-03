package com.toyproject.book.springboot.domian.posts;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PostsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Posts> findAll(){
        QPosts posts = QPosts.posts;
        List<Posts> result = jpaQueryFactory.selectFrom(posts)
                            .orderBy(posts.id.desc())
                            .fetch();
        log.info("posts size : {}", result.size());
        return result;
    }
}
