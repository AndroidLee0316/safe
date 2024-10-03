package com.pasc.safekeyboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pasc.lib.encryption.base.CloseUtils;
import com.pasc.lib.encryption.encode.Base64Util;
import com.pasc.lib.encryption.oneway.MD5Util;
import com.pasc.lib.encryption.oneway.SHAUtil;
import com.pasc.lib.encryption.symmetric.AESUtil;
import com.pasc.lib.encryption.symmetric.DESUtil;
import com.pasc.lib.encryption.unsymmetric.RSAUtil;

import java.io.File;
import java.io.FileOutputStream;

import javax.crypto.Cipher;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/5/5
 */
public class EncryptionTestActivity extends Activity implements View.OnClickListener{

    private TextView test_encryption_demo_result;

    private File mBase64EncryptionFile;
    private File mBase64DecryptionFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_encryption_demo);
        test_encryption_demo_result = findViewById(R.id.test_encryption_demo_result);
        findViewById(R.id.test_encryption_demo_char_encrypt).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_char_decrypt).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_file_encrypt).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_file_decrypt).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_char_md5).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_char_md5_2).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_char_md5_times).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_file_md5).setOnClickListener(this);

        findViewById(R.id.test_encryption_demo_char_sha).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_char_aes_encrypt).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_char_aes_decrypt).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_char_des_encrypt).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_char_des_decrypt).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_char_rsa_encrypt).setOnClickListener(this);
        findViewById(R.id.test_encryption_demo_char_rsa_decrypt).setOnClickListener(this);

        mBase64EncryptionFile = saveFile("mBase64EncryptionFile","0123456789");
        mBase64DecryptionFile = saveFile("mBase64DecryptionFile","MDEyMzQ1Njc4OQ==");
    }

    private File saveFile(String fileName, String data){

        File rootFile = getCacheDir();
        File file = new File(rootFile,fileName);

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                CloseUtils.close(fos);
            }
        }
        return file;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_encryption_demo_char_encrypt://Base64 字符加密

                test_encryption_demo_result.setText(Base64Util.base64EncodeStr("0123456789"));
                break;
            case R.id.test_encryption_demo_char_decrypt://Base64 字符解密
                test_encryption_demo_result.setText(Base64Util.base64DecodedStr("MDEyMzQ1Njc4OQ=="));
                break;
            case R.id.test_encryption_demo_file_encrypt://Base64 文件加密
                test_encryption_demo_result.setText(Base64Util.base64EncodeFile(mBase64EncryptionFile));
                break;
            case R.id.test_encryption_demo_file_decrypt://Base64 文件解密
                test_encryption_demo_result.setText(Base64Util.base64DecodedFile(mBase64DecryptionFile.getAbsolutePath()));
                break;
            case R.id.test_encryption_demo_char_md5://MD5 字符加密
                test_encryption_demo_result.setText(MD5Util.md5("0123456789"));
                break;
            case R.id.test_encryption_demo_char_md5_2://MD5 字符加密（加盐）
                test_encryption_demo_result.setText(MD5Util.md5("0123456789","8888"));
                break;
            case R.id.test_encryption_demo_char_md5_times://MD5 字符加密(多次)
                test_encryption_demo_result.setText(MD5Util.md5("0123456789",3));
                break;
            case R.id.test_encryption_demo_file_md5://MD5 文件加密
                test_encryption_demo_result.setText(MD5Util.md5(mBase64EncryptionFile));
                break;
            case R.id.test_encryption_demo_char_sha://SHA 字符加密
                test_encryption_demo_result.setText(SHAUtil.sha("0123456789", SHAUtil.SHA512));
                break;
            case R.id.test_encryption_demo_char_aes_decrypt://AES 字符加密
                test_encryption_demo_result.setText(AESUtil.aes("0123456789", "8888", Cipher.ENCRYPT_MODE));
                break;
            case R.id.test_encryption_demo_char_aes_encrypt://AES 字符解密
                test_encryption_demo_result.setText(AESUtil.aes("1B316BB9B1839EB4C67FAF3F56270209","8888",Cipher.DECRYPT_MODE));
                break;
            case R.id.test_encryption_demo_char_des_decrypt://DES 字符加密
                test_encryption_demo_result.setText(DESUtil.des("0123456789", "88888888", Cipher.ENCRYPT_MODE));
                break;
            case R.id.test_encryption_demo_char_des_encrypt://DES 字符解密
                test_encryption_demo_result.setText(DESUtil.des("88BE04DCABEB0C65B66A5BFFB621D31D","88888888",Cipher.DECRYPT_MODE));
                break;
            case R.id.test_encryption_demo_char_rsa_encrypt://RSA 字符加密
                try {
                    String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAviSuCu4Yg/WAyjp06qiaE/ioI2M/ACT9UTUVxWtM7IZlXMQZPjLn0H1x0zmJ/VLIhnBliyb06QLvtrrBFRt4jnOJR5LjoTg/g8XYdVXN6a+XFjqFvOUPgzZ7OdywOoXxiO+M7WrvT0XgqyBqCnDADpY1eucDqfIDYYOBHKbtMkh0N4ZVBcfULb1Sm+Q7ed+jUa8eXPQPhMrWvhQkIeZJh+hCIrNjXUxyfZPh1tSvqoJYArbyHZs8LnbUtjIQCx9OlR9+xJTx3L9h89I4D+hqA4CZqxUzfibsu5XgYKnoSri2OCR2FefSfYlCd8Fysp0wET/r1L141qnhoMQtrUs8jwIDAQAB";
                    String source = "高可用架构对于互联网服务基本是标配。";
                    test_encryption_demo_result.setText(RSAUtil.encrypt(publicKey, source));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.test_encryption_demo_char_rsa_decrypt://RSA 字符解密
                try {
                    String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAviSuCu4Yg/WAyjp06qiaE/ioI2M/ACT9UTUVxWtM7IZlXMQZPjLn0H1x0zmJ/VLIhnBliyb06QLvtrrBFRt4jnOJR5LjoTg/g8XYdVXN6a+XFjqFvOUPgzZ7OdywOoXxiO+M7WrvT0XgqyBqCnDADpY1eucDqfIDYYOBHKbtMkh0N4ZVBcfULb1Sm+Q7ed+jUa8eXPQPhMrWvhQkIeZJh+hCIrNjXUxyfZPh1tSvqoJYArbyHZs8LnbUtjIQCx9OlR9+xJTx3L9h89I4D+hqA4CZqxUzfibsu5XgYKnoSri2OCR2FefSfYlCd8Fysp0wET/r1L141qnhoMQtrUs8jwIDAQAB";
                    String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC+JK4K7hiD9YDKOnTqqJoT+KgjYz8AJP1RNRXFa0zshmVcxBk+MufQfXHTOYn9UsiGcGWLJvTpAu+2usEVG3iOc4lHkuOhOD+Dxdh1Vc3pr5cWOoW85Q+DNns53LA6hfGI74ztau9PReCrIGoKcMAOljV65wOp8gNhg4Ecpu0ySHQ3hlUFx9QtvVKb5Dt536NRrx5c9A+Eyta+FCQh5kmH6EIis2NdTHJ9k+HW1K+qglgCtvIdmzwudtS2MhALH06VH37ElPHcv2Hz0jgP6GoDgJmrFTN+Juy7leBgqehKuLY4JHYV59J9iUJ3wXKynTARP+vUvXjWqeGgxC2tSzyPAgMBAAECggEAMhFkhtpFOFIoFJgp+zRkRgf+9jqG91nGHmEVF4P2oH2PKUs1vmwXII43r8AB9uOai9QC2Q5sBQNR7dLlTtKJ/zCrIF6sc+JkzyUEp3jtnLAw35iPaLsER6/L6OOUwARPIpi5ijbTRxOGYmlJovAnkm+5K2CzVUe13jKLh+joool/ReZk0Rsr4tVLSLmvzDA/sRwYun0x0+jl5EZSQfwsVyN9bD5rY/In/EuvH9yj5R4lPe+mimF4Os6IgTsP5LzqDTAiFx5NNioFRJ2SkcTmM0CZQeMIBuvvF2HCtJlDEfCytD7wYup3GBvar2ccOe9T3YhJdsj5bfAJHVJtamxQwQKBgQDnYReMMzqAh2HOFL8QymzOjImsrOz6NCZatq38TU1hSe9PK+C0sFGhkd788y4AuURS1Btu4i7F+hOYcj1z3L+NSPGE3yLHVjakMrrNbA9rwG/t7oU0cG7d0WWM9bcTQiCSNcUyt69BGH3dZdqee1tITzqghE7+gh9RYiVcI6/8LwKBgQDSYEuWFLMUsR/s7unSHCucuEXjwbYrvknv8Y81sjvrWktNXrJoYlbGy/7HYA6lxzchtSxhPuUSjopwQ5scgMhqf8Gxz7jsDN9ak2dErF7cWRFYfh6aKhkbEw9oG01jIX15MK0TbMafoJslDhPQF1cP9i0+ZGg+gPbASdeUVRTNoQKBgQCOjwDOLgYeiMtXCOtL8hymCmsNDCKaaiUzgRijuhEyHzamJhe13Gj/TnwAh+hRI9UX333jjNJawqDuLXz1dQ5Eg6vjPQQVo2XZNzRnOuwpbJDKHUrPK3Lzkn+qIP6ii/y7eQu+GvSM/AUYsxfGy6RLYh1yJvLw1sVrBDiWk5prmwKBgFvgrmI3XBa3XKgPl5KptupVGEDmAveLvaLLLq5WzxB0eNqrduNbv2ZHBVhxvTPtk0hnZaB65XR7SD7LZ9zE6cKJVUCg5bRB0vIt2jYFydAWHhs1yYuuwxQt+NaQxfV7VN8uwQfww7ZHYDqIsWJ6Lw3Lh+rt0xEpJZrJJRulJNbBAoGBAK1OEnfBpSB99N8gdhp+ZGLsDfwFCQ2Cd4Jpsd4hxdwbXevNuA1OiE20sHPuKqEqfOKocgTMobCwbSfnymatRydVoeUumkEc4Ja+XDgH+P1eXQLdIuRCwh0AXl+vkuOCBDMw367Zp/j6vwPlNKh9ZmOBPwhV0Syv2Z8uGkTZ6g+f";
                    String source = "高可用架构对于互联网服务基本是标配。";
                    String aData = RSAUtil.encrypt(publicKey, source);
                    test_encryption_demo_result.setText(RSAUtil.decrypt(privateKey, aData));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
