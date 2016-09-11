package com.project;

import jsat.classifiers.CategoricalResults;
import jsat.classifiers.Classifier;
import jsat.classifiers.DataPoint;
import jsat.linear.DenseVector;
import jsat.linear.Vec;
import jsat.utils.SystemInfo;

import javax.xml.crypto.Data;
import java.awt.peer.SystemTrayPeer;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ben on 9/1/2016.
 */
public class MuseDataStream {
    private BlockingQueue<Action> decisionQueue;
    private String dataFile;
    private Classifier classifier;

    public MuseDataStream(String dataFile, Classifier classifier, BlockingQueue<Action> decisionQueue){
        this.dataFile = dataFile;
        this.classifier = classifier;
        this.decisionQueue = decisionQueue;
    }

    public void startMuseDataStream(String pathToExec, String... execParams){
        try{
            System.out.println("starting muse stream");
            RealTimeScanner scanner = new RealTimeScanner();
            scanner.start();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private class RealTimeScanner extends Thread {
        File sourceFile;
        FileReader reader;
        CategoricalResults results;
        BufferedReader bufferedReader;

        //variables to store spectrum values from muse headset and datapoint for classification
        List<Double> spectrumvalues = new ArrayList<>();
        DataPoint dataPoint;

        //store last 14 classifications for moving averaging
        int[] resultBuffer = {1,0,1,0,1,0,1,0,1,0,1,0,1,0};
        int i = 0;
        float avg;

        //strings to parse file data
        String[] museElements = {"/muse/elements/alpha_absolute", "/muse/elements/beta_absolute",
                                "/muse/elements/delta_absolute", "/muse/elements/gamma_absolute",
                                "/muse/elements/theta_absolute"};


        public RealTimeScanner() throws FileNotFoundException {
            sourceFile = new File(dataFile);
            reader = new FileReader(sourceFile);
            bufferedReader = new BufferedReader(reader);
        }

        @Override
        public void start() {
            try {
                System.out.println("started scanning");
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] words = line.split(", ");
                    if (Arrays.asList(museElements).contains(words[1])){
                        spectrumvalues.add(Double.parseDouble(words[2]));
                        spectrumvalues.add(Double.parseDouble(words[3]));
                        spectrumvalues.add(Double.parseDouble(words[4]));
                        spectrumvalues.add(Double.parseDouble(words[5]));

                        if(words[1].equals(museElements[4])) {
                            Vec vector = new DenseVector(spectrumvalues);
                            dataPoint = new DataPoint(vector);

                            spectrumvalues.clear();
                            results = classifier.classify(dataPoint);
                            resultBuffer[i % 14] = results.mostLikely();
                            avg = avgArray(resultBuffer, 14);

                            if (avg < 0.10)
                                //decisionQueue.offer(Action.OPEN, 5, TimeUnit.SECONDS);
                                System.out.println("open");
                            else if (avg > 0.90)
                                //decisionQueue.offer(Action.CLOSE, 5, TimeUnit.SECONDS);
                                System.out.println("close");
                            i++;
                        }
                    }
                    sleep(1);
                }
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        private float avgArray(int[] array, int length){
            int sum = 0;
            float avg;

            for(int i = 0; i < length; i++){
                sum += array[i];
            }
            avg = (float) sum/length;
            return avg;
        }

    }

}
