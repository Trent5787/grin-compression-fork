package edu.grinnell.csc207.compression;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

/**
 * A HuffmanTree derives a space-efficient coding of a collection of byte
 * values.
 *
 * The huffman tree encodes values in the range 0--255 which would normally take
 * 8 bits. However, we also need to encode a special EOF character to denote the
 * end of a .grin file. Thus, we need 9 bits to store each byte value. This is
 * fine for file writing (modulo the need to write in byte chunks to the file),
 * but Java does not have a 9-bit data type. Instead, we use the next larger
 * primitive integral type, short, to store our byte values.
 */
public class HuffmanTree {

    public class Node implements Comparable<Node> {

        short value;
        int freq;
        Node leftChild, rightChild;

        Node(short value, int freq) {
            this.value = value;
            this.freq = freq;
        }

        // Should create the internal node?
        Node(Node leftChild, Node rightChild) {
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.freq = leftChild.freq + rightChild.freq;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.freq, other.freq);
        }

        boolean isLeaf() {
            return leftChild == null && rightChild == null;
        }
    }

    private Node root;

    /**
     * Constructs a new HuffmanTree from a frequency map.
     *
     * @param freqs a map from 9-bit values to frequencies.
     */
    public HuffmanTree(Map<Short, Integer> freqs) {

        //theoretically creates a priority queue ordered in ascending order by value
        PriorityQueue<Node> queue = new PriorityQueue<>();

        for (Map.Entry<Short, Integer> entry : freqs.entrySet()) {
            queue.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (queue.size() >= 2) {
            //create a new node using the top two nodes of the queue as children
            Node firstval = queue.poll(); //or is this peek?
            Node secondval = queue.poll();
            //add this node back into the queue
            Node parent = new Node(firstval, secondval);
            queue.add(parent);
        }

        //To set the root
        if (!queue.isEmpty()) {
            root = queue.poll();
        }
    }

    /**
     * Constructs a new HuffmanTree from the given file.
     *
     * @param in the input file (as a BitInputStream)
     */
    public HuffmanTree(BitInputStream in) {
        // TODO: fill me in!
        //??
    }

    /**
     * Writes this HuffmanTree to the given file as a stream of bits in a
     * serialized format.
     *
     * @param out the output file as a BitOutputStream
     */
    public void serialize(BitOutputStream out) {
        // TODO: fill me in!
    }

    /**
     * A table of the mappings of leaf values to bits
     *
     * @table A map of shorts(the bits) and chars they represent
     */
    private Map<Short, String> Table(Node root) {
        Map<Short, String> bitStringtable = new HashMap<>();
        Recurser(root, "", bitStringtable);
        return bitStringtable;
    }

    /**
     * A helper method for Table to recursively add 0s and 1s
     */
    private void Recurser(Node node, String path, Map<Short, String> table) {
        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            table.put(node.value, path);
            return;
        }

        Recurser(node.leftChild, path + "0", table); //left is 0
        Recurser(node.rightChild, path + "1", table); // right is 1
    }

    /**
     * Encodes the file given as a stream of bits into a compressed format using
     * this Huffman tree. The encoded values are written, bit-by-bit to the
     * given BitOuputStream.
     *
     * @param in the file to compress.
     * @param out the file to write the compressed output to.
     */
    public void encode(BitInputStream in, BitOutputStream out) {
        Map<Short, String> encodingTable = Table(root);

        int value;
        while ((value = in.readBit()) != -1) {
            short s = (short) value;
            String code = encodingTable.get(s);

            for (char bit : code.toCharArray()) {
                //some sort of traversal and writing of the bits (but there is
                //no method for that...)
            }
        }
    }

    /**
     * Decodes a stream of huffman codes from a file given as a stream of bits
     * into their uncompressed form, saving the results to the given output
     * stream. Note that the EOF character is not written to out because it is
     * not a valid 8-bit chunk (it is 9 bits).
     *
     * @param in the file to decompress.
     * @param out the file to write the decompressed output to.
     */
    public void decode(BitInputStream in, BitOutputStream out) {
        // TODO: fill me in!
    }
}
