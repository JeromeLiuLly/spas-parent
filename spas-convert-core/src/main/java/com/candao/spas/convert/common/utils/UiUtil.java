package com.candao.spas.convert.common.utils;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

public class UiUtil {

	public static int lineHeight = 22;

	public static JButton createIconBtn(String path) {
		return createIconBtn(createIcon(path));
	}

	public static JButton createIconBtn(ImageIcon icon) {
		JButton btn = new JButton(icon);
		btn.setBorder(null);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setContentAreaFilled(false);
		return btn;
	}

	public static ImageIcon createIcon(String resourcePath) {
		return new ImageIcon(UiUtil.class.getResource(resourcePath));
	}

	public static JLabel createLabelWithBgColor(Color color) {
		JLabel txt = new JLabel("");
		txt.setBackground(color);
		txt.setOpaque(true);
		return txt;
	}

	public static JTextField createTextField(int width) {
		JTextField txt = new JTextField();
		txt.setPreferredSize(new Dimension(width, lineHeight));
		txt.setMaximumSize(txt.getPreferredSize());
		return txt;
	}

	public static JMenuItem createMenuItem(String text, ActionListener actionListener) {
		JMenuItem item = new JMenuItem(text);
		item.addActionListener(actionListener);
		return item;
	}

	public static int boxHLay(Component[] components, int sep) {
		int x = 0;
		for (Component component : components) {
			if (x != 0) {
				x += sep;
			}
			component.setLocation(x, component.getY());
			x += component.getPreferredSize().width;
		}
		return x;
	}

	public static interface MouseClickHandler {
		void onMouseClick(MouseEvent e);
	}

	public static void onMouseClick(Component comp, MouseClickHandler handler) {
		comp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handler.onMouseClick(e);
			}
		});
	}

	public static boolean isClick(MouseEvent e) {
		return e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1;
	}

	public static boolean isDoubleClick(MouseEvent e) {
		return e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2;
	}

	public static boolean isRightClick(MouseEvent e) {
		return e.getButton() == MouseEvent.BUTTON3;
	}

	public static boolean isDoubleOrRightClick(MouseEvent e) {
		return isDoubleClick(e) || isRightClick(e);
	}

	public static void repaint(Component component) {
		Component comp = component;
		while (comp != null) {
			comp.validate();
			if (comp.getParent() == null) {
				comp.repaint();
			}
			comp = comp.getParent();
		}
	}

	public static void setDefaultFont(Font font) {
		Enumeration<?> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, font);
			}
		}
	}

	public static Rectangle getBounds(Component comp, Component container) {
		Rectangle rect = comp.getBounds();
		while (comp.getParent() != container && comp.getParent() != null) {
			comp = comp.getParent();
			rect.x += comp.getX();
			rect.y += comp.getY();
		}
		if (comp.getParent() == container) {
			return rect;
		}
		return null;
	}

}
