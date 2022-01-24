package fr.redboxing.wakfu.server.crypto;

import javax.crypto.Cipher;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public abstract class CryptoCipher {

    protected final String algorithmName;
    protected final AlgorithmParameterSpec parameterSpecs;
    protected Cipher cipher;


    protected CryptoCipher(String algoName, AlgorithmParameterSpec parameterSpec) {
        algorithmName = algoName;
        parameterSpecs = parameterSpec;

        try {
            cipher = Cipher.getInstance(this.algorithmName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected EncodedKeySpec createKeySpec(byte[] key) {
        return new X509EncodedKeySpec(key);
    }

    public String getAlgorithm() {
        return this.algorithmName;
    }

    public abstract byte[] encode(byte[] data);

    public abstract byte[] decode(byte[] data);

}
