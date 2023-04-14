package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class PostRepository {

    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(0);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long iterateId = idCounter.getAndIncrement();
            Post newPost = new Post(iterateId, post.getContent());
            posts.put(iterateId, newPost);
            return newPost;
        }

        Post existPost = posts.get(post.getId());

        existPost.setContent(post.getContent());
        posts.put(post.getId(), existPost);
        return existPost;

    }

    public void removeById(long id) {
        if (posts.containsKey(id)) {
            posts.remove(id);
        } else {
            throw new NotFoundException("Такого id:" + id + " нет!");
        }
    }

}
