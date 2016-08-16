package castendyck.bytecode.reversecalllist;

import java.io.IOException;

public class Given {
    public static When given(ReverseCallListStateBuilder reverseCallListStateBuilder) throws IOException {
        final ReverseCallListStateBuilder.State state = reverseCallListStateBuilder.build();
        return new When(state);
    }
}
