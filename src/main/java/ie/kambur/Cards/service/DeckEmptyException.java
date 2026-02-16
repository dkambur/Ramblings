package ie.kambur.Cards.service;

public class DeckEmptyException extends RuntimeException {
    public DeckEmptyException(String message) {
        super(message);
    }
}
