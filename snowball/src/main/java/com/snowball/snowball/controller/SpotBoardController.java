package com.snowball.snowball.controller;

import com.snowball.snowball.entity.SpotBoard;
import com.snowball.snowball.entity.SpotBoardPost;
import com.snowball.snowball.entity.User;
import com.snowball.snowball.repository.SpotBoardPostRepository;
import com.snowball.snowball.repository.SpotBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/spots/{spotId}/board")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://snowball.iuorder.com",
        "https://snowball.iuorder.com"
}, allowedHeaders = "*", allowCredentials = "true")
public class SpotBoardController {

    private final SpotBoardRepository boardRepository;
    private final SpotBoardPostRepository postRepository;

    @Autowired
    public SpotBoardController(SpotBoardRepository boardRepository,
                               SpotBoardPostRepository postRepository) {
        this.boardRepository = boardRepository;
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<SpotBoardPost> getBoardPosts(@PathVariable Long spotId) {
        System.out.println("[SpotBoardController] GET /api/spots/" + spotId + "/board 호출됨!");
        List<SpotBoardPost> posts = postRepository.findByBoard_Spot_IdOrderByCreatedAtDesc(spotId);
        System.out.println("[SpotBoardController] 반환 게시글 개수: " + posts.size());
        return posts;
    }

    @PostMapping
    public SpotBoardPost createBoardPost(
            @PathVariable Long spotId,
            @RequestBody CreatePostRequest req,
            @RequestAttribute("user") User author
    ) {
        System.out.println("[SpotBoardController] POST /api/spots/" + spotId + "/board 등록 시도: " + req.getContent());
        SpotBoard board = boardRepository.findById(spotId)
                .stream().findFirst().orElseGet(() -> {
                    System.out.println("[SpotBoardController] 게시판 자동 생성 for spotId: " + spotId);
                    SpotBoard newBoard = new SpotBoard();
                    newBoard.setSpotId(spotId);
                    newBoard.setName("GUEST BOOK");
                    newBoard.setCreatedAt(LocalDateTime.now());
                    return boardRepository.save(newBoard);
                });

        System.out.println("[SpotBoardController] 실제 게시글 저장! author=" + author.getId());
        SpotBoardPost post = new SpotBoardPost();
        post.setBoard(board);
        post.setAuthor(author);
        post.setContent(req.getContent());
        post.setImageUrl(req.getImageUrl());
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public static class CreatePostRequest {
        private String content;
        private String imageUrl;
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }
}