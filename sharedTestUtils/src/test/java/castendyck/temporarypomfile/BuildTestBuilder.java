package castendyck.temporarypomfile;

public class BuildTestBuilder {
    private String outputDirectory;
    private String buildDirectory;

    public static BuildTestBuilder aBuild() {
        return new BuildTestBuilder();
    }

    public BuildTestBuilder withOutpoutDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
        return this;
    }

    public BuildTestBuilder withBuildDirectory(String buildDirectory) {
        this.buildDirectory = buildDirectory;
        return this;
    }

    public void writeToStringBuilder(StringBuilder stringBuilder) {
        stringBuilder.append("<build>");
        if (buildDirectory != null){
            stringBuilder.append("<directory>")
                    .append(buildDirectory)
                    .append("</directory>");
        }
        if (outputDirectory != null){
            stringBuilder.append("<outputDirectory>")
                    .append(outputDirectory)
                    .append("</outputDirectory>");
        }
        stringBuilder.append("</build>");
    }
}
