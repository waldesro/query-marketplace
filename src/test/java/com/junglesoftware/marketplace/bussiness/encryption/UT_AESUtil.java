package com.junglesoftware.marketplace.bussiness.encryption;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class UT_AESUtil {

  @TempDir
  private File folder;

  @Test
  void givenString_whenEncrypt_thenSuccess()
      throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
      BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

    String input = "MyPassword";
    SecretKey key = AESUtil.generateKey(128);
    IvParameterSpec ivParameterSpec = AESUtil.generateIv();
    String algorithm = "AES/CBC/PKCS5Padding";
    String cipherText = AESUtil.encrypt(algorithm, input, key, ivParameterSpec);
    String plainText = AESUtil.decrypt(algorithm, cipherText, key, ivParameterSpec);
    Assertions.assertEquals(input, plainText);
  }
  @Test
  void givenFile_whenEncrypt_thenSuccess()
      throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException,
      InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException,
      NoSuchPaddingException {

    SecretKey key = AESUtil.generateKey(128);
    String algorithm = "AES/CBC/PKCS5Padding";
    IvParameterSpec ivParameterSpec = AESUtil.generateIv();
    File inputFile = new File(folder, "inputFile.txt");
    File encryptedFile = new File(folder, "encryptedFile.txt");
    File decryptedFile = new File(folder, "decryptedFile.txt");
    List<String> lines = Arrays.asList("x", "y", "z");

    Files.write(inputFile.toPath(), lines);
    AESUtil.encryptFile(algorithm, key, ivParameterSpec, inputFile, encryptedFile);
    AESUtil.decryptFile(algorithm, key, ivParameterSpec, encryptedFile, decryptedFile);
    assertThat(inputFile).hasSameTextualContentAs(decryptedFile);
  }
}