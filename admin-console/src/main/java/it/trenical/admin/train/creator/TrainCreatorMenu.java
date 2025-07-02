package it.trenical.admin.train.creator;

import io.grpc.ManagedChannel;
import it.trenical.admin.train.creator.commands.*;
import it.trenical.frontend.cli.Cli;
import it.trenical.frontend.cli.Command;
import it.trenical.proto.train.RegisterTrainRequest;
import it.trenical.proto.train.TrainManagerGrpc;

import java.util.Scanner;

public class TrainCreatorMenu extends Cli {

    RegisterTrainRequest.Builder builder = RegisterTrainRequest.newBuilder();

    public TrainCreatorMenu(ManagedChannel channel) {
        super("TrainBuilder");
        registerCommands(
                new SetNameCommand(builder),
                new SetDepartureTime(builder),
                new SetTypeCommand(builder),
                new SetPathCommand(builder),
                new AddSeatsCommand(builder),
                new AddPlatformPreferenceCommand(builder),
                new GetStatusCommand(builder),
                new ConfirmCommand(builder, TrainManagerGrpc.newBlockingStub(channel))
        );
    }

    @Override
    protected void exit() {
        System.out.println("Sicuro di voler annullare l'operazione?[s/n]");
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scan.nextLine();
            if (input.equals("s")) {
                super.exit();
                return;
            } else if (input.equals("n"))
                return;
        }
    }
}
