package org.streamspf.hadoop.cdr;

import org.streamspf.hadoop.common.CSVParser;
import org.apache.hadoop.io.*;

import java.io.*;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CdrWritable
    implements WritableComparable<CdrWritable>, Cloneable {
  String symbol;
  String date;
  double open;
  double high;
  double low;
  double close;
  int volume;
  double adjClose;

  public CdrWritable() {
  }

  public CdrWritable(String symbol,
                            String date,
                            double open,
                            double high,
                            double low,
                            double close,
                            int volume,
                            double adjClose) {
    this.symbol = symbol;
    this.date = date;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
    this.adjClose = adjClose;
  }

  @Override
  public void write(DataOutput out) throws IOException {
    WritableUtils.writeString(out, symbol);
    WritableUtils.writeString(out, date);
    out.writeDouble(open);
    out.writeDouble(high);
    out.writeDouble(low);
    out.writeDouble(close);
    out.writeInt(volume);
    out.writeDouble(adjClose);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    symbol = WritableUtils.readString(in);
    date = WritableUtils.readString(in);
    open = in.readDouble();
    high = in.readDouble();
    low = in.readDouble();
    close = in.readDouble();
    volume = in.readInt();
    adjClose = in.readDouble();
  }

  @Override
  public int compareTo(CdrWritable passwd) {
    return CompareToBuilder.reflectionCompare(this, passwd);
  }

  @Override
  public String toString() {
    return ToStringBuilder
        .reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public double getOpen() {
    return open;
  }

  public void setOpen(double open) {
    this.open = open;
  }

  public double getHigh() {
    return high;
  }

  public void setHigh(double high) {
    this.high = high;
  }

  public double getLow() {
    return low;
  }

  public void setLow(double low) {
    this.low = low;
  }

  public double getClose() {
    return close;
  }

  public void setClose(double close) {
    this.close = close;
  }

  public int getVolume() {
    return volume;
  }

  public void setVolume(int volume) {
    this.volume = volume;
  }

  public double getAdjClose() {
    return adjClose;
  }

  public void setAdjClose(double adjClose) {
    this.adjClose = adjClose;
  }

  public static CdrWritable fromLine(String line)
      throws IOException {
    CSVParser parser = new CSVParser();
    String[] parts = parser.parseLine(line);

    CdrWritable cdr = new CdrWritable(
        parts[0], parts[1], Double.valueOf(parts[2]),
        Double.valueOf(parts[3]),
        Double.valueOf(parts[4]),
        Double.valueOf(parts[5]),
        Integer.valueOf(parts[6]),
        Double.valueOf(parts[7])
    );
    return cdr;
  }
}
