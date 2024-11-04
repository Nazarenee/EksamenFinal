package dat.config;

import dat.controllers.ExceptionController;
import dat.routes.Routes;
import dat.security.controllers.AccessController;
import dat.security.enums.Role;
import dat.security.routes.SecurityRoutes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ApplicationConfig {
    private static final AccessController accessController = new AccessController();
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static final ExceptionController exceptionController = new ExceptionController();
    private static final Routes routes = new Routes();
    private static int count = 1;

    private static void configuration(JavalinConfig config) {
        // General configuration
        config.showJavalinBanner = false;
        config.router.ignoreTrailingSlashes = true;
        config.router.treatMultipleSlashesAsSingleSlash = true;
        config.bundledPlugins.enableDevLogging();

        // Routing
        config.router.contextPath = "/api";
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);
        config.router.apiBuilder(routes.getRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());
    }

    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(ApplicationConfig::configuration);
        app.start(port);

        // Logging
        app.after(ApplicationConfig::afterRequest);
        logger.info("Server started on port: {}", port);

        // Exception handling
        exceptionController.setExceptionHandlers(app);

        // Authentication
        app.beforeMatched(ctx -> accessController.accessHandler(ctx));
        app.after(ApplicationConfig::afterRequest);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }

    /**
     *  Logging after each API request
     */
    private static void afterRequest(Context ctx) {
        String requestInfo = ctx.req().getMethod() + " " + ctx.req().getRequestURI();
        logger.info("Request #{} - {} handled with status {}", count++, requestInfo, ctx.status());
    }
}

