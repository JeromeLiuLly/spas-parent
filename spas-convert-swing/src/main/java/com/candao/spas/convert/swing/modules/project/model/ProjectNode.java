package com.candao.spas.convert.swing.modules.project.model;

import com.candao.spas.convert.swing.modules.tsf.model.Tsf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目上的一个节点（可以放置子节点或者放置协议）
 *
 */
public class ProjectNode {

	public String name;
	public File path;

	public ProjectNode parent;
	public List<ProjectNode> children;

	public Tsf tsf;

	public void parseRoot(File dir) {
		parent = null;
		parseDir(dir);
	}

	private void parseDir(File dir) {
		name = dir.getName();
		path = dir;

		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory() && tsf == null) {
				if (children == null) {
					children = new ArrayList<ProjectNode>();
				}
				ProjectNode child = new ProjectNode();
				child.parseDir(file);
				child.parent = this;
				children.add(child);

			} else if (file.isFile() && Tsf.isCfgFile(file)) {
				children = null;
				if (tsf == null) {
					tsf = new Tsf();
					tsf.projectNode = this;
				}
				tsf.addCfgFile(file);
			}
		}
	}

	/**
	 * 给DefaultMutableTreeNode用的
	 */
	@Override
	public String toString() {
		return name;
	}

}