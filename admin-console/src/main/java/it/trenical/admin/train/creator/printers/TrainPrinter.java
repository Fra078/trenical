package it.trenical.admin.train.creator.printers;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import it.trenical.proto.train.TrainResponse;
import it.trenical.proto.train.TrainTypeResponse;

public final class TrainPrinter {

    private TrainPrinter() {
        throw new IllegalStateException("Utility class");
    }

    public static String printTrain(TrainResponse train) {
        try {
            return JsonFormat.printer()
                    .includingDefaultValueFields().print(train);
        } catch (InvalidProtocolBufferException e) {
            return "Parsing error";
        }
    }

}
