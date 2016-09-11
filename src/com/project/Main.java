package com.project;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import jsat.ARFFLoader;
import jsat.DataSet;
import jsat.classifiers.DataPoint;

public class Main {

	public static void main(String[] args) throws IOException {

		BlockingQueue<Action> decisionQueue = new ArrayBlockingQueue<Action>(5);

		final String sourceFileName = "realtimedata.csv";
		final String arffFileName = "dataset.arff";
		final String[] recordingFiles ={"recording001.csv", "recording002.csv"};
		final String[] recordingFilesOutput = {"output-recording001.csv", "output-recording002.csv"};

		final double split[] = {0.85, 0.15};

		//method calls to setup up file dependencies
		setupRecordingOutputFiles(recordingFilesOutput, recordingFiles);
		setupArffFile(arffFileName, recordingFilesOutput);

		//load dataset
		File arffFile = new File("dataset.arff");
		DataSet dataset = ARFFLoader.loadArffFile(arffFile);

		//train classifier
		MuseLearning learningSuite = new MuseLearning(dataset, split);
		learningSuite.startLearning();
		System.out.println("# of errors: " + learningSuite.getErrorCount());

		//start streaming live Muse Data
		MuseDataStream dataStream = new MuseDataStream(sourceFileName, learningSuite.getClassifier(), decisionQueue);
		dataStream.startMuseDataStream("muse-player.exe", "-l 5000",
				"-C realtimedata.csv");

		//start Arduino Server
		//ArduinoServer arduinoServer = new ArduinoServer(decisionQueue);
		//arduinoServer.startTransmitting();
	}

	private static void setupRecordingOutputFiles(String[] recordingFilesOutput, String[] recordingFiles){
		//Check whether or not these post recording files already exist
		File[] f = {new File(recordingFilesOutput[0]), new File(recordingFilesOutput[1])};
		if(!f[0].exists() || !f[1].exists()){
			//parse muse files
			try{
				ParseMuseFile.convert(recordingFiles);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void setupArffFile(String arffFileName, String[] recordingFileOutput){
		//Check whether or not the arff file exists
		File arff = new File(arffFileName);
		if(!arff.exists()){
			//create arff files for JSAT
			try{
				ParseMuseFile.createArff(recordingFileOutput);
			} catch( IOException e){
				e.printStackTrace();
			}
		}
	}

}
