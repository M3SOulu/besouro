package besouro.measure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICoverageVisitor;
import org.jacoco.core.data.ExecutionDataStore;
import besouro.persistence.jacocofilestorage;

import besouro.plugin.ProgrammingSession;

public class CoverageMeter implements ICoverageVisitor {
	
	File jacocooFile = ProgrammingSession.jacocoFile;

	private Analyzer analyzer;
	final CoverageBuilder coverageBuilder = new CoverageBuilder();

	public CoverageMeter() {

		analyzer = new Analyzer(new ExecutionDataStore(), this);
	}

	public void execute(String file) throws IOException {
		// InputStream in = new FileInputStream(new File(file));
		// analyzer.analyzeClass(in,
		// "/Users/dfucci/Desktop/runtime-EclipseApplication/My project/");
		int result = analyzer.analyzeAll(new File(file));

		System.out.println(result);
	}

	public void visitCoverage(final IClassCoverage coverage) {

		
		try {
			FileWriter writer = new FileWriter(jacocooFile,true);
			String name = coverage.getName().toString();
			writer.append(name + "  ");
			
			int j = Integer.valueOf(coverage.getInstructionCounter().getTotalCount());
			
			writer.write(Integer.valueOf(j) + "  ");
			int t = Integer.valueOf(coverage.getBranchCounter().getTotalCount());
			
			writer.write(Integer.valueOf(t) + "  ");
			int l = coverage.getLineCounter().getTotalCount();
			
			writer.write(Integer.valueOf(l) + "  ");
			int m = coverage.getMethodCounter().getTotalCount();
			
			writer.write(Integer.valueOf(m) + "  ");
			int co = coverage.getComplexityCounter().getTotalCount();
			
			writer.write(Integer.valueOf(co) + "  ");
			writer.write("\n");
			int value1, value2, count1=0,count2=0,count3=0;
					float count4;
			
			for (int i = coverage.getFirstLine(); i <= coverage.getLastLine(); i++) {
				value1= Integer.valueOf(i); count1=count1+1;
				//writer.write(value1 + "   ");
				value2=coverage.getLine(i).getStatus();
				//writer.write(value2+ "   ");
				if(value2==0)
				{
					
				count2=count2+1;	
				}
				else
				{
					
					count3=count3+1;		
				}
				
				
			}
			System.out.println(count1);
			System.out.println(count2);
			System.out.println(count3);
			count4=(float)((count3*100)/count1);
			System.out.println(count4);
			writer.write("Percentage of Code coverage : ");
			writer.write(count4 + "   ");
			writer.write("\n");
			writer.flush();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	

}