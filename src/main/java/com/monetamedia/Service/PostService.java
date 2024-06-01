package com.monetamedia.Service;

import com.monetamedia.Exception.ResourceNotFoundException;
import com.monetamedia.Models.ApiResponse;
import com.monetamedia.Models.Post;
import com.monetamedia.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        if (post.getCreationdate() == null) {
            post.setCreationDate(LocalDateTime.now());
        }
        postRepository.save(post);
        return post;
    }


    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
    }
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPosts(int page, int size, String sortBy, String sortDir) {
        return postRepository.findAll(page, size, sortBy, sortDir);
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public List<Post> searchPosts(String query, int page, int size, String sortBy, String sortDir) {
        return postRepository.searchPosts(query, page, size, sortBy, sortDir);
    }

    public Post updatePost(Post post) {
        postRepository.update(post);
        return post;
    }
    public void likePost(Long postId, Long userId) {
        postRepository.addLike(postId, userId);
        updateLikesCount(postId);
    }

    public void unlikePost(Long postId, Long userId) {
        postRepository.removeLike(postId, userId);
        updateLikesCount(postId);
    }

    private void updateLikesCount(Long postId) {
        int likesCount = postRepository.countLikes(postId);
        Post post = getPostById(postId);
        post.setLikesCount(likesCount);
        postRepository.update(post);
    }
    public ApiResponse deletePost(Long id) {
        int rowsAffected = postRepository.delete(id);
        if (rowsAffected > 0) {
            return new ApiResponse("Post with ID " + id + " deleted successfully",true);
        } else {
            return new ApiResponse("Post with ID " + id + " not found",true);
        }
    }
}
