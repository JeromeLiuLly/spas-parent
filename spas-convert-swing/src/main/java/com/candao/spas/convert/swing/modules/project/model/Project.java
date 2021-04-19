package com.candao.spas.convert.swing.modules.project.model;

import java.io.File;

public class Project {

	public String path;
	public ProjectNode rootNode;

	public void open(String path) {
		this.path = path;

		rootNode = new ProjectNode();
		rootNode.parseRoot(new File(path));
	}

	public void close() {
	}

}