package com.adamhedges.financial.core.bars;

import com.adamhedges.utilities.datetime.DateUtilities;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

@Data
public class PriceBar implements Comparable<PriceBar> {

    // binary properties
    private long id = 0;
    private double open = 0;
    private double high = 0;
    private double low = 0;
    private double close = 0;
    private double vwap = 0;
    private long volume = 0;

    // derived properties
    private String symbol;
    private long date = 0;
    private int time = 0;

    public PriceBar(String symbol) {
        this.symbol = symbol;
    }

    public PriceBar(String symbol, long date, int time) {
        this.symbol = symbol;
        this.date = date;
        this.time = time;
        this.id = PriceBar.generateId(this.date, this.time);
    }

    public PriceBar(String symbol, double basisPrice) {
        this.symbol = symbol;
        this.open = basisPrice;
        this.high = basisPrice;
        this.low = basisPrice;
        this.close = basisPrice;
        this.vwap = basisPrice;
    }

    public PriceBar(String symbol, long date, int time, double basisPrice) {
        this.symbol = symbol;
        this.date = date;
        this.time = time;
        this.id = PriceBar.generateId(this.date, this.time);
        this.open = basisPrice;
        this.high = basisPrice;
        this.low = basisPrice;
        this.close = basisPrice;
        this.vwap = basisPrice;
    }

    public PriceBar(String symbol, Instant timestamp) {
        this.symbol = symbol;
        this.date = DateUtilities.getInstantDate(timestamp, DateUtilities.EASTERN_TIMEZONE);
        this.time = DateUtilities.getInstantTime(timestamp, DateUtilities.EASTERN_TIMEZONE);
        this.id = PriceBar.generateId(this.date, this.time);
    }

    public PriceBar(String symbol, Instant timestamp, ZoneId tz) {
        this.symbol = symbol;
        this.date = DateUtilities.getInstantDate(timestamp, tz);
        this.time = DateUtilities.getInstantTime(timestamp, tz);
        this.id = PriceBar.generateId(this.date, this.time);
    }

    public PriceBar(String symbol, double basisPrice, Instant timestamp) {
        this.symbol = symbol;
        this.date = DateUtilities.getInstantDate(timestamp, DateUtilities.EASTERN_TIMEZONE);
        this.time = DateUtilities.getInstantTime(timestamp, DateUtilities.EASTERN_TIMEZONE);
        this.id = PriceBar.generateId(this.date, this.time);
        this.open = basisPrice;
        this.high = basisPrice;
        this.low = basisPrice;
        this.close = basisPrice;
        this.vwap = basisPrice;
    }

    public PriceBar(String symbol, double basisPrice, Instant timestamp, ZoneId tz) {
        this.symbol = symbol;
        this.date = DateUtilities.getInstantDate(timestamp, tz);
        this.time = DateUtilities.getInstantTime(timestamp, tz);
        this.id = PriceBar.generateId(this.date, this.time);
        this.open = basisPrice;
        this.high = basisPrice;
        this.low = basisPrice;
        this.close = basisPrice;
        this.vwap = basisPrice;
    }

    public static long generateId(long date, int time) {
        return (date << 16) + time;
    }

    public static long extractDateFromId(long id) {
        return id >> 16;
    }

    public static int extractTimeFromId(long id) {
        return (int)(id % (1 << 16));
    }

    public boolean isUp() {
        return close > open;
    }

    public boolean isDown() {
        return close < open;
    }

    @Override
    public int compareTo(PriceBar other) {
        return Long.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PriceBar bar)) {
            return false;
        }

        if (!Objects.equals(bar.symbol, this.symbol))
            return false;
        if (bar.date != this.date)
            return false;
        if (bar.time != this.time)
            return false;
        if (bar.open != this.open)
            return false;
        if (bar.high != this.high)
            return false;
        if (bar.low != this.low)
            return false;
        if (bar.close != this.close)
            return false;
        if (bar.vwap != this.vwap)
            return false;
        if (bar.volume != this.volume)
            return false;

        return bar.id == this.id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void fillPriceDataFrom(PriceBar other) {
        this.open = other.open;
        this.high = other.high;
        this.low = other.low;
        this.close = other.close;
        this.vwap = other.vwap;
        this.volume = other.volume;
    }

    public void aggregate(double high, double low, double close, long volume, double vwap) {
        this.high = Math.max(this.high, high);
        this.low = Math.min(this.low, low);
        this.close = close;

        long totalVolume = this.volume + volume;
        double currentTotal = this.vwap * this.volume;
        double updateTotal = vwap * volume;

        this.volume = totalVolume;
        this.vwap = (currentTotal + updateTotal) / totalVolume;
    }

    @Override
    public String toString() {
        return String.format("%d %4s O=%,.2f H=%,.2f L=%,.2f C=%,.2f V=%d VWAP=%,.2f", date, time, open, high, low, close, volume, vwap);
    }

}
