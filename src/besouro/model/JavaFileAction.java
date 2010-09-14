package besouro.model;

import java.util.Date;

import org.eclipse.core.resources.IResource;

public class JavaFileAction extends ResourceAction {

	private int methodsCount;
	private int statementsCount;
	private int testAssertionsCount;
	private int testMethodsCount;
	private int fileSize = 0;
	
	private int methodIncrease = 0;
	private int statementIncrease = 0;
	private int testMethodIncrease = 0;
	private int testAssertionIncrease = 0;
	private boolean isTestEdit;

	private int fileSizeIncrease;

	
	private JavaFileAction previousAction;

	public JavaFileAction(Date clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}


	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getFileSize() {
		return this.fileSize;
	}

	public int getMethodsCount() {
		return methodsCount;
	}

	public void setMethodsCount(int methodsCount) {
		this.methodsCount = methodsCount;
	}

	public int getStatementsCount() {
		return statementsCount;
	}

	public void setStatementsCount(int statementsCount) {
		this.statementsCount = statementsCount;
	}

	public int getTestAssertionsCount() {
		return testAssertionsCount;
	}

	public void setTestAssertionsCount(int testAssertionsCount) {
		this.testAssertionsCount = testAssertionsCount;
	}

	public int getTestMethodsCount() {
		return testMethodsCount;
	}

	public void setTestMethodsCount(int testMethodsCount) {
		this.testMethodsCount = testMethodsCount;
	}

	
	
	public void setPreviousAction(JavaFileAction previousAction) {
		this.previousAction = previousAction;
	}

	public JavaFileAction getPreviousAction() {
		return this.previousAction;
	}
	
	public boolean isSubstantial() {
		if (isTestEdit) {
			return (this.getMethodIncrease() != 0 || this.getStatementIncrease() != 0
					|| this.getTestMethodIncrease() != 0 || this.getTestAssertionIncrease() != 0);
		} else {
			return (this.getFileSizeIncrease() != 0 || this.getMethodIncrease() != 0 || this.getStatementIncrease() != 0);
		}
	}

	public boolean isTestEdit() {
		return isTestEdit;
	}

	public void setIsTestEdit(boolean isTestEdit) {
		this.isTestEdit = isTestEdit;
	}

	public int getFileSizeIncrease() {
		if (getPreviousAction() != null) {
			return this.getFileSize() - getPreviousAction().getFileSize();
		} else {
			return fileSizeIncrease;

		}
	}

	// usefull for test
	public void setFileSizeIncrease(int size) {
		fileSizeIncrease = size;
	}
	
	// usefull for test
	public void setTestMethodIncrease(int value) {
		this.testMethodIncrease = value;
	}

	// usefull for test
	public void setTestAssertionIncrease(int value) {
		this.testAssertionIncrease = value;
	}
	
	// usefull for test
	public void setMethodIncrease(int methodIncrease) {
		this.methodIncrease = methodIncrease;
	}

	// usefull for test
	public void setStatementIncrease(int statementIncrease) {
		this.statementIncrease = statementIncrease;
	}
	
	public int getTestMethodIncrease() {
		if (getPreviousAction() != null) {
			return this.getTestMethodsCount() - getPreviousAction().getTestMethodsCount();
		}
		return this.testMethodIncrease;
	}

	public int getTestAssertionIncrease() {
		if (getPreviousAction() != null) {
			return this.getTestAssertionsCount() - getPreviousAction().getTestAssertionsCount();
		}
		return this.testAssertionIncrease;
	}

	public int getMethodIncrease() {
		if (getPreviousAction() != null) {
			return this.getMethodsCount() - getPreviousAction().getMethodsCount();
		}
		return this.methodIncrease;
	}

	public int getStatementIncrease() {
		if (getPreviousAction() != null) {
			return this.getStatementsCount() - getPreviousAction().getStatementsCount();
		}
		return this.statementIncrease;
	}
	

}
