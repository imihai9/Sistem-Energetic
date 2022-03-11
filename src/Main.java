import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fileio.input.Input;
import fileio.input.InputLoader;
import fileio.output.Output;
import simulation.Simulation;

import java.io.File;
import java.io.FileWriter;

/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() {
    }

    /**
     * Main function which reads the input file, starts simulation and writes the output files
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        File inputFile = new File(args[0]);

        InputLoader inputLoader = InputLoader.getInstance();
        Input inputData = inputLoader.readInitialData(inputFile);

        for (int i = 0; i <= inputData.getNumberOfTurns(); i++) {
            Simulation simulation = new Simulation(inputData, i);
            simulation.execute();
        }

        Output output = new Output(inputData.getConsumerList(), inputData.getDistributorList(),
                inputData.getProducerList());

        FileWriter fileWriter = new FileWriter(args[1]);

        ObjectWriter jsonWriter = new ObjectMapper()
                .configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false)
                .writer(new DefaultPrettyPrinter());

        jsonWriter.writeValue(fileWriter, output);
        fileWriter.write('\n');
        fileWriter.close();
    }
}
