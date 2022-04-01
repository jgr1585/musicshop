package at.fhv.teamd.musicshop.library.DTO;

import java.math.BigDecimal;

public interface MediumDTO {
    Long id();

    BigDecimal price();

    SupplierDTO supplier();
}
