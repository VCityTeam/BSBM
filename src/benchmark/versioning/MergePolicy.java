package benchmark.versioning;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BinaryOperator;

public enum MergePolicy {
    UNION(MergePolicy::union),
    INTERSECTION(MergePolicy::intersection),
    SYMMETRIC_DIFFERENCE(MergePolicy::symmetricDifference);

    private final BinaryOperator<Set<String>> operator;

    MergePolicy(BinaryOperator<Set<String>> operator) {
        this.operator = operator;
    }

    public Set<String> apply(Set<Set<String>> parentsData) {
        if (parentsData == null || parentsData.isEmpty()) {
            return new HashSet<>();
        }
        return parentsData.stream().reduce(this.operator).orElse(new HashSet<>());
    }

    private static Set<String> union(Set<String> s1, Set<String> s2) {
        Set<String> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    private static Set<String> intersection(Set<String> s1, Set<String> s2) {
        Set<String> result = new HashSet<>(s1);
        result.retainAll(s2);
        return result;
    }

    private static Set<String> symmetricDifference(Set<String> s1, Set<String> s2) {
        Set<String> result = new HashSet<>(s1);
        result.addAll(s2);
        Set<String> common = new HashSet<>(s1);
        common.retainAll(s2);
        result.removeAll(common);
        return result;
    }
}
