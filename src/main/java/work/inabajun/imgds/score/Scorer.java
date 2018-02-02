package work.inabajun.imgds.score;

import java.nio.file.Path;

public interface Scorer {

    /**
     * Calculate score of difference amount.
     *
     * @param image1Path image file1 path
     * @param image2Path image file2 path
     * @return
     */
    public double score (Path image1Path, Path image2Path);
}
