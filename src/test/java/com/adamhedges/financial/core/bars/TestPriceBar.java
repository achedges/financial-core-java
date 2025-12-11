package com.adamhedges.financial.core.bars;

import com.adamhedges.utilities.datetime.DateUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

public class TestPriceBar {

    @Test
    public void TestPriceBar_id() {
        long id = 1325873235120L;
        long dt = 20231220L;
        int tm = 1200;

        Assertions.assertEquals(id, PriceBar.generateId(dt, tm));
        Assertions.assertEquals(dt, PriceBar.extractDateFromId(id));
        Assertions.assertEquals(tm, PriceBar.extractTimeFromId(id));
    }

    @Test
    public void TestPriceBar_constructors() {
        String s = "TEST";
        
        PriceBar a = new PriceBar(s);
        Assertions.assertEquals(s, a.getSymbol());

        PriceBar b = new PriceBar(s, 20231220L, 1200);
        Assertions.assertEquals(s, b.getSymbol());
        Assertions.assertEquals(20231220L, b.getDate());
        Assertions.assertEquals(1200, b.getTime());
        
        PriceBar c = new PriceBar(s, 1.23);
        Assertions.assertEquals(s, c.getSymbol());
        Assertions.assertEquals(1.23, c.getOpen());
        Assertions.assertEquals(1.23, c.getHigh());
        Assertions.assertEquals(1.23, c.getLow());
        Assertions.assertEquals(1.23, c.getClose());
        Assertions.assertEquals(1.23, c.getVwap());

        ZonedDateTime timestamp = ZonedDateTime.of(2023, 12, 20, 12, 0, 0, 0, DateUtilities.EASTERN_TIMEZONE);
        PriceBar d = new PriceBar(s, timestamp.toInstant());
        Assertions.assertEquals(20231220L, d.getDate());
        Assertions.assertEquals(1200, d.getTime());

        PriceBar e = new PriceBar(s, 20241010, 1200, 10.0);
        Assertions.assertEquals(20241010L, e.getDate());
        Assertions.assertEquals(1200, e.getTime());
        Assertions.assertEquals(10.0, e.getOpen());
        Assertions.assertEquals(10.0, e.getHigh());
        Assertions.assertEquals(10.0, e.getLow());
        Assertions.assertEquals(10.0, e.getClose());
        Assertions.assertEquals(10.0, e.getVwap());

        PriceBar f = new PriceBar(s, timestamp.toInstant(), DateUtilities.EASTERN_TIMEZONE);
        Assertions.assertEquals(20231220L, f.getDate());
        Assertions.assertEquals(1200, f.getTime());

        PriceBar g = new PriceBar(s, 10.0, timestamp.toInstant());
        Assertions.assertEquals(20231220, g.getDate());
        Assertions.assertEquals(1200, g.getTime());
        Assertions.assertEquals(10.0, g.getOpen());
        Assertions.assertEquals(10.0, g.getHigh());
        Assertions.assertEquals(10.0, g.getLow());
        Assertions.assertEquals(10.0, g.getClose());
        Assertions.assertEquals(10.0, g.getVwap());

        PriceBar h = new PriceBar(s, 10.0, timestamp.toInstant(), DateUtilities.EASTERN_TIMEZONE);
        Assertions.assertEquals(20231220L, h.getDate());
        Assertions.assertEquals(1200, h.getTime());
    }

    @Test
    public void TestPriceBar_compareTo() {
        PriceBar a = new PriceBar("TEST", 20231219L, 1200);
        PriceBar b = new PriceBar("TEST", 20231220L, 1200);
        PriceBar c = new PriceBar("TEST", 20231220L, 1200);

        Assertions.assertEquals(-1, a.compareTo(b));
        Assertions.assertEquals(0, b.compareTo(c));
        Assertions.assertEquals(1, b.compareTo(a));
    }

    @Test
    public void TestPriceBar_equals() {
        PriceBar a = new PriceBar("TEST", 1.23);
        a.setDate(20231220L);
        a.setTime(1200);
        a.setVolume(1);

        PriceBar b = new PriceBar("TEST", 1.23);
        b.setDate(20231220L);
        b.setTime(1200);
        b.setVolume(1);

        Assertions.assertEquals(a, b);

        b.setVolume(0);
        Assertions.assertNotEquals(a, b);
        b.setVwap(0);
        Assertions.assertNotEquals(a, b);
        b.setClose(0);
        Assertions.assertNotEquals(a, b);
        b.setLow(0);
        Assertions.assertNotEquals(a, b);
        b.setHigh(0);
        Assertions.assertNotEquals(a, b);
        b.setOpen(0);
        Assertions.assertNotEquals(a, b);
        b.setTime(0);
        Assertions.assertNotEquals(a, b);
        b.setDate(0);
        Assertions.assertNotEquals(a, b);
        b.setSymbol("");
        Assertions.assertNotEquals(a, b);
    }

    @Test
    public void TestPriceBar_fillPriceDataFrom() {
        PriceBar a = new PriceBar("TEST", 1.23);
        a.setVolume(12);

        PriceBar b = new PriceBar("TEST");
        b.fillPriceDataFrom(a);

        Assertions.assertEquals(a.getOpen(), b.getOpen());
        Assertions.assertEquals(a.getHigh(), b.getHigh());
        Assertions.assertEquals(a.getLow(), b.getLow());
        Assertions.assertEquals(a.getClose(), b.getClose());
        Assertions.assertEquals(a.getVwap(), b.getVwap());
        Assertions.assertEquals(a.getVolume(), b.getVolume());
    }

    @Test
    public void TestPriceBar_isUp() {
        PriceBar b = new PriceBar("TEST");
        b.setOpen(10);
        b.setClose(11);
        Assertions.assertTrue(b.isUp());
        Assertions.assertFalse(b.isDown());

        b.setClose(9);
        Assertions.assertTrue(b.isDown());
        Assertions.assertFalse(b.isUp());
    }

    @Test
    public void TestPriceBar_aggregate() {
        PriceBar b = new PriceBar("TEST", 10.0);
        b.setVolume(100);
        b.setVwap(10.0);

        b.aggregate(11.0, 11.0, 11.0, 100, 11.0);
        Assertions.assertEquals(11.0, b.getHigh());
        Assertions.assertEquals(10.0, b.getLow());
        Assertions.assertEquals(11.0, b.getClose());
        Assertions.assertEquals(200, b.getVolume());
        Assertions.assertEquals(10.5, b.getVwap());

        b.aggregate(9.0, 9.0, 9.0, 100, 9.0);
        Assertions.assertEquals(11.0, b.getHigh());
        Assertions.assertEquals(9.0, b.getLow());
        Assertions.assertEquals(9.0, b.getClose());
        Assertions.assertEquals(300, b.getVolume());
        Assertions.assertEquals(10.0, b.getVwap());
    }

    @Test
    public void TestPriceBar_toString() {
        PriceBar bar = new PriceBar("TEST", 20250801, 1200, 10.12345);
        Assertions.assertEquals("20250801 1200 O=10.12 H=10.12 L=10.12 C=10.12 V=0 VWAP=10.12", bar.toString());
        Assertions.assertEquals("20250801 1200 O=10.12 H=10.12 L=10.12 C=10.12 V=0 VWAP=10.12", bar.toString(2));
        Assertions.assertEquals("20250801 1200 O=10.12345 H=10.12345 L=10.12345 C=10.12345 V=0 VWAP=10.12345", bar.toString(5));
    }

    @Test
    public void TestPriceBar_getHour_getMinute() {
        PriceBar bar = new PriceBar("test", 20251201, 827);
        Assertions.assertEquals(8, bar.getHour());
        Assertions.assertEquals(27, bar.getMinute());
    }

}
