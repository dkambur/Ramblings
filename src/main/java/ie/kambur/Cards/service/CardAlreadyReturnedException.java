package ie.kambur.Cards.service;

public class CardAlreadyReturnedException extends RuntimeException {
    public CardAlreadyReturnedException(String message) {
        super(message);
    }
}
