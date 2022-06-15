package com.toyproject.book.springboot.domian.posts;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyproject.book.springboot.web.dto.PostsListRequestDto;
import com.toyproject.book.springboot.web.dto.PostsListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Page<PostsListResponseDto> getPostByPaging(Pageable pageable, PostsListRequestDto dto){
        QPosts posts = QPosts.posts;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(posts.id.gt(0L));
        builder = eqTypeAndKeyword(builder, posts, dto);

        QueryResults<Posts> postsList
                = jpaQueryFactory.selectFrom(posts)
                                .where(builder)
                                .orderBy(posts.id.desc())
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetchResults();

        List<PostsListResponseDto> resultList = new ArrayList<>();
        postsList.getResults().forEach(p -> {
            PostsListResponseDto responseDto = new PostsListResponseDto(p);
            resultList.add(responseDto);
        });

        return new PageImpl<PostsListResponseDto>(resultList, pageable, postsList.getTotal());
    }

    public BooleanBuilder eqTypeAndKeyword(BooleanBuilder builder, QPosts p, PostsListRequestDto d){

        String keyword = Optional.ofNullable(d.getKeyword()).orElseGet(()->"");
        if("1".equals(d.getType())){
            builder.and(p.title.like("%"+keyword+"%"));
        }else if("2".equals(d.getType())){
            builder.and(p.author.like("%"+keyword+"%"));
        }

        return builder;
    }
}
