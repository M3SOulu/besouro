package besouro.listeners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import besouro.measure.CoverageMeter;
import besouro.measure.totallines;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestSuiteElement;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import besouro.measure.CoverageMeter;
import besouro.model.UnitTestAction;
import besouro.model.UnitTestCaseAction;
import besouro.model.UnitTestSessionAction;
import besouro.stream.ActionOutputStream;

public class JUnitListener extends TestRunListener {

	private ActionOutputStream stream;

	public JUnitListener(ActionOutputStream stream) {
		this.stream = stream;
	}

	/*
	 * private String getbinPath() { IWorkbenchWindow win =
	 * PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	 * 
	 * IWorkbenchPage page = win.getActivePage(); if (page != null) {
	 * IEditorPart editor = page.getActiveEditor(); if (editor != null) {
	 * IEditorInput input = editor.getEditorInput(); if (input instanceof
	 * IFileEditorInput) { String fullFileName = ((IFileEditorInput)
	 * input).getFile().getLocation().toOSString(); String binPath =
	 * fullFileName.substring(0, fullFileName.lastIndexOf("src")) + "bin/";
	 * return binPath; } } } return null; }
	 */
	@Override
	public void sessionFinished(ITestRunSession session) {

		boolean isSuccessfull = true;
		for (UnitTestAction action : getTestFileActions(session, session.getLaunchedProject())) {
			stream.addAction(action);
			isSuccessfull &= action.isSuccessful();

		}

		String workspace = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		String project = session.getLaunchedProject().getProject().getName().toString();
		String pathToBin = workspace + "/" + project + "/bin/";
		String pathToSrc = workspace + "/" + project + "/src/";
		if (isSuccessfull) {
			File folder = new File(pathToBin);
			File[] listOfFiles = folder.listFiles();
			totallines cnum = new totallines();
			try {
				cnum.Calculatenumberfile(pathToSrc);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isDirectory()) {
					String sourcefile = listOfFiles[i].toString();
					if (!(sourcefile.contains("Test"))) {
						String var = sourcefile;
						System.out.print(var);
						CoverageMeter coverageMeter = new CoverageMeter();
						try {
							coverageMeter.execute(var);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}

			// String projectlocation = ResourcesPlugin.getWorkspace().

		}

		IResource res = findTestResource(session.getLaunchedProject(), session.getTestRunName());

		String name = res != null ? res.getName() : session.getTestRunName();

		// registers the session action. It brakes the episode, but doesnt count
		// on the classification
		UnitTestSessionAction action = new UnitTestSessionAction(new Date(), name);
		action.setSuccessValue(isSuccessfull);
		stream.addAction(action);

	}

	private Collection<UnitTestCaseAction> getTestFileActions(ITestElement session, IJavaProject project) {

		List<UnitTestCaseAction> list = new ArrayList<UnitTestCaseAction>();

		if (session instanceof ITestSuiteElement) {

			ITestSuiteElement testCase = (ITestSuiteElement) session;

			ArrayList<String> testMethods = new ArrayList<String>();
			for (ITestElement singleTestMethod : testCase.getChildren()) {
				if (singleTestMethod instanceof ITestCaseElement) {
					ITestCaseElement testMethod = (ITestCaseElement) singleTestMethod;
					String testMethodName = this.testMethodToString(testMethod);
					testMethods.add(testMethodName);
				}
			}

			IResource res = findTestResource(project, testCase.getSuiteTypeName());

			UnitTestCaseAction action = new UnitTestCaseAction(new Date(), res.getName());
			action.setSuccessValue(testCase.getTestResult(true).equals(Result.OK));

			action.setTestMethods(testMethods);
			list.add(action);

		} else if (session instanceof ITestCaseElement) {

			ITestCaseElement testCase = (ITestCaseElement) session;

			IResource res = findTestResource(project, testCase.getTestClassName());

			// will reach this case only when user executes a single test method

			UnitTestCaseAction action = new UnitTestCaseAction(new Date(), res.getName());
			action.setSuccessValue(testCase.getTestResult(true).equals(Result.OK));
			list.add(action);

		} else if (session instanceof ITestElementContainer) {
			ITestElementContainer container = (ITestElementContainer) session;
			for (ITestElement child : container.getChildren()) {
				list.addAll(getTestFileActions(child, project));
			}
		}

		return list;

	}

	private String testMethodToString(ITestCaseElement testMethod) {
		String testMethodClassName = testMethod.getTestClassName();
		String testMethodName = testMethod.getTestMethodName();
		String testMethodResult = testMethod.getTestResult(true).toString();
		String failure = (testMethod.getFailureTrace() == null) ? "" : testMethod.getFailureTrace().toString();
		String testMethodStr = testMethodClassName + "." + testMethodName + " " + testMethodResult + failure;
		return testMethodStr;
	}

	private IResource findTestResource(IJavaProject project, String className) {
		IPath path = new Path(className.replaceAll("\\.", "/") + ".java");
		try {

			IJavaElement element = project.findElement(path);
			if (element != null)
				return element.getResource();
			else
				return null;

		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
	}

	// private void print(ITestElement session) {
	//
	//
	// if (session instanceof ITestSuiteElement) {
	//
	// ITestSuiteElement suite = (ITestSuiteElement) session;
	//
	// } else if (session instanceof ITestElementContainer) {
	//
	// ITestElementContainer suite = (ITestElementContainer) session;
	//
	// for (ITestElement test : suite.getChildren()) {
	// print(test);
	// }
	//
	// }
	//
	// }

}
