package castendyck.inmemorycompiling;

import castendyck.inmemorycompiling.methods.NormalMethodSourceCodeBuilder;
import org.junit.Test;

import static castendyck.inmemorycompiling.classes.NormalClassSourceCodeBuilder.sourceCodeForAClass;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClassSourceCodeBuilderTest {

    @Test
    public void testFormatCorrectForSample() throws Exception {
        final String sourceCode = sourceCodeForAClass()
                .named("someClass")
                .withAField(FieldSourceCodeBuilder.sourceCodeForField()
                        .withName("firstField")
                        .withType("String").initializedWithObject("String"))
                .withAField(FieldSourceCodeBuilder.sourceCodeForField()
                        .withName("secondField")
                        .withType("Object")
                        .initializedWithObject("List"))
                .withAMethod(NormalMethodSourceCodeBuilder.sourceCodeForAMethod()
                        .named("firstMethod")
                        .withAccess("public")
                        .callingOnField("secondField", "toString")
                        .callingOnField("System.out", "println")
                        .callingWithStringParameters("System.out", "println", "Here I am"))
                .withAMethod(NormalMethodSourceCodeBuilder.sourceCodeForAMethod()
                        .named("secondMethod")
                        .withAccess("private"))
                .build();

        assertThat(sourceCode, equalTo(
                "public  class someClass  {\n" +
                        "private String firstField = new String();\n" +
                        "private Object secondField = new List();\n" +

                        "public  void firstMethod(){\n" +
                        "secondField.toString();\n" +
                        "System.out.println();\n" +
                        "System.out.println(\"Here I am\");\n" +
                        "}\n\n" +

                        "private  void secondMethod(){\n" +
                        "}\n\n" +
                        "}"
        ));
    }
}