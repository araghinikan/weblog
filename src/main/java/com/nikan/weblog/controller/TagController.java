package com.nikan.weblog.controller;

import com.nikan.weblog.dto.TagDto;
import com.nikan.weblog.model.Tag;
import com.nikan.weblog.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
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
}
