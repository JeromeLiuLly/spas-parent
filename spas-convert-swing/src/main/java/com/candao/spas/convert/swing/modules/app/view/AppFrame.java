package com.candao.spas.convert.swing.modules.app.view;

import com.candao.spas.convert.swing.modules.app.control.AppControl;
import com.candao.spas.convert.swing.modules.project.view.ProjectView;
import com.candao.spas.convert.swing.modules.tsf.model.Tsf;
import com.candao.spas.convert.swing.modules.tsf.model.TsfData;
import com.candao.spas.convert.swing.modules.tsf.model.TsfEvent;
import com.candao.spas.convert.swing.modules.tsf.view.TsfEditView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

/**
 * 主窗口
 *
 */
public class AppFrame extends JFrame {

	private static final long serialVersionUID = -9050261640140146527L;

	public static AppFrame ins;

	private String defaultTitle = "json转换编辑器";

	public AppFrame() {
		ins = this;

		setTitle(defaultTitle);
		setSize(1200, 800);
		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				AppControl.ins.closeWindow();
			}
		});

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);

		JScrollPane leftScrollPane = new JScrollPane();
		leftScrollPane.setPreferredSize(new Dimension(200, 0));
		splitPane.setLeftComponent(leftScrollPane);
		ProjectView protoView = new ProjectView();
		leftScrollPane.setViewportView(protoView);

		JScrollPane rightScrollPane = new JScrollPane();
		splitPane.setRightComponent(rightScrollPane);
		TsfEditView tsfEditView = new TsfEditView();
		rightScrollPane.setViewportView(tsfEditView);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		Menus.ins.create(menuBar);

		TsfData.ins.on(TsfEvent.modifiedChange, this::refreshTitle);
		TsfData.ins.on(TsfEvent.editBegin, this::refreshTitle);
		TsfData.ins.on(TsfEvent.editEnd, this::refreshTitle);
	}

	private void refreshTitle(Object[] args) {
		String title = defaultTitle;

		Tsf tsf = TsfData.ins.tsf;
		if (tsf != null) {
			title += " - " + tsf.projectNode.path.getPath();
			if (TsfData.ins.modified) {
				title += "*";
			}
		}

		setTitle(title);
	}

}
