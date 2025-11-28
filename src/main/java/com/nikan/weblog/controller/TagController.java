package com.nikan.weblog.controller;

import com.nikan.weblog.dto.PostAndTagDto;
import com.nikan.weblog.dto.TagDto;
import com.nikan.weblog.model.Post;
import com.nikan.weblog.model.Tag;
import com.nikan.weblog.service.PostService;
import com.nikan.weblog.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final PostService postService;

    public TagController(TagService tagService,  PostService postService) {
        this.tagService = tagService;
        this.postService = postService;
    }

    private Tag convertToEntity(TagDto dto) {
        Tag tag = new Tag();
        tag.setName(dto.name());
        return tag;
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAll() {
        List<Tag> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable int id) {
        Optional<Tag> tag = tagService.findById(id);
        return tag.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tag> save(@Valid @RequestBody TagDto tagDto) {
        Tag tag = convertToEntity(tagDto);
        Tag savedTag = tagService.save(tag);
        return ResponseEntity.ok(savedTag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable int id, @Valid @RequestBody TagDto tagDto) {
        Optional<Tag> optionalTag = tagService.findById(id);
        if (optionalTag.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Tag tagToUpdate = optionalTag.get();
        tagToUpdate.setName(tagDto.name());
        Tag updatedTag = tagService.save(tagToUpdate);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        tagService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tagSlug}/posts")
    @ResponseBody
    public ResponseEntity<List<PostAndTagDto>> postsByTag(@PathVariable String tagSlug) {
        Tag tag = tagService.findBySlug(tagSlug);
        if (tag == null) {
            return ResponseEntity.notFound().build();
        }

        List<Post> posts = postService.findByTag(tag);
        List<PostAndTagDto> postAndTagDtos = posts.stream().map(post -> {
            PostAndTagDto dto = new PostAndTagDto();
            dto.setPostId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setExcerpt(post.getExcerpt());
            dto.setStatus(post.getStatus());
            dto.setPublishedAt(post.getPublishedAt());
            dto.setViews(post.getViews());
            dto.setSlug(post.getSlug());
            dto.setTagName(tag.getName());
            dto.setTagSlug(tag.getSlug());
            dto.setAuthorUsername(post.getAuthor().getUsername());
            dto.setAuthorFullName(post.getAuthor().getFullName());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(postAndTagDtos);
    }
}