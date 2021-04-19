package com.candao.spas.convert.swing.modules.app.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.candao.spas.convert.common.utils.FileUtil;
import com.candao.spas.convert.swing.base.constants.Errors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 程序设置
 *
 */
public class Setting {

	private static String settingDir = System.getProperty("user.home") + "/.candao-jsonte/";
	private static File settingFile = new File(settingDir + "setting.json");
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static Setting ins;

	public static void load() throws IOException {
		if (!settingFile.isFile()) {
			ins = new Setting();
			ins.useDefault();
		} else {
			String json = FileUtil.readTextFile(settingFile);
			ins = gson.fromJson(json, Setting.class);
		}
	}

	// *************************************************************************

	public String curProject;
	public List<String> recentProjects;

	public void useDefault() {
		recentProjects = new ArrayList<String>();
	}

	public void save() {
		try {
			String json = gson.toJson(this);
			FileUtil.writeTextFile(settingFile, json);
		} catch (IOException e) {
			Errors.dispatch("保存配置异常", e);
		}
	}

	public void setCurProject(String path) {
		boolean changed = false;

		if (!path.equals(curProject)) {
			curProject = path;
			changed = true;
		}

		boolean recentProjectsChanged = false;
		if (recentProjects.isEmpty() || !recentProjects.get(0).equals(path)) {
			recentProjects.remove(path);
			recentProjects.add(0, path);
			if (recentProjects.size() > 20) {
				recentProjects.remove(recentProjects.size() - 1);
			}
			changed = true;
			recentProjectsChanged = true;
		}

		if (changed) {
			save();
		}
		if (recentProjectsChanged) {
			AppData.ins.dispatch(SettingEvent.recentProjectsChange);
		}
	}

	public void clearCurProject() {
		curProject = null;
		save();
	}

}