package com.toyproject.book.springboot.web;

import com.toyproject.book.springboot.service.posts.PostsService;
import com.toyproject.book.springboot.web.dto.PostsListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model){
        List<PostsListResponseDto> resultList = postsService.findAllPosts();
        model.addAttribute("posts", resultList);
        model.addAttribute("totalPage", resultList.size());
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }


    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, ModelMap model){
        model.addAttribute("posts", postsService.findById(id));
        return "posts-update";
    }
}
