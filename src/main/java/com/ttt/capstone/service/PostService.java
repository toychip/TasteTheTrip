package com.ttt.capstone.service;

import com.ttt.capstone.domian.Post;
import com.ttt.capstone.domian.PostEditor;
import com.ttt.capstone.exception.PostNotFound;
import com.ttt.capstone.repository.PostRepository;
import com.ttt.capstone.request.PostCreate;
import com.ttt.capstone.request.PostEdit;
import com.ttt.capstone.request.PostSearch;
import com.ttt.capstone.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;

    public Post writeNn(PostCreate postCreate){
        // postCreate -> Entity 형태로 바꿔주어야함. postCreate는 RequestDTO이기 때문
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        return postRepository.save(post);
    }
    public void write(PostCreate postCreate){
        // postCreate -> Entity 형태로 바꿔주어야함. postCreate는 RequestDTO이기 때문
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);
    }

    public Long writePk(PostCreate postCreate){
        // postCreate -> Entity 형태로 바꿔주어야함. postCreate는 RequestDTO이기 때문
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        return post.getId();
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    // 여러개의 게시글 조회
//    public List<PostResponse> getList(Pageable pageable) {
////        Pageable pageable = PageRequest.of(page,10, Sort.by(Sort.Direction.DESC, "id"));
//
//        return postRepository.getList(1).stream()
//                .map(post -> new PostResponse(post))
//                .collect(Collectors.toList());
//    }
    public List<PostResponse> getList(PostSearch postSearch) {
//        Pageable pageable = PageRequest.of(page,10, Sort.by(Sort.Direction.DESC, "id"));

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuitor = post.toEditor();
        PostEditor postEditor = editorBuitor.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
//            이러한 불편한 상황들 때문에 postEditor를 사용함
//        post.edit(postEdit.getTitle() != null ? postEdit.getTitle() : post.getTitle(),
//                postEdit.getContent() != null ? postEdit.getContent() : post.getContent());
//        patch할때 body에 수정 정보를 내려 줄 것이면 아래와 같게
//        return new PostResponse(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        // -> 존재하는 경우
        postRepository.delete(post);
    }
}
