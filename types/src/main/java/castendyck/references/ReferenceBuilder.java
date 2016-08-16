package castendyck.references;

import javax.annotation.Nullable;

import static de.castendyck.enforcing.NullStringEscaper.escapeNullStringWith;

public class ReferenceBuilder {
    private String name;
    private String source;
    private String url;

    public static ReferenceBuilder aReference() {
        return new ReferenceBuilder();
    }

    public ReferenceBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ReferenceBuilder withSource(@Nullable String source) {
        String escapedValue = escapeNullStringWith(source, "");
        this.source = escapedValue;
        return this;
    }

    public ReferenceBuilder withUrl(@Nullable String url) {
        String escapedValue = escapeNullStringWith(url, "");
        this.url = escapedValue;
        return this;
    }

    public Reference build() {
        return new Reference(name, source, url);
    }
}
