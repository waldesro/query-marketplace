package com.junglesoftware.marketplace.common.validation.validator;

/**
 * Constant file for Regular expressions used for validation purposes
 */
public class RegExValidatorConstant {

  /**
   * An alphanumeric character:[\p{Alpha}\p{Digit}]
   * US-ASCII only
   * */
  public static final String IS_ALPHA_NUMERIC = "^[\\p{Alnum}]*$";

  /**
   * An alphanumeric character with Space: [\p{Alpha}\p{Digit}\p{space}]
   * US-ASCII only
   * */
  public static final String IS_ALPHA_NUMERIC_WITH_SPACE = "^[\\p{Alnum}\\p{Space}]*$";

  /**
   * Digit from 0-9
   * Empty string will be considered invalid
   * */
  public static final String IS_DIGIT = "^[\\p{Digit}]+$";

  /**
   * An alphabetic character
   * US-ASCII only
   * */
  public static final String IS_ALPHABETIC = "^[\\p{Alpha}]*$";

  /**
   * An alphabetic character with space
   * US-ASCII only
   * */
  public static final String IS_ALPHABETIC_WITH_SPACE = "^[\\p{Alpha}\\p{Space}]*$";

  /**
   * An alphanumeric character with UNICODE_CHARACTER_CLASS
   * Accepts alphabets from all languages
   * */
  public static final String IS_ALPHA_NUMERIC_UNICODE = "^[\\p{IsAlphabetic}\\p{IsDigit}]*$";

  /**
   * An alphabetic character with UNICODE_CHARACTER_CLASS
   * Accepts alphabets from all languages
   * */
  public static final String IS_ALPHABETIC_UNICODE = "^[\\p{IsAlphabetic}]*$";

  private RegExValidatorConstant() {
  }
}
