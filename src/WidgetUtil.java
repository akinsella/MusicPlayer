/*
 * Created on 23 nov. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

/**
 * @author Alexis KINSELLA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WidgetUtil
{

	public static List createList(Composite parent, Object layoutData, int style) { return createList(parent, null, null, null, layoutData, style); }

	public static List createList(Composite parent, Color fc, Color bc, Font font, Object layoutData, int style)
	{
		List lst = new List(parent, style);
		if (fc != null) lst.setForeground(fc);
		if (bc != null) lst.setBackground(bc);
		if (font != null) lst.setFont(font);
		if (layoutData != null) lst.setLayoutData(layoutData); 

		return lst;
	}

	public static Button createButton(Composite parent, String text, Object layoutData, int style)
	{
		return createButton(parent, text, parent.getForeground(), parent.getBackground(), null, layoutData, style);
	}

	public static Button createButton(Composite parent, String text, Color fc, Color bc, Font font, Object layoutData, int style)
	{
		Button btn = new Button(parent, style);
		if (text != null) btn.setText(text);
		if (fc != null) btn.setForeground(fc);
		if (bc != null) btn.setBackground(bc);
		if (font != null) btn.setFont(font);
		if (layoutData != null) btn.setLayoutData(layoutData); 

		return btn;
	}

}
