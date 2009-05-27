/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    Instances.java
 *    Copyright (C) 1999 Eibe Frank
 *    Copyright (C) 2002 Image Understanding Department, University of
 *                       Stuttgart 
 */


package weka.coreExtended;

import java.math.BigDecimal;
import weka.core.*;
import java.io.*;
import java.util.*;

/**                   
 * Class for handling an ordered set of weighted instances. <p>
 *
 * Typical usage (code from the main() method of this class): <p>
 *
 * <code>
 * ... <br>
 * 
 * // Read all the instances in the file <br>
 * reader = new FileReader(filename); <br>
 * instances = new Instances(reader); <br><br>
 *
 * // Make the last attribute be the class <br>
 * instances.setClassIndex(instances.numAttributes() - 1); <br><br>
 * 
 * // Print header and instances. <br>
 * System.out.println("\nDataset:\n"); <br> 
 * System.out.println(instances); <br><br>
 *
 * ... <br>
 * </code><p>
 *
 * All methods that change a set of instances are safe, ie. a change
 * of a set of instances does not affect any other sets of
 * instances. All methods that change a datasets's attribute
 * information clone the dataset before it is changed.
 *
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @author Len Trigg (trigg@cs.waikato.ac.nz)
 * @author Yang Zhou
 *
 * @version $Revision: 1.32.2.1, 29.May.2005 $ 
 */
public class Instances implements Serializable {
 
  /** The filename extension that should be used for arff files */
  public static String FILE_EXTENSION = ".arff";

  /** The dataset's name. */
  protected String m_RelationName;         

  /** The basic attribute information. */
  protected FastVector m_basicAttributes;

  /** The basic instances. */
  protected FastVector m_basicInstances;
  
  /** The meta attribute information. */
  protected FastVector m_metaAttributes;

  /** The instances. */
  protected FastVector m_metaInstances;

  /** The basic class attribute's index */
  protected int m_basicClassIndex;
  
  /** The meta class attribute's index */
  protected int m_metaClassIndex;

  /** Buffer of values for sparse instance */
  protected double[] m_ValueBuffer;

  /** Buffer of indices for sparse instance */
  protected int[] m_IndicesBuffer;
  
  /**creat a HashSet to hold all of the basicAttribute name */
  private  HashSet basicAttributeNames = new HashSet();
    
  /** creat a HashSet to hold all of the metaData name */
  private  HashSet metaDataNames  = new HashSet();
  
  /**
   * Reads an ARFF file from a reader, and assigns a weight of
   * one to each instance. Lets the index of the class 
   * attribute be undefined (negative).
   *
   * @param reader the reader
   * @exception IOException if the ARFF file is not read 
   * successfully
   */
  public Instances(Reader reader) throws IOException {

    StreamTokenizer tokenizer;

    tokenizer = new StreamTokenizer(reader);
    initTokenizer(tokenizer);
    m_basicClassIndex = -1;
    m_metaClassIndex = -1;
    m_basicInstances = new FastVector(1000);
    m_metaInstances = new FastVector(1000);
    readHeader(tokenizer);
    while (getInstance(tokenizer, true)) {};
    compactify();
  }
 
  /**
   * Reads the header of an ARFF file from a reader and 
   * reserves space for the given number of instances. Lets
   * the class index be undefined (negative).
   *
   * @param reader the reader
   * @param capacity the capacity
   * @exception IllegalArgumentException if the header is not read successfully
   * or the capacity is negative.
   * @exception IOException if there is a problem with the reader.
   */
   public Instances(Reader reader, int capacity) throws IOException {

    StreamTokenizer tokenizer;

    if (capacity < 0) {
      throw new IllegalArgumentException("Capacity has to be positive!");
    }
    tokenizer = new StreamTokenizer(reader); 
    initTokenizer(tokenizer);
   
    m_basicClassIndex = -1;
    m_metaClassIndex  = -1;
    m_basicInstances = new FastVector(capacity);
    m_metaInstances  = new FastVector(capacity); 
    readHeader(tokenizer);
  }

  /**
   * Constructor copying all instances and references to
   * the header information from the given set of instances.
   *
   * @param instances the set to be copied
   */
   //basic
  public Instances(Instances dataset) {
     
    this(dataset, dataset.numBasicInstances());

    dataset.copyBasicInstances(0, this, dataset.numBasicInstances());
    
  }

  /**
   * Constructor creating an empty set of instances. Copies references
   * to the header information from the given set of instances. Sets
   * the capacity of the set of instances to 0 if its negative.
   *
   * @param instances the instances from which the header 
   * information is to be taken
   * @param capacity the capacity of the new dataset 
   */
  public Instances(Instances dataset, int capacity) {
    
    if (capacity < 0) {
        capacity = 0;
    }
    
    // Strings only have to be "shallow" copied because
    // they can't be modified.
    m_basicClassIndex = dataset.m_basicClassIndex;
    m_metaClassIndex = dataset.m_metaClassIndex;
    m_RelationName = dataset.m_RelationName;
    
    m_basicAttributes = dataset.m_basicAttributes;
    m_basicInstances = new FastVector(capacity);
    
    m_metaAttributes = dataset.m_metaAttributes;
    m_metaInstances = new FastVector(capacity);
  }

  /**
   * Creates a new set of instances by copying a 
   * subset of another set.
   *
   * @param source the set of instances from which a subset 
   * is to be created
   * @param first the index of the first instance to be copied
   * @param toCopy the number of instances to be copied
   * @exception IllegalArgumentException if first and toCopy are out of range
   */
   //basic
  public Instances(Instances source, int first, int toCopy) {
    
    this(source, toCopy);

    if ((first < 0) || ((first + toCopy) > source.numBasicInstances())) {
      throw new IllegalArgumentException("Parameters first and/or toCopy out "+
                                         "of range");
    }
    source.copyBasicInstances(first, this, toCopy);
  }


  /**
   * Creates an empty set of instances. Uses the given
   * attribute information. Sets the capacity of the set of 
   * instances to 0 if its negative. Given attribute information
   * must not be changed after this constructor has been used.
   *
   * @param name the name of the relation
   * @param attInfo the attribute information
   * @param capacity the capacity of the set
   * @param typ meta or basic
   */
   //um basic und meta zu entscheiden ,fuegen noch eine Paramete 
   // 0 for basic ,1 for meta
  public Instances(String name, FastVector attInfo, int capacity ,int typ) {

    m_RelationName = name;
    m_basicClassIndex = -1;
    m_metaClassIndex  = -1;
    if(typ==0){
    m_basicAttributes = attInfo;
    for (int i = 0; i < numBasicAttributes(); i++) {
      basicAttribute(i).setIndex(i);
    }
    m_basicInstances = new FastVector(capacity);}
    else {
    if (typ==1){
       m_metaAttributes = attInfo;
       for (int i = 0; i < numMetaAttributes(); i++) {
       metaAttribute(i).setIndex(i);
    }
       m_metaInstances = new FastVector(capacity);
    		}
    else 
      throw new IllegalArgumentException("undefined Typ");
    
    		}
    }
  
  /**
   * Create a copy of the structure, but "cleanse" string types (i.e.
   * doesn't contain references to the strings seen in the past).
   *
   * @return a copy of the instance structure.
   */
  public Instances stringBasicFreeStructure() {

    FastVector atts = (FastVector)m_basicAttributes.copy();
    for (int i = 0 ; i < atts.size(); i++) {
      BasicAttribute att = (BasicAttribute)atts.elementAt(i);
      if (att.type() == Attribute.STRING) {
        atts.setElementAt(new BasicAttribute(att.name(), null), i);
      }
    }
    Instances result = new Instances(relationName(), atts, 0,0);
    result.m_basicClassIndex = m_basicClassIndex;
    return result;
  }
  
   /**
   * Create a copy of the structure, but "cleanse" string types (i.e.
   * doesn't contain references to the strings seen in the past).
   *
   * @return a copy of the instance structure.
   */
  public Instances stringMetaFreeStructure() {

    FastVector atts = (FastVector)m_metaAttributes.copy();
    for (int i = 0 ; i < atts.size(); i++) {
      MetaAttribute att = (MetaAttribute)atts.elementAt(i);
      if (att.type() == Attribute.STRING) {
        atts.setElementAt(new MetaAttribute(att.name(), null), i);
      }
    }
    Instances result = new Instances(relationName(), atts, 0,0);
    result.m_metaClassIndex = m_metaClassIndex;
    return result;
  }

  /**
   * Adds one basicInstance to the end of the set. 
   * Shallow copies instance before it is added. Increases the
   * size of the dataset if it is not large enough. Does not
   * check if the instance is compatible with the dataset.
   *
   * @param instance the instance to be added
   */
  public final void addBasicInstance(BasicInstance instance) {

    BasicInstance newInstance = (BasicInstance)instance.copy();

    newInstance.setDataset(this);
    m_basicInstances.addElement(newInstance);
  }

  /**
   * Adds one metaInstance to the end of the set. 
   * Shallow copies instance before it is added. Increases the
   * size of the dataset if it is not large enough. Does not
   * check if the instance is compatible with the dataset.
   *
   * @param instance the instance to be added
   */
  public final void addMetaInstance(MetaInstance instance) {
   
    MetaInstance newInstance = (MetaInstance)instance.copy();
    
    newInstance.setDataset(this);
    m_metaInstances.addElement(newInstance);
  }

  /**
   * Returns an BasicAttribute.
   *
   * @param index the attribute's index
   * @return the attribute at the given position
   */ 
  public final BasicAttribute basicAttribute(int index) {
    
    return (BasicAttribute) m_basicAttributes.elementAt(index);
  }
  
  /**
   * Returns an MetaAttribute.
   *
   * @param index the attribute's index
   * @return the attribute at the given position
   */ 
   public final MetaAttribute metaAttribute(int index) {
    
    return (MetaAttribute) m_metaAttributes.elementAt(index);
  }
  
  
  /**
   * Returns an BasicAttribute given its name. If there is more than
   * one attribute with the same name, it returns the first one.
   * Returns null if the attribute can't be found.
   *
   * @param name the attribute's name
   * @return the attribute with the given name, null if the
   * attribute can't be found
   */ 
  public final BasicAttribute basicAttribute(String name) {
    
    for (int i = 0; i < numBasicAttributes(); i++) {
      if (basicAttribute(i).name().equals(name)) {
	return basicAttribute(i);
      }
    }
    return null;
  }
  
   /**
   * Returns an MetaAttribute given its name. If there is more than
   * one attribute with the same name, it returns the first one.
   * Returns null if the attribute can't be found.
   *
   * @param name the attribute's name
   * @return the attribute with the given name, null if the
   * attribute can't be found 
   */
  public final Attribute metaAttribute(String name) {
    
    for (int i = 0; i < numMetaAttributes(); i++) {
      if (metaAttribute(i).name().equals(name)) {
	  return metaAttribute(i);
      }
    }
    return null;
  }
  

  /**
   * Checks if the given instance is compatible
   * with this dataset. Only looks at the size of
   * the instance and the ranges of the values for 
   * nominal and string attributes.
   *
   * @return true if the instance is compatible with the dataset 
   */
   //basic
  public final boolean checkBasicInstance(BasicInstance instance) {

    if (instance.numAttributes() != numBasicAttributes()) {
      return false;
    }
    for (int i = 0; i < numBasicAttributes(); i++) {
      if (instance.isMissing(i)) {
	continue;
      } else if (basicAttribute(i).isNominal() ||
		 basicAttribute(i).isString()) {
	if (!(Utils.eq(instance.value(i),
		       (double)(int)instance.value(i)))) {
	  return false;
	} else if (Utils.sm(instance.value(i), 0) ||
		   Utils.gr(instance.value(i),
			    basicAttribute(i).numValues())) {
	  return false;
	}
      }
    }
    return true;
  }
	
  /**
   * Returns the class attribute.
   *
   * @return the class attribute
   * @exception UnassignedClassException if the class is not set
   */
  public final BasicAttribute basicClassAttribute() {

    if (m_basicClassIndex < 0) {
      throw new UnassignedClassException("Class index is negative (not set)!");
    }
    return basicAttribute(m_basicClassIndex);
  }
  
   /**
   * Returns the class attribute.
   *
   * @return the class attribute
   * @exception UnassignedClassException if the class is not set
   */
  public final MetaAttribute metaClassAttribute() {

    if (m_metaClassIndex < 0) {
      throw new UnassignedClassException("Class index is negative (not set)!");
    }
    return metaAttribute(m_metaClassIndex);
  }
  

  /**
   * Returns the class basicaAttribute's index. Returns negative number
   * if it's undefined.
   *
   * @return the class index as an integer
   */
  public final int basicClassIndex() {
    
    return m_basicClassIndex;
  }
 
 
  /**
   * Returns the class metaAttribute's index. Returns negative number
   * if it's undefined.
   *
   * @return the class index as an integer
   */
 public final int metaClassIndex() {
    
    return m_metaClassIndex;
  }
 
 
  /**
   * Compactifies the set of instances. Decreases the capacity of
   * the set so that it matches the number of instances in the set.
   */
  public final void compactify() {

    m_basicInstances.trimToSize();
    m_metaInstances.trimToSize();
  }

  /**
   * Removes all basic instances from the set.
   */
   //basic
  public final void delete() {
    
    m_basicInstances = new FastVector();
  }

  /**
   * Removes an basic instance at the given position from the set.
   *
   * @param index the instance's position
   */
   //basic
  public final void delete(int index) {
    
    m_basicInstances.removeElementAt(index);
  }


  /**
   * Deletes an attribute at the given position 
   * (0 to numAttributes() - 1). A deep copy of the attribute
   * information is performed before the attribute is deleted.
   *
   * @param pos the attribute's position
   * @exception IllegalArgumentException if the given index is out of range or the
   * class attribute is being deleted
   */
   //basic
  public void deleteBasicAttributeAt(int position) {
	 
    if ((position < 0) || (position >= m_basicAttributes.size())) {
      throw new IllegalArgumentException("Index out of range");
    }
    if (position == m_basicClassIndex) {
      throw new IllegalArgumentException("Can't delete class attribute");
    }
    freshBasicAttributeInfo();
    if (m_basicClassIndex > position) {
      m_basicClassIndex--;
    }
    m_basicAttributes.removeElementAt(position);
    for (int i = position; i < m_basicAttributes.size(); i++) {
      BasicAttribute current = (BasicAttribute)m_basicAttributes.elementAt(i);
      current.setIndex(current.index() - 1);
    }
    for (int i = 0; i < numBasicInstances(); i++) {
      basicInstance(i).forceDeleteAttributeAt(position); 
    }
  }

  /**
   * Deletes all string attributes in the dataset. A deep copy of the attribute
   * information is performed before an attribute is deleted.
   *
   * @exception IllegalArgumentException if string attribute couldn't be 
   * successfully deleted (probably because it is the class attribute).
   */
   //basic
  public void deleteBasicStringAttributes() {

    int i = 0;
    while (i < m_basicAttributes.size()) {
      if (basicAttribute(i).isString()) {
	deleteBasicAttributeAt(i);
      } else {
	i++;
      }
    }
  }

  /**
   * Removes all instances with missing values for a particular
   * attribute from the dataset.
   *
   * @param attIndex the attribute's index
   */
   //basic
  public final void deleteWithMissingBasicAttribute(int attIndex) {

    FastVector newInstances = new FastVector(numBasicInstances());

    for (int i = 0; i < numBasicInstances(); i++) {
      if (!basicInstance(i).isMissing(attIndex)) {
    	newInstances.addElement(basicInstance(i));
      }
    }
    m_basicInstances = newInstances;
  }


  /**
   * Removes all instances with missing values for a particular
   * attribute from the dataset.
   *
   * @param att the attribute
   */
   //basic
  public final void deleteWithMissingBasicAttribute(Attribute att) {

    deleteWithMissingBasicAttribute(att.index());
  }

  /**
   * Removes all instances with a missing class value
   * from the dataset.
   *
   * @exception UnassignedClassException if class is not set
   */
   //basic
  public final void deleteWithMissingClass() {

    if (m_basicClassIndex < 0) {
      throw new UnassignedClassException("Class index is negative (not set)!");
    }
    deleteWithMissingBasicAttribute(m_basicClassIndex);
  }

  /**
   * Returns an enumeration of all the basic attributes.
   *
   * @return enumeration of all the basic attributes.
   */
  public Enumeration enumerateBasicAttributes() {

    return m_basicAttributes.elements(m_basicClassIndex);
  }


  /**
   * Returns an enumeration of all the meta attributes.
   *
   * @return enumeration of all the meta attributes.
   */
 public Enumeration enumerateMetaAttributes() {

    return m_metaAttributes.elements(m_metaClassIndex);
  }

  /**
   * Returns an enumeration of all basic instances in the dataset.
   *
   * @return enumeration of all basic instances in the dataset
   */
  public final Enumeration enumerateBasicInstances() {

    return m_basicInstances.elements();
  }

  /**
   * Returns an enumeration of all meta instances in the dataset.
   *
   * @return enumeration of all meta instances in the dataset
   */
  public final Enumeration enumerateMetaInstances() {

    return m_metaInstances.elements();
  }
  
  
  /**
   * Checks if two headers are equivalent.
   *
   * @param dataset another dataset
   * @return true if the header of the given dataset is equivalent 
   * to this header
   */
  public final boolean basicEqualHeaders(Instances dataset){

    // Check class and all attributes
    if (m_basicClassIndex != dataset.m_basicClassIndex) {
      return false;
    }
    if (m_basicAttributes.size() != dataset.m_basicAttributes.size()) {
      return false;
    }
    for (int i = 0; i < m_basicAttributes.size(); i++) {
      if (!(basicAttribute(i).equals(dataset.basicAttribute(i)))) {
	return false;
      }
    }
    return true;
  }
  
  
  /**
   * Checks if two headers are equivalent.
   *
   * @param dataset another dataset
   * @return true if the header of the given dataset is equivalent 
   * to this header
   */
  public final boolean metaEqualHeaders(Instances dataset){

    // Check class and all attributes
    if (m_metaClassIndex != dataset.m_metaClassIndex) {
      return false;
    }
    if (m_metaAttributes.size() != dataset.m_metaAttributes.size()) {
      return false;
    }
    for (int i = 0; i < m_metaAttributes.size(); i++) {
      if (!(metaAttribute(i).equals(dataset.metaAttribute(i)))) {
	return false;
      }
    }
    return true;
  }
 
  /**
   * Returns the first basic instance in the set.
   *
   * @return the first basic instance in the set
   */
   //basic
  public final Instance firstInstance() {
    
    return (Instance)m_basicInstances.firstElement();
  }
 
  /**
   * Inserts an BasicAttribute at the given position (0 to 
   * numAttributes()) and sets all values to be missing.
   * Shallow copies the attribute before it is inserted, and performs
   * a deep copy of the existing attribute information.
   *
   * @param att the attribute to be inserted
   * @param pos the attribute's position
   * @exception IllegalArgumentException if the given index is out of range
   */
  public void insertBasicAttributeAt(BasicAttribute att, int position) {
	 
    if ((position < 0) ||
	(position > m_basicAttributes.size())) {
      throw new IllegalArgumentException("Index out of range");
    }
    att = (BasicAttribute)att.copy();
    freshBasicAttributeInfo();
    att.setIndex(position);
    m_basicAttributes.insertElementAt(att, position);
    for (int i = position + 1; i < m_basicAttributes.size(); i++) {
      BasicAttribute current = (BasicAttribute)m_basicAttributes.elementAt(i);
      current.setIndex(current.index() + 1);
    }
    for (int i = 0; i < numBasicInstances(); i++) {
      basicInstance(i).forceInsertAttributeAt(position);
    }
    if (m_basicClassIndex >= position) {
      m_metaClassIndex++;
    }
  }

 /**
   * Inserts an MetaAttribute at the given position (0 to 
   * numAttributes()) and sets all values to be missing.
   * Shallow copies the attribute before it is inserted, and performs
   * a deep copy of the existing attribute information.
   *
   * @param att the attribute to be inserted
   * @param pos the attribute's position
   * @exception IllegalArgumentException if the given index is out of range
   */
  public void insertMetaAttributeAt(MetaAttribute att, int position) {
	 
    if ((position < 0) ||
	(position > m_metaAttributes.size())) {
      throw new IllegalArgumentException("Index out of range");
    }
    att = (MetaAttribute)att.copy();
    freshBasicAttributeInfo();
    att.setIndex(position);
    m_metaAttributes.insertElementAt(att, position);
    for (int i = position + 1; i < m_metaAttributes.size(); i++) {
      MetaAttribute current = (MetaAttribute)m_metaAttributes.elementAt(i);
      current.setIndex(current.index() + 1);
    }
    for (int i = 0; i < numMetaInstances(); i++) {
      metaInstance(i).forceInsertAttributeAt(position);
    }
    if (m_metaClassIndex >= position) {
      m_metaClassIndex++;
    }
  }
  
  /**
   * Returns the BasicInstance at the given position.
   *
   * @param index the instance's index
   * @return the instance at the given position
   */
  public final BasicInstance  basicInstance(int index) {

    return (BasicInstance)m_basicInstances.elementAt(index);
  }
  
  /**
   * Returns the BasicInstance at the given position.
   *
   * @param index the instance's index
   * @return the instance at the given position
   */
  public final MetaInstance metaInstance(int index) {

    return (MetaInstance)m_metaInstances.elementAt(index);
  }
  
  /**
   * Returns the last BasicInstance in the set.
   *
   * @return the last instance in the set
   */
  public final BasicInstance lastBasicInstance() {
    
    return (BasicInstance)m_basicInstances.lastElement();
  }

 /**
   * Returns the last MetaInstance in the set.
   *
   * @return the last instance in the set
   */
  public final MetaInstance lastMetaInstance() {
    
    return (MetaInstance)m_metaInstances.lastElement();
  }


  /**
   * Returns the mean (mode) for a numeric (nominal) attribute as
   * a floating-point value. Returns 0 if the attribute is neither nominal nor 
   * numeric. If all values are missing it returns zero.
   *
   * @param attIndex the attribute's index
   * @return the mean or the mode
   */
   //basic
  public final double meanOrMode(int attIndex) {

    double result, found;
    int [] counts;

    if (basicAttribute(attIndex).isReal()||basicAttribute(attIndex).isInteger()) {
      result = found = 0;
      for (int j = 0; j < numBasicInstances(); j++) {
	if (!basicInstance(j).isMissing(attIndex)) {
	  found += basicInstance(j).weight();
	  result += basicInstance(j).weight()*basicInstance(j).value(attIndex);
	}
      }
      if (Utils.eq(found, 0)) {
	return 0;
      } else {
	return result / found;
      }
    } else if (basicAttribute(attIndex).isNominal()) {
      counts = new int[basicAttribute(attIndex).numValues()];
      for (int j = 0; j < numBasicInstances(); j++) {
	if (!basicInstance(j).isMissing(attIndex)) {
	  counts[(int) basicInstance(j).value(attIndex)] += basicInstance(j).weight();
	}
      }
      return (double)Utils.maxIndex(counts);
    } else {
      return 0;
    }
  }

  /**
   * Returns the mean (mode) for a numeric (nominal) attribute as a
   * floating-point value.  Returns 0 if the attribute is neither
   * nominal nor numeric.  If all values are missing it returns zero.
   *
   * @param att the attribute
   * @return the mean or the mode 
   */
  public final double meanOrMode(Attribute att) {

    return meanOrMode(att.index());
  }

  /**
   * Returns the number of basic attributes.
   *
   * @return the number of basic attributes as an integer
   */
  public final int numBasicAttributes() {

    return m_basicAttributes.size();
  }

/**
   * Returns the number of meta attributes.
   *
   * @return the number of meta attributes as an integer
   */
  public final int numMetaAttributes() {

    return m_metaAttributes.size();
  }

  /**
   * Returns the number of class labels.
   *
   * @return the number of class labels as an integer if the class 
   * attribute is nominal, 1 otherwise.
   * @exception UnassignedClassException if the class is not set
   */
  public final int numBasicClasses() {
    
    if (m_basicClassIndex < 0) {
      throw new UnassignedClassException("Class index is negative (not set)!");
    }
    if (!basicClassAttribute().isNominal()) {
      return 1;
    } else {
      return basicClassAttribute().numValues();
    }
  }


 /**
   * Returns the number of class labels.
   *
   * @return the number of class labels as an integer if the class 
   * attribute is nominal, 1 otherwise.
   * @exception UnassignedClassException if the class is not set
   */
  public final int numMetaClasses() {
    
    if (m_metaClassIndex < 0) {
      throw new UnassignedClassException("Class index is negative (not set)!");
    }
    if (!metaClassAttribute().isNominal()) {
      return 1;
    } else {
      return metaClassAttribute().numValues();
    }
  }
  
 /**
   * Gets the value of all instances in this dataset for a particular
   * attribute. Useful in conjunction with Utils.sort to allow iterating
   * through the dataset in sorted order for some attribute.
   *
   * @param index the index of the attribute.
   * @return an array containing the value of the desired attribute for
   * each instance in the dataset. 
   */
  public double [] basicAttributeToDoubleArray(int index) {

    double [] result = new double[numBasicInstances()];
    for (int i = 0; i < result.length; i++) {
      result[i] = basicInstance(i).value(index);
    }
    return result;
  }
  
  /**
   * Gets the value of all instances in this dataset for a particular
   * attribute. Useful in conjunction with Utils.sort to allow iterating
   * through the dataset in sorted order for some attribute.
   *
   * @param index the index of the attribute.
   * @return an array containing the value of the desired attribute for
   * each instance in the dataset. 
   */
  public double [] metaAttributeToDoubleArray(int index) {

    double [] result = new double[numMetaInstances()];
    for (int i = 0; i < result.length; i++) {
      result[i] = metaInstance(i).value(index);
    }
    return result;
  }
  
  /**
   * Returns the number of distinct values of a given attribute.
   * Returns the number of instances if the attribute is a
   * string attribute. The value 'missing' is not counted.
   *
   * @param attIndex the attribute
   * @return the number of distinct values of a given attribute
   */
   //basic
  public final int numDistinctValues(int attIndex) {

    if (basicAttribute(attIndex).isReal()||basicAttribute(attIndex).isInteger()) {
      double [] attVals = basicAttributeToDoubleArray(attIndex);
      int [] sorted = Utils.sort(attVals);
      double prev = 0;
      int counter = 0;
      for (int i = 0; i < sorted.length; i++) {
	BasicInstance current = basicInstance(sorted[i]);
	if (current.isMissing(attIndex)) {
	  break;
	}
	if ((i == 0) || 
	    Utils.gr(current.value(attIndex), prev)) {
	  prev = current.value(attIndex);
	  counter++;
	}
      }
      return counter;
    } else {
      return basicAttribute(attIndex).numValues();
    }
  }

  /**
   * Returns the number of distinct values of a given attribute.
   * Returns the number of instances if the attribute is a
   * string attribute. The value 'missing' is not counted.
   *
   * @param att the attribute
   * @return the number of distinct values of a given attribute
   */
  public final int numDistinctValues(Attribute att) {

    return numDistinctValues(att.index());
  }
  
  /**
   * Returns the number of basic instances in the dataset.
   *
   * @return the number of meta instances in the dataset as an integer
   */
  public final int numBasicInstances() {

    return m_basicInstances.size();
  }
  
  /**
   * Returns the number of meta instances in the dataset.
   *
   * @return the number of meta instances in the dataset as an integer
   */
  public final int numMetaInstances() {

    return m_metaInstances.size();
  }


  /**
   * Shuffles the instances in the set so that they are ordered 
   * randomly.
   *
   * @param random a random number generator
   */
   //basic
  public final void randomize(Random random) {

    for (int j = numBasicInstances() - 1; j > 0; j--)
      swap(j,(int)(random.nextDouble()*(double)j));
  }

  /**
   * Reads a single instance from the reader and appends it
   * to the dataset.  Automatically expands the dataset if it
   * is not large enough to hold the instance. This method does
   * not check for carriage return at the end of the line.
   *
   * @param reader the reader 
   * @return false if end of file has been reached
   * @exception IOException if the information is not read 
   * successfully
   */ 
  public final boolean readInstance(Reader reader) 
       throws IOException {

    StreamTokenizer tokenizer = new StreamTokenizer(reader);
    
    initTokenizer(tokenizer);
    return getInstance(tokenizer, false);
  }    

  /**
   * Returns the relation's name.
   *
   * @return the relation's name as a string
   */
  public final String relationName() {

    return m_RelationName;
  }

  
  /**
   * Creates a new dataset of the same size using random sampling
   * with replacement.
   *
   * @param random a random number generator
   * @return the new dataset
   */
   //basic
  public final Instances resample(Random random) {

    Instances newData = new Instances(this, numBasicInstances());
    while (newData.numBasicInstances() < numBasicInstances()) {
      int i = (int) (random.nextDouble() * (double) numBasicInstances());
      newData.addBasicInstance(basicInstance(i));
    }
    return newData;
  }

  /**
   * Creates a new dataset of the same size using random sampling
   * with replacement according to the current instance weights. The
   * weights of the instances in the new dataset are set to one.
   *
   * @param random a random number generator
   * @return the new dataset
   */
   //basic
  public final Instances resampleWithWeights(Random random) {

    double [] weights = new double[numBasicInstances()];
    boolean foundOne = false;
    for (int i = 0; i < weights.length; i++) {
      weights[i] = basicInstance(i).weight();
      if (!Utils.eq(weights[i], weights[0])) {
        foundOne = true;
      }
    }
    if (foundOne) {
      return resampleWithWeights(random, weights);
    } else {
      return new Instances(this);
    }
  }


  /**
   * Creates a new dataset of the same size using random sampling
   * with replacement according to the given weight vector. The
   * weights of the instances in the new dataset are set to one.
   * The length of the weight vector has to be the same as the
   * number of instances in the dataset, and all weights have to
   * be positive.
   *
   * @param random a random number generator
   * @param weights the weight vector
   * @return the new dataset
   * @exception IllegalArgumentException if the weights array is of the wrong
   * length or contains negative weights.
   */
   //basic
  public final Instances resampleWithWeights(Random random, 
					     double[] weights) {

    if (weights.length != numBasicInstances()) {
      throw new IllegalArgumentException("weights.length != numInstances.");
    }
    Instances newData = new Instances(this, numBasicInstances());
    double[] probabilities = new double[numBasicInstances()];
    double sumProbs = 0, sumOfWeights = Utils.sum(weights);
    for (int i = 0; i < numBasicInstances(); i++) {
      sumProbs += random.nextDouble();
      probabilities[i] = sumProbs;
    }
    Utils.normalize(probabilities, sumProbs / sumOfWeights);

    // Make sure that rounding errors don't mess things up
    probabilities[numBasicInstances() - 1] = sumOfWeights;
    int k = 0; int l = 0;
    sumProbs = 0;
    while ((k < numBasicInstances() && (l < numBasicInstances()))) {
      if (weights[l] < 0) {
	throw new IllegalArgumentException("Weights have to be positive.");
      }
      sumProbs += weights[l];
      while ((k < numBasicInstances()) &&
	     (probabilities[k] <= sumProbs)) { 
	newData.addBasicInstance(basicInstance(l));
	newData.basicInstance(k).setWeight(1);
	k++;
      }
      l++;
    }
    return newData;
  }

  /** 
   * Sets the class attribute.
   *
   * @param att attribute to be the class
   */
  public final void setBasicClass(Attribute att) {

    m_basicClassIndex = att.index();
  }

   /** 
   * Sets the class attribute.
   *
   * @param att attribute to be the class
   */
 public final void setMetaClass(Attribute att) {

    m_metaClassIndex = att.index();
  }
  
  /** 
   * Sets the class index of the set.
   * If the class index is negative there is assumed to be no class.
   * (ie. it is undefined)
   *
   * @param classIndex the new class index
   * @exception IllegalArgumentException if the class index is too big or < 0
   */
  public final void setBasicClassIndex(int classIndex) {

    if (classIndex >= numBasicAttributes()) {
      throw new IllegalArgumentException("Invalid class index: " + classIndex);
    }
    m_basicClassIndex = classIndex;
  }

  /** 
   * Sets the class index of the set.
   * If the class index is negative there is assumed to be no class.
   * (ie. it is undefined)
   *
   * @param classIndex the new class index
   * @exception IllegalArgumentException if the class index is too big or < 0
   */
  public final void setMetaClassIndex(int classIndex) {

    if (classIndex >= numMetaAttributes()) {
      throw new IllegalArgumentException("Invalid class index: " + classIndex);
    }
    m_metaClassIndex = classIndex;
  }
  
  /**
   * Sets the relation's name.
   *
   * @param newName the new relation name.
   */
  public final void setRelationName(String newName) {
    
    m_RelationName = newName;
  }

  /**
   * Stratifies a set of instances according to its class values 
   * if the class attribute is nominal (so that afterwards a 
   * stratified cross-validation can be performed).
   *
   * @param numFolds the number of folds in the cross-validation
   * @exception UnassignedClassException if the class is not set
   */
   //basic
  public final void stratify(int numFolds) {
    
    if (numFolds <= 0) {
      throw new IllegalArgumentException("Number of folds must be greater than 1");
    }
    if (m_basicClassIndex < 0) {
      throw new UnassignedClassException("Class index is negative (not set)!");
    }
    if (basicClassAttribute().isNominal()) {

      // sort by class
      int index = 1;
      while (index < numBasicInstances()) {
	BasicInstance instance1 = basicInstance(index - 1);
	for (int j = index; j < numBasicInstances(); j++) {
	  BasicInstance instance2 = basicInstance(j);
	  if ((instance1.classValue() == instance2.classValue()) ||
	      (instance1.classIsMissing() && 
	       instance2.classIsMissing())) {
	    swap(index,j);
	    index++;
	  }
	}
	index++;
      }
      stratStep(numFolds);
    }
  }
 
  /**
   * Computes the sum of all the instances' weights.
   *
   * @return the sum of all the instances' weights as a double
   */
   //basic
  public final double sumOfWeights() {
    
    double sum = 0;

    for (int i = 0; i < numBasicInstances(); i++) {
      sum += basicInstance(i).weight();
    }
    return sum;
  }

  /**
   * Creates the test set for one fold of a cross-validation on 
   * the dataset.
   *
   * @param numFolds the number of folds in the cross-validation. Must
   * be greater than 1.
   * @param numFold 0 for the first fold, 1 for the second, ...
   * @return the test set as a set of weighted instances
   * @exception IllegalArgumentException if the number of folds is less than 2
   * or greater than the number of instances.
   */
   //basic
  public Instances testCV(int numFolds, int numFold) {

    int numInstForFold, first, offset;
    Instances test;
    
    if (numFolds < 2) {
      throw new IllegalArgumentException("Number of folds must be at least 2!");
    }
    if (numFolds > numBasicInstances()) {
      throw new IllegalArgumentException("Can't have more folds than instances!");
    }
    numInstForFold = numBasicInstances() / numFolds;
    if (numFold < numBasicInstances() % numFolds){
      numInstForFold++;
      offset = numFold;
    }else
      offset = numBasicInstances() % numFolds;
    test = new Instances(this, numInstForFold);
    first = numFold * (numBasicInstances() / numFolds) + offset;
    copyBasicInstances(first, test, numInstForFold);
    return test;
  }
 
  /**
   * Returns the dataset as a string in ARFF format. Strings
   * are quoted if they contain whitespace characters, or if they
   * are a question mark.
   *
   * @return the dataset in ARFF format as a string
   */
  public final String toString() {
    
    StringBuffer text = new StringBuffer();
    
    text.append("@relation " + Utils.quote(m_RelationName) + "\n\n");
     
    for (int i = 0; i < numMetaAttributes(); i++) {
      text.append(metaAttribute(i) + "\n");
    }
     text.append("\n");
     
    for (int i = 0; i < numMetaInstances(); i++) {
      text.append(metaInstance(i));
      if (i < numMetaInstances() - 1) {
	    text.append('\n');
      }
    }
    
    for (int i = 0; i < numBasicAttributes(); i++) {
      text.append(basicAttribute(i) + "\n");
    }
    text.append("\n@data\n");
    for (int i = 0; i < numBasicInstances(); i++) {
      text.append(basicInstance(i));
      if (i < numBasicInstances() - 1) {
	text.append('\n');
      }
    }
    return text.toString();
  }

  /**
   * Creates the training set for one fold of a cross-validation 
   * on the dataset.
   *
   * @param numFolds the number of folds in the cross-validation. Must
   * be greater than 1.
   * @param numFold 0 for the first fold, 1 for the second, ...
   * @return the training set as a set of weighted 
   * instances
   * @exception IllegalArgumentException if the number of folds is less than 2
   * or greater than the number of instances.
   */
  public Instances trainCV(int numFolds, int numFold) {

    int numInstForFold, first, offset;
    Instances train;
 
    if (numFolds < 2) {
      throw new IllegalArgumentException("Number of folds must be at least 2!");
    }
    if (numFolds > numBasicInstances()) {
      throw new IllegalArgumentException("Can't have more folds than instances!");
    }
    numInstForFold = numBasicInstances() / numFolds;
    if (numFold < numBasicInstances() % numFolds) {
      numInstForFold++;
      offset = numFold;
    }else
      offset = numBasicInstances() % numFolds;
    train = new Instances(this, numBasicInstances() - numInstForFold);
    first = numFold * (numBasicInstances() / numFolds) + offset;
    copyBasicInstances(0, train, first);
    copyBasicInstances(first + numInstForFold, train,
		  numBasicInstances() - first - numInstForFold);

    return train;
  }

 
  /**
   * Calculates summary statistics on the values that appear in this
   * set of instances for a specified attribute.
   *
   * @param index the index of the attribute to summarize.
   * @return an AttributeStats object with it's fields calculated.
   */
   
  public AttributeStats basicAttributeStats(int index) {

    AttributeStats result = new AttributeStats();
    if (basicAttribute(index).isNominal()) {
      result.nominalCounts = new int [basicAttribute(index).numValues()];
    }
    if (basicAttribute(index).isInteger()||basicAttribute(index).isReal()) {
      result.numericStats = new weka.experiment.Stats();
    }
    result.totalCount = numBasicInstances();

    double [] attVals = basicAttributeToDoubleArray(index);
    int [] sorted = Utils.sort(attVals);
    int currentCount = 0;
    double prev = Instance.missingValue();
    for (int j = 0; j < numBasicInstances(); j++) {
      BasicInstance current = basicInstance(sorted[j]);
      if (current.isMissing(index)) {
	result.missingCount = numBasicInstances() - j;
	break;
      }
      if (Utils.eq(current.value(index), prev)) {
	currentCount++;
      } else {
	result.addDistinct(prev, currentCount);
	currentCount = 1;
	prev = current.value(index);
      }
    }
    result.addDistinct(prev, currentCount);
    result.distinctCount--; // So we don't count "missing" as a value 
    return result;
  }
  
  
  /**
   * Calculates summary statistics on the values that appear in this
   * set of instances for a specified attribute.
   *
   * @param index the index of the attribute to summarize.
   * @return an AttributeStats object with it's fields calculated.
   */
  public AttributeStats metaAttributeStats(int index) {

    AttributeStats result = new AttributeStats();
    if (metaAttribute(index).isNominal()) {
      result.nominalCounts = new int [metaAttribute(index).numValues()];
    }
    if (metaAttribute(index).isInteger()||metaAttribute(index).isReal()) {
      result.numericStats = new weka.experiment.Stats();
    }
    result.totalCount = numMetaInstances();

    double [] attVals = metaAttributeToDoubleArray(index);
    int [] sorted = Utils.sort(attVals);
    int currentCount = 0;
    double prev = Instance.missingValue();
    for (int j = 0; j < numMetaInstances(); j++) {
      MetaInstance current = metaInstance(sorted[j]);
      if (current.isMissing(index)) {
	result.missingCount = numMetaInstances() - j;
	break;
      }
      if (Utils.eq(current.value(index), prev)) {
	currentCount++;
      } else {
	result.addDistinct(prev, currentCount);
	currentCount = 1;
	prev = current.value(index);
      }
    }
    result.addDistinct(prev, currentCount);
    result.distinctCount--; // So we don't count "missing" as a value 
    return result;
  }
  
  /**
   * Generates a string summarizing the set of instances. Gives a breakdown
   * for each attribute indicating the number of missing/discrete/unique
   * values and other information.
   *
   * @return a string summarizing the dataset
   */
  public String toSummaryString() {
  	
    StringBuffer result = new StringBuffer();
 
    result.append("Relation Name:  ").append(relationName()).append('\n');
    result.append("Num Basic Instances:  ").append(numBasicInstances()).append('\n');
    result.append("Num Basic Attributes: ").append(numBasicAttributes()).append('\n');
    result.append('\n');

    result.append(Utils.padLeft("", 5)).append(Utils.padRight("basicAttribute Name", 25));
    result.append(Utils.padLeft("Type", 5)).append(Utils.padLeft("Nom", 5));
    result.append(Utils.padLeft("Int", 5)).append(Utils.padLeft("Real", 5));
    result.append(Utils.padLeft("Missing", 12));
    result.append(Utils.padLeft("Unique", 12));
    result.append(Utils.padLeft("Dist", 6)).append('\n');
    for (int i = 0; i < numBasicAttributes(); i++) {
      BasicAttribute a = basicAttribute(i);
      AttributeStats as = basicAttributeStats(i);
      result.append(Utils.padLeft("" + (i + 1), 4)).append(' ');
      result.append(Utils.padRight(a.name(), 25)).append(' ');
      long percent;
      switch (a.type()) {
      case Attribute.NOMINAL:
	result.append(Utils.padLeft("Nom", 4)).append(' ');
	percent = Math.round(100.0 * as.intCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * as.realCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
	 case Attribute.NUMERIC:
	result.append(Utils.padLeft("Num", 4)).append(' ');
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * as.intCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	percent = Math.round(100.0 * as.realCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
      case Attribute.REAL:
	result.append(Utils.padLeft("Rea", 4)).append(' ');
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * as.intCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	percent = Math.round(100.0 * as.realCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
	  case Attribute.INTEGER:
	result.append(Utils.padLeft("Int", 4)).append(' ');
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * as.intCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	percent = Math.round(100.0 * as.realCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
      case Attribute.STRING:
	result.append(Utils.padLeft("Str", 4)).append(' ');
	percent = Math.round(100.0 * as.intCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * as.realCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
      default:
	result.append(Utils.padLeft("???", 4)).append(' ');
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * as.intCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	percent = Math.round(100.0 * as.realCount / as.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
      }
      result.append(Utils.padLeft("" + as.missingCount, 5)).append(" /");
      percent = Math.round(100.0 * as.missingCount / as.totalCount);
      result.append(Utils.padLeft("" + percent, 3)).append("% ");
      result.append(Utils.padLeft("" + as.uniqueCount, 5)).append(" /");
      percent = Math.round(100.0 * as.uniqueCount / as.totalCount);
      result.append(Utils.padLeft("" + percent, 3)).append("% ");
      result.append(Utils.padLeft("" + as.distinctCount, 5)).append(' ');
      result.append('\n');
    }
    result.append('\n');
    
    result.append("Num Meta Instances:  ").append(numMetaInstances()).append('\n');
    result.append("Num Meta Attributes: ").append(numMetaAttributes()).append('\n');
    result.append('\n');
    
    result.append(Utils.padLeft("", 5)).append(Utils.padRight("metaAttribute Name", 25));
    result.append(Utils.padLeft("Type", 5)).append(Utils.padLeft("Nom", 5));
    result.append(Utils.padLeft("Int", 5)).append(Utils.padLeft("Real", 5));
    result.append(Utils.padLeft("Missing", 12));
    result.append(Utils.padLeft("Unique", 12));
    result.append(Utils.padLeft("Dist", 6)).append('\n');
    
    for (int i = 0; i < numMetaAttributes(); i++) {
      MetaAttribute b = metaAttribute(i);
      AttributeStats ass = metaAttributeStats(i);
      result.append(Utils.padLeft("" + (i + 1), 4)).append(' ');
      result.append(Utils.padRight(b.name(), 25)).append(' ');
      long percent;
      switch (b.type()) {
      case Attribute.NOMINAL:
	result.append(Utils.padLeft("Nom", 4)).append(' ');
	percent = Math.round(100.0 * ass.intCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * ass.realCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
      case Attribute.NUMERIC:
	result.append(Utils.padLeft("Num", 4)).append(' ');
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * ass.intCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	percent = Math.round(100.0 * ass.realCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
      case Attribute.REAL:
	result.append(Utils.padLeft("Rea", 4)).append(' ');
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * ass.intCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	percent = Math.round(100.0 * ass.realCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
	  case Attribute.INTEGER:
	result.append(Utils.padLeft("Int", 4)).append(' ');
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * ass.intCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	percent = Math.round(100.0 * ass.realCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
      case Attribute.STRING:
	result.append(Utils.padLeft("Str", 4)).append(' ');
	percent = Math.round(100.0 * ass.intCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * ass.realCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
      default:
	result.append(Utils.padLeft("???", 4)).append(' ');
	result.append(Utils.padLeft("" + 0, 3)).append("% ");
	percent = Math.round(100.0 * ass.intCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	percent = Math.round(100.0 * ass.realCount / ass.totalCount);
	result.append(Utils.padLeft("" + percent, 3)).append("% ");
	break;
      }
      result.append(Utils.padLeft("" + ass.missingCount, 5)).append(" /");
      percent = Math.round(100.0 * ass.missingCount / ass.totalCount);
      result.append(Utils.padLeft("" + percent, 3)).append("% ");
      result.append(Utils.padLeft("" + ass.uniqueCount, 5)).append(" /");
      percent = Math.round(100.0 * ass.uniqueCount / ass.totalCount);
      result.append(Utils.padLeft("" + percent, 3)).append("% ");
      result.append(Utils.padLeft("" + ass.distinctCount, 5)).append(' ');
      result.append('\n');
      }
      
    return result.toString();
  }
  
  /**
   * Reads a single instance using the tokenizer and appends it
   * to the dataset. Automatically expands the dataset if it
   * is not large enough to hold the instance.
   *
   * @param tokenizer the tokenizer to be used
   * @param flag if method should test for carriage return after 
   * each instance
   * @return false if end of file has been reached
   * @exception IOException if the information is not read 
   * successfully
   */ 
  protected boolean getInstance(StreamTokenizer tokenizer, 
				boolean flag) 
       throws IOException {
    
    // Check if any basic attributes have been declared.
    if (m_basicAttributes.size() == 0) {
      errms(tokenizer,"no header information available");
    }
    // Check if end of file reached.
     getFirstToken(tokenizer);
    if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
      return false;
    }
   
      return getBasicInstanceFull(tokenizer, flag);
    }
  
  /**
   * Reads a single basic instance using the tokenizer and appends it
   * to the dataset. Automatically expands the dataset if it
   * is not large enough to hold the basic instance.
   *
   * @param tokenizer the tokenizer to be used
   * @param flag if method should test for carriage return after 
   * each instance
   * @return false if end of file has been reached
   * @exception IOException if the information is not read 
   * successfully */
   protected boolean getBasicInstanceFull(StreamTokenizer tokenizer, 
				    boolean flag) 
       throws IOException {

    double[] instance = new double[numBasicAttributes()];
    int index;
    
    // Get values for all attributes.
    for (int i = 0; i < numBasicAttributes(); i++){
      
      // Get next token
      if (i > 0) {
	getNextToken(tokenizer);
      }
            
      // Check if value is missing.
      if  (tokenizer.ttype == '?') {
	instance[i] = Instance.missingValue();
      } else {

	// Check if token is valid.
	if (tokenizer.ttype != StreamTokenizer.TT_WORD) {
	  errms(tokenizer,"not a valid value");
	}
	if (basicAttribute(i).isNominal()) {
	  
	  // Check if value appears in header.
	  index = basicAttribute(i).indexOfValue(tokenizer.sval);
	  if (index == -1) {
	    errms(tokenizer,"nominal value not declared in header");
	  }
	  instance[i] = (double)index;
	}
	
	 //Check if value is really a number
	else if(basicAttribute(i).isReal()||basicAttribute(i).isInteger()){
	  try{
	    instance[i] = Double.valueOf(tokenizer.sval).
	      doubleValue();
	  } catch (NumberFormatException e) {
	    errms(tokenizer,"number expected");
	  }	
	
	  if (basicAttribute(i).isInteger()) {
		
	// Check if value is really a integer
	BigDecimal number = new BigDecimal(tokenizer.sval);
    int temp = number.intValue();
    String numberStr = String.valueOf(number);
    if(!numberStr.equals(String.valueOf(temp)))   
  	 errms(tokenizer, "integer expected");
	
	}} else { 
	  instance[i] = basicAttribute(i).addStringValue(tokenizer.sval);
	}
      }
    }
    if (flag) {
      getLastToken(tokenizer,true);
    }
      
    // Add instance to dataset
    addBasicInstance(new BasicInstance(1, instance));
    return true;
  }
   
  /**
   * Reads a single meta instance using the tokenizer and appends it
   * to the dataset. Automatically expands the dataset if it
   * is not large enough to hold the meta instance.
   *
   * @param tokenizer the tokenizer to be used
   * @param flag if method should test for carriage return after 
   * each instance
   * @return false if end of file has been reached
   * @exception IOException if the information is not read 
   * successfully
   */
 protected boolean getMetaInstanceFull(StreamTokenizer tokenizer, 
				    boolean flag) 
       throws IOException {

    double[] instance = new double[numMetaAttributes()];
    int index;
    
     getNextToken(tokenizer);
     metaDataNames.add(tokenizer.sval);

    for (int i = 0; i < numMetaAttributes(); i++){
      
      // Get next token
    
    	getNextToken(tokenizer);
      
      // Check if value is missing.
      if  (tokenizer.ttype == '?') {
    	instance[i] = Instance.missingValue();
      } else {

   //	Check if token is valid.
	if (tokenizer.ttype != StreamTokenizer.TT_WORD) {
	  errms(tokenizer,"not a valid value");
	}
	
	if (metaAttribute(i).isNominal()) {
	  
	  // Check if value appears in header.
	  index = metaAttribute(i).indexOfValue(tokenizer.sval);
	  if (index == -1) {
	    errms(tokenizer,"nominal value not declared in header");
	  }
	  instance[i] = (double)index;
	} 
	
	//Check if value is really a number
	else if (metaAttribute(i).isReal()||metaAttribute(i).isInteger()) {
	   try{
	    instance[i] = Double.valueOf(tokenizer.sval).
	      doubleValue();
	  } catch (NumberFormatException e) {
	    errms(tokenizer,"number expected");	
	}
	
	  if (metaAttribute(i).isInteger()) {
	  
	  // Check if value is really a integer
	
	BigDecimal number = new BigDecimal(tokenizer.sval);
    int temp = number.intValue();
    String numberStr = String.valueOf(number);
    if(!numberStr.equals(String.valueOf(temp)))   
  	 errms(tokenizer, "integer expected");
	}} else { 
	  instance[i] = metaAttribute(i).addStringValue(tokenizer.sval);
	}
      }}
    
    if (flag) {
      getLastToken(tokenizer,true);
    }
    
    // Add instance to dataset
    addMetaInstance(new MetaInstance(1, instance));
    
    return true;
  }

  /**
   * Reads and stores header of an ARFF file.
   *
   * @param tokenizer the stream tokenizer
   * @exception IOException if the information is not read 
   * successfully
   */ 
   protected void readHeader(StreamTokenizer tokenizer) 
     throws IOException{
    
    String attributeName;
    
    HashSet metaAttributeNames = new HashSet();
     
    FastVector metaAttributeValues, basicAttributeValues;
    int i;

    // Get name of relation.
    getFirstToken(tokenizer);
    if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
      errms(tokenizer,"premature end of file");
    }
    if (tokenizer.sval.equalsIgnoreCase("@relation")){
      getNextToken(tokenizer);
      m_RelationName = tokenizer.sval;
      getLastToken(tokenizer,false);
    } else {
      errms(tokenizer,"keyword @relation expected");
    }

    // Create vectors to hold information temporarily.
    m_basicAttributes = new FastVector();
    m_metaAttributes = new FastVector();

    // For parsing meta-attribute and meta-data comment lines should be
    // processed and the comment char is deaktivated.
    tokenizer.ordinaryChar('%');
 
    // Get meta attribute declarations.
      getFirstToken(tokenizer);
      if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
      errms(tokenizer,"premature end of file");
    }
    
     while (tokenizer.sval.equalsIgnoreCase("@meta-attribute")) {

      // Get attribute name.
      getNextToken(tokenizer);
      attributeName = tokenizer.sval;
      getNextToken(tokenizer);
      metaAttributeNames.add(attributeName);

      // Check if attribute is nominal.
      if (tokenizer.ttype == StreamTokenizer.TT_WORD) {

	// Attribute is real, integer, or string.
	if (tokenizer.sval.equalsIgnoreCase("real") ||
	    tokenizer.sval.equalsIgnoreCase("numeric")) {
	  m_metaAttributes.addElement(new MetaAttribute(attributeName,false,
						 numMetaAttributes()));
	  readTillEOL(tokenizer);
	}else if( tokenizer.sval.equalsIgnoreCase("integer")){
	 m_metaAttributes.addElement(new MetaAttribute(attributeName,true,
						 numMetaAttributes()));
	  readTillEOL(tokenizer);	
	}
	 else if (tokenizer.sval.equalsIgnoreCase("string")) {
	  m_metaAttributes.
	    addElement(new MetaAttribute(attributeName, null,
				     numMetaAttributes()));
	  readTillEOL(tokenizer);
	} else {
	  errms(tokenizer,"no valid attribute type or invalid "+
		"enumeration");
	}
      } else {

	// Attribute is nominal.
	metaAttributeValues = new FastVector();
	tokenizer.pushBack();
	
	// Get values for nominal attribute.
	if (tokenizer.nextToken() != '{') {
	  errms(tokenizer,"{ expected at beginning of enumeration");
	}
	while (tokenizer.nextToken() != '}') {
	  if (tokenizer.ttype == StreamTokenizer.TT_EOL) {
	    errms(tokenizer,"} expected at end of enumeration");
	  } else {
	    metaAttributeValues.addElement(tokenizer.sval);
	  }
	}
	if (metaAttributeValues.size() == 0) {
	  errms(tokenizer,"no nominal values found");
	}
	m_metaAttributes.addElement(new MetaAttribute(attributeName, metaAttributeValues,
				   numBasicAttributes()));
      }
      getLastToken(tokenizer,false);
      getFirstToken(tokenizer);
      if (tokenizer.ttype == StreamTokenizer.TT_EOF)
	      errms(tokenizer,"premature end of file");
    }
    
     //read @meta-data
    while(tokenizer.sval.equalsIgnoreCase("@meta-data")){
    	
    	 getMetaInstanceFull(tokenizer,true);
         getFirstToken(tokenizer);
         }

    // After parsing meta-attribute and meta-data the comment char is
    // reaktivated.
    tokenizer.commentChar('%');

    while(tokenizer.sval.equalsIgnoreCase("@attribute")) {

      // Get attribute name.
      getNextToken(tokenizer);
      attributeName = tokenizer.sval;
      
      //add the name to TreeSet
      basicAttributeNames.add(attributeName);
      
      getNextToken(tokenizer);

      // Check if attribute is nominal.
      if (tokenizer.ttype == StreamTokenizer.TT_WORD) {

	// Attribute is real, integer, or string.
	if (tokenizer.sval.equalsIgnoreCase("real") ||
	    tokenizer.sval.equalsIgnoreCase("numeric")) {
	    m_basicAttributes.addElement(new BasicAttribute(attributeName,false,
						 numBasicAttributes()));
	  readTillEOL(tokenizer);
	}else if( tokenizer.sval.equalsIgnoreCase("integer")){
	  m_basicAttributes.addElement(new BasicAttribute(attributeName,true,
						 numBasicAttributes()));
	  readTillEOL(tokenizer);	
	}
	 else if (tokenizer.sval.equalsIgnoreCase("string")) {
	  m_basicAttributes.
	    addElement(new BasicAttribute(attributeName, null,
				     numBasicAttributes()));
	  readTillEOL(tokenizer);
	} else {
	  errms(tokenizer,"no valid attribute type or invalid "+
		"enumeration");
	}
      } else {

	// Attribute is nominal.
	basicAttributeValues = new FastVector();
	tokenizer.pushBack();
	
	// Get values for nominal attribute.
	if (tokenizer.nextToken() != '{') {
	  errms(tokenizer,"{ expected at beginning of enumeration");
	}
	while (tokenizer.nextToken() != '}') {
	  if (tokenizer.ttype == StreamTokenizer.TT_EOL) {
	    errms(tokenizer,"} expected at end of enumeration");
	  } else {
	    basicAttributeValues.addElement(tokenizer.sval);
	  }
	}
	if (basicAttributeValues.size() == 0) {
	  errms(tokenizer,"no nominal values found");
	}
	m_basicAttributes.addElement(new BasicAttribute(attributeName, basicAttributeValues,
				   numBasicAttributes()));
      }
      getLastToken(tokenizer,false);
      
      getFirstToken(tokenizer);
      if (tokenizer.ttype == StreamTokenizer.TT_EOF)
	  errms(tokenizer,"premature end of file");
    }
    
    // Check if data part follows. We can't easily check for EOL
    
    if (!tokenizer.sval.equalsIgnoreCase("@data")) {
       errms(tokenizer,"keyword @data expected");
    }
    
   //Cheak if metaDataNames are really a part from basicAttributeNames
    Iterator metaDataIter;
    metaDataIter = metaDataNames.iterator();
     
    Object temp;
    HashSet difference = new HashSet();
    while(metaDataIter.hasNext()){
   	
       temp = metaDataIter.next();
       if(!basicAttributeNames.contains(temp)){
         difference.add(temp);
       }
    }
       
    if(!difference.isEmpty()){  
     System.out.println("Warning!!! Following metaData names do not exist"
                        + " in basicAttributeNames");
     System.out.println(difference); }
   
    // Check if any attributes have been declared.
    if (m_basicAttributes.size() == 0) {
      errms(tokenizer,"no basic attributes declared");
    }

    // Allocate buffers in case sparse instances have to be read
    m_ValueBuffer = new double[numBasicAttributes()];
    m_IndicesBuffer = new int[numBasicAttributes()];
  }

  /**
   * Copies basicInstances from one set to the end of another 
   * one.
   *
   * @param source the source of the instances
   * @param from the position of the first instance to be copied
   * @param dest the destination for the instances
   * @param num the number of instances to be copied
   */
  private void copyBasicInstances(int from, Instances dest, int num) {
    
    for (int i = 0; i < num; i++) {
      dest.addBasicInstance(basicInstance(from + i));
    }
  }
  
   /**
   * Copies metaInstances from one set to the end of another 
   * one.
   *
   * @param source the source of the instances
   * @param from the position of the first instance to be copied
   * @param dest the destination for the instances
   * @param num the number of instances to be copied
   */
  private void copyMetaInstances(int from, Instances dest, int num) {
    
    for (int i = 0; i < num; i++) {
      dest.addMetaInstance(metaInstance(from + i));
    }
  }
  /**
   * Throws error message with line number and last token read.
   *
   * @param theMsg the error message to be thrown
   * @param tokenizer the stream tokenizer
   * @throws IOExcpetion containing the error message
   */
  private void errms(StreamTokenizer tokenizer, String theMsg) 
       throws IOException {
    
    throw new IOException(theMsg + ", read " + tokenizer.toString());
  }
  
  /**
   * Replaces the basic attribute information by a clone of
   * itself.
   */
  private void freshBasicAttributeInfo() {

    m_basicAttributes = (FastVector) m_basicAttributes.copyElements();
  }

  /**
   * Replaces the meta attribute information by a clone of
   * itself.
   */
  private void freshMetaAttributeInfo() {

    m_metaAttributes = (FastVector) m_metaAttributes.copyElements();
  }
  
  /**
   * Gets next token, skipping empty lines.
   *
   * @param tokenizer the stream tokenizer
   * @exception IOException if reading the next token fails
   */
  private void getFirstToken(StreamTokenizer tokenizer) 
    throws IOException{

    boolean readNextLine = true;
    while (readNextLine) {
        tokenizer.nextToken();
        if (tokenizer.ttype != StreamTokenizer.TT_EOL) {
            if (tokenizer.ttype == '%') {
                // The next line may be a comment line or may contain
                // a special item.
                tokenizer.nextToken();
                if ((tokenizer.ttype == StreamTokenizer.TT_WORD)
                    && (tokenizer.sval.equalsIgnoreCase("@meta-attribute")
                        || tokenizer.sval.equalsIgnoreCase("@meta-data"))) {
                    // The line contains a special item.
                    readNextLine = false;
                } else {
                    // The line contains a comment.
                    readTillEOL(tokenizer);
                }
            } else {
                // The next line is not empty and not a comment line.
                readNextLine = false;
            }
        }
    }

    if ((tokenizer.ttype == '\'') ||
	(tokenizer.ttype == '"')) {
      tokenizer.ttype = StreamTokenizer.TT_WORD;
    } else if ((tokenizer.ttype == StreamTokenizer.TT_WORD) &&
	       (tokenizer.sval.equals("?"))){
      tokenizer.ttype = '?';
    }
  }

  /**
   * Gets index, checking for a premature and of line.
   *
   * @param tokenizer the stream tokenizer
   * @exception IOException if it finds a premature end of line
   */
  private void getIndex(StreamTokenizer tokenizer) throws IOException{
    
    if (tokenizer.nextToken() == StreamTokenizer.TT_EOL) {
      errms(tokenizer,"premature end of line");
    }
    if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
      errms(tokenizer,"premature end of file");
    }
  }
  
  /**
   * Gets token and checks if its end of line.
   *
   * @param tokenizer the stream tokenizer
   * @exception IOException if it doesn't find an end of line
   */
  private void getLastToken(StreamTokenizer tokenizer, boolean endOfFileOk) 
       throws IOException{

    if ((tokenizer.nextToken() != StreamTokenizer.TT_EOL) &&
	((tokenizer.nextToken() != StreamTokenizer.TT_EOF) || !endOfFileOk)) {
      errms(tokenizer,"end of line expected");
    }
  }

  /**
   * Gets next token, checking for a premature and of line.
   *
   * @param tokenizer the stream tokenizer
   * @exception IOException if it finds a premature end of line
   */
  private void getNextToken(StreamTokenizer tokenizer) 
       throws IOException{
    
    if (tokenizer.nextToken() == StreamTokenizer.TT_EOL) {
      errms(tokenizer,"premature end of line");
    }
    if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
      errms(tokenizer,"premature end of file");
    } else if ((tokenizer.ttype == '\'') ||
	       (tokenizer.ttype == '"')) {
      tokenizer.ttype = StreamTokenizer.TT_WORD;
    } else if ((tokenizer.ttype == StreamTokenizer.TT_WORD) &&
	       (tokenizer.sval.equals("?"))){
      tokenizer.ttype = '?';
    }
  }
	
  /**
   * Initializes the StreamTokenizer used for reading the ARFF file.
   *
   * @param tokenizer the stream tokenizer
   */
  private void initTokenizer(StreamTokenizer tokenizer){

    tokenizer.resetSyntax();         
    tokenizer.whitespaceChars(0, ' ');    
    tokenizer.wordChars(' '+1,'\u00FF');
    tokenizer.whitespaceChars(',',',');
    tokenizer.commentChar('%');
    tokenizer.quoteChar('"');
    tokenizer.quoteChar('\'');
    tokenizer.ordinaryChar('{');
    tokenizer.ordinaryChar('}');
    tokenizer.eolIsSignificant(true);
  }
 
  /**
   * Returns string including all instances, their weights and
   * their indices in the original dataset.
   *
   * @return description of instance and its weight as a string
   */
   //basic
  private String instancesAndWeights(){

    StringBuffer text = new StringBuffer();

    for (int i = 0; i < numBasicInstances(); i++) {
      text.append(basicInstance(i) + " " + basicInstance(i).weight());
      if (i < numBasicInstances() - 1) {
	text.append("\n");
      }
    }
    return text.toString();
  }
  
  /**
   * Implements quicksort.
   *
   * @param attIndex the attribute's index
   * @param lo0 the first index of the subset to be sorted
   * @param hi0 the last index of the subset to be sorted
   */
  private void quickSort(int attIndex, int lo0, int hi0) {
    
    int lo = lo0, hi = hi0;
    double mid, midPlus, midMinus;
    
    if (hi0 > lo0) {
      
      // Arbitrarily establishing partition element as the 
      // midpoint of the array.
      mid = basicInstance((lo0 + hi0) / 2).value(attIndex);
      midPlus = mid + 1e-6;
      midMinus = mid - 1e-6;

      // loop through the array until indices cross
      while(lo <= hi) {
	
	// find the first element that is greater than or equal to 
	// the partition element starting from the left Index.
	while ((basicInstance(lo).value(attIndex) < 
		midMinus) && (lo < hi0)) {
	  ++lo;
	}
	
	// find an element that is smaller than or equal to
	// the partition element starting from the right Index.
	while ((basicInstance(hi).value(attIndex)  > 
		midPlus) && (hi > lo0)) {
	  --hi;
	}
	
	// if the indexes have not crossed, swap
	if(lo <= hi) {
	  swap(lo,hi);
	  ++lo;
	  --hi;
	}
      }
      
      // If the right index has not reached the left side of array
      // must now sort the left partition.
      if(lo0 < hi) {
	quickSort(attIndex,lo0,hi);
      }
      
      // If the left index has not reached the right side of array
      // must now sort the right partition.
      if(lo < hi0) {
	quickSort(attIndex,lo,hi0);
      }
    }
  }

  /**
   * Reads and skips all tokens before next end of line token.
   *
   * @param tokenizer the stream tokenizer
   */
  private void readTillEOL(StreamTokenizer tokenizer) 
       throws IOException{
    
    while (tokenizer.nextToken() != StreamTokenizer.TT_EOL) {};
    tokenizer.pushBack();
  }

  /**
   * Help function needed for stratification of set.
   *
   * @param numFolds the number of folds for the stratification
   */
  private void stratStep (int numFolds){
    
    FastVector newVec = new FastVector(m_basicInstances.capacity());
    int start = 0, j;

    // create stratified batch
    while (newVec.size() < numBasicInstances()) {
      j = start;
      while (j < numBasicInstances()) {
	newVec.addElement(basicInstance(j));
	j = j + numFolds;
      }
      start++;
    }
    m_basicInstances = newVec;
  }
  
  /**
   * Swaps two instances in the set.
   *
   * @param i the first instance's index
   * @param j the second instance's index
   */
  private void swap(int i, int j){
    
    m_basicInstances.swap(i, j);
  }

  /**
   * Merges two sets of Instances together. The resulting set will have
   * all the attributes of the first set plus all the attributes of the 
   * second set. The number of instances in both sets must be the same.
   *
   * @param first the first set of Instances
   * @param second the second set of Instances
   * @return the merged set of Instances
   * @exception IllegalArgumentException if the datasets are not the same size
   */
  public static Instances mergeInstances(Instances first, Instances second) {

    if (first.numBasicInstances() != second.numBasicInstances()) {
      throw new IllegalArgumentException("Instance sets must be of the same size");
    }

    // Create the vector of merged attributes
    FastVector newAttributes = new FastVector();
    for (int i = 0; i < first.numBasicAttributes(); i++) {
      newAttributes.addElement(first.basicAttribute(i));
    }
    for (int i = 0; i < second.numBasicAttributes(); i++) {
      newAttributes.addElement(second.basicAttribute(i));
    }
    
    // Create the set of Instances
    Instances merged = new Instances(first.relationName() + '_'
				     + second.relationName(), 
				     newAttributes, 
				     first.numBasicInstances(),0);
    // Merge each instance
    for (int i = 0; i < first.numBasicInstances(); i++) {
      merged.addBasicInstance(first.basicInstance(i).mergeInstance(second.basicInstance(i)));
    }
    return merged;
  }

  /**
   * Main method for this class -- just prints a summary of a set
   * of instances.
   *
   * @param argv should contain one element: the name of an ARFF file
   */
  public static void main(String [] args) {

    try {
      Reader r = null;
      if (args.length > 1) {
	throw (new Exception("Usage: Instances <filename>"));
      } else if (args.length == 0) {
        r = new BufferedReader(new InputStreamReader(System.in));
      } else {
        r = new BufferedReader(new FileReader(args[0]));
         }
      Instances i = new Instances(r);
     
      System.out.println(i.toSummaryString());
       } 
      catch (Exception ex) {
      System.err.println(ex.getMessage());
     }
  }
}

     

