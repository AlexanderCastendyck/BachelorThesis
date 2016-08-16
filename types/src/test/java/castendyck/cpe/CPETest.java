package castendyck.cpe;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class CPETest {
    @Test
    public void getVendor_returnsCorrectPart() throws Exception {
        final CPE cpe = CPE.createNew("cpe:/a:apache:org.apache.sling.servlets.post:2.2.0");

        final String vendor = cpe.getVendor();

        assertThat(vendor, equalTo("apache"));
    }

    @Test
    public void getProduct_returnsCorrectPart() throws Exception {
        final CPE cpe = CPE.createNew("cpe:/a:apache:org.apache.sling.servlets.post:2.2.0");

        final String product = cpe.getProduct();

        assertThat(product, equalTo("org.apache.sling.servlets.post"));
    }

    @Test
    public void getVersion_returnsCorrectPart() throws Exception {
        final CPE cpe = CPE.createNew("cpe:/a:apache:org.apache.sling.servlets.post:2.2.0");

        final String version = cpe.getVersion();

        assertThat(version, equalTo("2.2.0"));
    }

}