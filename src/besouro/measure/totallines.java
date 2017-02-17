package besouro.measure;

import java.io.*;
import besouro.plugin.ProgrammingSession;
import besouro.plugin.ProgrammingSession;

public class totallines {

	int codeLines = 0, commentLines = 0, totalLines = 0;
	boolean commentStarted = false;

	public static void Calculatenumberfile(String file) throws IOException {

		File folder = new File(file);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				String sourcefile = listOfFiles[i].toString();
				System.out.println(sourcefile);
				File folder2 = new File(sourcefile);
				File[] listofFiles2 = folder2.listFiles();
				for (int j = 0; j < listofFiles2.length; j++) {
					if (!(listofFiles2[j].isDirectory())) {
						String sourcefile2 = listofFiles2[j].toString();

						totallines obj = new totallines();
						obj.analyzeFile(sourcefile2);
					}
				}
			}
		}
	}

	public void analyzeFile(String File) throws IOException {
		File jacocooFile = ProgrammingSession.jacocoFile;
		FileWriter writer = new FileWriter(jacocooFile, true);
		BufferedReader br = null;
		String sCurrentLine = null;
		File fname = null;
		String id;
		boolean sameLine = false;
		try {
			br = new BufferedReader(new FileReader(File));

			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.trim();
				sameLine = false;
				// System.out.println(sCurrentLine);
				while (sCurrentLine != null && sCurrentLine.length() > 0) {
					// System.out.println("for line: " + sCurrentLine + " and
					// sameLine:" + sameLine);
					sCurrentLine = analyzeLine(sCurrentLine, sameLine);
					sameLine = true;
				}
			}
			fname = new File(File);
			id = fname.getName();
			writer.write("Program name: " + id + "  ");
			writer.write("\n");
			totalLines = codeLines + commentLines;
			System.out.println("Total number of Lines are : " + totalLines + "  ");
			writer.write("Total number of Lines are : " + totalLines + "  ");
			System.out.println("Number of comments: " + commentLines);
			writer.write("Number of comments: " + commentLines + "  ");
			System.out.println("Number of code lines: " + codeLines);
			writer.write("Number of code lines: " + codeLines + "  ");

			writer.write("\n");
			writer.write("----------------------------------------------------------");
			
			writer.write("\n");
			writer.flush();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				// close bufferReader
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	
	
	public String analyzeLine(String sCurrentLine, boolean sameLine) {

		if (commentStarted && sCurrentLine.contains("*/")) {
			if (!sameLine)
				commentLines++;
			commentStarted = false;
			if (!sCurrentLine.endsWith("*/"))
				sCurrentLine = sCurrentLine.substring(sCurrentLine.indexOf("*/") + 2).trim();
			else
				sCurrentLine = null;
		} else if (sCurrentLine.startsWith("//")) {
			if (!sameLine)
				commentLines++;
			sCurrentLine = null;
		} else if (sCurrentLine.contains("/*")) {
			commentStarted = true;
			if (!sCurrentLine.startsWith("/*")) {
				if (!sameLine)
					codeLines++;
				sCurrentLine = sCurrentLine.substring(sCurrentLine.indexOf("/*")).trim();
			} else {
				if (!sameLine)
					commentLines++;
				if (sCurrentLine.contains("*/")) {
					commentStarted = false;
					if (!sCurrentLine.endsWith("*/"))
						sCurrentLine = sCurrentLine.substring(sCurrentLine.indexOf("*/") + 2).trim();
					else
						sCurrentLine = null;
				} else
					sCurrentLine = null;
			}
		} else if (commentStarted) {
			if (!sameLine)
				commentLines++;
			sCurrentLine = null;
		} else {
			commentStarted = false;
			if (!sameLine)
				codeLines++;
			if (sCurrentLine.contains(";")) {
				if (!sCurrentLine.endsWith(";")) {
					sCurrentLine = sCurrentLine.substring(sCurrentLine.indexOf(";") + 1).trim();
				} else
					sCurrentLine = null;
			} else
				sCurrentLine = null;
		}

		return sCurrentLine;
	}
}