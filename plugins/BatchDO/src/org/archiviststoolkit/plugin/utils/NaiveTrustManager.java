package org.archiviststoolkit.plugin.utils;
/**
 * Class to get around the exception that is thrown when using ssl if the
 * certificate is not trusted.
 *
 * The code was taken from
 * http://www.howardism.org/Technical/Java/SelfSignedCerts.html
 *
 * @author Nathan Stevens
 * @date Jan 5, 2011;
 */
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class NaiveTrustManager implements X509TrustManager {

    /**
     * Doesn't throw an exception, so this is how it approves a certificate.
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], String)
     **/
    public void checkClientTrusted(X509Certificate[] cert, String authType)
            throws CertificateException {
    }

    /**
     * Doesn't throw an exception, so this is how it approves a certificate.
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], String)
     **/
    public void checkServerTrusted(X509Certificate[] cert, String authType)
            throws CertificateException {
    }

    /**
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     **/
    public X509Certificate[] getAcceptedIssuers() {
        return null;  // I've seen someone return new X509Certificate[ 0 ];
    }

}
