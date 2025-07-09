package it.trenical.admin.promotion.creator;

import it.trenical.admin.promotion.creator.commands.*;
import it.trenical.admin.promotion.creator.commands.conditions.*;
import it.trenical.admin.promotion.creator.commands.effect.CountDiscountEffectCommand;
import it.trenical.admin.promotion.creator.commands.effect.FixedAmountEffectCommand;
import it.trenical.admin.promotion.creator.commands.effect.FixedPriceEffectCommand;
import it.trenical.admin.promotion.creator.commands.effect.PercentageEffectCommand;
import it.trenical.frontend.cli.Cli;
import it.trenical.frontend.cli.Command;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.PromotionServiceGrpc;

import java.util.Scanner;

public class PromotionCreator extends Cli {

    private final PromotionServiceGrpc.PromotionServiceBlockingStub stub;

    public PromotionCreator(
            PromotionServiceGrpc.PromotionServiceBlockingStub stub,
            PromotionMessage.Builder builder,
            boolean update
    ) {
        super("PromotionCreator");
        this.stub = stub;
        registerCommands(
                new PromoSetNameCommand(builder),
                new PromoSetDescriptionCommand(builder),
                new PromoGetStatusCommand(builder),
                new PromoRemoveCondition(builder),
                new PromoConfirmCommand(stub, builder, update, super::exit)
        );
        registerCommands(
                new CountDiscountEffectCommand(builder),
                new FixedAmountEffectCommand(builder),
                new FixedPriceEffectCommand(builder),
                new PercentageEffectCommand(builder)
        );
        registerCommands(
                new DayConditionCommand(builder),
                new FidelityConditionCommand(builder),
                new MinTicketConditionCommand(builder),
                new PathConditionCommand(builder),
                new PeriodConditionCommand(builder),
                new TrainTypeConditionCommand(builder)
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
