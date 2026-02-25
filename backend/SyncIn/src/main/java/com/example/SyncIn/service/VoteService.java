package com.example.SyncIn.service;

import com.example.SyncIn.dto.CreateVoteRequest;
import com.example.SyncIn.dto.VoteResponse;
import com.example.SyncIn.model.*;
import com.example.SyncIn.repository.CommentRepository;
import com.example.SyncIn.repository.PostRepository;
import com.example.SyncIn.repository.VoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VoteService {

    private VoteRepository voteRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    public VoteService(VoteRepository voteRepository, PostRepository postRepository, CommentRepository commentRepository)
    {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public ResponseEntity<VoteResponse> votePost(Long postId, User user, VoteType voteType)
    {

        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found!"));

        Optional<Vote> voteOpt = voteRepository.findByUserIdAndPostId(user.getId(), postId);

        if(voteOpt.isEmpty())
        {
            Vote newVote = Vote.builder()
                    .voteType(voteType)
                    .user(user)
                    .post(post)
                    .build();

            if(voteType == VoteType.DOWNVOTE)
            {
                post.downVoteIncrease();
            }
            else
            {
                post.upVoteIncrease();
            }

            voteRepository.save(newVote);
        }
        else
        {
            Vote vote = voteOpt.get();
            if(vote.getVoteType() == voteType)
            {
                voteRepository.delete(vote);

                if(voteType == VoteType.DOWNVOTE)
                {
                    post.downVoteDecrease();
                }
                else
                {
                    post.upVoteDecrease();
                }
            }
            else
            {
                vote.setVoteType(voteType);

                if(voteType == VoteType.DOWNVOTE)
                {
                    post.downVoteIncrease();
                    post.upVoteDecrease();
                }
                else
                {
                    post.upVoteIncrease();
                    post.downVoteDecrease();
                }
            }
        }

        return ResponseEntity.ok(
                new VoteResponse(
                        post.getUpVoteCount(),
                        post.getDownVoteCount(),
                        voteType.toString()
                )
        );
    }


    public ResponseEntity<VoteResponse> voteComment(Long commentId, User user, VoteType voteType)
    {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Post not found!"));

        Optional<Vote> voteOpt = voteRepository.findByUserIdAndCommentId(user.getId(), commentId);

        if(voteOpt.isEmpty())
        {
            Vote newVote = Vote.builder()
                    .voteType(voteType)
                    .user(user)
                    .comment(comment)
                    .build();

            if(voteType == VoteType.DOWNVOTE)
            {
                comment.downVoteIncrease();
            }
            else
            {
                comment.upVoteIncrease();
            }

            voteRepository.save(newVote);
        }
        else
        {
            Vote vote = voteOpt.get();
            if(vote.getVoteType() == voteType)
            {
                voteRepository.delete(vote);

                if(voteType == VoteType.DOWNVOTE)
                {
                    comment.downVoteDecrease();
                }
                else
                {
                    comment.upVoteDecrease();
                }
            }
            else
            {
                vote.setVoteType(voteType);

                if(voteType == VoteType.DOWNVOTE)
                {
                    comment.downVoteIncrease();
                    comment.upVoteDecrease();
                }
                else
                {
                    comment.upVoteIncrease();
                    comment.downVoteDecrease();
                }
            }
        }

        return ResponseEntity.ok(
                new VoteResponse(
                        comment.getUpVoteCount(),
                        comment.getDownVoteCount(),
                        voteType.toString()
                )
        );
    }
}
