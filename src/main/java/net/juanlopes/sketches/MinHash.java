package net.juanlopes.sketches;

import net.juanlopes.sketches.util.Preconditions;

import java.util.Arrays;

public class MinHash {
    private final int[] data;

    public MinHash(int k) {
        Preconditions.checkArgument(Integer.bitCount(k) == 1, "bit count must be a power of two");
        this.data = new int[k];
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

            long value = data[i] + (1L << 31);
            sum += (double) value / (1L << 32);
            total++;
        }
        return total / sum;
    }

    public void offer(Object obj) {
        int hash = obj.hashCode();
        for (int i = 0; i < data.length; i++) {
            hash = mix(hash);
            data[i] = Math.min(data[i], hash);
        }
    }

    public double similarity(MinHash other) {
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
}
