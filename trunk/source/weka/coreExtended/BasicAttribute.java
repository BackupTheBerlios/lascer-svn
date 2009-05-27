/*
 * Filename     : BasicAttribute.java
 * Last change  : 28. May 2005 by Dietmar Lippold
 * Authors      : Eibe Frank (eibe@cs.waikato.ac.nz)
 *                Yang Zhou
 * Copyright (C): Eibe Frank, 1999
 *                Image Understanding Department, University of Stuttgart, 2002
 *
 * This file was developed on basis of the weka package
 * (http://sourceforge.net/projects/weka).
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package weka.coreExtended;

import weka.core.*;
import java.io.*;
import java.util.*;

/** 
 * Class for handling a basic attribute.
 *
 * @author  Yang Zhou
 *
 * @version  8. Jul. 2002
 */
public class BasicAttribute extends Attribute {
	
  /**
   * Constructor for a numeric attribute.
   *
   * @param attributeName the name for the attribute
   */
  private BasicAttribute(String attributeName) {

  
    super(attributeName);
  }
  
  
  /**
   * Constructor for a special numeric attribute.
   *
   * @param attributeName the name for the attribute
   */
  public BasicAttribute(String attributeName,boolean isInt) {

  
    super(attributeName,isInt);
  }
  
  /**
   * Constructor for nominal attributes and string attributes.
   * If a null vector of attribute values is passed to the method,
   * the attribute is assumed to be a string.
   *
   * @param attributeName the name for the attribute
   * @param attributeValues a vector of strings denoting the 
   * attribute values. Null if the attribute is a string attribute.
   */
  public BasicAttribute(String attributeName,FastVector attributeValues) {
  	
  	super(attributeName, attributeValues);

    }
  
   /**
   * Constructor for a numeric attribute with a particular index.
   *
   * @param attributeName the name for the attribute
   * @param index the attribute's index
   */
   
  BasicAttribute(String attributeName, boolean isInt, int index) {
      
      super(attributeName ,isInt,index);
  }
  
  /**
   * Constructor for nominal attributes and string attributes with
   * a particular index.
   * If a null vector of attribute values is passed to the method,
   * the attribute is assumed to be a string.
   *
   * @param attributeName the name for the attribute
   * @param attributeValues a vector of strings denoting the attribute values.
   * Null if the attribute is a string attribute.
   * @param index the attribute's index
   */
 
  BasicAttribute(String attributeName, FastVector attributeValues,  int index) {
  	
    super(attributeName,attributeValues,index);
  }
 
   /**
   * Returns a description of this attribute in ARFF format. Quotes
   * strings if they contain whitespace characters, or if they
   * are a question mark.
   *
   * @return a description of this basicAttribute as a string
   */
   
   public final String toString() {
    
    StringBuffer text = new StringBuffer();
    
    text.append("@attribute " + Utils.quote(m_Name) + " ");
    if (isNominal()) {
      text.append('{');
      Enumeration enum = enumerateValues();
      while (enum.hasMoreElements()) {
	text.append(Utils.quote((String) enum.nextElement()));
	if (enum.hasMoreElements())
	  text.append(',');
      }
      text.append('}');
    } else {
      if (isInteger()) {
	text.append("integer");
      } else {
      if (isReal()) {
	text.append("real");
	}
	else 
	{
	text.append("string");
      }}
    }
    return text.toString();
  }

  /**
   * Produces a shallow copy of this attribute.
   *
   * @return a copy of this attribute with the same index
   */
   
 public final Object copy() {

    BasicAttribute copy = new BasicAttribute(m_Name);

    copy.m_Index = m_Index;
    if (!isNominal() && !isString())
      return copy;
  //  copy.m_Type = m_Type;
    copy.m_Values = m_Values;
    copy.m_Hashtable = m_Hashtable;
 
    return copy;
  }
  
  /**
   * Produces a shallow copy of this attribute with a new name.
   *
   * @param newName the name of the new attribute
   * @return a copy of this attribute with the same index
   */
 
 final  Attribute copy(String newName) {

    BasicAttribute copy = new BasicAttribute(newName);

    copy.m_Index = m_Index;
    if (!isNominal() && !isString())
      return copy;
    copy.m_Type = m_Type;
    copy.m_Values = m_Values;
    copy.m_Hashtable = m_Hashtable;
 
    return copy;
}
  
  /**
   * Tests if given attribute is equal to this attribute.
   *
   * @param other the Object to be compared to this attribute
   * @return true if the given attribute is equal to this attribute
   */
   
   public final boolean equals(Object other) {

    if ((other == null) || !(other.getClass().equals(this.getClass()))) {
      return false;
    }
    BasicAttribute att = (BasicAttribute) other;
    if (!m_Name.equals(att.m_Name)) {
      return false;
    }
     if (isReal() && att.isReal()){
    	return true;
    }
    if (isInteger() && att.isInteger()){
    	return true;
    }
    if (isReal() || att.isReal()){
    	return false;
    }
    if (isInteger() || att.isInteger()){
    	return false;
    }
    
    if (m_Values.size() != att.m_Values.size()) {
      return false;
    }
    for (int i = 0; i < m_Values.size(); i++) {
      if (!m_Values.elementAt(i).equals(att.m_Values.elementAt(i))) {
	return false;
      }
    }
    return true;
  }
  
 
  public static void main(String[] ops) {
  	
 

    try {
      
      // Create numeric attributes "length" and "weight"
      BasicAttribute length = new BasicAttribute("length");
      BasicAttribute weight = new BasicAttribute("weight");
      
      // Create vector to hold nominal values "first", "second", "third" 
      FastVector my_nominal_values = new FastVector(3); 
      my_nominal_values.addElement("first"); 
      my_nominal_values.addElement("second"); 
      my_nominal_values.addElement("third"); 
      
      // Create nominal attribute "position" 
      BasicAttribute position = new BasicAttribute("position", my_nominal_values);
      position.toString();

      // Print the name of "position"
      System.out.println("Name of \"position\": " + position.name());

      // Print the values of "position"
      Enumeration attValues = position.enumerateValues();
      while (attValues.hasMoreElements()) {
    String string = (String)attValues.nextElement();
	System.out.println("Value of \"position\": " + string);
      }

      // Shallow copy attribute "position"
      BasicAttribute copy = (BasicAttribute) position.copy();

      // Test if attributes are the same
      System.out.println("Copy is the same as original: " + copy.equals(position));

      // Print index of attribute "weight" (should be unset: -1)
      System.out.println("Index of basicAttribute \"weight\" (should be -1): " + 
			 weight.index());

      // Print index of value "first" of attribute "position"
      System.out.println("Index of value \"first\" of \"position\" (should be 0): " +
			 position.indexOfValue("first"));

      // Tests type of attribute "position"
      System.out.println("\"position\" is real: " + position.isReal());
      System.out.println("\"position\" is integer: " + position.isInteger());
      System.out.println("\"position\" is nominal: " + position.isNominal());
      System.out.println("\"position\" is string: " + position.isString());

      // Prints name of attribute "position"
      System.out.println("Name of \"position\": " + position.name());
    
      // Prints number of values of attribute "position"
      System.out.println("Number of values for \"position\": " + position.numValues());

      // Prints the values (againg)
      for (int i = 0; i < position.numValues(); i++) {
	System.out.println("Value " + i + ": " + position.value(i));
      }

      // Prints the attribute "position" in ARFF format
      System.out.println(position);

      // Checks type of attribute "position" using constants
      switch (position.type()) {
       case Attribute.REAL:
	System.out.println("\"position\" is real");
	break;	
      case Attribute.INTEGER:
	System.out.println("\"position\" is integer");
	
	break;
      case Attribute.NOMINAL:
	System.out.println("\"position\" is nominal");
	break;
      case Attribute.STRING:
	System.out.println("\"position\" is string");
	break;
      default:
	System.out.println("\"position\" has unknown type");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

