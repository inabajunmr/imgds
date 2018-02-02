package work.inabajun.imgds.score;

import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;


/**
 * Scoring assertion is ambiguous because I can't understand logic of scoring.
 */
public class ScorerTest {

    private final Scorer sut = new ScorerImpl();

    // 1_1 and 1_2 are alike. They and 2_1 are not alike.
    private final Path NOTING_IMG_PATH = FileSystems.getDefault().getPath("images", "nothing");
    private final Path EXIST_IMG_1_1 = FileSystems.getDefault().getPath("images", "1_1.jpg");
    private final Path EXIST_IMG_1_2 = FileSystems.getDefault().getPath("images", "1_2.jpg");
    private final Path EXIST_IMG_2_1= FileSystems.getDefault().getPath("images", "2_1.jpg");

    @Test(expected = IllegalArgumentException.class)
    public void nullPath1(){
        sut.score(null, EXIST_IMG_1_1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPath2(){
        sut.score(EXIST_IMG_1_1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notExistPath1(){
        sut.score(NOTING_IMG_PATH, EXIST_IMG_1_1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notExistPath2(){
        sut.score(EXIST_IMG_1_1, NOTING_IMG_PATH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notExistPath1AndPath2(){
        sut.score(NOTING_IMG_PATH, EXIST_IMG_1_1);
    }

    @Test
    public void scoreHi(){
        // hi score
        double score = sut.score(EXIST_IMG_1_1, EXIST_IMG_1_2);
        assertThat(score).isLessThan(30.0d);
    }

    @Test
    public void scoreLow(){
        // low score
        double score = sut.score(EXIST_IMG_1_1, EXIST_IMG_2_1);
        assertThat(score).isLessThan(10.0d);
    }

}