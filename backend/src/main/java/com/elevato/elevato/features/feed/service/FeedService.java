package com.elevato.elevato.features.feed.service;

import com.elevato.elevato.exception.ForbiddenException;
import com.elevato.elevato.exception.ResourceNotFoundException;
import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.elevato.elevato.features.authentication.repository.AuthenticationUserRepository;
import com.elevato.elevato.features.feed.dto.CommentDto;
import com.elevato.elevato.features.feed.dto.PostDto;
import com.elevato.elevato.features.feed.dto.PostMapper;
import com.elevato.elevato.features.feed.model.Comment;
import com.elevato.elevato.features.feed.model.Post;
import com.elevato.elevato.features.feed.repository.CommentRepository;
import com.elevato.elevato.features.feed.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FeedService {
    Logger logger = LoggerFactory.getLogger(FeedService.class);
    private final PostRepository postRepository;
    private final AuthenticationUserRepository authenticationUserRepository;
    private final CommentRepository commentRepository;

    public FeedService(PostRepository postRepository, AuthenticationUserRepository authenticationUserRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.authenticationUserRepository = authenticationUserRepository;
        this.commentRepository = commentRepository;
    }

    public Post createPost(Long id, PostDto post) {
        AuthenticationUser user = authenticationUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found") );
        Post newPost = new Post(post.getContent(), post.getPicture(), user);
       return postRepository.save(newPost);
    }

    public Post editPost(Long userId, PostDto post, Long postId) {

        Post oldPost = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        AuthenticationUser user = authenticationUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!oldPost.getAuthor().equals(user)){
            throw new ForbiddenException("User is not allowed to edit this post");
        }
        oldPost.setContent(post.getContent());
        oldPost.setPicture(post.getPicture());
        return postRepository.save(oldPost);
    }


    public List<Post> getFeed(Long id) {
        return postRepository.findPostsNotByUser(id);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post not found"));
    }

    public void deletePost(Long userId, Long postId) {
        AuthenticationUser user = authenticationUserRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        if(!post.getAuthor().equals(user)){
            throw new ForbiddenException("User is not allowed to do this.");
        }
        postRepository.delete(post);
    }

    public List<Post> getAllPostsByAuthorId(Long authorId) {
        AuthenticationUser user = authenticationUserRepository.findById(authorId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        return user.getPosts();
    }

    public Post likePost(Long userId, Long postId) {
        AuthenticationUser user = authenticationUserRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        if(post.getLikes().contains(user)){
            post.getLikes().remove(user);
        }else{
            post.getLikes().add(user);
        }
        return postRepository.save(post);
    }

    public Comment addComment(Long userId, Long postId, CommentDto comment) {
        AuthenticationUser user = authenticationUserRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        Comment cmt = new Comment(post, user, comment.getContent());
        return commentRepository.save(cmt);
    }

    public Comment editComment(Long userId, Long commentId, CommentDto comment) {
        AuthenticationUser user = authenticationUserRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Comment cmt = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        if(!cmt.getUser().equals(user)){
            throw new ForbiddenException("User is not allowed to edit this comment");
        }
        cmt.setContent(comment.getContent());
        return commentRepository.save(cmt);
    }

    public void deleteComment(Long userId, Long commentId) {
        AuthenticationUser user = authenticationUserRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Comment cmt = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        if(!cmt.getUser().equals(user)){
            throw new ForbiddenException("User is not allowed to delete this comment");
        }
        commentRepository.delete(cmt);
    }

    public List<Comment> getComments( Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        return post.getComments();
    }

    public Set<AuthenticationUser> getLikes(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        return post.getLikes();
    }
}
