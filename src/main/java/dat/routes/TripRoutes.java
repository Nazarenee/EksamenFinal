package dat.routes;

import dat.controllers.TripController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class TripRoutes {
    private final TripController tripController = TripController.getInstance();

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/", tripController::readAll);
            get("/{id}", tripController::read);
            post("/", tripController::create);
            post("/{tripId}/guide/{guideId}", tripController::addGuideToTrip);
            put("/{id}", tripController::update);
            delete("/{id}", tripController::delete);
            post("/populate", tripController::populate);
            get("/category/{category}", tripController::filterTripByCategory);
            get("/packinglist/{category}", tripController::PackingListByCategory);
            get("/packinglist/weight/{id}", tripController::getPackingListWeight);


          /*  post("/", tripController::create, Role.USER, Role.ADMIN);
            put("/{id}", tripController::update, Role.ADMIN);
            delete("/{id}", tripController::delete, Role.ADMIN);
            */
        };
    }
}
