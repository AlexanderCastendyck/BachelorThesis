package castendyck.scope;

import castendyck.scope.internal.MavenScopeImpl;

public class MavenScopeFactory {
    public static MavenScope createNew(String input){
        return MavenScopeImpl.parse(input);
    }
}
