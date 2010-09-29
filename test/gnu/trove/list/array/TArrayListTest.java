///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001, Eric D. Friedman All Rights Reserved.
// Copyright (c) 2009, Rob Eden All Rights Reserved.
// Copyright (c) 2009, Jeff Randall All Rights Reserved.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
///////////////////////////////////////////////////////////////////////////////

package gnu.trove.list.array;

import gnu.trove.list.TIntList;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;



public class TArrayListTest extends TestCase {

    private TIntList list;


    public void setUp() throws Exception {
        super.setUp();

        list = new TIntArrayList( 15, Integer.MIN_VALUE );
        list.add( 1 );
        list.add( 2 );
        list.add( 3 );
        list.add( 4 );
        list.add( 5 );
    }


    public void tearDown() throws Exception {
        super.tearDown();
    }


    public void testToArray() {
        assertTrue( Arrays.equals( new int[]{1, 2, 3, 4, 5}, list.toArray() ) );
        assertTrue( Arrays.equals( new int[]{1, 2, 3, 4}, list.toArray( 0, 4 ) ) );
        assertTrue( Arrays.equals( new int[]{2, 3, 4, 5}, list.toArray( 1, 4 ) ) );
        assertTrue( Arrays.equals( new int[]{2, 3, 4}, list.toArray( 1, 3 ) ) );

        int[] array_correct_size = new int[5];
        list.toArray( array_correct_size );
        assertEquals( 1, array_correct_size[0] );
        assertEquals( 2, array_correct_size[1] );
        assertEquals( 3, array_correct_size[2] );
        assertEquals( 4, array_correct_size[3] );
        assertEquals( 5, array_correct_size[4] );
        assertEquals( array_correct_size.length, list.size() );

        int[] array_too_long = new int[8];
        list.toArray( array_too_long );
        assertEquals( 1, array_too_long[0] );
        assertEquals( 2, array_too_long[1] );
        assertEquals( 3, array_too_long[2] );
        assertEquals( 4, array_too_long[3] );
        assertEquals( 5, array_too_long[4] );
        assertEquals( list.getNoEntryValue(), array_too_long[5] );

        int[] array_too_short = new int[2];
        list.toArray( array_too_short );
        assertEquals( 1, array_too_short[0] );
        assertEquals( 2, array_too_short[1] );
    }


    public void testSubList() throws Exception {
        TIntList subList = list.subList( 1, 4 );
        assertEquals( 3, subList.size() );
        assertEquals( 2, subList.get( 0 ) );
        assertEquals( 4, subList.get( 2 ) );
    }


    public void testSublist_Exceptions() {
        try {
            list.subList( 1, 0 );
            fail( "expected IllegalArgumentException when end < begin" );
        }
        catch ( IllegalArgumentException expected ) {
        }

        try {
            list.subList( -1, 3 );
            fail( "expected IndexOutOfBoundsException when begin < 0" );
        }
        catch ( IndexOutOfBoundsException expected ) {
        }

        try {
            list.subList( 1, 42 );
            fail( "expected IndexOutOfBoundsException when end > length" );
        }
        catch ( IndexOutOfBoundsException expected ) {
        }
    }


    public void testMax() {
        assertEquals( 5, list.max() );
        assertEquals( 1, list.min() );

        TIntArrayList list2 = new TIntArrayList();
        list2.add( 3 );
        list2.add( 1 );
        list2.add( 2 );
        list2.add( 5 );
        list2.add( 4 );
        assertEquals( 5, list2.max() );
        assertEquals( 1, list2.min() );
    }


    public void testSerialization() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream( bout );
        oout.writeObject( list );
        oout.close();

        ObjectInputStream oin = new ObjectInputStream(
                new ByteArrayInputStream( bout.toByteArray() ) );

        TIntArrayList new_list = (TIntArrayList) oin.readObject();

        assertEquals( list, new_list );
    }


	// From bug 3077245
	public void testInvalidStartRemoveZeroLength() {
		try {
			TIntArrayList bug = new TIntArrayList();
			bug.remove( 0, 0 );
		}
		catch( ArrayIndexOutOfBoundsException ex ) {
			ex.printStackTrace();
			fail( "Shouldn't raise exception" );
		}
	}
}
