package castendyck.vulnerablepoint;

public interface VulnerablePointsProvider {

    CollectedVulnerablePoints collectVulnerablePoints(VulnerablePointsRequestDto vulnerablePointsRequestDto) throws VulnerablePointsCollectingException;
}
