package factories;

import entities.Consumer;
import fileio.input.ConsumerInput;

public final class ConsumerFactory {
    private static ConsumerFactory instance = null;

    private ConsumerFactory() {
    }

    public static ConsumerFactory getInstance() {
        if (instance == null) {
            instance = new ConsumerFactory();
        }
        return instance;
    }

    public Consumer createConsumer(final String entityType, final ConsumerInput consumerInput) {
        if (entityType.equals("basic consumer")) {
            return new Consumer(consumerInput);
        } else {
            return null;
        }
    }
}
