/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*$Id: StylePatternFactory.java,v 1.1.2.3.2.5 2007/09/13 20:33:07 christianfoltin Exp $*/

package freemind.modes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import freemind.common.TextTranslator;
import freemind.common.XmlBindingTools;
import freemind.controller.actions.generated.instance.Pattern;
import freemind.controller.actions.generated.instance.PatternEdgeColor;
import freemind.controller.actions.generated.instance.PatternEdgeStyle;
import freemind.controller.actions.generated.instance.PatternEdgeWidth;
import freemind.controller.actions.generated.instance.PatternIcon;
import freemind.controller.actions.generated.instance.PatternNodeBackgroundColor;
import freemind.controller.actions.generated.instance.PatternNodeColor;
import freemind.controller.actions.generated.instance.PatternNodeFontBold;
import freemind.controller.actions.generated.instance.PatternNodeFontItalic;
import freemind.controller.actions.generated.instance.PatternNodeFontName;
import freemind.controller.actions.generated.instance.PatternNodeFontSize;
import freemind.controller.actions.generated.instance.PatternNodeStyle;
import freemind.controller.actions.generated.instance.PatternPropertyBase;
import freemind.controller.actions.generated.instance.Patterns;
import freemind.main.Tools;

/**
 * This class constructs patterns from files or from nodes and saves them back.
 */
public class StylePatternFactory {
	public static final String FALSE_VALUE = "false";

	public static final String TRUE_VALUE = "true";

	public static List loadPatterns(File file) throws Exception {
		return loadPatterns(new BufferedReader(new FileReader(file)));
	}

	/**
	 * @return a List of Pattern elements.
	 * @throws Exception
	 */
	public static List loadPatterns(Reader reader) throws Exception {
		Patterns patterns = (Patterns) XmlBindingTools.getInstance()
				.unMarshall(reader);
		return patterns.getListChoiceList();
	}

	/**
	 *            the result is written to, and it is closed afterwards
	 *            List of Pattern elements.
	 * @throws Exception
	 */
	public static void savePatterns(Writer writer, List listOfPatterns)
			throws Exception {
		Patterns patterns = new Patterns();
		for (Iterator iter = listOfPatterns.iterator(); iter.hasNext();) {
			Pattern pattern = (Pattern) iter.next();
			patterns.addChoice(pattern);
		}
		String marshalledResult = XmlBindingTools.getInstance().marshall(
				patterns);
		writer.write(marshalledResult);
		writer.close();
	}

	public static Pattern createPatternFromNode(MindMapNode node) {
		Pattern pattern = new Pattern();

		if (node.getColor() != null) {
			PatternNodeColor subPattern = new PatternNodeColor();
			subPattern.setValue(Tools.colorToXml(node.getColor()));
			pattern.setPatternNodeColor(subPattern);
		}
		//#if defined(BACKGROUND_COLOR)
		//@#$LPS-BACKGROUND_COLOR:GranularityType:Statement
		if (node.getBackgroundColor() != null) {
			PatternNodeBackgroundColor subPattern = new PatternNodeBackgroundColor();
			subPattern.setValue(Tools.colorToXml(node.getBackgroundColor()));
			pattern.setPatternNodeBackgroundColor(subPattern);
		}
		//#endif
		if (node.getStyle() != null) {
			PatternNodeStyle subPattern = new PatternNodeStyle();
			subPattern.setValue(node.getStyle());
			pattern.setPatternNodeStyle(subPattern);
		}

		PatternNodeFontBold nodeFontBold = new PatternNodeFontBold();
		nodeFontBold.setValue(node.isBold() ? TRUE_VALUE : FALSE_VALUE);
		pattern.setPatternNodeFontBold(nodeFontBold);
		PatternNodeFontItalic nodeFontItalic = new PatternNodeFontItalic();
		nodeFontItalic.setValue(node.isItalic() ? TRUE_VALUE : FALSE_VALUE);
		pattern.setPatternNodeFontItalic(nodeFontItalic);
		if (node.getFontSize() != null) {
			PatternNodeFontSize nodeFontSize = new PatternNodeFontSize();
			nodeFontSize.setValue(node.getFontSize());
			pattern.setPatternNodeFontSize(nodeFontSize);
		}
		if (node.getFontFamilyName() != null) {
			PatternNodeFontName subPattern = new PatternNodeFontName();
			subPattern.setValue(node.getFontFamilyName());
			pattern.setPatternNodeFontName(subPattern);
		}
		//#if defined(ICONS)
		//@#$LPS-ICONS:GranularityType:Statement
		if (node.getIcons().size() == 1) {
			PatternIcon iconPattern = new PatternIcon();
			iconPattern.setValue(((MindIcon) node.getIcons().get(0)).getName());
			pattern.setPatternIcon(iconPattern);
		}
		//#endif
		if (node.getEdge().getColor() != null) {
			PatternEdgeColor subPattern = new PatternEdgeColor();
			subPattern.setValue(Tools.colorToXml(node.getEdge().getColor()));
			pattern.setPatternEdgeColor(subPattern);
		}
		if (node.getEdge().getStyle() != null) {
			PatternEdgeStyle subPattern = new PatternEdgeStyle();
			subPattern.setValue(node.getEdge().getStyle());
			pattern.setPatternEdgeStyle(subPattern);
		}
		if (node.getEdge().getWidth() != EdgeAdapter.DEFAULT_WIDTH) {
			PatternEdgeWidth subPattern = new PatternEdgeWidth();
			subPattern.setValue("" + node.getEdge().getWidth());
			pattern.setPatternEdgeWidth(subPattern);
		}

		return pattern;
	}

	public static String toString(Pattern pPattern, TextTranslator translator) {
		String result = "";
		if (pPattern.getPatternNodeColor() != null) {
			result = addSeparatorIfNecessary(result);
			if (pPattern.getPatternNodeColor().getValue() == null) {
				result += "-" + translator.getText("PatternToString.color");
			} else {
				result += "+" + translator.getText("PatternToString.color");
			}
		}
		if (pPattern.getPatternNodeBackgroundColor() != null) {
			result = addSeparatorIfNecessary(result);
			if (pPattern.getPatternNodeBackgroundColor().getValue() == null) {
				result += "-"
						+ translator.getText("PatternToString.backgroundColor");
			} else {
				result += "+"
						+ translator.getText("PatternToString.backgroundColor");
			}
		}
		result = addSubPatternToString(translator, result, pPattern.getPatternNodeFontSize(),
				"PatternToString.NodeFontSize");
		result = addSubPatternToString(translator, result, pPattern.getPatternNodeFontName(),
		"PatternToString.FontName");
		result = addSubPatternToString(translator, result, pPattern.getPatternNodeFontBold(),
		"PatternToString.FontBold");
		result = addSubPatternToString(translator, result, pPattern.getPatternNodeFontItalic(),
		"PatternToString.FontItalic");
		result = addSubPatternToString(translator, result, pPattern.getPatternEdgeStyle(),
		"PatternToString.EdgeStyle");
		result = addSubPatternToString(translator, result, pPattern.getPatternEdgeColor(),
		"PatternToString.EdgeColor");
		result = addSubPatternToString(translator, result, pPattern.getPatternEdgeWidth(),
		"PatternToString.EdgeWidth");
		//#if defined(ICONS)
		//@#$LPS-ICONS:GranularityType:Statement
		result = addSubPatternToString(translator, result, pPattern.getPatternIcon(),
		"PatternToString.Icon");
		//#endif
		result = addSubPatternToString(translator, result, pPattern.getPatternChild(),
		"PatternToString.Child");
		return result;
	}

	private static String addSubPatternToString(TextTranslator translator,
			String result, PatternPropertyBase patternType, String patternString) {
		if (patternType != null) {
			result = addSeparatorIfNecessary(result);
			if (patternType.getValue() == null) {
				result += "-"
						+ translator.getText(patternString);
			} else {
				result += "+"
						+ translator.getText(patternString) + " "
						+ patternType.getValue();
			}
		}
		return result;
	}

	private static String addSeparatorIfNecessary(String result) {
		if (result.length() > 0) {
			result += ", ";
		}
		return result;
	}

	private static final String PATTERN_DUMMY = "<pattern name='dummy'/>";

	public static Pattern getPatternFromString(String pattern) {
		String patternString = pattern;
		if (patternString == null) {
			patternString = PATTERN_DUMMY;
		}
		Pattern pat = (Pattern) XmlBindingTools.getInstance().unMarshall(
				patternString);
		return pat;
	}

    private static final String PATTERNS_DUMMY = "<patterns/>";
	public static Patterns getPatternsFromString(String patterns) {
	    String patternsString = patterns;
	    if (patternsString == null) {
	        patternsString = PATTERNS_DUMMY;
	    }
	    Patterns pat = (Patterns) XmlBindingTools.getInstance().unMarshall(
	            patternsString);
	    return pat;
	}

	/** Build the intersection of two patterns. Only, if the 
	 *  property is the same, or both properties are
	 *  to be removed, it is kept, otherwise it is set to 'don't touch'. 
	 */
	public static Pattern intersectPattern(Pattern p1, Pattern p2){
		Pattern result = new Pattern();
		result.setPatternEdgeColor((PatternEdgeColor) processPatternProperties(p1.getPatternEdgeColor(), p2.getPatternEdgeColor(), new PatternEdgeColor()));
		result.setPatternEdgeStyle((PatternEdgeStyle) processPatternProperties(p1.getPatternEdgeStyle(), p2.getPatternEdgeStyle(), new PatternEdgeStyle()));
		result.setPatternEdgeWidth((PatternEdgeWidth) processPatternProperties(p1.getPatternEdgeWidth(), p2.getPatternEdgeWidth(), new PatternEdgeWidth()));
		//#if defined(ICONS)
		//@#$LPS-ICONS:GranularityType:Statement
		result.setPatternIcon((PatternIcon) processPatternProperties(p1.getPatternIcon(), p2.getPatternIcon(), new PatternIcon()));
		//#endif
		result.setPatternNodeBackgroundColor((PatternNodeBackgroundColor) processPatternProperties(p1.getPatternNodeBackgroundColor(), p2.getPatternNodeBackgroundColor(), new PatternNodeBackgroundColor()));
		result.setPatternNodeColor((PatternNodeColor) processPatternProperties(p1.getPatternNodeColor(), p2.getPatternNodeColor(), new PatternNodeColor()));
		result.setPatternNodeFontBold((PatternNodeFontBold) processPatternProperties(p1.getPatternNodeFontBold(), p2.getPatternNodeFontBold(), new PatternNodeFontBold()));
		result.setPatternNodeFontItalic((PatternNodeFontItalic) processPatternProperties(p1.getPatternNodeFontItalic(), p2.getPatternNodeFontItalic(), new PatternNodeFontItalic()));
		result.setPatternNodeFontName((PatternNodeFontName) processPatternProperties(p1.getPatternNodeFontName(), p2.getPatternNodeFontName(), new PatternNodeFontName()));
		result.setPatternNodeFontSize((PatternNodeFontSize) processPatternProperties(p1.getPatternNodeFontSize(), p2.getPatternNodeFontSize(), new PatternNodeFontSize()));
		result.setPatternNodeStyle((PatternNodeStyle) processPatternProperties(p1.getPatternNodeStyle(), p2.getPatternNodeStyle(), new PatternNodeStyle()));
		return result;
	}

	private static PatternPropertyBase processPatternProperties(PatternPropertyBase prop1, PatternPropertyBase prop2, PatternPropertyBase destination) {
		if(prop1 == null || prop2 == null){
			return null;
		}
		// both delete the value or both have the same value:
		if(Tools.safeEquals(prop1.getValue(), prop2.getValue())){
			destination.setValue(prop1.getValue());
			return destination;
		}
		return null;
	}

	public static Pattern createPatternFromSelected(MindMapNode focussed, List selected) {
		Pattern nodePattern = createPatternFromNode(focussed);
		for (Iterator iter = selected.iterator(); iter.hasNext();) {
			MindMapNode node = (MindMapNode) iter.next();
			Pattern tempNodePattern = createPatternFromNode(node);
			nodePattern = intersectPattern(nodePattern, tempNodePattern);
		}
		return nodePattern;
	}
	
}
