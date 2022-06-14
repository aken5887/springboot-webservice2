package com.toyproject.book.springboot.web.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PageDto {
    private List<Integer> pageList = new ArrayList<>();
    private int previousPageNo;
    private int nextPageNo;
    private long totalPage;
    private boolean hasNext;
    private boolean hasPrev;
    private int currentPageNo;
    private int startPageNo;

    public PageDto(Pageable pageable, Page<PostsListResponseDto> posts) {
        int totalPage = posts.getTotalPages();
        int currentPageNo = pageable.getPageNumber() + 1;

        int lastPageNo = (int) (Math.ceil(currentPageNo / 5.0)) * 5;
        int startPageNo = lastPageNo - 4;

        if (lastPageNo > totalPage) {
            lastPageNo = totalPage;
        }

        for (int i = startPageNo; i <= lastPageNo; i++) {
            pageList.add(i);
        }

        if(pageList.isEmpty()){
            pageList.add(1);
        }

        this.previousPageNo = pageable.previousOrFirst().getPageNumber();
        this.nextPageNo = pageable.next().getPageNumber();
        this.totalPage = posts.getTotalElements();
        this.hasNext = posts.hasNext();
        this.hasPrev = posts.hasPrevious();
        this.currentPageNo = currentPageNo;
        this.startPageNo = startPageNo;
    }

    @Override
    public String toString() {
        return "PageDto{" +
                "pageList=" + pageList.toString() +
                ", previousPageNo=" + previousPageNo +
                ", nextPageNo=" + nextPageNo +
                ", totalPage=" + totalPage +
                ", hasNext=" + hasNext +
                ", hasPrev=" + hasPrev +
                ", currentPageNo=" + currentPageNo +
                ", startPageNo=" + startPageNo +
                '}';
    }
}
