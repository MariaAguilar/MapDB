package org.mapdb;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

@SuppressWarnings({"rawtypes","unchecked"})
public class SerializerTest {

    @Test public void UUID2(){
        UUID u = UUID.randomUUID();
        assertEquals(u, SerializerBaseTest.clone2(u,Serializer.UUID));
    }

    @Test public void string_ascii(){
        String s = "adas9 asd9009asd";
        assertEquals(s,SerializerBaseTest.clone2(s,Serializer.STRING_ASCII));
        s = "";
        assertEquals(s, SerializerBaseTest.clone2(s,Serializer.STRING_ASCII));
        s = "    ";
        assertEquals(s, SerializerBaseTest.clone2(s,Serializer.STRING_ASCII));
    }

    @Test public void compression_wrapper() throws IOException {
        byte[] b = new byte[100];
        new Random().nextBytes(b);
        Serializer<byte[]> ser = new Serializer.CompressionWrapper(Serializer.BYTE_ARRAY);
        assertTrue(Serializer.BYTE_ARRAY.equals(b, SerializerBaseTest.clone2(b, ser)));

        b = Arrays.copyOf(b, 10000);
        assertTrue(Serializer.BYTE_ARRAY.equals(b, SerializerBaseTest.clone2(b, ser)));

        DataIO.DataOutputByteArray out = new DataIO.DataOutputByteArray();
        ser.serialize(out,b);
        assertTrue(out.pos < 1000);
    }

    @Test public void array(){
        Serializer.Array s = new Serializer.Array(Serializer.INTEGER);

        Object[] a = new Object[]{1,2,3,4};

        assertArrayEquals(a, UtilsTest.clone(a, s));
        assertEquals(s, UtilsTest.clone(s, Serializer.BASIC));
    }

    void testLong(Serializer<Long> ser){
        for(Long i= (long) -1e5;i<1e5;i++){
            assertEquals(i, UtilsTest.clone(i,ser));
        }

        for(Long i=0L;i>0;i+=1+i/10000){
            assertEquals(i, UtilsTest.clone(i, ser));
            assertEquals(new Long(-i), UtilsTest.clone(-i, ser));
        }
    }

    @Test public void Long(){
        testLong(Serializer.LONG);
    }


    @Test public void Long_packed(){
        testLong(Serializer.LONG_PACKED);
    }

    @Test public void Long_packed_zigzag(){
        testLong(Serializer.LONG_PACKED_ZIGZAG);
    }


    void testInt(Serializer<Integer> ser){
        for(Integer i= (int) -1e5;i<1e5;i++){
            assertEquals(i, UtilsTest.clone(i,ser));
        }

        for(Integer i=0;i>0;i+=1+i/10000){
            assertEquals(i, UtilsTest.clone(i, ser));
            assertEquals(new Long(-i), UtilsTest.clone(-i, ser));
        }
    }

    @Test public void Int(){
        testInt(Serializer.INTEGER);
    }


    @Test public void Int_packed(){
        testInt(Serializer.INTEGER_PACKED);
    }

    @Test public void Int_packed_zigzag(){
        testInt(Serializer.INTEGER_PACKED_ZIGZAG);
    }
}
