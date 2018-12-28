package com.harrysoft.burstcoinexplorer.test;

import com.harrysoft.burstcoinexplorer.util.FileSizeUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FileSizeUtilsTest {
    @Test
    public void testFileSizeFormatting() {
        assertEquals("123 Bytes (100%)", FileSizeUtils.formatBlockSize(123, 1d));
        assertEquals("1.205 KB (50%)", FileSizeUtils.formatBlockSize(1234, 0.5d));
        assertEquals("12.056 KB (0%)", FileSizeUtils.formatBlockSize(12345, 0d));
        assertEquals("120.562 KB (1%)", FileSizeUtils.formatBlockSize(123456, 0.01d));
        assertEquals("1.177 MB (0.1%)", FileSizeUtils.formatBlockSize(1234567, 0.001d));
        assertEquals("11.774 MB (0.01%)", FileSizeUtils.formatBlockSize(12345678, 0.0001d));
        assertEquals("117.738 MB (0.001%)", FileSizeUtils.formatBlockSize(123456789, 0.00001d));
        assertEquals("1.150 GB (0%)", FileSizeUtils.formatBlockSize(1234567891, 0.000005d));
    }
}
