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
            get("/", tripController::readAll, Role.USER, Role.ADMIN);
            get("/{id}", tripController::read);
            post("/", tripController::create);
            put("/{id}", tripController::update);
            delete("/{id}", tripController::delete);
            post("/populate", tripController::populate);
            put("/{tripid}/guides/{guideid}", tripController::addGuideToTrip);
            get("/category/{category}", tripController::filterTripByCategory);
            get("/packinglist/{category}", tripController::PackingListByCategory);

          /*  post("/", tripController::create, Role.USER, Role.ADMIN);
            put("/{id}", tripController::update, Role.ADMIN);
            delete("/{id}", tripController::delete, Role.ADMIN);
            */
        };
    }
}
