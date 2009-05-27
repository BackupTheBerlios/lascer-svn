/*
 * Filename     : MetaInstance.java
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
import java.util.*;
import java.io.*;

/** 
 * Class for handling a meta instance.
 *
 * @author  Eibe Frank (eibe@cs.waikato.ac.nz)
 * @author  Yang Zhou
 *
 * @version  8. Jul. 2002
 */
public class MetaInstance extends Instance {
	
	
 /**
   * Constructor that copies the attribute values and the weight from
   * the given instance. Reference to the dataset is set to null.
   * (ie. the instance doesn't have access to information about the
   * attribute types)
   *
   * @param instance the instance from which the attribute
   * values and the weight are to be copied 
   */
  public MetaInstance(MetaInstance instance) {
    
   super(instance);
  }

  /**
   * Constructor that inititalizes instance variable with given
   * values. Reference to the dataset is set to null. (ie. the instance
   * doesn't have access to information about the attribute types)
   *
   * @param weight the instance's weight
   * @param attValues a vector of attribute values 
   */
  public MetaInstance(double weight, double[] attValues){
    
   super(weight,attValues);
  }

  /**
   * Constructor of an instance that sets weight to one, all values to
   * be missing, and the reference to the dataset to null. (ie. the instance
   * doesn't have access to information about the attribute types)
   *
   * @param numAttributes the size of the instance 
   */
  public MetaInstance(int numAttributes) {
   
   super(numAttributes);
  }
	

  /**
   * Returns the attribute with the given index.
   *
   * @param index the attribute's index
   * @return the attribute at the given position
   * @exception UnassignedDatasetException if instance doesn't have access to a
   * dataset
   */ 
  public Attribute attribute(int index) {
   
    if (m_Dataset == null) {
      throw new UnassignedDatasetException("Instance doesn't have access to a dataset!");
    }
    return m_Dataset.metaAttribute(index);
  }

  /**
   * Returns the attribute with the given index. Does the same
   * thing as attribute().
   *
   * @param indexOfIndex the index of the attribute's index 
   * @return the attribute at the given position
   * @exception UnassignedDatasetException if instance doesn't have access to a
   * dataset
   */ 
  public Attribute attributeSparse(int indexOfIndex) {
   
    if (m_Dataset == null) {
      throw new UnassignedDatasetException("Instance doesn't have access to a dataset!");
    }
    return m_Dataset.metaAttribute(indexOfIndex);
  }

  /**
   * Returns class attribute.
   *
   * @return the class attribute
   * @exception UnassignedDatasetException if the class is not set or the
   * instance doesn't have access to a dataset
   */
  public Attribute classAttribute() {

    if (m_Dataset == null) {
      throw new UnassignedDatasetException("Instance doesn't have access to a dataset!");
    }
    return m_Dataset.metaClassAttribute();
  }
  
  
   /** Returns the class attribute's index.
   *
   * @return the class index as an integer 
   * @exception UnassignedDatasetException if instance doesn't have access to a dataset 
   */
  public int classIndex() {
    
    if (m_Dataset == null) {
      throw new UnassignedDatasetException("Instance doesn't have access to a dataset!");
    }
    return m_Dataset.metaClassIndex();
  }
  
  
  /**
   * Returns an instance's class value in internal format. (ie. as a
   * floating-point number)
   *
   * @return the corresponding value as a double (If the 
   * corresponding attribute is nominal (or a string) then it returns the 
   * value's index as a double).
   * @exception UnassignedClassException if the class is not set or the instance doesn't
   * have access to a dataset 
   */
  public double classValue() {
    
    if (classIndex() < 0) {
      throw new UnassignedClassException("Class is not set!");
    }
    return value(classIndex());
  }
  
  
  /**
   * Tests if an instance's class is missing.
   *
   * @return true if the instance's class is missing
   * @exception UnassignedClassException if the class is not set or the instance doesn't
   * have access to a dataset
   */
  public boolean classIsMissing() {

    if (classIndex() < 0) {
      throw new UnassignedClassException("Class is not set!");
    }
    return isMissing(classIndex());
  }
  
  
  
   /**
   * Sets the class value of an instance to be "missing". A deep copy of
   * the vector of attribute values is performed before the
   * value is set to be missing.
   *
   * @exception UnassignedClassException if the class is not set
   * @exception UnassignedDatasetException if the instance doesn't
   * have access to a dataset
   */
  public void setClassMissing() {

    if (classIndex() < 0) {
      throw new UnassignedClassException("Class is not set!");
    }
    setMissing(classIndex());
  }
  
  
  
  /**
   * Sets the class value of an instance to the given value (internal
   * floating-point format).  A deep copy of the vector of attribute
   * values is performed before the value is set.
   *
   * @param value the new attribute value (If the corresponding
   * attribute is nominal (or a string) then this is the new value's
   * index as a double).  
   * @exception UnassignedClassException if the class is not set
   * @exception UnaddignedDatasetException if the instance doesn't
   * have access to a dataset 
   */
  public void setClassValue(double value) {

    if (classIndex() < 0) {
      throw new UnassignedClassException("Class is not set!");
    }
    setValue(classIndex(), value);
  }


/**
   * Sets the class value of an instance to the given value. A deep
   * copy of the vector of attribute values is performed before the
   * value is set.
   *
   * @param value the new class value (If the class
   * is a string attribute and the value can't be found,
   * the value is added to the attribute).
   * @exception UnassignedClassException if the class is not set
   * @exception UnassignedDatasetException if the dataset is not set
   * @exception IllegalArgumentException if the attribute is not
   * nominal or a string, or the value couldn't be found for a nominal
   * attribute 
   */
  public final void setClassValue(String value) {

    if (classIndex() < 0) {
      throw new UnassignedClassException("Class is not set!");
    }
    setValue(classIndex(), value);
  }
  /**
   * Produces a shallow copy of this instance. The copy has
   * access to the same dataset. (if you want to make a copy
   * that doesn't have access to the dataset, use 
   * <code>new Instance(instance)</code>
   *
   * @return the shallow copy
   */
  public Object copy() {

    MetaInstance result = new MetaInstance(this);
    result.m_Dataset = m_Dataset;
    return result;
  }
	
	
	/**
   * Returns the description of one instance. If the instance
   * doesn't have access to a dataset, it returns the internal
   * floating-point values. Quotes string
   * values that contain whitespace characters.
   *
   * @return the instance's description as a string
   */
  public String toString() {

    StringBuffer text = new StringBuffer();
    
    for (int i = 0; i < m_AttValues.length; i++) {
      if (i > 0) text.append(",");
      text.append(toString(i));
    }

    return text.toString();
  }

  /**
   * Returns the description of one value of the instance as a 
   * string. If the instance doesn't have access to a dataset, it 
   * returns the internal floating-point value. Quotes string
   * values that contain whitespace characters, or if they
   * are a question mark.
   *
   * @param attIndex the attribute's index
   * @return the value's description as a string
   */
  public final String toString(int attIndex) {

   StringBuffer text = new StringBuffer();
   
   if (isMissing(attIndex)) {
     text.append("?");
   } else {
     if (m_Dataset == null) {
       text.append(Utils.doubleToString(m_AttValues[attIndex],6));
     } else {
       if (m_Dataset.metaAttribute(attIndex).isNominal() || 
	   m_Dataset.metaAttribute(attIndex).isString()) {
         text.append(Utils.quote(stringValue(attIndex)));
       } else {
	 text.append(Utils.doubleToString(value(attIndex),6));
       }
     }
   }
   return text.toString();
  }
  
  /** 
   * Returns the value of a nominal (or string) attribute
   * for the instance.
   *
   * @param attIndex the attribute's index
   * @return the value as a string
   * @exception IllegalArgumentException if the attribute is not a nominal
   * (or string) attribute.
   * @exception UnassignedDatasetException if the instance doesn't belong
   * to a dataset.
   */
  public final String stringValue(int attIndex) {

    if (m_Dataset == null) {
      throw new UnassignedDatasetException("Instance doesn't have access to a dataset!");
    } 
    if (!m_Dataset.metaAttribute(attIndex).isNominal() &&
	!m_Dataset.metaAttribute(attIndex).isString()) {
      throw new IllegalArgumentException("Attribute neither nominal nor string!");
    }
    return m_Dataset.metaAttribute(attIndex).
      value((int) value(attIndex));
  }


  /**
   * Returns the description of one value of the instance as a 
   * string. If the instance doesn't have access to a dataset it 
   * returns the internal floating-point value. Quotes string
   * values that contain whitespace characters, or if they
   * are a question mark.
   * The given attribute has to belong to a dataset.
   *
   * @param att the attribute
   * @return the value's description as a string
   */
  public final String toString(Attribute att) {
   
   return toString(att.index());
  }
  
  
  public Instance mergeInstance(Instance inst) {

    int m = 0;
    double [] newVals = new double[numAttributes() + inst.numAttributes()];
    for (int j = 0; j < numAttributes(); j++, m++) {
      newVals[m] = value(j);
    }
    for (int j = 0; j < inst.numAttributes(); j++, m++) {
      newVals[m] = inst.value(j);
    }
    return new MetaInstance(1.0, newVals);
  }
	
	
	/**
   * Sets a value of a nominal or string attribute to the given
   * value. Performs a deep copy of the vector of attribute values
   * before the value is set.
   *
   * @param attIndex the attribute's index
   * @param value the new attribute value (If the attribute
   * is a string attribute and the value can't be found,
   * the value is added to the attribute).
   * @exception UnassignedDatasetException if the dataset is not set
   * @exception IllegalArgumentException if the selected
   * attribute is not nominal or a string, or the supplied value couldn't 
   * be found for a nominal attribute 
   */
  public final void setValue(int attIndex, String value) {
    
    int valIndex;

    if (m_Dataset == null) {
      throw new UnassignedDatasetException("Instance doesn't have access to a dataset!");
    }
    if (!attribute(attIndex).isNominal() &&
	!attribute(attIndex).isString()) {
      throw new IllegalArgumentException("Attribute neither nominal nor string!");
    }
    valIndex = attribute(attIndex).indexOfValue(value);
    if (valIndex == -1) {
      if (attribute(attIndex).isNominal()) {
	throw new IllegalArgumentException("Value not defined for given nominal attribute!");
      } else {
	attribute(attIndex).forceAddValue(value);
	valIndex = attribute(attIndex).indexOfValue(value);
      }
    }
    setValue(attIndex, (double)valIndex); 
  }


  /**
   * Returns an enumeration of all the attributes.
   *
   * @return enumeration of all the attributes
   * @exception UnassignedDatasetException if the instance doesn't
   * have access to a dataset 
   */
  public Enumeration enumerateAttributes() {

    if (m_Dataset == null) {
      throw new UnassignedDatasetException("Instance doesn't have access to a dataset!");
    }
    return m_Dataset.enumerateMetaAttributes();
  }
  
  
   /**
   * Returns the number of class labels.
   *
   * @return the number of class labels as an integer if the 
   * class attribute is nominal, 1 otherwise.
   * @exception UnassignedDatasetException if instance doesn't have access to any
   * dataset
   */
  public int numClasses() {
    
    if (m_Dataset == null) {
      throw new UnassignedDatasetException("Instance doesn't have access to a dataset!");
    }
    return m_Dataset.numMetaClasses();
  }
  
  
  
  /**
   * Tests if the headers of two meta instances are equivalent.
   *
   * @param instance another instance
   * @return true if the header of the given instance is 
   * equivalent to this instance's header
   * @exception UnassignedDatasetException if instance doesn't have access to any
   * dataset
   */
  public boolean equalHeaders(Instance inst) {

    if (m_Dataset == null) {
      throw new UnassignedDatasetException("Instance doesn't have access to a dataset!");
    }
    return m_Dataset.metaEqualHeaders(inst.m_Dataset);
  }
	/**
   * Main method for testing this class.
   */
  
  public static void main(String[] options) {

    try {

      // Create numeric attributes "length" and "weight"
      MetaAttribute length = new MetaAttribute("length",false);
      MetaAttribute weight = new MetaAttribute("weight",true);
      
      // Create vector to hold nominal values "first", "second", "third" 
      FastVector my_nominal_values = new FastVector(3); 
      my_nominal_values.addElement("first"); 
      my_nominal_values.addElement("second"); 
      my_nominal_values.addElement("third"); 
      
      // Create nominal MetaAttribute "position" 
      MetaAttribute position = new MetaAttribute("position", my_nominal_values);
      
      // Create vector of the above MetaAttributes 
      FastVector attributes = new FastVector(3);
      attributes.addElement(length);
      attributes.addElement(weight);
      attributes.addElement(position);
      
      
     
      // Create the empty dataset "race" with above attributes
      Instances race = new Instances("race", attributes, 0,1);
 
      // Make position the class MetaAttribute
      race.setMetaClassIndex(position.index());
      
      // Create empty instance with three MetaAttribute values
      MetaInstance inst = new MetaInstance(3);
      
      // Set instance's values for the MetaAttributes "length", "weight", and "position"
      inst.setValue(length, 5.3);
      inst.setValue(weight, 300);
      inst.setValue(position, "first");
      
      // Set instance's dataset to be the dataset "race"
      inst.setDataset(race);
      
      // Print the instance
      System.out.println("The instance: " + inst);
      
      // Print the first MetaAttribute
      System.out.println("First MetaAttribute: " + inst.attribute(0));
      
      // Print the class MetaAttribute
      System.out.println("Class MetaAttribute: " + inst.classAttribute());
      
      // Print the class index
      System.out.println("Class index: " + inst.classIndex());
      
      // Say if class is missing
      System.out.println("Class is missing: " + inst.classIsMissing());
      
      // Print the instance's class value in internal format
      System.out.println("Class value (internal format): " + inst.classValue());
      
      // Print a shallow copy of this instance
      Instance copy = (Instance) inst.copy();
      System.out.println("Shallow copy: " + copy);
      
      // Set dataset for shallow copy
      copy.setDataset(inst.dataset());
      System.out.println("Shallow copy with dataset set: " + copy);
      
      // Unset dataset for copy, delete first attribute, and insert it again
      copy.setDataset(null);
      copy.deleteAttributeAt(0);
      copy.insertAttributeAt(0);
      copy.setDataset(inst.dataset());
      System.out.println("Copy with first MetaAttribute deleted and inserted: " + copy); 
      
      // Enumerate attributes (leaving out the class attribute)
      System.out.println("Enumerating attributes (leaving out class):");
      Enumeration enum = inst.enumerateAttributes();
      while (enum.hasMoreElements()) {
	MetaAttribute att = (MetaAttribute) enum.nextElement();
	System.out.println(att);
      }
      
      // Headers are equivalent?
      System.out.println("Header of original and copy equivalent: " +
			 inst.equalHeaders(copy));

      // Test for missing values
      System.out.println("Length of copy missing: " + copy.isMissing(length));
      System.out.println("Weight of copy missing: " + copy.isMissing(weight.index()));
      System.out.println("Length of copy missing: " + 
			 Instance.isMissingValue(copy.value(length)));
      System.out.println("Missing value coded as: " + Instance.missingValue());

      // Prints number of attributes and classes
      System.out.println("Number of MetaAttributes: " + copy.numAttributes());
      System.out.println("Number of classes: " + copy.numClasses());

      // Replace missing values
      double[] meansAndModes = {2, 3, 0};
      copy.replaceMissingValues(meansAndModes);
      System.out.println("Copy with missing value replaced: " + copy);

      // Setting and getting values and weights
      copy.setClassMissing();
      System.out.println("Copy with missing class: " + copy);
      copy.setClassValue(0);
      System.out.println("Copy with class value set to first value: " + copy);
      copy.setClassValue("third");
      System.out.println("Copy with class value set to \"third\": " + copy);
      copy.setMissing(1);
      System.out.println("Copy with second MetaAttribute set to be missing: " + copy);
      copy.setMissing(length);
      System.out.println("Copy with length set to be missing: " + copy);
      copy.setValue(0, 0);
      System.out.println("Copy with first MetaAttribute set to 0: " + copy);
      copy.setValue(weight, 1);
      System.out.println("Copy with weight MetaAttribute set to 1: " + copy);
      copy.setValue(position, "second");
      System.out.println("Copy with position set to \"second\": " + copy);
      copy.setValue(2, "first");
      System.out.println("Copy with last MetaAttribute set to \"first\": " + copy);
      System.out.println("Current weight of instance copy: " + copy.weight());
      copy.setWeight(2);
      System.out.println("Current weight of instance copy (set to 2): " + copy.weight());
      System.out.println("Last value of copy: " + copy.toString(2));
      System.out.println("Value of position for copy: " + copy.toString(position));
      System.out.println("Last value of copy (internal format): " + copy.value(2));
      System.out.println("Value of position for copy (internal format): " + 
			 copy.value(position));
    } catch (Exception e) {
      e.printStackTrace();
     
    }
  }
}

