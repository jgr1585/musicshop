package at.fhv.teamd.musicshop.backend.domain.TEMPARCHIVE.medium;

import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;

import java.math.BigDecimal;

public class DigitalMedium extends Medium {

    public DigitalMedium(BigDecimal price, Supplier supplier) {
        super(price, supplier);
    }
}
