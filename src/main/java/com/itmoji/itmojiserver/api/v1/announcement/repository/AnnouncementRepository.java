package com.itmoji.itmojiserver.api.v1.announcement.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.itmoji.itmojiserver.api.v1.announcement.Post;
import com.itmoji.itmojiserver.api.v1.announcement.PostCategory;

public interface AnnouncementRepository extends JpaRepository<Post, Long> {

    @Query("""
            SELECT p 
            FROM Post p
            WHERE p.id < :postId
            ORDER BY p.id DESC
            LIMIT 1
            """)
    Optional<Post> findTopByIdLessThan(Long postId);


    @Query("""
                SELECT p
                FROM Post p
                WHERE p.id > :postId
                ORDER BY p.id ASC
                LIMIT 1
            """)
    Optional<Post> findTopByIdGreaterThan(Long postId);

    @Query("""
            SELECT p
            FROM Post p
            WHERE p.isPinned = false
            ORDER BY p.createdAt DESC 
            """)
    Page<Post> findAllWithoutPinned(Pageable pageable);

    @Query("""
            SELECT p
            FROM Post p
            WHERE p.isPinned = true
            ORDER BY p.createdAt DESC 
            """)
    List<Post> findAllWithPinned();

    @Query("""
            SELECT p
            FROM Post p
            WHERE (
                    (:searchType = 'TITLE' AND LOWER(p.title) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) OR
                    (:searchType = 'CONTENT' AND LOWER(p.content) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) OR
                    (:searchType = 'TITLE_CONTENT' AND
                        (LOWER(p.title) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :searchQuery, '%')))
                    )
                )
            ORDER BY p.createdAt DESC
            """)
    Page<Post> findPostsBySearchQuery(Pageable pageable, String searchType, String searchQuery);

    @Query("""
            SELECT COUNT(p)
            FROM Post p
            WHERE p.isPinned = true
            """)
    int countPinnedPosts();


    @Query("""
            SELECT p 
            FROM Post p 
            WHERE p.postCategory = :postCategory
            AND p.isPinned = false
            """)
    Page<Post> findPostsByPostCategory(Pageable pageable, PostCategory postCategory);
}
