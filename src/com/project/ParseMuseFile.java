package com.project;

import jsat.classifiers.DataPoint;
import jsat.linear.DenseVector;
import jsat.linear.Vec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

public class ParseMuseFile {
	
	public static void convert(String... filenames) throws IOException{
		for (String name: filenames){
			File museFile = new File(name);
			File outFile = new File("output-"+name);
			FileReader reader = new FileReader(museFile);
			FileWriter writer = new FileWriter(outFile);
			BufferedReader fileIn = new BufferedReader(reader);
			BufferedWriter fileOut = new BufferedWriter(writer);
			
			String line;
			while ((line = fileIn.readLine()) != null){
				String[] words = line.split(", ");
				switch(words[1]){
					case "/muse/elements/alpha_absolute":{
						StringBuilder stringBuilder = new StringBuilder();
						for(int i = 2; i < words.length; i++){
							stringBuilder.append(words[i]);
							stringBuilder.append(", ");
						}
						fileOut.write(stringBuilder.toString());
						break;
					}
					case "/muse/elements/beta_absolute":{
						StringBuilder stringBuilder = new StringBuilder();
						for(int i = 2; i < words.length; i++){
							stringBuilder.append(words[i]);
							stringBuilder.append(", ");
						}
						fileOut.write(stringBuilder.toString());
						break;
					}
					case "/muse/elements/delta_absolute":{
						StringBuilder stringBuilder = new StringBuilder();
						for(int i = 2; i < words.length; i++){
							stringBuilder.append(words[i]);
							stringBuilder.append(", ");
						}
						fileOut.write(stringBuilder.toString());
						break;
					}
					case "/muse/elements/gamma_absolute":{
						StringBuilder stringBuilder = new StringBuilder();
						for(int i = 2; i < words.length; i++){
							stringBuilder.append(words[i]);
							stringBuilder.append(", ");
						}
						fileOut.write(stringBuilder.toString());
						break;
					}
					case "/muse/elements/theta_absolute":{
						StringBuilder stringBuilder = new StringBuilder();
						for(int i = 2; i < words.length; i++){
							stringBuilder.append(words[i]);
							stringBuilder.append(", ");
						}
						fileOut.write(stringBuilder.toString() + "\n");
						break;
					}	
				}
			}
			fileIn.close();
			fileOut.close();
		}
	}
	
	public static void createArff(String ...filenames) throws IOException{
		//prepare files
		File features = new File("dataset.arff");
		
		List<File> dataIn = new ArrayList<>();
		dataIn.add(new File(filenames[0]));
		dataIn.add(new File(filenames[1]));
		
		List<FileReader> reader = new ArrayList<>();
		reader.add(new FileReader(dataIn.get(0)));
		reader.add(new FileReader(dataIn.get(1)));
		
		FileWriter writer = new FileWriter(features);
		
		List<BufferedReader> fileIn = new ArrayList<>();
		fileIn.add(new BufferedReader(reader.get(0)));
		fileIn.add(new BufferedReader(reader.get(1)));
		
		BufferedWriter fileOut = new BufferedWriter(writer);
		
		//set up header
		String[] attributes = {"alpha_Ch1", "alpha_Ch2", "alpha_Ch2", "alpha_Ch2",
		                       "beta_Ch1", "beta_Ch2", "beta_Ch3", "beta_Ch4",
		                       "delta_Ch1", "delta_Ch2", "delta_Ch3", "delta_Ch4",
		                       "gamma_Ch1", "gamma_Ch2", "gamma_Ch3", "gamma_Ch3",
		                       "theta_Ch1", "theta_Ch2", "theta_Ch3", "theta_Ch4"};
		String[] y_val = {"close", "open"};
		
		fileOut.write("@RELATION power-spectrum\n\n");
		for(String attribute : attributes){
			fileOut.write("@ATTRIBUTE " + attribute + " NUMERIC\n");
		}
		fileOut.write("@ATTRIBUTE class {close, open}\n\n");
		fileOut.write("@DATA\n\n");
		
		String line;
		for(int i = 0; i < 2; i++){
			while ((line = fileIn.get(i).readLine()) != null){
				line.replace(" ", "");
				fileOut.write(line + y_val[i] + "\n");
			}
			fileIn.get(i).close();
		}
		fileOut.close();
	}

}
