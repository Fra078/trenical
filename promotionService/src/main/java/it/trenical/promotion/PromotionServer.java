package it.trenical.promotion;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.promotion.managers.FidelityProgramManager;
import it.trenical.promotion.managers.PromotionBroadcastManager;
import it.trenical.promotion.managers.PromotionManager;
import it.trenical.promotion.mappers.ConditionMapper;
import it.trenical.promotion.mappers.EffectMapper;
import it.trenical.promotion.mappers.PromotionMapper;
import it.trenical.promotion.mappers.visitors.ConditionMapperVisitor;
import it.trenical.promotion.mappers.visitors.EffectMapperVisitor;
import it.trenical.promotion.repository.FidelityProgramRepository;
import it.trenical.promotion.repository.PromotionRepository;
import it.trenical.promotion.repository.map.FidelityProgramMapRepository;
import it.trenical.promotion.repository.map.PromotionRepositoryMap;
import it.trenical.promotion.services.PromotionService;
import it.trenical.server.structures.PersistentMapDecorator;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class PromotionServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        PromotionMapper promotionMapper = preparePromotionMapper();

        PromotionRepository promotionRepository = preparePromotionRepository();
        FidelityProgramRepository fidelityRepository = prepareFidelityRepository();

        PromotionBroadcastManager broadcast = new PromotionBroadcastManager();

        PromotionManager promotionManager =
                new PromotionManager(promotionRepository, fidelityRepository);

        FidelityProgramManager fidelityProgramManager
                = new FidelityProgramManager(fidelityRepository);

        Server server = ServerBuilder.forPort(5606)
                .addService(new PromotionService(promotionManager, fidelityProgramManager, broadcast, promotionMapper))
                .build().start();

        System.out.println("Server started on port " + server.getPort());
        server.awaitTermination();
    }

    private static PromotionMapper preparePromotionMapper() {
        ConditionMapperVisitor conditionVisitor = new ConditionMapperVisitor();
        ConditionMapper conditionMapper = new ConditionMapper(conditionVisitor);
        EffectMapperVisitor effectVisitor = new EffectMapperVisitor();
        EffectMapper effectMapper = new EffectMapper(effectVisitor);
        return new PromotionMapper(conditionMapper, effectMapper);
    }

    private static PromotionRepository preparePromotionRepository() {
        return new PromotionRepositoryMap(
                new PersistentMapDecorator<>(new ConcurrentHashMap<>(), "db/promotions.dat"));
    }

    private static FidelityProgramRepository prepareFidelityRepository() {
        return new FidelityProgramMapRepository(
                new PersistentMapDecorator<>(new ConcurrentHashMap<>(), "db/fidelity.dat")
        );
    }

}
