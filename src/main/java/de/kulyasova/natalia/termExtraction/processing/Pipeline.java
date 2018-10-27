package de.kulyasova.natalia.termExtraction.processing;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stopwordremover.StopWordRemover;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static de.tudarmstadt.ukp.dkpro.core.stopwordremover.StopWordRemover.PARAM_MODEL_LOCATION;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.JCasFactory.createJCas;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;
import static org.apache.uima.fit.util.JCasUtil.select;

public class Pipeline {
    public List<String> getTerms(String text) throws UIMAException {

        JCas jCas = createJCas();
        jCas.setDocumentLanguage("de");

        jCas.setDocumentText(text);

        runPipeline(jCas,
                createEngineDescription(OpenNlpSegmenter.class),
                createEngineDescription(OpenNlpPosTagger.class),
                createEngineDescription(LanguageToolLemmatizer.class),
                createEngineDescription(StopWordRemover.class, PARAM_MODEL_LOCATION, "classpath:/stopwords.txt"));

        return select(jCas, Lemma.class)
                .stream()
                .map(Lemma::getValue)
                .filter((lemmaString) -> lemmaString.length() > 4)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws MalformedURLException {
//        Pipeline pipeline = new Pipeline();
//        List<String> lemmas = pipeline.getTerms("Ich bin Martin Hubers Frau.");
//
//        List<String> filtered = lemmas.stream().filter((it) -> it.length() > 4).collect(Collectors.toList());
//
//        System.out.println("lemmas = " + filtered);

        File file = new File("/home/natalia/IdeaProjects/uimaFitPipeline/src/main/resources/stopwords.txt");
        URL fileURL = file.toURI().toURL();
        System.out.println("uri from file: "+ fileURL);

        URL jarURL = Pipeline.class.getResource("/stopwords.txt");
        System.out.println("uri from classpath: "+jarURL);
        System.out.println();


        System.out.println("file = " + file);
        System.out.println("file.exists() = " + file.exists());
        File fileFromClasspathURL = new File(jarURL.getFile());
        System.out.println("fileFromClasspathURL = " + fileFromClasspathURL);
        System.out.println("fileFromClasspathURL.exists() = " + fileFromClasspathURL.exists());

//        try {
//            InputStream inputStream = jarURL.openStream();
//
//            String textFromFile = IOUtils.toString(inputStream, "utf8");
//            System.out.println("textFromFile = \n" + textFromFile);
//
//            FileUtils.write(new File(file.getAbsolutePath()+".copy"), textFromFile, "utf8");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        URL urlFromFileWithBlank = Pipeline.class.getResource("/stop words.txt");
        File fileWithBlank = new File(urlFromFileWithBlank.getFile());
        System.out.println("fileWithBlank = " + fileWithBlank);
        System.out.println("fileWithBlank.exists() = " + fileWithBlank.exists());
    }
}
