package work.inabajun.imgds.score;
import lombok.RequiredArgsConstructor;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
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
public class ScorerImpl implements Scorer {

    private final String indexPath;

    @Override
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
        // setup for indexer
        IndexWriterConfig conf = new IndexWriterConfig(new WhitespaceAnalyzer());
        try(IndexWriter iw = new IndexWriter(FSDirectory.open(Paths.get("index")), conf);){
            GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(false, false);
            globalDocumentBuilder.addExtractor(CEDD.class);
            globalDocumentBuilder.addExtractor(FCTH.class);
            globalDocumentBuilder.addExtractor(AutoColorCorrelogram.class);

            BufferedImage img = ImageIO.read(new FileInputStream(image1Path.toString()));
            Document document = globalDocumentBuilder.createDocument(img, image1Path.toString());
            iw.addDocument(document);
        }

        // search(and scoring)
        // TODO
        return 0;
    }
}
