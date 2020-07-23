package pt.up.hs.pbanalysis.service.dto;

public enum LengthUnit {
    NM(1000000D),
    MM(1D),
    CM(0.1D),
    M(0.001D);

    private double rate;

    LengthUnit(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }
}
