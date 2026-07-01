package benchmark.versioning;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VersionConsistencyChecker {
    public static boolean isConsistent(VersionGraph graph) {
        MergePolicy policy = graph.getGlobalPolicy();
        for (Version v : graph.getVersions()) {
            List<Version> parents = v.getParents();
            if (parents.size() >= 2) {
                Set<Set<String>> parentsData = parents.stream()
                        .map(Version::getData)
                        .collect(Collectors.toSet());
                Set<String> expectedData = policy.apply(parentsData);
                if (!v.getData().equals(expectedData)) {
                    System.out.println("[DEBUG_LOG] Consistency violation at version: " + v.getId());
                    System.out.println("[DEBUG_LOG] Expected: " + expectedData);
                    System.out.println("[DEBUG_LOG] Actual:   " + v.getData());
                    return false;
                }
            }
        }
        return true;
    }
}
