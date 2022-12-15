package multijoueur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 * Sérialisation en chaîne de caractères des objets pour être intégré plus facilement à une requête pour les Sockets 
 *
 * @author Manon
 */
public final class SerializeToString {
    /**
     * Désérialise un objet contenu dans une chaîne de caractères
     * 
     * @param s Chaîne de caractères à désérialiser
     * 
     * @return Objet désérialisé
     * 
     * @throws IOException Entrée/sortie
     * @throws ClassNotFoundException Classe non trouvé
     */
    public static Object fromString(String s) throws IOException, ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }
    
    /**
     * Sérialise un objet en chaîne de caractères
     * 
     * @param o Objet à sérialiser
     * 
     * @return Chaîne de caractères
     * 
     * @throws IOException Entrée/sortie
     */
    public static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
    }
}
