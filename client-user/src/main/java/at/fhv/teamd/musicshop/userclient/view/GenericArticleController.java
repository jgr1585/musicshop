package at.fhv.teamd.musicshop.userclient.view;

import at.fhv.teamd.musicshop.library.dto.ArticleDTO;
import at.fhv.teamd.musicshop.library.dto.LineItemDTO;
import at.fhv.teamd.musicshop.library.dto.MediumDTO;

public interface GenericArticleController {
    void setMediumType(MediumDTO mediumDTO);
    void setMediumType(LineItemDTO lineItemDTO);
}
