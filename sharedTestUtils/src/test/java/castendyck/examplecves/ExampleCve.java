package castendyck.examplecves;

import castendyck.cpe.CPE;
import castendyck.cve.CVE;
import castendyck.cve.CveBuilder;
import castendyck.cvss.CvssBuilder;
import castendyck.cwe.CWE;
import castendyck.references.Reference;
import castendyck.references.ReferenceBuilder;
import castendyck.vulnerablesoftware.VulnerableSoftware;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExampleCve {
    public static List<CVE> cveForMailApi() {
        Set<VulnerableSoftware> vulnerableSoftware = asSet(VulnerableSoftware.createFromString("cpe:/a:sun:javamail"));

        Set<Reference> references = Collections.singleton(
                ReferenceBuilder.aReference().withName("20071116 Javamail login username and password same email problem")
                        .withSource("BUGTRAQ")
                        .withUrl("http://archives.neohapsis.com/archives/bugtraq/2007-11/0239.html")
                        .build()
        );

        final List<CPE> cpes = Collections.singletonList(CPE.createNew("cpe:/a:sun:javamail"));

        final CVE cve = CveBuilder.aCve()
                .withName("CVE-2007-6059")
                .withDescription("** DISPUTED **  Javamail does not properly handle a series of invalid login attempts in which the same e-mail address is entered as username and password, and the domain portion of this address yields a Java UnknownHostException error, which allows remote attackers to cause a denial of service (connection pool exhaustion) via a large number of requests, resulting in a SQLNestedException.  NOTE: Sun disputes this issue, stating \"The report makes references to source code and files that do not exist in the mentioned products.\"")
                .withCwe(CWE.createNew("CWE-399 Resource Management Errors"))
                .withCvss(CvssBuilder.aCvss()
                        .withAScore(5)
                        .withAccessVector("NETWORK")
                        .withAccessComplexity("LOW")
                        .withAuthentication("NONE")
                        .withConfidentialityImpact("NONE")
                        .withIntegrityImpact("NONE")
                        .withAvailabilityImpact("PARTIAL")
                        .build())
                .withVulnerableSoftwareList(vulnerableSoftware)
                .withReferences(references)
                .withCpes(cpes)
                .build();

        return Collections.singletonList(cve);
    }

    public static List<CVE> cveForCommonsCollection() {
        Set<VulnerableSoftware> vulnerableSoftware = asSet(
                VulnerableSoftware.createFromString("cpe:/a:apache:commons_collections:3.2.1"),
                VulnerableSoftware.createFromString("cpe:/a:apache:commons_collections:4.0")
        );
        Set<Reference> references = Collections.singleton(
                ReferenceBuilder.aReference().withName("20151209 Vulnerability in Java Deserialization Affecting Cisco Products")
                        .withSource("CISCO")
                        .withUrl("http://tools.cisco.com/security/center/content/CiscoSecurityAdvisory/cisco-sa-20151209-java-deserialization")
                        .build()
        );

        final List<CPE> cpes = Collections.singletonList(CPE.createNew("cpe:/a:apache:commons_collections:3.2.1"));

        final CVE cve = CveBuilder.aCve()
                .withName("CVE-2015-6420")
                .withDescription("Serialized-object interfaces in certain Cisco Collaboration and Social Media; Endpoint Clients and Client Software; Network Application, Service, and Acceleration; Network and Content Security Devices; Network Management and Provisioning; Routing and Switching - Enterprise and Service Provider; Unified Computing; Voice and Unified Communications Devices; Video, Streaming, TelePresence, and Transcoding Devices; Wireless; and Cisco Hosted Services products allow remote attackers to execute arbitrary commands via a crafted serialized Java object, related to the Apache Commons Collections (ACC) library.")
                .withCwe(CWE.createNew(""))
                .withCvss(CvssBuilder.aCvss()
                        .withAScore(7.5)
                        .withAccessVector("NETWORK")
                        .withAccessComplexity("LOW")
                        .withAuthentication("NONE")
                        .withConfidentialityImpact("PARTIAL")
                        .withIntegrityImpact("PARTIAL")
                        .withAvailabilityImpact("PARTIAL")
                        .build())
                .withVulnerableSoftwareList(vulnerableSoftware)
                .withReferences(references)
                .withCpes(cpes)
                .build();

        return Collections.singletonList(cve);
    }

    public static List<CVE> cvesForAether() {
        return Arrays.asList(
                cveForCVE_2010_4647(),
                cveForCVE_2008_7271()
        );
    }

    private static CVE cveForCVE_2010_4647() {
        Set<VulnerableSoftware> vulnerableSoftware = asSet(
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.3.1.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:rc1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:rc2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:rc3"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:rc4"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.5.2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:m1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:m2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:m3"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:m4"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:m5"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:m6"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6:m7"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.6.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.4.2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.5.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.1.2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.2.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.0.2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.1.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.3.2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.4.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.2.2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.3.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.0.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:2.1.2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:2.1.3"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:2.0.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:2.0.2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:2.1.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:2.1"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.0"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.3"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.2"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:2.0"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:1.0"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.5"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.4")
        );


        Set<Reference> references = asSet(
                ReferenceBuilder.aReference().withName("http://yehg.net/lab/pr0js/advisories/eclipse/[eclipse_help_server]_cross_site_scripting")
                        .withSource("MISC")
                        .withUrl("http://yehg.net/lab/pr0js/advisories/eclipse/%5Beclipse_help_server%5D_cross_site_scripting")
                        .build(),
                ReferenceBuilder.aReference().withName("[oss-security] 20110106 Re: CVE Request: Eclipse IDE Version: 3.6.1 | Help Server Local Cross Site Scripting (XSS)")
                        .withSource("MLIST")
                        .withUrl("http://openwall.com/lists/oss-security/2011/01/06/16")
                        .build(),
                ReferenceBuilder.aReference().withName("FEDORA-2010-18990")
                        .withSource("FEDORA")
                        .withUrl("http://lists.fedoraproject.org/pipermail/package-announce/2010-December/052532.html")
                        .build(),
                ReferenceBuilder.aReference().withName("RHSA-2011:0568")
                        .withSource("REDHAT")
                        .withUrl("http://www.redhat.com/support/errata/RHSA-2011-0568.html")
                        .build(),
                ReferenceBuilder.aReference().withName("eclipseide-querystring-xss(64833)")
                        .withSource("XF")
                        .withUrl("http://xforce.iss.net/xforce/xfdb/64833")
                        .build(),
                ReferenceBuilder.aReference().withName("FEDORA-2010-19006")
                        .withSource("FEDORA")
                        .withUrl("http://lists.fedoraproject.org/pipermail/package-announce/2010-December/052554.html")
                        .build(),
                ReferenceBuilder.aReference().withName("MDVSA-2011:032")
                        .withSource("MANDRIVA")
                        .withUrl("http://www.mandriva.com/security/advisories?name=MDVSA-2011:032")
                        .build(),
                ReferenceBuilder.aReference().withName("[oss-security] 20110106 CVE Request: Eclipse IDE Version: 3.6.1 | Help Server Local Cross Site Scripting (XSS)")
                        .withSource("MLIST")
                        .withUrl("http://openwall.com/lists/oss-security/2011/01/06/7")
                        .build(),
                ReferenceBuilder.aReference().withName("https://bugs.eclipse.org/bugs/show_bug.cgi?id=329582")
                        .withSource("MISC")
                        .withUrl("https://bugs.eclipse.org/bugs/show_bug.cgi?id=329582")
                        .build()
        );

        final List<CPE> cpes = Collections.singletonList(CPE.createNew("cpe:/a:eclipse:eclipse_ide:3.6.1"));

        final CVE cve = CveBuilder.aCve()
                .withName("CVE-2010-4647")
                .withDescription("Multiple cross-site scripting (XSS) vulnerabilities in the Help Contents web application (aka the Help Server) in Eclipse IDE before 3.6.2 allow remote attackers to inject arbitrary web script or HTML via the query string to (1) help/index.jsp or (2) help/advanced/content.jsp.")
                .withCwe(CWE.createNew("CWE-79 Improper Neutralization of Input During Web Page Generation ('Cross-site Scripting')"))
                .withCvss(CvssBuilder.aCvss()
                        .withAScore(4.3)
                        .withAccessVector("NETWORK")
                        .withAccessComplexity("MEDIUM")
                        .withAuthentication("NONE")
                        .withConfidentialityImpact("NONE")
                        .withIntegrityImpact("PARTIAL")
                        .withAvailabilityImpact("NONE")
                        .build())
                .withVulnerableSoftwareList(vulnerableSoftware)
                .withReferences(references)
                .withCpes(cpes)
                .build();

        return cve;
    }

    private static CVE cveForCVE_2008_7271() {
        Set<VulnerableSoftware> vulnerableSoftware = asSet(VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide"),
                VulnerableSoftware.createFromString("cpe:/a:eclipse:eclipse_ide:3.3.2")
        );
        Set<Reference> references = asSet(
                ReferenceBuilder.aReference().withName("https://bugs.eclipse.org/bugs/show_bug.cgi?id=223539")
                        .withSource("MISC")
                        .withUrl("https://bugs.eclipse.org/bugs/show_bug.cgi?id=223539")
                        .build(),
                ReferenceBuilder.aReference().withName("http://r00tin.blogspot.com/2008/04/eclipse-local-web-server-exploitation.html")
                        .withSource("MISC")
                        .withUrl("http://r00tin.blogspot.com/2008/04/eclipse-local-web-server-exploitation.html")
                        .build()
        );

        final List<CPE> cpes = Collections.singletonList(CPE.createNew("cpe:/a:eclipse:eclipse_ide"));

        final CVE cve = CveBuilder.aCve()
                .withName("CVE-2008-7271")
                .withDescription("Multiple cross-site scripting (XSS) vulnerabilities in the Help Contents web application (aka the Help Server) in Eclipse IDE, possibly 3.3.2, allow remote attackers to inject arbitrary web script or HTML via (1) the searchWord parameter to help/advanced/searchView.jsp or (2) the workingSet parameter in an add action to help/advanced/workingSetManager.jsp, a different issue than CVE-2010-4647.")
                .withCwe(CWE.createNew("CWE-79 Improper Neutralization of Input During Web Page Generation ('Cross-site Scripting')"))
                .withCvss(CvssBuilder.aCvss()
                        .withAScore(4.3)
                        .withAccessVector("NETWORK")
                        .withAccessComplexity("MEDIUM")
                        .withAuthentication("NONE")
                        .withConfidentialityImpact("NONE")
                        .withIntegrityImpact("PARTIAL")
                        .withAvailabilityImpact("NONE")
                        .build())
                .withVulnerableSoftwareList(vulnerableSoftware)
                .withReferences(references)
                .withCpes(cpes)
                .build();

        return cve;
    }

    public static CVE cveForCVE_2013_22541() {
        Set<VulnerableSoftware> vulnerableSoftware = asSet(VulnerableSoftware.createFromString("cpe:/a:apache:org.apache.sling.servlets.post:2.2.0"),
                VulnerableSoftware.createFromString("cpe:/a:apache:org.apache.sling.servlets.post:2.3.0")
        );
        Set<Reference> references = asSet(
                ReferenceBuilder.aReference().withName("62903")
                        .withSource("BID")
                        .withUrl("http://www.securityfocus.com/bid/62903")
                        .build(),
                ReferenceBuilder.aReference().withName("apache-sling-cve20132254-dos(87765)")
                        .withSource("XF")
                        .withUrl("http://xforce.iss.net/xforce/xfdb/87765")
                        .build(),
                ReferenceBuilder.aReference().withName("https://issues.apache.org/jira/browse/SLING-2913")
                        .withSource("CONFIRM")
                        .withUrl("https://issues.apache.org/jira/browse/SLING-2913")
                        .build(),
                ReferenceBuilder.aReference().withName("[sling-dev] 20131009 [CVE-2013-2254] Apache Sling denial of service vulnerability")
                        .withSource("MLIST")
                        .withUrl("http://mail-archives.apache.org/mod_mbox/sling-dev/201310.mbox/%3CCAKkCf4pue6PnESsP1KTdEDJm1gpkANFaK%2BvUd9mzEVT7tXL%2B3A%40mail.gmail.com%3E")
                        .build()
        );

        final List<CPE> cpes = Collections.singletonList(CPE.createNew("cpe:/a:apache:org.apache.sling.servlets.post:2.2.0"));

        final CVE cve = CveBuilder.aCve()
                .withName("CVE-2013-2254")
                .withDescription("The deepGetOrCreateNode function in impl/operations/AbstractCreateOperation.java in org.apache.sling.servlets.post.bundle 2.2.0 and 2.3.0 in Apache Sling does not properly handle a NULL value that returned when the session does not have permissions to the root node, which allows remote attackers to cause a denial of service (infinite loop) via unspecified vectors.")
                .withCwe(CWE.createNew("CWE-119 Improper Restriction of Operations within the Bounds of a Memory Buffer"))
                .withCvss(CvssBuilder.aCvss()
                        .withAScore(5.0)
                        .withAccessVector("NETWORK")
                        .withAccessComplexity("LOW")
                        .withAuthentication("NONE")
                        .withConfidentialityImpact("NONE")
                        .withIntegrityImpact("NONE")
                        .withAvailabilityImpact("PARTIAL")
                        .build())
                .withVulnerableSoftwareList(vulnerableSoftware)
                .withReferences(references)
                .withCpes(cpes)
                .build();

        return cve;
    }

    public static CVE cveForCVE_2014_0086() throws Exception{
        Set<VulnerableSoftware> vulnerableSoftware = asSet(
                VulnerableSoftware.createFromString("cpe:/a:redhat:jboss_web_framework_kit:2.5.0"),
                VulnerableSoftware.createFromString("cpe:/cpe:/a:redhat:richfaces:5.0.0:alpha1"),
                VulnerableSoftware.createFromString("cpe:/cpe:/a:redhat:richfaces:5.0.0:alpha2"),
                VulnerableSoftware.createFromString("cpe:/cpe:/a:redhat:richfaces:5.0.0:alpha3"),
                VulnerableSoftware.createFromString("cpe:/cpe:/a:redhat:richfaces:4.3.4"),
                VulnerableSoftware.createFromString("cpe:/cpe:/a:redhat:richfaces:4.3.5")
        );
        Set<Reference> references = asSet(
                ReferenceBuilder.aReference().withName("https://github.com/pslegr/core-1/commit/8131f15003f5bec73d475d2b724472e4b87d0757")
                        .withSource("CONFIRM")
                        .withUrl("https://github.com/pslegr/core-1/commit/8131f15003f5bec73d475d2b724472e4b87d0757")
                        .build(),
                ReferenceBuilder.aReference().withName("https://issues.jboss.org/browse/RF-13250")
                        .withSource("CONFIRM")
                        .withUrl("https://issues.jboss.org/browse/RF-13250")
                        .build(),
                ReferenceBuilder.aReference().withName("https://bugzilla.redhat.com/show_bug.cgi?id=1067268")
                        .withSource("CONFIRM")
                        .withUrl("https://bugzilla.redhat.com/show_bug.cgi?id=1067268")
                        .build()
        );

        final List<CPE> cpes = Collections.singletonList(CPE.createNew("cpe:/cpe:/a:redhat:richfaces:5.0.0:alpha3"));

        final CVE cve = CveBuilder.aCve()
                .withName("CVE-2014-0086")
                .withDescription("The doFilter function in webapp/PushHandlerFilter.java in JBoss RichFaces 4.3.4, 4.3.5, and 5.x allows remote attackers to cause a denial of service (memory consumption and out-of-memory error) via a large number of malformed atmosphere push requests.")
                .withCwe(CWE.createNew("CWE-20 Improper Input Validation"))
                .withCvss(CvssBuilder.aCvss()
                        .withAScore(4.3)
                        .withAccessVector("NETWORK")
                        .withAccessComplexity("MEDIUM")
                        .withAuthentication("NONE")
                        .withConfidentialityImpact("NONE")
                        .withIntegrityImpact("NONE")
                        .withAvailabilityImpact("PARTIAL")
                        .build())
                .withVulnerableSoftwareList(vulnerableSoftware)
                .withReferences(references)
                .withCpes(cpes)
                .build();

        return cve;
    }

    private static Set<Reference> asSet(Reference... references) {
        final Set<Reference> set = Arrays.stream(references)
                .collect(Collectors.toSet());
        return set;
    }

    private static Set<VulnerableSoftware> asSet(VulnerableSoftware... vulnerableSoftware) {
        final Set<VulnerableSoftware> set = Arrays.stream(vulnerableSoftware)
                .collect(Collectors.toSet());
        return set;
    }
}
