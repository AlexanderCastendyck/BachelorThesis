package de.castendyck.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {

    public static Path resolveRelative(String start, String relative) {
        final Path relativePath = Paths.get(relative);
        final Path startPath = Paths.get(start);
        return resolveRelative(startPath, relativePath);
    }

    public static Path resolveRelative(Path start, Path relative){
        final String stringRepresentation = relative.toString();
        final String[] split = stringRepresentation.split("/");

        Path resultingPath = start;
        for (String pathPart : split) {
            if(pathPart.isEmpty()){
                continue;
            }

            if(pathPart.equals("..")){
                resultingPath = resultingPath.getParent();
            }else{
                resultingPath = resultingPath.resolve(pathPart);
            }
        }
        return resultingPath;
    }
}
