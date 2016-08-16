package castendyck.functionsignature;

import castendyck.functionsignature.internal.FunctionSignatureImpl;

public class FunctionSignatureFactory {
    public static FunctionSignature createNew(String value){
        return FunctionSignatureImpl.parse(value);
    }
}
