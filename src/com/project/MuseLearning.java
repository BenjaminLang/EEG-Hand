package com.project;

import jsat.DataSet;
import jsat.classifiers.CategoricalResults;
import jsat.classifiers.ClassificationDataSet;
import jsat.classifiers.Classifier;
import jsat.classifiers.DataPoint;
import jsat.classifiers.svm.LSSVM;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RunnableFuture;

public class MuseLearning {

    private List<DataSet> datasets;
    private ClassificationDataSet trainingSet;
    private ClassificationDataSet cvSet;
    private Classifier classifier;

    public MuseLearning(DataSet dataset, double... splitRatio){
        this.datasets = dataset.randomSplit(splitRatio);
        this.trainingSet = new ClassificationDataSet(this.datasets.get(0), 0);
        this.cvSet = new ClassificationDataSet(this.datasets.get(1), 0);
    }

    public void startLearning(){
        this.classifier = new LSSVM();
        this.classifier.trainC(this.trainingSet);
    }

    public Classifier getClassifier(){
        return this.classifier;
    }

    public int getErrorCount(){
        int errorCount = 0;
        for(int i = 0; i < datasets.get(1).getSampleSize(); i++){
            DataPoint datapoint = cvSet.getDataPoint(i);
            int value = cvSet.getDataPointCategory(i);

            CategoricalResults results = classifier.classify(datapoint);
            int predicted = results.mostLikely();
            if (predicted != value)
                errorCount++;
        }
        return errorCount;
    }


}
