package com.candao.spas.convert.common.utils;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;

public class JTreeUtil {

	public static void expandTree(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll(tree, new TreePath(root), expand);
	}

	private static void expandAll(JTree tree, TreePath path, boolean expand) {
		TreeNode node = (TreeNode) path.getLastPathComponent();

		if (node.getChildCount() >= 0) {
			Enumeration<?> enumeration = node.children();
			while (enumeration.hasMoreElements()) {
				TreeNode n = (TreeNode) enumeration.nextElement();
				TreePath p = path.pathByAddingChild(n);

				expandAll(tree, p, expand);
			}
		}

		if (expand) {
			tree.expandPath(path);
		} else {
			tree.collapsePath(path);
		}
	}

}
