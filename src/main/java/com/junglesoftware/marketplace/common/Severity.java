package com.junglesoftware.marketplace.common;

import javax.validation.Payload;

public abstract class Severity implements Payload {
  public static final OK OK = new OK();
  public static final WARNING WARNING = new WARNING();
  public static final ERROR ERROR = new ERROR();
  
  private Severity() {}
  
  public static final class OK extends Severity {
    @Override public String toString() { return "OK"; }
  }

  public static final class WARNING extends Severity {
    @Override public String toString() { return "WARNING"; }
  }

  public static final class ERROR extends Severity {
    @Override public String toString() { return "ERROR"; }
  }
}
