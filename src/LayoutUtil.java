/*
 * Created on 23 nov. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * @author Alexis KINSELLA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LayoutUtil
{
	
	public static GridLayout createGridLayout(int numColumns, 
            								  int marginWidth, int marginHeight,
											  int horizontalSpacing, int verticalSpacing, 
											  boolean makeColumnsEqualWidth)
	{
		GridLayout gl = new GridLayout();
		gl.numColumns = numColumns;
		gl.marginWidth = marginWidth;
		gl.marginHeight = marginHeight;
		gl.horizontalSpacing = horizontalSpacing;
		gl.verticalSpacing = verticalSpacing;
		gl.makeColumnsEqualWidth = makeColumnsEqualWidth;
		return gl;
	}
	
	public static GridData createGridData(int hType, int vType)
	{
		return createGridData(hType, vType, SWT.DEFAULT, SWT.DEFAULT, 1, 1);
	}
	
	public static GridData createGridData(int hType, int vType, int widthHint, int heightHint)
	{
		return createGridData(hType, vType, widthHint, heightHint, 1, 1);
	}
	
	public static GridData createGridData(int hType, int vType, int widthHint, int heightHint, int hSpan, int vSpan)
	{
		GridData gd = new GridData(hType, vType, hType != SWT.DEFAULT, vType != SWT.DEFAULT);
		if (hSpan > 1) gd.horizontalSpan = hSpan;
		if (vSpan > 1) gd.verticalSpan = vSpan;
		if (widthHint > 0) gd.widthHint = widthHint;
		if (heightHint > 0) gd.heightHint = heightHint;
		return gd;
	}
	
}
