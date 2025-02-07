package com.itmoji.itmojiserver.api.v1.announcement.service;

import com.itmoji.itmojiserver.api.v1.announcement.Post;
import com.itmoji.itmojiserver.api.v1.announcement.repository.AnnouncementRepository;
import org.springframework.transaction.annotation.Transactional;
import com.itmoji.itmojiserver.api.v1.announcement.PostCategory;
import com.itmoji.itmojiserver.api.v1.announcement.dto.PostDTO;
import com.itmoji.itmojiserver.api.v1.announcement.SearchType;
import com.itmoji.itmojiserver.api.v1.announcement.dto.PostSummaryDTO;
import com.itmoji.itmojiserver.api.v1.announcement.dto.request.PostCreateRequestDTO;
import com.itmoji.itmojiserver.api.v1.announcement.dto.request.PostSearchRequest;
import com.itmoji.itmojiserver.api.v1.announcement.dto.request.UpdatePostRequestDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private static final int PINNED_LIMIT = 3;

    private final AnnouncementRepository announcementRepository;

    @Transactional
    public void createPost(final PostCreateRequestDTO request) {
        if (request.isPinned()) {
            checkPinnedPostCount();
        }

        PostCategory category = PostCategory.valueOf(request.postCategory().toUpperCase());
        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .postCategory(category)
                .isPinned(request.isPinned())
                .build();

        announcementRepository.save(post);
    }

    private void checkPinnedPostCount() {
        if (announcementRepository.countPinnedPosts() >= PINNED_LIMIT) {
            throw new IllegalArgumentException("고정글은 " + PINNED_LIMIT + "개를 초과할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public PostDTO findPostById(final Long postId) {
        final Post post = announcementRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        final Post previousPost = announcementRepository.findTopByIdLessThan(postId).orElse(null);
        final Post nextPost = announcementRepository.findTopByIdGreaterThan(postId).orElse(null);

        return new PostDTO(post, previousPost, nextPost);
    }

    @Transactional
    public void deletePostById(final Long postId) {
        if (!announcementRepository.existsById(postId)) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        announcementRepository.deleteById(postId);
    }

    @Transactional
    public void updatePostById(final Long postId, final UpdatePostRequestDTO request) {
        final Post beforePost = announcementRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if (request.isPinned()) {
            checkPinnedPostCount();
        }

        beforePost.update(request.title(), request.content(), PostCategory.find(request.postCategory()),
                request.isPinned());
        announcementRepository.save(beforePost);
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryDTO> findPosts(Pageable pageable, String category) {
        if (category != null) {
            final PostCategory postCategory = PostCategory.find(category);
            final Page<Post> postsByPostCategory = announcementRepository.findPostsByPostCategory(pageable,
                    postCategory);

            return postsByPostCategory.map(PostSummaryDTO::new);
        }

        final Page<Post> posts = announcementRepository.findAllWithoutPinned(pageable);

        return posts.map(PostSummaryDTO::new);
    }

    @Transactional(readOnly = true)
    public List<PostSummaryDTO> findPinnedPosts() {
        final List<Post> posts = announcementRepository.findAllWithPinned();

        return posts.stream().map(PostSummaryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryDTO> findPostsBySearchQuery(final Pageable pageable, final PostSearchRequest request) {
        final String searchTypeName = SearchType.findSearchType(request.type()).name();
        final Page<Post> posts = announcementRepository.findPostsBySearchQuery(pageable, searchTypeName,
                request.query());

        return posts.map(PostSummaryDTO::new);
    }
}
