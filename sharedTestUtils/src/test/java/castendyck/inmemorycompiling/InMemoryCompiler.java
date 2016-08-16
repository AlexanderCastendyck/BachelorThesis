package castendyck.inmemorycompiling;

import javax.tools.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryCompiler {
    private final CompilationFileManager fileManager;
    private final JavaCompiler compiler;
    private final DiagnosticCollector<JavaFileObject> diagnosticCollector;

    public InMemoryCompiler(JavaFileManager fileManager) {
        this.fileManager = new CompilationFileManager(fileManager);
        this.compiler = ToolProvider.getSystemJavaCompiler();
        diagnosticCollector = new DiagnosticCollector<>();
    }

    public List<ByteCode> compile(List<SourceCode> filesToCompile) throws CompilationFinishedWithWarningsExceptions, IOException {
        final JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, null, null, filesToCompile);
        task.call();

        final List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();
        if (diagnostics.isEmpty()) {
            return fileManager.getCompiledByteCode();
        } else {
            StringBuilder messageBuilder = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
                appendCurrentDiagnostic(messageBuilder, diagnostic);
            }

            final String warnings = messageBuilder.toString();

            final String code = filesToCompile.stream()
                    .map(SourceCode::getCode)
                    .collect(Collectors.joining("\n\n"));

            final String message = warnings + "\n\n"+ "Code:\n" + code;
            throw new CompilationFinishedWithWarningsExceptions(message);
        }
    }

    private void appendCurrentDiagnostic(StringBuilder messageBuilder, Diagnostic<? extends JavaFileObject> diagnostic) {
        final String message = diagnostic.getMessage(null);
        final long lineNumber = diagnostic.getLineNumber();
        final JavaFileObject source = diagnostic.getSource();
        final String sourceFile = source.getName();
        final Diagnostic.Kind kind = diagnostic.getKind();
        messageBuilder.append("Compilation " + kind + ":\n")
                .append(message)
                .append("; (line ")
                .append(lineNumber)
                .append(" in ")
                .append(sourceFile)
                .append(" )\n\n");
    }
}
