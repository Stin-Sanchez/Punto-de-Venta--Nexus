package util;

import javax.swing.*;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) { // Corregido: Se verifica si es null
            try {
                // Crear el registro de configuración
                registry = new StandardServiceRegistryBuilder().configure().build();

                // Crear las fuentes de metadatos
                MetadataSources sources = new MetadataSources(registry);

                // Crear los metadatos
                Metadata metadata = sources.getMetadataBuilder().build();

                // Crear la SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al abrir la sesión: " + e.getMessage());
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}


