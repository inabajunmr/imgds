package work.inabajun.imgds.score;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;


/**
 * Scoring assertion is ambiguous because I can't understand logic of scoring.
 */
public class ScorerTest {

    private final Scorer sut = new ScorerImpl("images");

    // 1_1 and 1_2 are alike. They and 2_1 are not alike.
    private final Path NOTING_IMG_PATH = FileSystems.getDefault().getPath("images", "nothing");
    private final Path EXIST_IMG_1_1 = FileSystems.getDefault().getPath("images", "1_1.jpg");
    private final Path EXIST_IMG_1_2 = FileSystems.getDefault().getPath("images", "1_2.jpg");
    private final Path EXIST_IMG_2_1= FileSystems.getDefault().getPath("images", "2_1.jpg");

    @Test(expected = IllegalArgumentException.class)
    public void nullPath1() throws IOException {
        sut.score(null, EXIST_IMG_1_1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPath2() throws IOException {
        sut.score(EXIST_IMG_1_1, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void notExistPath1() throws IOException {
        sut.score(NOTING_IMG_PATH, EXIST_IMG_1_1);
    }

    @Test(expected = FileNotFoundException.class)
    public void notExistPath2() throws IOException {
        sut.score(EXIST_IMG_1_1, NOTING_IMG_PATH);
    }

    @Test(expected = FileNotFoundException.class)
    public void notExistPath1AndPath2() throws IOException {
        sut.score(NOTING_IMG_PATH, NOTING_IMG_PATH);
    }

    @Test
    public void scoreHi() throws IOException {
        // hi score
        double score = sut.score(EXIST_IMG_1_1, EXIST_IMG_1_2);
        assertThat(score).isLessThan(30.0d);
    }

    @Test
    public void scoreLow() throws IOException {
        // low score
        double score = sut.score(EXIST_IMG_1_1, EXIST_IMG_2_1);
        assertThat(score).isLessThan(10.0d);
    }

}