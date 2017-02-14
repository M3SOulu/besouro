package besouro.persistence;

import java.io.BufferedReader;
import besouro.measure.CoverageMeter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import besouro.model.Jacoco;
import besouro.stream.jacocoOutputStream;

public class jacocofilestorage implements jacocoOutputStream {

	private File file;
	private FileWriter writer;

	public jacocofilestorage(File f) {
		try {
			
			this.file = f;
			
			if (!file.exists()) {
				file.createNewFile();
			}
			
			writer = new FileWriter(file,true);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addAction(Jacoco jacoco) {
		try {
			
			writer.write(jacoco.toString());
			writer.write("\n");
			writer.flush();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	
	

}
