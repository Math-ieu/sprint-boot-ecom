package fr.smartshop.productservice.exception;

/**
 * Exception personnalisée pour gérer les cas où une ressource n'est pas trouvée.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructeur avec un message personnalisé.
     *
     * @param message le message d'erreur
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructeur avec un message personnalisé et une cause.
     *
     * @param message le message d'erreur
     * @param cause   la cause de l'erreur
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
