package at.fhv.teamd.musicshop.userclient.view;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;

public interface GenericArticleController {
    void setMediumType(ArticleDTO articleDTO, MediumDTO mediumDTO);
    void setMediumType(LineItemDTO lineItemDTO);
}
