package de.kulyasova.natalia.termExtraction.processing;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

import java.util.List;
import java.util.stream.Collectors;

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
                createEngineDescription(LanguageToolLemmatizer.class));

        return select(jCas, Lemma.class)
                .stream()
                .map(Lemma::getValue)
                .filter((lemmaString)->lemmaString.length()>4)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws UIMAException {
        Pipeline pipeline = new Pipeline();
        List<String> lemmas = pipeline.getTerms("Ich bin Martin Hubers Frau.");

        List<String> filtered = lemmas.stream().filter((it) -> it.length() > 4).collect(Collectors.toList());

        System.out.println("lemmas = " + filtered);
    }
}
