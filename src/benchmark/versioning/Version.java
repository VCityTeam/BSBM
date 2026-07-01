package benchmark.versioning;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Version {
    private final String id;
    private final Set<String> data;
    private final List<Version> parents;

    public Version(String id, Set<String> data, List<Version> parents) {
        this.id = id;
        this.data = Collections.unmodifiableSet(new HashSet<>(data));
        this.parents = Collections.unmodifiableList(parents);
    }

    public String getId() {
        return id;
    }

    public Set<String> getData() {
        return data;
    }

    public List<Version> getParents() {
        return parents;
    }

    @Override
    public String toString() {
        return "Version{" +
                "id='" + id + '\'' +
                ", data=" + data +
                ", parents=" + parents.stream().map(Version::getId).toList() +
                '}';
    }
}
