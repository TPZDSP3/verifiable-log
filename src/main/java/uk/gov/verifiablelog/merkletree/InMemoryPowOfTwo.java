package uk.gov.verifiablelog.merkletree;

import java.util.TreeMap;

/**
 * A {@link MemoizationStore} that stores Merkle Tree root hashes in memory for intermediate subtrees
 * of a power of two in size, i.e. for subtrees of size 1, 2, 4, 8, 16 etc.
 */
public class InMemoryPowOfTwo implements MemoizationStore {

    private TreeMap<Integer, TreeMap<Integer, byte[]>> data;

    public InMemoryPowOfTwo() {
        data = new TreeMap<>();
    }

    /**
     * Adds the root hash of a subtree to the set of known intermediate Merkle Tree root hashes stored in memory
     * if the subtree is a power of two in size, otherwise does nothing.
     * @param start The zero-based index of the first leaf in the subtree
     * @param size The number of leaves in the subtree
     * @param value The Merkle Tree root hash of the subtree
     */
    @Override
    public void put(Integer start, Integer size, byte[] value) {
        if (Integer.bitCount(size) <= 1) {
            TreeMap<Integer, byte[]> sizeBucket = data.get(size);

            if (sizeBucket == null) {
                sizeBucket = new TreeMap<>();
                data.put(size, sizeBucket);
            }
            sizeBucket.put(start, value);
        }
    }

    /**
     * Retrieves the root hash of a subtree from the set of known intermediate Merkle Tree root hashes
     * if it exists in the in-memory store.
     * @param start The zero-based index of the first leaf in the subtree
     * @param size The number of leaves in the subtree
     * @return The Merkle Tree root hash of the subtree if it exists in the in-memory store, else null.
     */
    @Override
    public byte[] get(Integer start, Integer size) {
        TreeMap<Integer, byte[]> sizeBucket = data.get(size);
        return sizeBucket == null ? null : sizeBucket.get(start);
    }
}
