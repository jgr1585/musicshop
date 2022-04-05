package at.fhv.teamd.musicshop.userclient.view;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;

import java.util.Optional;

public interface GenericArticleController {
    void setMediumType(ArticleDTO articleDTO, MediumDTO mediumDTO);
    void setMediumType(ArticleDTO articleDTO, MediumDTO mediumDTO, LineItemDTO lineItemDTO);
}