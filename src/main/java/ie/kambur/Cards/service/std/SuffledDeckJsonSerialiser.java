package ie.kambur.Cards.service.std;

import ie.kambur.Cards.ShuffledDeck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SuffledDeckJsonSerialiser {
    public static byte[] serialise (ShuffledDeck<?,?> shuffledDeck) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try ( ObjectOutputStream oos = new ObjectOutputStream(baos) ) {
            shuffledDeck.writeExternal(oos);
            return baos.toByteArray();
        }
    }
}
