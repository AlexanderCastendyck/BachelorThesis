package castendyck.vulnerablepoint.usedcpefinding;

import castendyck.vulnerablepoint.usedcpefinding.internal.UsedCpeFinderImpl;

public class UsedCpeFinderFactory {
    public static UsedCpeFinderImpl newInstance(){
        return new UsedCpeFinderImpl();
    }
}
