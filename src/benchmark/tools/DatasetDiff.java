package benchmark.tools;

import java.io.*;
import java.util.*;

/**
 * Computes the RDF diff between two N-Triples files.
 * Outputs additions (triples in file2 but not file1) and deletions (triples in file1 but not file2).
 *
 * Usage: DatasetDiff <file1.nt> <file2.nt> <additions.nt> <deletions.nt>
 */
public class DatasetDiff {

	public static void main(String[] args) {
		if (args.length != 4) {
			System.err.println("Usage: DatasetDiff <file1.nt> <file2.nt> <additions.nt> <deletions.nt>");
			System.exit(1);
		}

		String file1Path = args[0];
		String file2Path = args[1];
		String additionsPath = args[2];
		String deletionsPath = args[3];

		try {
			List<String> triples1 = readAndSort(file1Path);
			List<String> triples2 = readAndSort(file2Path);
			mergeAndDiff(triples1, triples2, additionsPath, deletionsPath);
		} catch (IOException e) {
			System.err.println("Error during diff computation: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Reads all non-empty lines from an N-Triples file and returns them sorted.
	 */
	private static List<String> readAndSort(String filePath) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (!line.isEmpty()) {
				lines.add(line);
			}
		}
		reader.close();
		Collections.sort(lines);
		return lines;
	}

	/**
	 * Walks two sorted lists in parallel and writes additions and deletions.
	 * Additions: lines in sorted2 but not in sorted1.
	 * Deletions: lines in sorted1 but not in sorted2.
	 */
	private static void mergeAndDiff(List<String> sorted1, List<String> sorted2,
			String additionsPath, String deletionsPath) throws IOException {
		BufferedWriter addWriter = new BufferedWriter(new FileWriter(additionsPath));
		BufferedWriter delWriter = new BufferedWriter(new FileWriter(deletionsPath));

		int i = 0, j = 0;
		long additions = 0, deletions = 0;

		while (i < sorted1.size() && j < sorted2.size()) {
			int cmp = sorted1.get(i).compareTo(sorted2.get(j));
			if (cmp < 0) {
				// In file1 but not file2 -> deletion
				delWriter.write(sorted1.get(i));
				delWriter.newLine();
				deletions++;
				i++;
			} else if (cmp > 0) {
				// In file2 but not file1 -> addition
				addWriter.write(sorted2.get(j));
				addWriter.newLine();
				additions++;
				j++;
			} else {
				// Same triple in both -> skip
				i++;
				j++;
			}
		}

		// Remaining in file1 -> deletions
		while (i < sorted1.size()) {
			delWriter.write(sorted1.get(i));
			delWriter.newLine();
			deletions++;
			i++;
		}

		// Remaining in file2 -> additions
		while (j < sorted2.size()) {
			addWriter.write(sorted2.get(j));
			addWriter.newLine();
			additions++;
			j++;
		}

		addWriter.close();
		delWriter.close();

		System.out.println("  -> " + additionsPath + " (" + additions + " triples)");
		System.out.println("  -> " + deletionsPath + " (" + deletions + " triples)");
	}
}
