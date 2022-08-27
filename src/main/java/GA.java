import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GA {

    List<Hromozom> generacija;

    public GA() {
        this.generacija = initPopulation();
    }

    // 1.generisanje hromozoma
    public List<Hromozom> initPopulation() {
        List<Hromozom> populacija = new ArrayList<>();
        Double[] tezina = new Double[32];

        for (int i = 0; i < Main.vp; i++) {
            for (int j = 0; j < 32; j++) {
                Random r = new Random();
                tezina[j] = (Main.phigh - Main.plow) * r.nextDouble(0, 1) + Main.plow;
                System.out.println("Dodata tezina " + tezina[j] + " na hromozom " + i);
            }
            populacija.add(new Hromozom(tezina));
        }
        return populacija;
    }

    // 2.
    public void prilagodjenost(List<Hromozom> g) {

        for (Hromozom h : g) {

            ProcessBuilder pb = new ProcessBuilder( "src/main/java/fitness",
                    h.getTezina()[0].toString(), h.getTezina()[1].toString(),h.getTezina()[2].toString(),h.getTezina()[3].toString(),
                    h.getTezina()[4].toString(), h.getTezina()[5].toString(),h.getTezina()[6].toString(),h.getTezina()[7].toString(),
                    h.getTezina()[8].toString(), h.getTezina()[9].toString(),h.getTezina()[10].toString(),h.getTezina()[11].toString(),
                    h.getTezina()[12].toString(), h.getTezina()[13].toString(),h.getTezina()[14].toString(),h.getTezina()[15].toString(),
                    h.getTezina()[16].toString(), h.getTezina()[17].toString(),h.getTezina()[18].toString(),h.getTezina()[19].toString(),
                    h.getTezina()[20].toString(), h.getTezina()[21].toString(),h.getTezina()[22].toString(),h.getTezina()[23].toString(),
                    h.getTezina()[24].toString(), h.getTezina()[25].toString(),h.getTezina()[26].toString(),h.getTezina()[27].toString(),
                    h.getTezina()[28].toString(), h.getTezina()[29].toString(),h.getTezina()[30].toString(),h.getTezina()[31].toString());

            try {
                Process process = pb.start();
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                if ((line = br.readLine()) == null)
                    System.out.println("Greska pri izracunavanju troska.");
                else {
                    sb.append(line);
                }
                String res = sb.toString();
                Double fitness = Double.valueOf(res);
                System.out.println(res);
                h.setFitness(fitness);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //3. pragovsko
    public List<Hromozom> odsecanje(List<Hromozom> g) {

        Double avg = 0.0;
        List<Hromozom> res = new ArrayList<Hromozom>();

        for (Hromozom h : g)
            avg += h.getFitness();

        avg = avg / g.size();

        for (Hromozom h : g)
            if (h.getFitness() >= avg) res.add(h);

        return res;
    }

    //4. turnirska
    public Hromozom selekcija(List<Hromozom> g) {
        Hromozom h = g.get(0);
        Double max = h.getFitness();

        for(Hromozom hr : g){
            if(hr.getFitness() > max)
                h = hr;
                max = hr.getFitness();
        }

        return h;
    }

    //heuristicko ukrstanje
    public Hromozom ukrstanje(Hromozom h1, Hromozom h2) {

        List<Hromozom> res = new ArrayList<Hromozom>();
        Random rand = new Random();
        double r = rand.nextDouble(0, 1);
        double x1;
        double x2;
        if (h1.getFitness() > h2.getFitness()) {
            x1 = h1.getFitness();
            x2 = h2.getFitness();
        } else {
            x2 = h1.getFitness();
            x1 = h2.getFitness();
        }

        Double xp = r * (x1 - x2) + x1;
        return new Hromozom(xp);
    }

    public Hromozom mutacija(Hromozom h1) {
        Random rand = new Random();
        if(rand.nextInt(0,1)<= Main.vMutacije)
        h1.setFitness((Main.phigh - Main.plow) * rand.nextDouble(0, 1) + Main.plow);

        return h1;
    }

    public void ga() {
        int brojGeneracije = 0;
        List<Hromozom> turnir = new ArrayList<Hromozom>();
        int i = 0; // iterator za parenje
        List<Hromozom> parenje = new ArrayList<Hromozom>();
        List<Hromozom> ukrsteni = new ArrayList<Hromozom>();
        Random rand = new Random();
        Hromozom best = null;

        while (brojGeneracije < 150) {
            prilagodjenost(generacija);
            odsecanje(generacija);
            generacija.sort(new Comparator<Hromozom>() {
                public int compare(Hromozom o1, Hromozom o2) {
                    return o2.getFitness().compareTo(o1.getFitness());
                }
            });

            if (best != null) {
                if (generacija.get(0).getFitness() > best.getFitness()) best = generacija.get(0);
            }else
                best = generacija.get(0);

            parenje.clear();

            while( i < Main.jzp) {
                turnir.clear();
                while (turnir.size() <= 3) {
                    int ucesnik = rand.nextInt(generacija.size());
                    if (!turnir.contains(generacija.get(ucesnik)))
                        turnir.add(generacija.get(ucesnik));
                }
                Hromozom h = selekcija(turnir);
                if (!parenje.contains(h)) {
                    parenje.add(h);
                    i++;
                }
            }
            generacija.addAll(parenje);

            int n = parenje.size()/2;
            for ( int j = 0; j< n; j++){

                Hromozom h = ukrstanje(parenje.get(j*2), parenje.get(j*2+1));
                ukrsteni.add(h);
            }

            mutacija(ukrsteni.get(rand.nextInt(ukrsteni.size())));

            brojGeneracije++;
        }
    }

}
