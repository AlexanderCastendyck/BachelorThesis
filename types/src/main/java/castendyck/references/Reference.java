package castendyck.references;

import static de.castendyck.enforcing.NotNullConstraintEnforcer.ensureNotNullOrEmptyString;

public class Reference {
    private final String name;
    private final String source;
    private final String url;


    Reference(String name, String source, String url) {
        ensureNotNullOrEmptyString(name);
        this.name = name;
        ensureNotNullOrEmptyString(source);
        this.source = source;
        ensureNotNullOrEmptyString(url);
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reference reference = (Reference) o;

        if (!name.equals(reference.name)) return false;
        if (!source.equals(reference.source)) return false;
        return url.equals(reference.url);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Reference{" +
                "name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
