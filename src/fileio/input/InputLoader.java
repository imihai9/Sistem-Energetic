package fileio.input;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Consumer;
import entities.Distributor;
import entities.Producer;
import factories.ConsumerFactory;
import factories.DistributorFactory;
import factories.ProducerFactory;
import updates.DistributorChange;
import updates.MonthlyUpdate;
import updates.ProducerChange;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class InputLoader {
    private static InputLoader instance = null;

    private InputLoader() {
    }

    public static InputLoader getInstance() {
        if (instance == null) {
            instance = new InputLoader();
        }
        return instance;
    }

    /**
     * Reads initial data from the inputFile, places it into lists
     * Creates an Input object that holds the lists
     */
    public Input readInitialData(final File inputFile) throws IOException {
        List<Consumer> consumerList = new ArrayList<>();
        List<Distributor> distributorList = new ArrayList<>();
        List<Producer> producerList = new ArrayList<>();
        List<MonthlyUpdate> monthlyUpdateList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(inputFile);

        int numberOfTurns = root.at("/numberOfTurns").asInt();

        JsonNode consumerListRoot = root.at("/initialData/consumers");
        JsonNode distributorListRoot = root.at("/initialData/distributors");
        JsonNode producerListRoot = root.at("/initialData/producers");
        JsonNode monthlyUpdateListRoot = root.at("/monthlyUpdates");

        for (JsonNode consumerNode : consumerListRoot) {
            ConsumerInput consumerInput = mapper.treeToValue(consumerNode, ConsumerInput.class);
            consumerList.add(ConsumerFactory.getInstance().createConsumer(
                    "basic consumer", consumerInput));
        }

        for (JsonNode distributorNode : distributorListRoot) {
            DistributorInput distributorInput = mapper.treeToValue(distributorNode,
                    DistributorInput.class);
            distributorList.add(DistributorFactory.getInstance().createDistributor(
                    "basic distributor", distributorInput));
        }

        for (JsonNode producerNode : producerListRoot) {
            ProducerInput producerInput = mapper.treeToValue(producerNode,
                    ProducerInput.class);
            producerList.add(ProducerFactory.getInstance().createProducer(
                    "basic producer", producerInput));
        }

        for (JsonNode monthlyUpdateNode : monthlyUpdateListRoot) {
            JsonNode newConsumersListRoot = monthlyUpdateNode.at("/newConsumers");
            JsonNode distributorChangesListRoot = monthlyUpdateNode.at("/distributorChanges");
            JsonNode producerChangesListRoot = monthlyUpdateNode.at("/producerChanges");

            List<Consumer> newConsumerList = new ArrayList<>();
            List<DistributorChange> distributorChangeList = new ArrayList<>();
            List<ProducerChange> producerChangeList = new ArrayList<>();

            for (JsonNode newConsumerNode : newConsumersListRoot) {
                ConsumerInput consumerInput =
                        mapper.treeToValue(newConsumerNode, ConsumerInput.class);
                newConsumerList.add(ConsumerFactory.getInstance().createConsumer(
                        "basic consumer", consumerInput));
            }

            for (JsonNode distributorChangeNode : distributorChangesListRoot) {
                distributorChangeList.add(mapper.treeToValue(distributorChangeNode,
                        DistributorChange.class));
            }

            for (JsonNode producerChangeNode : producerChangesListRoot) {
                producerChangeList.add(mapper.treeToValue(producerChangeNode,
                        ProducerChange.class));
            }

            monthlyUpdateList.add(new MonthlyUpdate(newConsumerList, distributorChangeList,
                    producerChangeList));
        }

        return new Input(numberOfTurns, consumerList, distributorList, producerList,
                monthlyUpdateList);
    }
}
