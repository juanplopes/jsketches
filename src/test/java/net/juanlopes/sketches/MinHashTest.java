package net.juanlopes.sketches;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class MinHashTest {
    @Test
    public void testError() {
        double err1A = 0, err2A = 0;
        double err1B = 0, err2B = 0;

        int tests = 1000;
        for (int seed = 0; seed < tests; seed++) {
            int cnt = 100000;
            int cnt1 = cnt/3;
            int cnt2 = cnt/2;

            MinHashV2 h1 = new MinHashV2(seed, 1024);
            MinHashV2 h2 = new MinHashV2(seed, 1024);


            for (int i = 0; i < cnt; i++) {
                h1.offer("test" + i);
                h2.offer("test" + i);
            }

            for (int i = 0; i < cnt1; i++) {
                h1.offer("xxx" + i);
            }

            for (int i = 0; i < cnt2; i++) {
                h2.offer("yyy" + i);
            }

            double h1c = h1.cardinality();
            double h2c = h2.cardinality();
            double h1o = h1.cardinalityOld();
            double h2o = h2.cardinalityOld();
            double sim = h1.similarity(h2);
            double simO = h1.similarityOld(h2) / Math.sqrt(2);

            err1A += h1c / (cnt + cnt1);
            err2A += h1o / (cnt + cnt1);
            err1B += sim * (cnt + cnt1 + cnt2) / (cnt);
            err2B += simO * (cnt + cnt1 + cnt2) / (cnt);

//            System.out.println(h1c + " " + h1c / (cnt + cnt1));
//            System.out.println(h2c + " " + h2c / (cnt + cnt2));
//            System.out.println(h1o + " " + h1o / (cnt + cnt1));
//            System.out.println(h2o + " " + h2o / (cnt + cnt2));
//            System.out.println(sim + " " + sim * (cnt + cnt1 + cnt2) / (cnt));
//            System.out.println(simO + " " + simO * (cnt + cnt1 + cnt2) / (cnt));
        }

        System.out.println(err1A / tests);
        System.out.println(err2A / tests);
        System.out.println(err1B / tests);
        System.out.println(err2B / tests);

    }
}