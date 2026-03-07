package com.elevato.elevato.features.feed.controller;

import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.elevato.elevato.features.feed.dto.CommentDto;
import com.elevato.elevato.features.feed.dto.PostDto;
import com.elevato.elevato.features.feed.model.Comment;
import com.elevato.elevato.features.feed.model.Post;
import com.elevato.elevato.features.feed.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/feed")
public class FeedController {

    private final FeedService feedService;

    public FeedController( FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestAttribute("authenticatedUser")AuthenticationUser user, @RequestBody PostDto post){
        Post createdPost = feedService.createPost(user.getId(), post);
        return ResponseEntity.created(null).body(createdPost);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<Post> editPost(@RequestAttribute("authenticatedUser")AuthenticationUser user, @RequestBody PostDto post,@PathVariable Long postId){
        Post editedPost = feedService.editPost(user.getId(),post,postId);
        return ResponseEntity.ok(editedPost);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId){
        Post post = feedService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@RequestAttribute("authenticatedUser") AuthenticationUser user, @PathVariable Long postId){
        feedService.deletePost(user.getId(), postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Post>> getFeed(@RequestAttribute("authenticatedUser")AuthenticationUser user){
        List<Post> feed = feedService.getFeed(user.getId());
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> feed = feedService.getAllPosts();
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/posts/user/{authorId}")
    public ResponseEntity<List<Post>> getAllPostsOfAuthor(@PathVariable Long authorId){
        List<Post> feed = feedService.getAllPostsByAuthorId(authorId);
        return ResponseEntity.ok(feed);
    }

    @PutMapping("/posts/{postId}/likes")
    public ResponseEntity<Post> likePost(@RequestAttribute("authenticatedUser") AuthenticationUser user, @PathVariable Long postId){
        return ResponseEntity.ok(feedService.likePost(user.getId(),postId));
    }

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<Set<AuthenticationUser>> getLikes(@PathVariable Long postId){
        return ResponseEntity.ok(feedService.getLikes(postId));
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> addComment(@RequestAttribute("authenticatedUser") AuthenticationUser user,
                                              @PathVariable Long postId,
                                              @RequestBody CommentDto comment){
        Comment addedComment = feedService.addComment(user.getId(), postId, comment);
        return ResponseEntity.ok(addedComment);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(
                                              @PathVariable Long postId){
        List<Comment> comments = feedService.getComments(postId);
        return ResponseEntity.ok(comments);
    }


    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> editComment(@RequestAttribute("authenticatedUser") AuthenticationUser user,
                                              @PathVariable Long commentId,
                                              @RequestBody CommentDto comment){
        Comment editedComment = feedService.editComment(user.getId(), commentId, comment);
        return ResponseEntity.ok(editedComment);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@RequestAttribute("authenticatedUser") AuthenticationUser user,
                                               @PathVariable Long commentId){
        feedService.deleteComment(user.getId(), commentId);
        return ResponseEntity.noContent().build();
    }



}
