package com.candao.spas.convert.swing.modules.project.control;

import com.candao.spas.convert.common.utils.PathUtil;
import com.candao.spas.convert.swing.base.constants.Errors;
import com.candao.spas.convert.swing.modules.app.view.AppFrame;
import com.candao.spas.convert.swing.modules.project.model.Project;
import com.candao.spas.convert.swing.modules.project.model.ProjectData;
import com.candao.spas.convert.swing.modules.project.model.ProjectNode;
import com.candao.spas.convert.swing.modules.tsf.control.TsfControl;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 项目模块控制器
 *
 */
public class ProjectControl {

	public static final ProjectControl ins = new ProjectControl();

	private ProjectData data = ProjectData.ins;

	public boolean checkModifiedAndPrompt() {
		return TsfControl.ins.checkModifiedAndPrompt();
	}

	/**
	 * 选择要打开的项目
	 */
	public void chooseOpenProject() {
		if (!checkModifiedAndPrompt()) {
			return;
		}

		File curDir;
		File selectedFile = null;
		Project curProject = data.curProject;
		if (curProject != null) {
			selectedFile = curProject.rootNode.path;
			curDir = selectedFile.getParentFile();
		} else {
			curDir = new File(System.getProperty("user.dir"));
		}

		JFileChooser fc = new JFileChooser(curDir);
		fc.setDialogTitle("打开项目");
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (selectedFile != null) {
			fc.setSelectedFile(selectedFile);
		}
		fc.showDialog(AppFrame.ins, "选择");

		File file = fc.getSelectedFile();
		if (file != null) {
			openProject(file);
		}
	}

	/**
	 * 打开项目
	 */
	public void openProject(File dir) {
		if (!dir.isDirectory()) {
			return;
		}
		try {
			String path = PathUtil.getCanonicalPath(dir);

			Project curProject = data.curProject;
			if (curProject != null) {
				if (curProject.path.equals(path)) {
					return;
				}
				if (!closeProject()) {
					return;
				}
			}

			data.openProject(path);
		} catch (Exception e) {
			Errors.dispatch("打开项目异常", e);
		}
	}

	/**
	 * 关闭项目
	 */
	public boolean closeProject() {
		if (!checkModifiedAndPrompt()) {
			return false;
		}
		data.closeProject();
		return true;
	}

	/**
	 * 打开所在文件夹
	 */
	public void exploreDir(ProjectNode node) {
		try {
			Desktop.getDesktop().open(node.path);
		} catch (IOException e) {
			Errors.dispatch("打开文件夹异常", e);
		}
	}

}
