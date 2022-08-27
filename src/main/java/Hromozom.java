import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hromozom {

    private int duzina;
    private Double[] tezina;
    private Double fitness;

    public Hromozom() {
        tezina = new Double[32];
        duzina = tezina.length;
    }

    public Hromozom(Double[] tezina) {
        this.tezina = tezina;
        this.duzina = tezina.length;
    }

    public Hromozom(Double fitness) {
        this.tezina = new Double[32];
        this.duzina = tezina.length;
        this.fitness = fitness;
    }
}
