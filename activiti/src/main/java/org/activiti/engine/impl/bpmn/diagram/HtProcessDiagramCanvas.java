package org.activiti.engine.impl.bpmn.diagram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import com.hotent.activiti.def.graph.ilog.activiti.ProcessDiagramCanvas;

public class HtProcessDiagramCanvas extends ProcessDiagramCanvas{

	protected static Stroke THICK2_TASK_BORDER_STROKE = new BasicStroke(2.0F);
	protected static int LABEL_WRAP_WIDTH = 100;
	
	public HtProcessDiagramCanvas(int width, int height) {
		super(width, height);
	}
	
	public HtProcessDiagramCanvas(int width, int height, int minX, int minY) {
	    super(width, height, minX, minY);
    }
	
	 public void drawHighLight(int x, int y, int width, int height, String colorStr) {
	    boolean isNormal=false;
		if(colorStr==null){
			colorStr="#000000";
			isNormal=true;
		}
		Color color=Color.decode(colorStr);
	    Paint originalPaint = g.getPaint();
	    Stroke originalStroke = g.getStroke();

	    this.g.setPaint(color);
		if(isNormal){
			this.g.setStroke(THICK2_TASK_BORDER_STROKE);
		}else{
			this.g.setStroke(THICK_TASK_BORDER_STROKE);
		}

	    RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
	    g.draw(rect);

	    g.setPaint(originalPaint);
	    g.setStroke(originalStroke);
     }
	 
	 public void drawLabel(String text, int x, int y, int width, int height, boolean centered){
		 float interline = 1.0f;
			
		    // text
		    if (text != null && text.length()>0) {
//		      Paint originalPaint = g.getPaint();
		      Font originalFont = g.getFont();
//		      g.setPaint(new Color(80, 160, 240));
		      g.setFont(LABEL_FONT);
		      int wrapWidth = LABEL_WRAP_WIDTH;
		      int textY = y + height;
		      
		      // TODO: use drawMultilineText()
		      AttributedString as = new AttributedString(text);
		      as.addAttribute(TextAttribute.FOREGROUND, g.getPaint());
		      as.addAttribute(TextAttribute.FONT, g.getFont());
		      AttributedCharacterIterator aci = as.getIterator();
		      FontRenderContext frc = new FontRenderContext(null, true, false);
		      LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
		      
		      while (lbm.getPosition() < text.length()) {
		    	  TextLayout tl = lbm.nextLayout(wrapWidth);
		    	  textY += tl.getAscent();
		    	  Rectangle2D bb = tl.getBounds();
		    	  float tY = (float) (x);
		    	  if (centered)
		        	  tY += (int)(width/2 - bb.getWidth()/2);
		    	  tl.draw(g, tY, textY);
		    	  textY += tl.getDescent() + tl.getLeading() + (interline - 1.0f) * tl.getAscent();
		      }
//		      g.setPaint(originalPaint);
		      g.setFont(originalFont);
		    }
	  }
}
