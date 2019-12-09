package net.juanlopes.sketches;

import net.juanlopes.sketches.util.Preconditions;

import java.util.Arrays;

public class MinHashV2 {
    private final int[] data;
    private final int bitCount;
    private final int seed;

    public MinHashV2(int seed, int k) {
        this.seed = seed;
        Preconditions.checkArgument(Integer.bitCount(k) == 1, "bit count must be a power of two");
        this.data = new int[k];
        this.bitCount = Integer.numberOfTrailingZeros(k);
        Arrays.fill(data, Integer.MAX_VALUE);
    }

    public static int mix(int h) {
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return h;
    }

    public double cardinality() {
        double sum = 0;
        int total = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == Integer.MAX_VALUE) continue;

            sum += (double) data[i] / (1L << (32 - bitCount));
            total++;
        }
        return total * total / sum;
    }

    public double cardinalityOld() {
        double sum = 0;
        int total = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == Integer.MAX_VALUE) continue;

            sum += Math.pow(2, -Integer.numberOfLeadingZeros(data[i]) + bitCount - 1);
            total++;
        }
        double alphaMM = (0.7213 / (1 + 1.079 / total)) * total * total;
        return alphaMM / sum;
    }


    public void offer(Object obj) {
        int hash = mix(seed + obj.hashCode());
        int index = hash & (data.length - 1);
        data[index] = Math.min(data[index], hash >>> bitCount);
    }

    public double similarity(MinHashV2 other) {
        Preconditions.checkArgument(data.length == other.data.length, "minhashes must have same size");

        int count = 0;
        int total = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == Integer.MAX_VALUE && other.data[i] == Integer.MAX_VALUE) continue;
            if (data[i] == other.data[i])
                count++;
            total++;
        }
        return (double) count / total;
    }

    public double similarityOld(MinHashV2 other) {
        Preconditions.checkArgument(data.length == other.data.length, "minhashes must have same size");

        int count = 0;
        int total = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == Integer.MAX_VALUE && other.data[i] == Integer.MAX_VALUE) continue;
            if (Integer.numberOfLeadingZeros(data[i]) == Integer.numberOfLeadingZeros(other.data[i]))
                count++;
            total++;
        }
        return (double) count / total;
    }
}
