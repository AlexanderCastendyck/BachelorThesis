package castendyck.inmemorycompiling;

import castendyck.inmemorycompiling.classes.ClassSourceCodeBuilder;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ByteCodeBuilder {
    private final List<ClassSourceCodeBuilder> classSourceCodeBuilders;
    private final InMemoryCompiler inMemoryCompiler;

    private ByteCodeBuilder() {
        classSourceCodeBuilders = new ArrayList<>();
        final JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager standardFileManager = systemJavaCompiler.getStandardFileManager(null, null, null);
        inMemoryCompiler = new InMemoryCompiler(standardFileManager);
    }

    public static ByteCodeBuilder aByteCode(){
        return new ByteCodeBuilder();
    }

    public ByteCodeBuilder forAClass(ClassSourceCodeBuilder classSourceCodeBuilder){
        classSourceCodeBuilders.add(classSourceCodeBuilder);
        return this;
    }

    public ByteCodeBuilder withClasses(List<ClassSourceCodeBuilder> classSourceCodeBuilder){
        classSourceCodeBuilder.stream()
                .forEach(this::forAClass);
        return this;
    }

    public List<ByteCode> build() throws CompilationFinishedWithWarningsExceptions, IOException {
        final List<SourceCode> sourceCodes = classSourceCodeBuilders.stream()
                .map(builder -> SourceCode.sourceCodeFor(builder.getName(), builder.build()))
                .collect(Collectors.toList());

        final List<ByteCode> byteCodes = inMemoryCompiler.compile(sourceCodes);

        return byteCodes;
    }
}
