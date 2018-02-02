package work.inabajun.imgds.score;

import lombok.RequiredArgsConstructor;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class Scorer{

    private final String indexPath;

    /**
     * Calculate score of difference amount.
     *
     * <p>
     * Alike=tiny score.
     * ex.<br>
     * A=A'...1.0<br>
     * A=B...10.0
     * </p>
     *
     * @param image1Path image file1 path
     * @param image2Path image file2 path
     * @return score
     */
    public double score(Path image1Path, Path image2Path) throws IOException {
        // not null
        if (image1Path == null || image2Path == null) {
            throw new IllegalArgumentException("Must set all args!");
        }

        // existence of file
        if (Files.notExists(image1Path)) {
            throw new FileNotFoundException("File not found! path:" + image1Path.toString());
        }
        if (Files.notExists(image2Path)) {
            throw new FileNotFoundException("File not found! path:" + image2Path.toString());
        }

        // create index
        index(image1Path);

        // search(and scoring)
        return search(image2Path);
    }

    private void index(Path imagePath) throws IOException {
        IndexWriterConfig conf = new IndexWriterConfig(new WhitespaceAnalyzer());
        try (IndexWriter iw = new IndexWriter(FSDirectory.open(Paths.get(indexPath)), conf);) {
            GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(false, false);
            globalDocumentBuilder.addExtractor(CEDD.class);
            globalDocumentBuilder.addExtractor(FCTH.class);
            globalDocumentBuilder.addExtractor(AutoColorCorrelogram.class);

            BufferedImage img = ImageIO.read(new FileInputStream(imagePath.toString()));
            Document document = globalDocumentBuilder.createDocument(img, imagePath.toString());
            iw.addDocument(document);
        }
    }

    private double search(Path imagePath) throws IOException {
        BufferedImage img = ImageIO.read(imagePath.toFile());

        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        ImageSearcher searcher = new GenericFastImageSearcher(1, CEDD.class);
        ImageSearchHits hits = searcher.search(img, indexReader);

        if (hits.length() == 0) {
            return Double.MAX_VALUE;
        }

        return hits.score(0);
    }
}
