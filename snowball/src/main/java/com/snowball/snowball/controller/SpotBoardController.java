package com.snowball.snowball.controller;

import com.snowball.snowball.repository.UserRepository;

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
    private final UserRepository userRepository;

    @Autowired
    public SpotBoardController(SpotBoardRepository boardRepository,
                               SpotBoardPostRepository postRepository,
                               UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<SpotBoardPostDTO> getBoardPosts(@PathVariable Long spotId) {
        System.out.println("[SpotBoardController] GET /api/spots/" + spotId + "/board 호출됨!");
        List<SpotBoardPost> posts = postRepository.findByBoard_Spot_IdOrderByCreatedAtDesc(spotId);
        System.out.println("[SpotBoardController] 반환 게시글 개수: " + posts.size());
        return posts.stream().map(SpotBoardPostDTO::new).toList();
    }

    @PostMapping
    public SpotBoardPostDTO createBoardPost(
            @PathVariable Long spotId,
            @RequestBody CreatePostRequest req,
            @RequestAttribute(value = "user", required = false) User author // 인증 없을 때 게스트 fallback
    ) {
        if (author == null) {
            // 게스트(비로그인) 글 등록 허용. 반드시 id=1 유저가 있어야 함!
            System.out.println("[SpotBoardController] 인증 없이 등록됨, author=null → 게스트로 대체");
            author = userRepository.findById(1L).orElseThrow(() -> new IllegalStateException("id=1 게스트 유저가 DB에 없습니다!"));
        }
        System.out.println("[SpotBoardController] POST /api/spots/" + spotId + "/board 등록 시도: " + req.getContent());
        SpotBoard board = boardRepository.findBySpot_Id(spotId)
                .stream().findFirst().orElseGet(() -> {
                    System.out.println("[SpotBoardController] 게시판 자동 생성 for spotId: " + spotId);
                    SpotBoard newBoard = new SpotBoard();
                    newBoard.setSpotId(spotId);
                    newBoard.setName("GUEST BOOK");
                    newBoard.setCreatedAt(LocalDateTime.now());
                    return boardRepository.save(newBoard);
                });

        System.out.println("author: " + author + ", authorId: " + (author != null ? author.getId() : null));
        System.out.println("board: " + board + ", boardId: " + (board != null ? board.getId() : null));
        System.out.println("content: " + req.getContent());
        System.out.println("imageUrl: " + req.getImageUrl());

        System.out.println("[SpotBoardController] 실제 게시글 저장! author=" + author.getId());
        SpotBoardPost post = new SpotBoardPost();
        post.setBoard(board);
        post.setAuthor(author);
        post.setContent(req.getContent());
        post.setImageUrl(req.getImageUrl());
        post.setCreatedAt(LocalDateTime.now());
        SpotBoardPost saved = postRepository.save(post);
        return new SpotBoardPostDTO(saved);
    }

    public static class CreatePostRequest {
        private String content;
        private String imageUrl;
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }

    public static class SpotBoardPostDTO {
        public Long id;
        public String content;
        public String imageUrl;
        public String createdAt;
        public Long authorId;
        public String authorNickname;
        public Long boardId;

        public SpotBoardPostDTO(SpotBoardPost post) {
            this.id = post.getId();
            this.content = post.getContent();
            this.imageUrl = post.getImageUrl();
            this.createdAt = post.getCreatedAt() != null ? post.getCreatedAt().toString() : null;
            this.authorId = post.getAuthor() != null ? post.getAuthor().getId() : null;
            this.authorNickname = post.getAuthor() != null ? post.getAuthor().getNickname() : null;
            this.boardId = post.getBoard() != null ? post.getBoard().getId() : null;
        }
    }
}