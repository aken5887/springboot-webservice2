package com.toyproject.book.springboot.web;

import com.toyproject.book.springboot.config.auth.LoginUser;
import com.toyproject.book.springboot.config.auth.dto.SessionUser;
import com.toyproject.book.springboot.service.posts.PostsService;
import com.toyproject.book.springboot.web.dto.PageDto;
import com.toyproject.book.springboot.web.dto.PostsListRequestDto;
import com.toyproject.book.springboot.web.dto.PostsListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(@PageableDefault(sort="id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
//                        String type, String keyword,
                        @ModelAttribute("requestDto") PostsListRequestDto requestDto,
                        Model model, @LoginUser SessionUser user){
//        List<PostsListResponseDto> resultList = postsService.findAllPosts();
//        model.addAttribute("posts", resultList);
//        model.addAttribute("totalPage", resultList.size());
//        SessionUser user = (SessionUser) httpSession.getAttribute("user"); @LoginUser사용
        /**
         * @PaeableDefault
         * size : 한 페이지에 담을 모델의 수를 정할 수 있음. 기본 값은 10
         * sort
         * direction
         * Pageable pageable : PageableDefault 값을 갖고 있는 변수를 선언
         */
        Page<PostsListResponseDto> posts = postsService.getPostPageList(pageable, requestDto);
        PageDto pageDto = new PageDto(pageable, posts);

        model.addAttribute("posts", posts);
        model.addAttribute("pageDto", pageDto);
        if(user != null){
            model.addAttribute("loginUserName", user.getName());
        }

        log.info("------------------------------------------");
        log.info("requestDto : {}", requestDto.toString());
        log.info("pageDto : {}", pageDto.toString());
        log.info("------------------------------------------");

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(ModelMap model, @LoginUser SessionUser user){
        if(user != null){
            model.addAttribute("loginUserName", user.getName());
        }
        return "posts-save";
    }


    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, ModelMap model, @LoginUser SessionUser user){
        model.addAttribute("posts", postsService.findById(id));
        if(user != null){
            model.addAttribute("loginUserName", user.getName());
        }
        return "posts-update";
    }
}
