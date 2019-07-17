package com.deepesh.finalproject.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.deepesh.finalproject.Activity.Fragment.AllTeachersFragment;
import com.deepesh.finalproject.Activity.Fragment.DetailsFragment;
import com.deepesh.finalproject.Activity.Fragment.SearchFragment;
import com.deepesh.finalproject.Activity.Fragment.StudentsFragment;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FingerPrintActivity extends AppCompatActivity {

    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "anykey";
    private Cipher cipher;
    private TextView textView;

    public KeyguardManager keyguardManager;
    public FingerprintManager fingerprintManager;

    SharedPreferences preferences;
    //getfwpusername Must be Static
    public static String getfwpusername;
    //String getfwpusername="student";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        //preferences=getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        //getfwpusername = preferences.getString(Util.KEY_ST,null);
        textView=(TextView)findViewById(R.id.eTxtFingerPrint);
        Intent rcv=getIntent();
        getfwpusername = rcv.getStringExtra("youare");
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        checkFingerPrintScanner();

    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void checkFingerPrintScanner(){
        // Check whether the device has a Fingerprint sensor.
        if(!fingerprintManager.isHardwareDetected()){
            /**
             * An error message will be displayed if the device does not contain the fingerprint hardware.
             * However if you plan to implement a default authentication method,
             */
            /*if (getfwpusername.equalsIgnoreCase("teacher")) {
                passDataForTeacher();
            } else if (getfwpusername.equalsIgnoreCase("student")) {
                passDataForStudent();
            } else {
                Toast.makeText(FingerPrintActivity.this, "getfwpusername in Null", Toast.LENGTH_SHORT).show();
            }*/
            Toast.makeText(this, "Your Device does not have a Fingerprint Sensor", Toast.LENGTH_SHORT).show();
            textView.setText("Your Device does not have a Fingerprint Sensor");
        }else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                textView.setText("Fingerprint authentication permission not enabled");
            }else{
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    textView.setText("Register at least one fingerprint in Settings");
                }else{
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        textView.setText("Lock screen security not enabled in Settings");
                    }else{
                        generateKey();


                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintAuthenticationHandler helper = new FingerprintAuthenticationHandler(this);
                            helper.startAuth(fingerprintManager, cryptoObject);
                            //finish();
                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public void passDataForTeacher(){
        Intent intent1=new Intent(FingerPrintActivity.this,MainActivity.class);
        intent1.putExtra("youare","teacher");
        Intent intent2=new Intent(FingerPrintActivity.this,AllTeachersFragment.class);
        intent2.putExtra("youare","teacher");
        Intent intent3=new Intent(FingerPrintActivity.this,DetailsFragment.class);
        intent3.putExtra("youare","teacher");
        Intent intent4=new Intent(FingerPrintActivity.this,StudentsFragment.class);
        intent4.putExtra("youare","teacher");
        Intent intent5=new Intent(FingerPrintActivity.this,SearchFragment.class);
        intent5.putExtra("youare","teacher");

        startActivity(intent1);
        finish();
    }
    public void passDataForStudent(){
        Intent intent1=new Intent(FingerPrintActivity.this,MainActivity.class);
        intent1.putExtra("youare","student");
        Intent intent2=new Intent(FingerPrintActivity.this,AllTeachersFragment.class);
        intent2.putExtra("youare","student");
        Intent intent3=new Intent(FingerPrintActivity.this,DetailsFragment.class);
        intent3.putExtra("youare","student");
        Intent intent4=new Intent(FingerPrintActivity.this,StudentsFragment.class);
        intent4.putExtra("youare","student");
        Intent intent5=new Intent(FingerPrintActivity.this,SearchFragment.class);
        intent5.putExtra("youare","student");


        startActivity(intent1);
        finish();
    }

}
