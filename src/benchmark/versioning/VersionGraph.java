package benchmark.versioning;

import java.util.*;
import java.util.stream.Collectors;

public class VersionGraph {
    private final Map<String, Version> versions = new HashMap<>();
    private final MergePolicy globalPolicy;

    public VersionGraph(MergePolicy globalPolicy) {
        this.globalPolicy = globalPolicy;
    }

    public Version createRoot(String id, Set<String> initialData) {
        if (versions.containsKey(id)) {
            throw new IllegalArgumentException("Version with id " + id + " already exists.");
        }
        Version root = new Version(id, initialData, Collections.emptyList());
        versions.put(id, root);
        return root;
    }

    public Version createTransition(String id, Version parent, Set<String> additions, Set<String> deletions) {
        if (versions.containsKey(id)) {
            throw new IllegalArgumentException("Version with id " + id + " already exists.");
        }
        Set<String> newData = new HashSet<>(parent.getData());
        newData.removeAll(deletions);
        newData.addAll(additions);
        Version v = new Version(id, newData, Collections.singletonList(parent));
        versions.put(id, v);
        return v;
    }

    public Version createMerge(String id, List<Version> parents) {
        if (versions.containsKey(id)) {
            throw new IllegalArgumentException("Version with id " + id + " already exists.");
        }
        if (parents.size() < 2) {
            throw new IllegalArgumentException("Merge node must have at least 2 parents.");
        }
        
        Set<Set<String>> parentsData = parents.stream()
                .map(Version::getData)
                .collect(Collectors.toSet());
        
        Set<String> mergedData = globalPolicy.apply(parentsData);
        Version v = new Version(id, mergedData, parents);
        versions.put(id, v);
        return v;
    }

    public Collection<Version> getVersions() {
        return Collections.unmodifiableCollection(versions.values());
    }

    public MergePolicy getGlobalPolicy() {
        return globalPolicy;
    }
}
