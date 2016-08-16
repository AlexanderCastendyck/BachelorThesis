package castendyck.sourcecode.localrepositorywithsourcecode;

import castendyck.sourcecode.jarpacking.internal.JarPackerImpl;
import castendyck.sourcecode.localrepositorywithsourcecode.internal.LocalRepositoryWithSourceCodeCreatorImpl;

public class LocalRepositoryWithSourceCodeCreatorFactory {
    public static LocalRepositoryWithSourceCodeCreator newInstance(){
        final JarPackerImpl jarPacker = new JarPackerImpl();
        final LocalRepositoryWithSourceCodeCreatorImpl localRepositoryWithSourceCodeCreator = new LocalRepositoryWithSourceCodeCreatorImpl(jarPacker);
        return localRepositoryWithSourceCodeCreator;
    }
}
