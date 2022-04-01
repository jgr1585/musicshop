package at.fhv.teamd.musicshop.userclient.view.generic;

import at.fhv.teamd.musicshop.library.DTO.AnalogMediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;

import java.util.Optional;

public interface GenericArticleController {
    void setMediumType(ArticleDTO articleDTO, AnalogMediumDTO analogMedium, Optional<LineItemDTO> lineItemDTO);
}
