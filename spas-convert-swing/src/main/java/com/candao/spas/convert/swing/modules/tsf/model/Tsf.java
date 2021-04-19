package com.candao.spas.convert.swing.modules.tsf.model;

import com.candao.spas.convert.swing.modules.project.model.ProjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 一条协议（一个json文件转换规则）
 *
 */
public class Tsf {

	public static final String inFileName = "in.json";
	public static final String outFileName = "out.json";
	public static final String inSpecFileName = "spec-in.json";
	public static final String outSpecFileName = "spec-out.json";

	public static Set<String> cfgFileNames = new HashSet<String>(Arrays.asList(new String[] {
			inFileName,
			outFileName,
			inSpecFileName,
			outSpecFileName,
	}));

	public static boolean isCfgFile(File file) {
		return cfgFileNames.contains(file.getName());
	}

	// *************************************************************************

	public ProjectNode projectNode;

	public File inFile;
	public File outFile;
	public File inSpecFile;
	public File outSpecFile;

	public void addCfgFile(File file) {
		String fileName = file.getName();
		switch (fileName) {
		case inFileName:
			inFile = file;
			break;
		case outFileName:
			outFile = file;
			break;
		case inSpecFileName:
			inSpecFile = file;
			break;
		case outSpecFileName:
			outSpecFile = file;
			break;
		default:
			break;
		}
	}

	public void beginEdit() throws IOException {
		if (inSpecFile == null) {
			inSpecFile = createFile(inSpecFileName);
		}
		if (outSpecFile == null) {
			outSpecFile = createFile(outSpecFileName);
		}
	}

	private File createFile(String fileName) throws IOException {
		File file = new File(projectNode.path.getPath() + "/" + fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

}
