package Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import Structures.Sequence;
import Structures.SequenceListe;

public class Configuration {

    static Configuration instance = null;
    Properties properties;
	Logger logger;

    public static Configuration instance() {
		if (instance == null)
			instance = new Configuration();
		return instance;
	}

    protected Configuration() {
        properties = new Properties();
        try {
            InputStream propertiesInputStream = charge("configVisiteRoyale.cfg");
            properties.load(propertiesInputStream);
            String home = System.getProperty("user.home");

            File file = new File(home + File.separator + ".visiteRoyale");
            if(!file.isFile()) { 
                file.createNewFile();
            }

            FileInputStream fileInputStream = new FileInputStream(home + File.separator + ".visiteRoyale");
            properties = new Properties(properties);
            properties.load(fileInputStream);
        } catch (Exception e) {
            Configuration.instance().logger().warning("Erreur du chargement de la configuration : " + e + "\n");
        }
	}

    public static InputStream charge(String nom) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(nom);
    }

    public String lis(String cle) {
        String resultat = properties.getProperty(cle);
        if (resultat == null) {
            throw new NoSuchElementException("Propriete " + cle + " non definie dans 'configGaufre.cfg'");
        }
        return resultat;
    }

    public <E> Sequence<E> nouvelleSequence() {
        String type = lis("Sequence");
        switch (type) {
            case "Liste":
                return new SequenceListe<>();
            default:
                throw new RuntimeException("Type de sequence inconnu : " + type);
        }
    }
    
    public Logger logger() {
		if (logger == null) {
			System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s : %5$s%n");
			logger = Logger.getLogger("VisiteRoyale.Logger");
            logger.setLevel(Level.parse(lis("LogLevel")));
		}
		return logger;
	}
}
