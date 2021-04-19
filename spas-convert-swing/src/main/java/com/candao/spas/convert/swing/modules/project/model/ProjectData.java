package com.candao.spas.convert.swing.modules.project.model;

import com.candao.spas.convert.swing.base.event.EventDispatcher;
import com.candao.spas.convert.swing.modules.app.model.Setting;

/**
 * 项目模块数据
 *
 */
public class ProjectData extends EventDispatcher {

	public static final ProjectData ins = new ProjectData();

	public Project curProject;

	public void openProject(String path) {
		if (curProject != null) {
			throw new IllegalStateException("curProject != null");
		}

		Setting.ins.setCurProject(path);

		curProject = new Project();
		curProject.open(path);

		dispatch(ProjectEvent.afterProjectOpen);
	}

	public void closeProject() {
		if (curProject != null) {
			dispatch(ProjectEvent.beforeProjectClose);

			curProject.close();
			curProject = null;

			Setting.ins.clearCurProject();
		}
	}

}
