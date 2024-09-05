package prj6;
import java.util.PriorityQueue;

import java.util.HashMap;
import java.util.Map;
import java.io.*;







    // ... (your existing HuffmanNode class)



class HuffmanNode implements Comparable<HuffmanNode> {
    char data;
    int frequency;
    HuffmanNode left, right;

    public int compareTo(HuffmanNode node) {
        return frequency - node.frequency;
    }
}

public class HuffmanFileCompresser {

    public static void compressFile(String inputFileName, String outputFileName) throws IOException {
        FileInputStream fis = new FileInputStream(inputFileName);
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFileName));
        
        // Count frequency of each character in the input file
        int[] frequency = new int[256];
        int byteRead;
        while ((byteRead = fis.read()) != -1) {
            frequency[byteRead]++;
        }
        
        // Create a priority queue to store Huffman nodes
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (int i = 0; i < 256; i++) {
            if (frequency[i] > 0) {
                HuffmanNode node = new HuffmanNode();
                node.data = (char) i;
                node.frequency = frequency[i];
                pq.add(node);
            }
        }

        // Build Huffman tree
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode merged = new HuffmanNode();
            merged.frequency = left.frequency + right.frequency;
            merged.left = left;
            merged.right = right;
            pq.add(merged);
        }
        HuffmanNode root = pq.poll();

        // Build Huffman codes
        Map<Character, String> huffmanCodes = new HashMap<>();
        buildHuffmanCodes(root, "", huffmanCodes);

        // Write Huffman codes and compressed data to output file
        dos.writeInt(huffmanCodes.size());
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            dos.writeChar(entry.getKey());
            dos.writeUTF(entry.getValue());
        }

        fis.close();
        fis = new FileInputStream(inputFileName);
        StringBuilder compressedData = new StringBuilder();
        while ((byteRead = fis.read()) != -1) {
            compressedData.append(huffmanCodes.get((char) byteRead));
        }
        fis.close();

        int index = 0;
        while (index < compressedData.length()) {
            int endIndex = Math.min(index + 8, compressedData.length());
            String byteString = compressedData.substring(index, endIndex);
            dos.writeByte(Integer.parseInt(byteString, 2));
            index += 8;
        }
        dos.close();
    }

    public static void decompressFile(String inputFileName, String outputFileName) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(inputFileName));
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFileName));

        // Read Huffman codes
        int numCodes = dis.readInt();
        Map<String, Character> huffmanMap = new HashMap<>();
        for (int i = 0; i < numCodes; i++) {
            char character = dis.readChar();
            String code = dis.readUTF();
            huffmanMap.put(code, character);
        }

        // Read compressed data
        StringBuilder compressedData = new StringBuilder();
        byte currentByte;
        while (dis.available() > 0) {
            currentByte = dis.readByte();
            compressedData.append(String.format("%8s", Integer.toBinaryString(currentByte & 0xFF)).replace(' ', '0'));
        }
        dis.close();

        // Decompress using Huffman codes
        StringBuilder decompressedData = new StringBuilder();
        String currentCode = "";
        for (int i = 0; i < compressedData.length(); i++) {
            currentCode += compressedData.charAt(i);
            if (huffmanMap.containsKey(currentCode)) {
                decompressedData.append(huffmanMap.get(currentCode));
                currentCode = "";
            }
        }

        dos.write(decompressedData.toString().getBytes());  // Write bytes directly
        dos.close();
    }
    private static void buildHuffmanCodes(HuffmanNode node, String code, Map<Character, String> huffmanCodes) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.data, code);
        }
        buildHuffmanCodes(node.left, code + "0", huffmanCodes);
        buildHuffmanCodes(node.right, code + "1", huffmanCodes);
    }
}

    