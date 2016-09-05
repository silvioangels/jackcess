/*
Copyright (c) 2016 James Ahlborn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.healthmarketscience.jackcess;

import java.util.Arrays;

import com.healthmarketscience.jackcess.Database.FileFormat;
import static com.healthmarketscience.jackcess.impl.JetFormatTest.*;
import com.healthmarketscience.jackcess.impl.TableImpl;
import junit.framework.TestCase;
import static com.healthmarketscience.jackcess.TestUtil.*;

/**
 *
 * @author James Ahlborn
 */
public class TableUpdaterTest extends TestCase
{

  public TableUpdaterTest(String name) throws Exception {
    super(name);
  }

  public void testTableUpdating() throws Exception {
    for (final FileFormat fileFormat : SUPPORTED_FILEFORMATS) {
      Database db = create(fileFormat);

      Table t1 = new TableBuilder("TestTable")
        .addColumn(new ColumnBuilder("id", DataType.LONG))
        .toTable(db);
      
      Table t2 = new TableBuilder("TestTable2")
        .addColumn(new ColumnBuilder("id2", DataType.LONG))
        .toTable(db);

      new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME)
        .addColumns("id").setPrimaryKey()
        .addToTable(t1);
      new ColumnBuilder("data", DataType.TEXT)
        .addToTable(t1);
      new ColumnBuilder("bigdata", DataType.MEMO)
        .addToTable(t1);

      new ColumnBuilder("data2", DataType.TEXT)
        .addToTable(t2);
      new ColumnBuilder("bigdata2", DataType.MEMO)
        .addToTable(t2);

      Relationship rel = new RelationshipBuilder("TestTable", "TestTable2")
        .addColumns("id", "id2")
        .setReferentialIntegrity()
        .setCascadeDeletes()
        .toRelationship(db);

      assertEquals("TestTableTestTable2", rel.getName());
      assertSame(t1, rel.getFromTable());
      assertEquals(Arrays.asList(t1.getColumn("id")), rel.getFromColumns());
      assertSame(t2, rel.getToTable());
      assertEquals(Arrays.asList(t2.getColumn("id2")), rel.getToColumns());
      assertFalse(rel.isOneToOne());
      assertTrue(rel.hasReferentialIntegrity());
      assertTrue(rel.cascadeDeletes());
      assertFalse(rel.cascadeUpdates());
      assertEquals(Relationship.JoinType.INNER, rel.getJoinType());

      assertEquals(2, t1.getIndexes().size());
      assertEquals(1, ((TableImpl)t1).getIndexDatas().size());

      assertEquals(1, t2.getIndexes().size());
      assertEquals(1, ((TableImpl)t2).getIndexDatas().size());

      for(int i = 0; i < 10; ++i) {
        t1.addRow(i, "row" + i, "row-data" + i);
      }

      for(int i = 0; i < 10; ++i) {
        t2.addRow(i, "row2_" + i, "row-data2_" + i);
      }

      try {
        t2.addRow(13, "row13", "row-data13");
        fail("ConstraintViolationException should have been thrown");
      } catch(ConstraintViolationException cv) {
        // success
      }

      Row r1 = CursorBuilder.findRowByPrimaryKey(t1, 5);
      t1.deleteRow(r1);
   
      int id = 0;
      for(Row r : t1) {
        assertEquals(id, r.get("id"));
        ++id;
        if(id == 5) {
          ++id;
        }
      }
 
      id = 0;
      for(Row r : t2) {
        assertEquals(id, r.get("id2"));
        ++id;
        if(id == 5) {
          ++id;
        }
      }
 
      db.close();
    }    
  }
}