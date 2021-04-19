package com.candao.spas.convert.swing.modules.tsf.view;

import com.candao.spas.convert.common.utils.UiUtil;
import com.candao.spas.convert.swing.modules.tsf.model.*;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * 协议节点明细编辑视图
 *
 */
public class TsfNodeEditView extends JPanel {

	private static final long serialVersionUID = 7731455866032590552L;

	public static TsfNodeEditView ins;

	public JComboBox<TsfNodeType> typeComb;
	public JTextField nameTxt;
	public JLabel condLbl;
	public JCheckBox condMultiChk;
	public JTextField condTxt;
	public JLabel cvtLbl;
	public JComboBox<TsfCvtType> cvtTypeComb;
	public JTextField cvtArgTxt;
	public JLabel dstLbl;
	public JTextField dstTxt;

	public TsfNodeView nodeView;

	public TsfNodeEditView() {
		ins = this;

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		typeComb = new JComboBox<TsfNodeType>(TsfNodeType.values());
		typeComb.setMaximumSize(new Dimension(100, UiUtil.lineHeight));
		add(typeComb);

		add(nameTxt = UiUtil.createTextField(100));

		add(condLbl = new JLabel("条件："));
		add(condMultiChk = new JCheckBox("多选"));
		add(condTxt = UiUtil.createTextField(100));

		add(cvtLbl = new JLabel("转换："));
		List<TsfCvtType> cvtTypes = new ArrayList<TsfCvtType>();
		cvtTypes.add(null);
		cvtTypes.addAll(Arrays.asList(TsfCvtType.values()));
		cvtTypeComb = new JComboBox<TsfCvtType>(cvtTypes.toArray(new TsfCvtType[0]));
		cvtTypeComb.setMaximumSize(new Dimension(100, UiUtil.lineHeight));
		add(cvtTypeComb);

		cvtArgTxt = UiUtil.createTextField(200);
		add(cvtArgTxt);

		add(dstLbl = new JLabel("输出："));
		add(dstTxt = UiUtil.createTextField(100));
	}

	public void beginEdit(TsfNodeView nodeView) {
		endEdit();

		this.nodeView = nodeView;

		if (nodeView != null) {
			TsfNode tn = nodeView.node;
			boolean isInput = nodeView.isInput;

			typeComb.setSelectedItem(tn.type);

			nameTxt.setText(tn.srcName);

			condLbl.setVisible(isInput);
			condMultiChk.setVisible(isInput);
			condMultiChk.setSelected(tn.cond == null ? true : tn.cond.isMulti);
			condTxt.setVisible(isInput);
			condTxt.setText(tn.cond == null ? "" : tn.cond.toEditString());

			cvtLbl.setVisible(isInput);
			cvtTypeComb.setVisible(isInput);
			cvtArgTxt.setVisible(isInput);
			if (tn.cvt != null) {
				cvtTypeComb.setSelectedItem(tn.cvt.type);
				cvtArgTxt.setText(tn.cvt.arg);
			} else {
				cvtTypeComb.setSelectedItem(null);
				cvtArgTxt.setText("");
			}

			dstLbl.setVisible(isInput);
			dstTxt.setVisible(isInput);
			refreshDstPath();

			nodeView.onEditBegin(this);
		}
	}

	public void endEdit() {
		if (nodeView != null) {
			TsfNode tn = new TsfNode();

			tn.type = (TsfNodeType) typeComb.getSelectedItem();
			tn.srcName = nameTxt.getText();

			String cond = condTxt.getText();
			if (!cond.isEmpty()) {
				tn.cond = new TsfCond(condMultiChk.isSelected(), cond);
			}

			TsfCvtType cvtType = (TsfCvtType) cvtTypeComb.getSelectedItem();
			if (cvtType != null) {
				String cvtArg = cvtArgTxt.getText();
				tn.cvt = new TsfCvt(cvtType, cvtArg);
			}

			String dst = dstTxt.getText();
			if (!dst.isEmpty()) {
				tn.dstPath = dst;
			}

			TsfData.ins.updateNode(nodeView.node, tn);

			nodeView.onEditEnd(this);
			nodeView = null;
		}
	}

	public void refreshDstPath() {
		if (nodeView != null) {
			dstTxt.setText(nodeView.node.dstPath);
		}
	}

}