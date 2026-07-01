package benchmark.versioning;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // Demonstrate with Union policy
        System.out.println("--- Testing with UNION Policy ---");
        testPolicy(MergePolicy.UNION);

        // Demonstrate with Intersection policy
        System.out.println("\n--- Testing with INTERSECTION Policy ---");
        testPolicy(MergePolicy.INTERSECTION);

        // Demonstrate with Symmetric Difference policy
        System.out.println("\n--- Testing with SYMMETRIC_DIFFERENCE Policy ---");
        testPolicy(MergePolicy.SYMMETRIC_DIFFERENCE);
    }

    private static void testPolicy(MergePolicy policy) {
        VersionGraph graph = new VersionGraph(policy);

        // Case 1: Root Node
        Set<String> initialData = new HashSet<>(Arrays.asList("fileA", "fileB"));
        Version v0 = graph.createRoot("V0", initialData);
        System.out.println("Created Root: " + v0);

        // Case 2: Transition Nodes (Branching)
        // Branch 1
        Version v1 = graph.createTransition("V1", v0, 
                new HashSet<>(Arrays.asList("fileC")), 
                new HashSet<>(Arrays.asList("fileA")));
        System.out.println("Created V1 (Branch 1): " + v1);

        // Branch 2
        Version v2 = graph.createTransition("V2", v0, 
                new HashSet<>(Arrays.asList("fileD")), 
                new HashSet<>(Arrays.asList("fileB")));
        System.out.println("Created V2 (Branch 2): " + v2);

        // Case 3: Merge Node
        Version v3 = graph.createMerge("V3", Arrays.asList(v1, v2));
        System.out.println("Created V3 (Merge of V1 and V2): " + v3);

        // Consistency Verification
        boolean consistent = VersionConsistencyChecker.isConsistent(graph);
        System.out.println("Graph is consistent: " + consistent);
        
        if (!consistent) {
            throw new RuntimeException("Consistency check failed!");
        }
    }
}
