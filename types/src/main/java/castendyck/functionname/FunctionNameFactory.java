package castendyck.functionname;

import castendyck.functionname.internal.FunctionNameImpl;

public class FunctionNameFactory {
    public static FunctionName createNew(String input){
        return FunctionNameImpl.parse(input);
    }
}
