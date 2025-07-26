package com.snowball.snowball.controller;

import com.snowball.snowball.entity.SpotBoard;
import com.snowball.snowball.entity.SpotBoardPost;
import com.snowball.snowball.entity.User;
import com.snowball.snowball.config.repository.SpotBoardPostRepository;
import com.snowball.snowball.config.repository.SpotBoardRepository;
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
        return postRepository.findByBoard_Spot_IdOrderByCreatedAtDesc(spotId);
    }

    @PostMapping
    public SpotBoardPost createBoardPost(
            @PathVariable Long spotId,
            @RequestBody CreatePostRequest req,
            @RequestAttribute("user") User author
    ) {
        SpotBoard board = boardRepository.findById(spotId)
                .stream().findFirst().orElseGet(() -> {
                    SpotBoard newBoard = new SpotBoard();
                    newBoard.setSpotId(spotId);
                    newBoard.setName("GUEST BOOK");
                    newBoard.setCreatedAt(LocalDateTime.now());
                    return boardRepository.save(newBoard);
                });

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