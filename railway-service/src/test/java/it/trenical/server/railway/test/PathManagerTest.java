package it.trenical.server.railway.test;

import io.grpc.StatusRuntimeException;
import it.trenical.proto.railway.GetPathRequest;
import it.trenical.proto.railway.RegisterPathRequest;
import it.trenical.server.database.DatabaseManager;
import it.trenical.server.railway.exceptions.LinkNotFoundException;
import it.trenical.server.railway.exceptions.StationExistsException;
import it.trenical.server.railway.managers.PathManager;
import it.trenical.server.railway.models.Path;
import it.trenical.server.railway.models.Station;
import it.trenical.server.railway.repositories.PathRepository;
import it.trenical.server.railway.repositories.StationRepository;
import it.trenical.server.railway.repositories.jdbc.PathJdbcRepository;
import it.trenical.server.railway.repositories.jdbc.StationJdbcRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PathManagerTest {

    private PathManager pathManager;
    private PathRepository pathRepository;
    private StationRepository stationRepository;
    private DatabaseManager dbManager;

    @BeforeEach
    void setUp() {
        dbManager = new DatabaseManager("mem:testdb;DB_CLOSE_DELAY=-1");
        stationRepository = new StationJdbcRepository(dbManager);
        pathRepository = new PathJdbcRepository(dbManager);
        pathManager = new PathManager(pathRepository);
    }

     @AfterEach
     void clearDb(){
        dbManager.withConnection(connection -> {
            connection.prepareStatement("DROP ALL OBJECTS ").execute();
        });
     }

    @Test
    void testRegisterPathSuccess() throws StationExistsException {
        RegisterPathRequest request = RegisterPathRequest.newBuilder()
                .addStations("Milano")
                .addStations("Roma")
                .build();
        stationRepository.save(new Station("Milano", "MI", 1));
        stationRepository.save(new Station("Roma", "RM", 1));
        stationRepository.insertLink("Milano", "Roma", 570);
        pathManager.registerPath(request);
        List<Path> found = new ArrayList<>();
        pathRepository.findBySubpath("Milano", "Roma", found::add);
        assertEquals(1, found.size());
    }

    @Test
    void testRegisterPathLinkNotFound() {
        RegisterPathRequest request = RegisterPathRequest.newBuilder()
                .addStations("Milano")
                .addStations("Roma")
                .build();
        assertThrows(StatusRuntimeException.class, () -> pathManager.registerPath(request));
    }

    @Test
    void testGetPathFound() throws StationExistsException {
        stationRepository.save(new Station("Milano", "MI", 1));
        stationRepository.save(new Station("Roma", "RM", 1));
        stationRepository.insertLink("Milano", "Roma", 570);
        int pathId = pathRepository.registerPath(List.of("Milano", "Roma"));
        GetPathRequest request = GetPathRequest.newBuilder().setId(pathId).build();
        assertEquals(pathId, pathManager.getPath(request).getId());
    }

    @Test
    void testGetPathNotFound() {
        GetPathRequest request = GetPathRequest.newBuilder().setId(999).build();
        assertThrows(StatusRuntimeException.class, () -> pathManager.getPath(request));
    }
}