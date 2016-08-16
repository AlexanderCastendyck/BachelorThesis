package castendyck.inmemorycompiling;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

public class SourceCode extends SimpleJavaFileObject {
    private String className;

    private String code;

    private SourceCode(URI uri,String className,  String code) {
        super(uri, Kind.SOURCE);
        this.className = className;
        this.code = code;
    }

    public static SourceCode sourceCodeFor(String className, String code) {
        final String name = "string:///" + className.replace('.', '/') + Kind.SOURCE.extension;
        URI uri = URI.create(name);
        return new SourceCode(uri, className, code);
    }

    @Override
    public CharSequence getCharContent(boolean b) throws IOException {
        return code;
    }

    public String getCode() {
        return code;
    }

    public String getClassName() {
        return className;
    }
}
