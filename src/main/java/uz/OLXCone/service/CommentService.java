package uz.OLXCone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.OLXCone.component.MyMapper;
import uz.OLXCone.model.Ads;
import uz.OLXCone.model.Comment;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.CommentInDTO;
import uz.OLXCone.payload.out.CommentOutDTO;
import uz.OLXCone.repository.AdsRepository;
import uz.OLXCone.repository.CommentRepository;
import uz.OLXCone.utils.Checks;
import uz.OLXCone.utils.CommonUtils;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final MyMapper mapper;

    public ApiResult<?> getByAdsId(String adsId, Integer page) {
        if (Checks.isUUID(adsId)) {
            UUID id = UUID.fromString(adsId);
            boolean isExists = adsRepository.
                    existsByIdAndIsActiveTrue(id);
            if (isExists) {
                PageRequest pageRequest =
                        PageRequest.of(page < 0 ? 0 : page, 15);
                Page<Comment> commentPage = commentRepository.
                        findByAdsIdOrderByTimeAsc(id, pageRequest);
                if (!commentPage.isEmpty()) {
                    Page<CommentOutDTO> dtoPage =
                            commentPage.map(mapper::commentToCommentOutDTO);
                    return ApiResult.
                            noMessage(true, dtoPage);
                } else
                    return ApiResult.noObject(
                            "ads haven't comment",
                            true);
            }
        }
        return ApiResult.noObject(
                "ads not found",
                false);
    }

    public ApiResult<Object> write(String adsId, CommentInDTO commentInDTO) {
        if (Checks.isUUID(adsId)) {
            Optional<Ads> adsOptional = adsRepository.findById(
                    UUID.fromString(adsId)
            );
            if (adsOptional.isPresent()) {
                Comment comment = mapper.
                        commentInDTOToComment(commentInDTO);
                comment.setOwner(CommonUtils.getCurrentUser());
                comment.setAds(adsOptional.get());
                commentRepository.save(comment);
                return ApiResult.noObject(
                        "successfully saved",
                        true);
            }
        }
        return ApiResult.noObject(
                "Ads not found",
                false);
    }

    public ApiResult<Object> delete(String id) {
        if (Checks.isUUID(id)) {
            Optional<Comment> commentOptional =
                    commentRepository.findById(
                            UUID.fromString(id));
            if (commentOptional.isPresent()) {
                Comment comment = commentOptional.get();
                if (CommonUtils.isCurrentUser(comment.getOwner())) {
                    commentRepository.
                            delete(comment);
                    return ApiResult.noObject(
                            "successfully deleted",
                            true);
                }
            }
        }
        return ApiResult.noObject(
                "comment not found",
                false);
    }
}
