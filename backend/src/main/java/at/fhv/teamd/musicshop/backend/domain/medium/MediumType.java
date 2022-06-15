package at.fhv.teamd.musicshop.backend.domain.medium;

import java.util.EnumSet;

public enum MediumType {
    CD, VINYL, CASSETTE, DIGITAL;

    public static EnumSet<MediumType> analogMediumTypes() {
        return EnumSet.complementOf(digitalMediumTypes());
    }

    public static EnumSet<MediumType> digitalMediumTypes() {
        return EnumSet.of(DIGITAL);
    }

    public static EnumSet<MediumType> allMediumTypes() {
        return EnumSet.allOf(MediumType.class);
    }
}
