/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2015-2017 by the contributors of the JetUML project.
 *
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package ca.mcgill.cs.jetuml.application;

import java.util.StringTokenizer;

/**
 *  A string that can extend over multiple lines.
 */
public class MultiLineString implements Cloneable
{
	/**
	 * How to align the text in this string.
	 */
	public enum Align
	{ LEFT, CENTER, RIGHT }
	
	private String aText = "";
	private Align aJustification = Align.CENTER;
	private boolean aBold = false;
	private boolean aUnderlined = false;
	
	/**
     * Constructs an empty, centered, normal size multi-line
     * string that is not underlined and not bold.
	 */
	public MultiLineString() 
	{}
	
	/**
     * Constructs an empty, centered, normal size multi-line
     * string that is not underlined. pBold determines if it is bold.
     * @param pBold True if the string should be bold.
	 */
	public MultiLineString(boolean pBold) 
	{ 
		aBold = pBold;
	}
	
	/**
     * Sets the value of the text property.
     * @param pText the text of the multiline string
	 */
	public void setText(String pText)
	{ 
		aText = pText; 
	}
   
	/**
     * Gets the value of the text property.
     * @return the text of the multi-line string
	 */
	public String getText() 
	{ 
		return aText; 
	}
   
	/**
     * Sets the value of the justification property.
     * @param pJustification the justification.
	 */
	public void setJustification(Align pJustification) 
	{ 
		assert pJustification != null;
		aJustification = pJustification;
	}
   
	/**
     * Gets the value of the justification property.
     * @return the justification, one of LEFT, CENTER, RIGHT
	 */
	public Align obtainJustification() 
	{ 
		return aJustification;
	}
   
	/**
     * Gets the value of the underlined property.
     * @return true if the text is underlined
	 */
	public boolean isUnderlined() 
	{ 
		return aUnderlined;
	}
	
	/**
	 * @return The value of the bold property.
	 */
	public boolean isBold()
	{
		return aBold;
	}
  
	/**
     * Sets the value of the underlined property.
     * @param pUnderlined true to underline the text
	 */
	public void setUnderlined(boolean pUnderlined) 
	{ 
		aUnderlined = pUnderlined; 
	}
   
	@Override
	public String toString()
	{
		return aText.replace('\n', '|');
	}

	/**
	 * @return an HTML version of the text of the string,
	 * taking into account the properties (underline, bold, etc.)
	 */
	public String convertToHtml()
	{
		StringBuffer prefix = new StringBuffer();
		StringBuffer suffix = new StringBuffer();
		StringBuffer htmlText = new StringBuffer();
		
		// Add some spacing before and after the text.
		prefix.append("&nbsp;");
		suffix.insert(0, "&nbsp;");
		if(aUnderlined) 
		{
			prefix.append("<u>");
			suffix.insert(0, "</u>");
		}
		if(aBold)
		{
			prefix.append("<b>");
			suffix.insert(0, "</b>");
		}

		htmlText.append("<html><p align=\"" + aJustification.toString().toLowerCase() + "\">");
		StringTokenizer tokenizer = new StringTokenizer(aText, "\n");
		boolean first = true;
		while(tokenizer.hasMoreTokens())
		{
			if(first) 
			{
				first = false;
			}
			else 
			{
				htmlText.append("<br>");
			}
			htmlText.append(prefix);
			String next = tokenizer.nextToken();
			String next0 = next.replaceAll("&", "&amp;");
			String next1 = next0.replaceAll("<", "&lt;");
			String next2 = next1.replaceAll(">", "&gt;");
			htmlText.append(next2);
			htmlText.append(suffix);
		}      
		htmlText.append("</p></html>");
		return htmlText.toString();
	}
	
	@Override
	public boolean equals(Object pObject)
	{
		if( this == pObject )
		{
			return true;
		}
		if( pObject == null )
		{
			return false;
		}
		if( pObject.getClass() != getClass() )
		{
			return false;
		}
		MultiLineString theString = (MultiLineString)pObject;
		return aText.equals(theString.aText) && aJustification == theString.aJustification &&
				aBold == theString.aBold && aUnderlined == theString.aUnderlined;
	}
	
	@Override
	public int hashCode()
	{
		return convertToHtml().toString().hashCode();
	}
	
	@Override
	public MultiLineString clone()
	{
		try
		{
			return (MultiLineString) super.clone();
		}
		catch (CloneNotSupportedException exception)
		{
			return null;
		}
	}
}
