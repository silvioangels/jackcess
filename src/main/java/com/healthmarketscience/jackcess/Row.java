/*
Copyright (c) 2013 James Ahlborn

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA
*/

package com.healthmarketscience.jackcess;

import java.util.Date;
import java.util.Map;
import java.math.BigDecimal;

import com.healthmarketscience.jackcess.complex.ComplexValueForeignKey;


/**
 * A row of data as column name->value pairs.  Values are strongly typed, and
 * column names are case sensitive.
 *
 * @author James Ahlborn
 * @usage _general_class_
 */
public interface Row extends Map<String,Object>
{
  /**
   * @return the id of this row 
   */
  public RowId getId();

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a String.
   */
  public String getString(String name);

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a Boolean.
   */
  public Boolean getBoolean(String name);

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a Byte.
   */
  public Byte getByte(String name);

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a Short.
   */
  public Short getShort(String name);

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a Integer.
   */
  public Integer getInt(String name);

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a BigDecimal.
   */
  public BigDecimal getBigDecimal(String name);

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a Float.
   */
  public Float getFloat(String name);

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a Double.
   */
  public Double getDouble(String name);

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a Date.
   */
  public Date getDate(String name);

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a byte[].
   */
  public byte[] getBytes(String name);

  /**
   * Convenience method which gets the value for the row with the given name,
   * casting it to a ComplexValueForeignKey.
   */
  public ComplexValueForeignKey getForeignKey(String name);
}
