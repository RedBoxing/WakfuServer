package fr.redboxing.wakfu.server.models;

public enum Breed {
    COMMON(-2),
    NONE(-1),
    FECA(1),
    OSAMODAS(2),
    ENUTROF(3),
    SRAM(4),
    XELOR(5),
    ECAFLIP(6),
    ENIRIPSA(7),
    IOP(8),
    CRA(9),
    SADIDA(10),
    SACRIER(11),
    PANDAWA(12),
    ROUBLARD(13),
    ZOBAL(14),
    OUGINAK(15),
    STEAMER(16),
    SOUL(17),
    ELIOTROPE(18),
    HUPPERMAGE(19);

    private final short m_breedId;

    private Breed(final int id) {
        this.m_breedId = (short)id;
    }

    public static Breed getBreedFromId(final int id) {
        for (final Breed breed : values()) {
            if (breed.getBreedId() == id) {
                return breed;
            }
        }
        return Breed.NONE;
    }

    public short getBreedId() {
        return m_breedId;
    }
}