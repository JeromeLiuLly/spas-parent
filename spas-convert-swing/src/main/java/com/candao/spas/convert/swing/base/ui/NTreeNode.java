package com.candao.spas.convert.swing.base.ui;

import com.candao.spas.convert.common.utils.UiUtil;
import com.candao.spas.convert.sdk.condition.ArgParser;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NTreeNode<TView extends NTreeNode<TView>> extends JPanel {

	private static final long serialVersionUID = 1571120386826965796L;

	protected JButton expandBtn;
	private ImageIcon[] expandBtnIcons;
	protected JPanel rightContainer;
	public JPanel infoContainer;
	public JPanel infoView;
	protected JPanel childContainer;

	protected JLabel typeIcon;
	protected JLabel nameTxt;

	protected Color normalBg;

	public TView parent;
	public List<TView> children;

	protected boolean expanded = true;
	protected boolean allowChild = false;
	protected boolean childrenVisible = false;

	public NTreeNode(ImageIcon[] expandBtnIcons) {
		this.expandBtnIcons = expandBtnIcons;

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		expandBtn = UiUtil.createIconBtn(expandBtnIcons[0]);
		expandBtn.setAlignmentY(TOP_ALIGNMENT);
		expandBtn.addActionListener(this::onExpand);
		setExpandBtnIcon();
		add(expandBtn);

		rightContainer = new JPanel();
		rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
		rightContainer.setAlignmentY(TOP_ALIGNMENT);
		add(rightContainer);

		infoContainer = new JPanel();
		infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
		infoContainer.setAlignmentX(LEFT_ALIGNMENT);
		rightContainer.add(infoContainer);

		infoView = new JPanel();
		infoView.setLayout(new BoxLayout(infoView, BoxLayout.X_AXIS));
		infoView.setAlignmentX(LEFT_ALIGNMENT);
		infoContainer.add(infoView);

		normalBg = infoView.getBackground();

		childContainer = new JPanel();
		childContainer.setAlignmentX(LEFT_ALIGNMENT);
		childContainer.setLayout(new BoxLayout(childContainer, BoxLayout.Y_AXIS));

		typeIcon = new JLabel("");
		infoView.add(typeIcon);

		nameTxt = new JLabel("");
		infoView.add(nameTxt);
	}

	private void setExpandBtnIcon() {
		if (allowChild) {
			expandBtn.setEnabled(true);
			expandBtn.setIcon(expanded ? expandBtnIcons[2] : expandBtnIcons[1]);
		} else {
			expandBtn.setEnabled(false);
			expandBtn.setIcon(expandBtnIcons[0]);
		}
	}

	public void setAllowChild(boolean value) {
		if (allowChild != value) {
			allowChild = value;
			setExpandBtnIcon();
			refreshChildrenVisible();
		}
	}

	public void setExpanded(boolean value) {
		if (expanded != value) {
			expanded = value;
			setExpandBtnIcon();
			refreshChildrenVisible();
		}
	}

	private void refreshChildrenVisible() {
		boolean value = allowChild && expanded;
		if (childrenVisible != value) {
			childrenVisible = value;
			if (value) {
				rightContainer.add(childContainer);
			} else {
				rightContainer.remove(childContainer);
			}
			UiUtil.repaint(rightContainer);
		}
	}

	private void onExpand(ActionEvent e) {
		if (allowChild || expanded) {
			setExpanded(!expanded);
		}
	}

	@SuppressWarnings("unchecked")
	public TView getRootView() {
		NTreeNode<?> v = this;
		while (v.parent != null) {
			v = v.parent;
		}
		return (TView) v;
	}

	@SuppressWarnings("unchecked")
	protected void addChild(TView childView, int index) {
		if (children == null) {
			children = new ArrayList<TView>();
		}

		childView.parent = (TView) this;
		children.add(index, childView);

		childView.setAlignmentX(LEFT_ALIGNMENT);
		childContainer.add(childView, index);
	}

	protected boolean removeChild(TView childView) {
		if (childView.parent != this) {
			return false;
		}
		if (children != null && children.remove(childView)) {
			childView.parent = null;
			childContainer.remove(childView);
			return true;
		}
		return false;
	}

	protected void clearChildren() {
		if (children != null) {
			childContainer.removeAll();
			children = null;
		}
	}

	public String name() {
		return nameTxt.getText();
	}

	@SuppressWarnings("unchecked")
	public TView findNodeViewByPath(String path) {
		if (path.isEmpty()) {
			return (TView) this;
		}
		return findNodeViewByPath(new ArgParser(path, "."));
	}

	public TView findNodeViewByPath(ArgParser path) {
		if (children == null) {
			return null;
		}

		String name = path.nextArg();
		for (TView view : children) {
			if (name.equals(view.name())) {
				if (!path.hasMoreArgs()) {
					return view;
				} else {
					return view.findNodeViewByPath(path);
				}
			}
		}
		return null;
	}

}