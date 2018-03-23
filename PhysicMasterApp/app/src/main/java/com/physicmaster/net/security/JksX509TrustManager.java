package com.physicmaster.net.security;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class JksX509TrustManager implements X509TrustManager {
    private X509TrustManager sunJSSEX509TrustManager;
    private static final String TAG = "JksX509TrustManager";

    public JksX509TrustManager(InputStream jksInputStream, char[] password) throws
            KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException,
            NoSuchProviderException {
        try {
            // create a "default" JSSE X509TrustManager.
            KeyStore ks = KeyStore.getInstance("BKS");
            ks.load(jksInputStream, password);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ks);
            TrustManager tms[] = tmf.getTrustManagers();
            /*
             * Iterate over the returned trustmanagers, look
             * for an instance of X509TrustManager.  If found,
             * use that as our "default" trust manager.
             */
            for (int i = 0; i < tms.length; i++) {
                Log.d(TAG, "JksX509TrustManager: " + tms[i].toString());
                if (tms[i] instanceof X509TrustManager) {
                    sunJSSEX509TrustManager = (X509TrustManager) tms[i];
                    return;
                }
            }
            if (sunJSSEX509TrustManager == null) {
                throw new NullPointerException("can not find sunJSSEX509TrustManager from " +
                        "TrustManagerFactory");
            }
        } finally {
            if (jksInputStream != null) {
                try {
                    jksInputStream.close();
                } catch (Exception e) {
                }
            }
        }
    }


    public void checkClientTrusted(X509Certificate[] chain, String authType) throws
            CertificateException {
        this.sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws
            CertificateException {
        this.sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return this.sunJSSEX509TrustManager.getAcceptedIssuers();
    }

    public static void main(String[] args) {
        try {
            JksX509TrustManager manager = new JksX509TrustManager(null, "".toCharArray());
            Log.d(TAG, "main: ");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }
}

