package org.nlp4han.coref.centering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.nlp4han.coref.hobbs.AttributeFilter;
import org.nlp4han.coref.hobbs.AttributeGeneratorByDic;
import org.nlp4han.coref.hobbs.EvaluationHobbs;
import org.nlp4han.coref.hobbs.Hobbs;
import org.nlp4han.coref.hobbs.NodeNameFilter;
import org.nlp4han.coref.hobbs.PNFilter;
import org.nlp4han.coref.hobbs.TreeNodeUtil;

import com.lc.nlp4han.constituent.BracketExpUtil;
import com.lc.nlp4han.constituent.TreeNode;

public class TestCenteringBFP
{
	@Test
	public void testCenteringBFP_1()
	{
		String str;
		str = "((IP(NP(PN 我))(VP(PP(P在)(LCP(NP(DNP(NP(NP(NR 太平洋))(NP(NN 酒店)))(DEG 的))(NP(NN 咖啡厅)))(LC 里)))(VP(VV 看见)(AS 了)(NP(NR 庞德))))(PU 。)))";
		TreeNode s1 = BracketExpUtil.generateTreeNoTopBracket(str);
		str = "((IP(IP(NP(NP(PN 他))(CC 和)(NP(QP(CD 一)(CLP(M 个)))(NP(NN 姑娘))))(VP(VV 坐)(ADVP(AD 在一起))))(PU 。)))";
		TreeNode s2 = BracketExpUtil.generateTreeNoTopBracket(str);

		List<TreeNode> ss = new ArrayList<TreeNode>();
		ss.add(s1);
		ss.add(s2);

		CenteringBFP bfp = new CenteringBFP();
		bfp.setGrammaticalRoleRuleSet(GrammaticalRoleRuleSet.getGrammaticalRoleRuleSet());		//设置语法角色规则集，此规则集为缺省值
		
		AttributeFilter attributeFilter = new AttributeFilter(new PNFilter(new NodeNameFilter())); // 组合过滤器
		attributeFilter.setAttributeGenerator(new AttributeGeneratorByDic()); // 装入属性生成器
		bfp.setAttributeFilter(attributeFilter);	//设置属性过滤器，此过滤器为缺省值
		
		Map<TreeNode, TreeNode> result = bfp.resolve(ss);
		
		Map<TreeNode, TreeNode> goal = new HashMap<TreeNode, TreeNode>();
		goal.put(TreeNodeUtil.getAllLeafNodes(s2).get(0), TreeNodeUtil.getAllLeafNodes(s1).get(9));

		assertEquals(goal, result);

	}

	@Test
	public void testCenteringBFP_2()
	{
		String str;
		// 句一：小明的妈妈是一名教师。
		str = "((IP(NP(DNP(NP(NN 小明))(DEG 的))(NP(NN 妈妈)))(VP(VC 是)(NP(QP(CD 一)(CLP(M 名)))(NP(NN 教师))))(PU 。)))";
		TreeNode s1 = BracketExpUtil.generateTreeNoTopBracket(str);
		// 句二：家长们都很喜欢她。
		str = "((IP(IP(NP(NN 家长们))(VP(ADVP(AD 都))(ADVP(AD 很))(VP(VV 喜欢)(NP(PN 她)))))(PU 。)))";
		TreeNode s2 = BracketExpUtil.generateTreeNoTopBracket(str);

		List<TreeNode> ss = new ArrayList<TreeNode>();
		ss.add(s1);
		ss.add(s2);

		CenteringBFP bfp = new CenteringBFP();
		bfp.setGrammaticalRoleRuleSet(GrammaticalRoleRuleSet.getGrammaticalRoleRuleSet());

		Map<TreeNode, TreeNode> result = bfp.resolve(ss);

		Map<TreeNode, TreeNode> goal = new HashMap<TreeNode, TreeNode>();
		goal.put(TreeNodeUtil.getAllLeafNodes(s2).get(4), TreeNodeUtil.getAllLeafNodes(s1).get(2));

		assertNotEquals(goal, result);
	}

	@Test
	public void testGetTransition()
	{

	}
}
