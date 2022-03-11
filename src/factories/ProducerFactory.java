package factories;


import entities.Producer;
import fileio.input.ProducerInput;

public final class ProducerFactory {
    private static ProducerFactory instance = null;

    private ProducerFactory() {
    }

    public static ProducerFactory getInstance() {
        if (instance == null) {
            instance = new ProducerFactory();
        }
        return instance;
    }

    public Producer createProducer(final String entityType,
                                   final ProducerInput producerInput) {
        if (entityType.equals("basic producer")) {
            return new Producer(producerInput);
        } else {
            return null;
        }
    }
}
