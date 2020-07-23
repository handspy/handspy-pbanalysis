package pt.up.hs.pbanalysis.service.dto;

public enum TimeUnit {
    NS(1000000D),
    MS(1D),
    S(0.001D),
    M(1D / 60000D),
    H(1D / (60000D * 60000D));

    private double rate;

    TimeUnit(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }
}
