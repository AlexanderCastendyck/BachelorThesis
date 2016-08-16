package castendyck.mavenproperty;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MavenPropertyBuilder {
    private final Map<String, String> properties = new HashMap<>();

    public static MavenPropertyBuilder createProperties() {
        return new MavenPropertyBuilder();
    }

    public MavenPropertyBuilder withAProperty(String propertyName, @Nullable String propertyValue) {
        if (propertyValue == null) {
            this.properties.put(propertyName, "");
        } else {
            this.properties.put(propertyName, propertyValue);
        }
        return this;
    }

    public MavenProperties build() {
        final MavenProperties mavenProperties = new MavenProperties(properties);
        return mavenProperties;
    }
}
