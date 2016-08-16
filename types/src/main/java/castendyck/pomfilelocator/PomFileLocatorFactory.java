package castendyck.pomfilelocator;

import castendyck.pomfilelocator.internal.PomFileLocatorImpl;

public class PomFileLocatorFactory {

    public static PomFileLocator createDefaultOne() {
        return new PomFileLocatorImpl();
    }
}
